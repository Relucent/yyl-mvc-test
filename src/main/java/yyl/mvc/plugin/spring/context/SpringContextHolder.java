package yyl.mvc.plugin.spring.context;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringContextHolder {

    // ==============================Fields===========================================
    private static final AtomicReference<ApplicationContext> CONTEXT = new AtomicReference<>();

    // ==============================Constructors=====================================
    protected SpringContextHolder() {
    }

    // ==============================Methods==========================================
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getApplicationContext().getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getApplicationContext().getBean(name, requiredType);
    }

    public static <T> T createBean(Class<T> beanClass) {
        return getApplicationContext().getAutowireCapableBeanFactory().createBean(beanClass);
    }

    public static void autowireBean(Object existingBean) {
        getApplicationContext().getAutowireCapableBeanFactory().autowireBean(existingBean);
    }

    public static ApplicationContext getApplicationContext() {
        ApplicationContext context = CONTEXT.get();
        if (context == null) {
            throw new RuntimeException("The applicaitoncontext property is null, please check whether springcontextholder is injected!");
        }
        return context;
    }

    // ==============================OverrideMethods==================================
    protected void setApplicationContext(ApplicationContext applicationContext) {
        CONTEXT.set(applicationContext);
    }
}
