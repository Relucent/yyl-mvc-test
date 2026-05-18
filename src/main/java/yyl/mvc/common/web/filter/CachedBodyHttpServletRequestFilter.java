package yyl.mvc.common.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CachedBodyHttpServletRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String contentType = request.getContentType();

        boolean cacheable = !(request instanceof CachedBodyHttpServletRequestWrapper)
                && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE);//

        if (cacheable) {
            request = new CachedBodyHttpServletRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }
}