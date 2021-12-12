package com.woomoolmarket.aop.logging;

import static org.springframework.util.StringUtils.hasText;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Log4j2
public class ReadableRequestWrapper extends HttpServletRequestWrapper {

    private final Charset encoding;
    private final Map<String, String[]> params = new HashMap<>();
    private byte[] rawData;

    public ReadableRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap()); // 원래의 파라미터를 저장

        String characterEncoding = request.getCharacterEncoding(); // 인코딩 설정
        this.encoding = hasText(characterEncoding) ? Charset.forName(characterEncoding) : StandardCharsets.UTF_8;

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(ContentType.MULTIPART_FORM_DATA.getMimeType())) { // 파일 업로드시 로깅제외
            return;
        }

        try (InputStream is = request.getInputStream()) {
            this.rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성

            // body 파싱
            String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (StringUtils.isEmpty(collect)) { // body 가 없을경우 로깅 제외
                return;
            }

            JSONParser jsonParser = new JSONParser();
            Object parse = jsonParser.parse(collect);
            if (parse instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) jsonParser.parse(collect);
                setParameter("requestBody", jsonArray.toJSONString());
            } else {
                JSONObject jsonObject = (JSONObject) jsonParser.parse(collect);
                for (Object o : jsonObject.keySet()) {
                    String key = (String) o;
                    setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
                }
            }
        } catch (Exception e) {
            log.info("[WOOMOOL-FAILED] :: Can't initialize ReadableRequestWrapper => {}", e.getMessage());
        }
    }

    @Override
    public String getParameter(String name) {
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            return paramArray[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] dummyParamValue = params.get(name);

        if (dummyParamValue != null) {
            result = new String[dummyParamValue.length];
            System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
        }
        return result;
    }

    public void setParameter(String name, String value) {
        String[] param = {value};
        setParameter(name, param);
    }

    public void setParameter(String name, String[] values) {
        params.put(name, values);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
}
