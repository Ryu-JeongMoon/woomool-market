package com.woomoolmarket.util.slack;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Plugin(name = "Slack", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class SlackAppender extends AbstractAppender {

  private static final Map<Integer, String> ICON_MAP = Map.of(
    Level.TRACE.intLevel(), ":pawprints:",
    Level.DEBUG.intLevel(), ":beetle:",
    Level.INFO.intLevel(), ":suspect:",
    Level.WARN.intLevel(), ":goberserk:",
    Level.ERROR.intLevel(), ":feelsgood:",
    Level.FATAL.intLevel(), ":finnadie:"
  );

  private static final Map<Integer, String> COLOR_MAP = Map.of(
    Level.TRACE.intLevel(), "#6f6d6d",
    Level.DEBUG.intLevel(), "#b5dae9",
    Level.INFO.intLevel(), "#5f9ea0",
    Level.WARN.intLevel(), "#ff9122",
    Level.ERROR.intLevel(), "#ff4444",
    Level.FATAL.intLevel(), "#b03e3c"
  );

  private final WebClient slackWebClient;
  private final String url;
  private final String channel;
  private final String username;

  public SlackAppender(
    String name,
    Filter filter,
    Layout<? extends Serializable> layout,
    boolean ignoreExceptions,
    Property[] properties,
    WebClient slackWebClient,
    String url, String channel, String username) {
    super(name, filter, layout, ignoreExceptions, properties);

    this.slackWebClient = slackWebClient;
    this.url = url;
    this.channel = channel;
    this.username = username;
  }

  @PluginFactory
  public static SlackAppender createAppender(
    @PluginElement("Filter") final Filter filter,
    @PluginElement("Layout") Layout<? extends Serializable> layout,
    @PluginAttribute("name") @Required(message = "No name provided") final String name,
    @PluginAttribute("url") @Required(message = "No webhookUrl provided") final String url,
    @PluginAttribute("channel") @Required(message = "No channel provided") final String channel,
    @PluginAttribute("username") @Required(message = "No username provided") final String username
  ) {
    if (layout == null)
      layout = PatternLayout.createDefaultLayout();

    WebClient slackWebClient = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
        .doOnConnected(
          connection -> connection
            .addHandlerLast(new ReadTimeoutHandler(3))
            .addHandlerLast(new WriteTimeoutHandler(3)))
        .compress(true)))
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();

    return new SlackAppender(name, filter, layout, false, Property.EMPTY_ARRAY, slackWebClient, url, channel, username);
  }

  @Override
  public void append(LogEvent event) {
    if (event.getMessage() != null) {
      String logStatement = event.getMessage().getFormattedMessage();
      SlackMessage slackMessage = new SlackMessage();
      slackMessage.channel = channel;
      slackMessage.icon_emoji = ICON_MAP.get(event.getLevel().intLevel());
      slackMessage.username = username;
      slackMessage.attachments = new ArrayList<>();
      Attachment attachment = new Attachment();
      attachment.color = COLOR_MAP.get(event.getLevel().intLevel());
      attachment.fallback = logStatement;
      slackMessage.text = attachment.fallback;
      attachment.text = getLayout().toSerializable(event).toString();
      slackMessage.attachments.add(attachment);

      slackWebClient.post().uri(url).bodyValue(slackMessage).retrieve().bodyToMono(Void.class).subscribe();
    }
  }
}
