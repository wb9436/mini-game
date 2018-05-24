package com.zhiyou.wxgame.configuration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean管理器
 * 
 * @WB 
 * 
 */
@Component
public class ApplicationBeanManager implements ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(ApplicationBeanManager.class);
	private static ApplicationContext applictionContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.debug("ApplicationContext registed-->{}", applicationContext);
		applictionContext = applicationContext;
	}
	
	 /**
     * 获取容器
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applictionContext;
    }

    /**
     * 获取容器对象
     * @param beanName
     * @return
     */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) applictionContext.getBean(beanName);
	}
	
	/**
     * 获取容器对象
     * @param type
     * @param <T>
     * @return
     */
	public static <T> T getBean(Class<T> type) {
		return applictionContext.getBean(type);
	}
}
