package yyl.mvc.plugin.spring.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.github.relucent.base.common.logging.Logger;
import com.github.relucent.base.common.web.WebUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import yyl.mvc.common.web.ControllerUtil;
import yyl.mvc.plugin.expection.ErrorType;
import yyl.mvc.plugin.expection.GeneralException;

/**
 * spring_mvc 异常处理
 */
public class SpringMvcExceptionResolver extends SimpleMappingExceptionResolver implements HandlerExceptionResolver {

    // ==============================Fields===========================================
    protected final Logger LOG = Logger.getLogger(getClass());
    protected String errorPage = "/_common/error";

    // ==============================Methods==========================================
    /**
     * 处理异常
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        logException(ex);

        if (WebUtil.isAjax(request)) {
            try {
                String message = ex.getMessage();
                if (message == null) {
                    message = "Service Error !";
                }
                Map<String, Object> error = new HashMap<>();
                error.put("successed", false);
                error.put("message", message);
                error.put("@failure", Boolean.TRUE);// failure|success
                response.setCharacterEncoding("utf-8");
                ControllerUtil.write(error, response);
            } catch (Exception e) {
                LOG.error("!", e);
            }
        } else {
            Map<String, Object> map = Maps.newHashMap();
            map.put("errorMsg", Throwables.getStackTraceAsString(ex));// 将错误信息传递给view
            return new ModelAndView(errorPage, map);
        }
        return new ModelAndView();
    }

    /**
     * 异常日志
     * @param ex 异常
     */
    protected void logException(Exception ex) {
        if (ex instanceof GeneralException && ErrorType.PROMPT.equals(((GeneralException) ex).getType())) {
            LOG.warn(ex.toString());
        } else {
            LOG.error("!", ex);
        }
    }

    // ==============================IocMethods=======================================
    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
