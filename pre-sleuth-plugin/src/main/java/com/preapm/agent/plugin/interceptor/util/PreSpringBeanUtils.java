package com.preapm.agent.plugin.interceptor.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 描述:
 * spring bean 工具類
 *
 * @author 34861
 * @create 2018-01-31 22:28
 */
@Component
public class PreSpringBeanUtils implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(PreSpringBeanUtils.applicationContext == null) {
            PreSpringBeanUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean
    public static <T> T getBean(Class<T> clazz){
    	try {
    		  return getApplicationContext().getBean(clazz);
    	}catch (Exception e) {
		}
    	return null;
      
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
