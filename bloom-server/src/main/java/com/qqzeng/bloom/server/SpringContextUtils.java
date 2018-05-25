package com.qqzeng.bloom.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * ApplicationContextAware Wrapper for bean acquisition.
 * </p>
 * Created by qqzeng.
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    /**
     * <code>ApplicationContext</code> will be autowired at the time of <code>SpringContextUtils</code> instantiation.
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotaClazz) {
        return applicationContext.getBeansWithAnnotation(annotaClazz);
    }
}
