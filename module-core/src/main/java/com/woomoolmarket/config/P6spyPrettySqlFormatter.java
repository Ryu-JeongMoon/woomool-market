package com.woomoolmarket.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.internal.FormatStyle;

@Slf4j
public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {

  private static final String QUERY_FILE_NAME = "logs/query/woomool-p6spy-%s.log";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public String formatMessage(
    final int connectionId, final String now, final long elapsed,
    final String category, final String prepared, final String sql, final String url) {

    Stack<String> callStack = new Stack<>();
    addStackTraceToCallStack(callStack);

    StringBuilder callStackBuilder = getCallStackBuilder(callStack);
    String message =
      "\n\n\tConnection ID: " + connectionId + "\n\tExecution Time: " + elapsed + " ms\n"
        + "\n\tCall Stack (number 1 is entry point): " + callStackBuilder + "\n"
        + "\n----------------------------------------------------------------------------------------------------";

    return getFormattedSQL(sql, category, message);
  }

  private void addStackTraceToCallStack(Stack<String> callStack) {
    StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
    Arrays.stream(stackTraceElements)
      .map(StackTraceElement::toString)
      .filter(element -> element.startsWith("io.p6spy") && !element.contains("P6spyPrettySqlFormatter"))
      .forEach(callStack::push);
  }

  private StringBuilder getCallStackBuilder(Stack<String> callStack) {
    StringBuilder callStackBuilder = new StringBuilder();
    int order = 1;
    while (callStack.size() != 0) {
      callStackBuilder.append("\n\t\t").append(order++).append(". ").append(callStack.pop());
    }

    return callStackBuilder;
  }

  private String getFormattedSQL(String sql, String category, String message) {
    if (sql.trim().isEmpty())
      return "";

    String formattedSql = formatSQL(sql, category);
    writeQuery(formattedSql, message);
    return "\n" + formattedSql + message;
  }

  private String formatSQL(String sql, String category) {
    if (StringUtils.equals(Category.STATEMENT.getName(), category)) {
      String s = sql.trim().toLowerCase(Locale.ROOT);
      sql = StringUtils.startsWithAny(s, "create", "alter", "comment")
        ? FormatStyle.DDL.getFormatter().format(sql)
        : FormatStyle.BASIC.getFormatter().format(sql);
    }

    return sql;
  }

  private void writeQuery(String sql, String message) {
    CompletableFuture.runAsync(() -> {
      File file = new File(String.format(QUERY_FILE_NAME, LocalDate.now()));
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
        writer.write(LocalDateTime.now().format(FORMATTER));
        writer.write(sql + message + "\n");
      } catch (IOException e) {
        log.error("[WOOMOOL-ERROR] :: exception {}", e);
      }
    });
  }
}
