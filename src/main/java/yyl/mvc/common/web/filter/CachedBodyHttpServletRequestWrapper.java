package yyl.mvc.common.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * @see ContentCachingRequestWrapper
 */
public class CachedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    // =================================Fields=================================================
    private byte[] cachedBody;
    private boolean isFormPost;
    private boolean isJsonPost;

    // ==============================Constructors=====================================
    public CachedBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String contentType = request.getContentType();

        if (contentType != null && HttpMethod.POST.matches(getMethod())) {
            isFormPost = contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            isJsonPost = contentType.contains(MediaType.APPLICATION_JSON_VALUE);
        }

        if (isJsonPost) {
            InputStream requestInputStream = request.getInputStream();
            cachedBody = StreamUtils.copyToByteArray(requestInputStream);
        } else if (isFormPost) {
            cachedBody = copyParametersToByteArray(request);
        }
    }

    // ==============================Methods==========================================
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBody == null) {
            return super.getInputStream(); // 没缓存的情况回退到原始流
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(cachedBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public byte[] getBody() {
        return cachedBody;
    }

    public String getBodyAsString() {
        if (cachedBody == null) {
            return null;
        }
        return new String(cachedBody, StandardCharsets.UTF_8);
    }

    public boolean isCached() {
        return cachedBody != null;
    }

    public boolean isFormPost() {
        return isFormPost;
    }

    public boolean isJsonPost() {
        return isJsonPost;
    }

    // ==============================PrivateMethods===================================
    private static byte[] copyParametersToByteArray(HttpServletRequest request) {
        Map<String, String[]> form = request.getParameterMap();
        List<String> names = new ArrayList<>(form.keySet());
        Collections.sort(names);
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = StandardCharsets.UTF_8.name();
        }
        StringBuilder sb = new StringBuilder(256);
        boolean first = true;
        for (String name : names) {
            String[] values = form.get(name);
            if (values == null) {
                continue;
            }
            for (String value : values) {
                if (!first) {
                    sb.append('&');
                }
                first = false;
                sb.append(encode(name, encoding));
                sb.append('=');

                if (value != null) {
                    sb.append(encode(value, encoding));
                }
            }
        }
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String encode(String value, String encoding) {
        try {
            return URLEncoder.encode(value, encoding);
        } catch (Exception e) {
            throw new IllegalArgumentException("URL encode failed", e);
        }
    }
}