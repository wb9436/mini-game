package com.zhiyou.wxgame.configuration.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 方便静态引用application.properties的参数
 * Autowired annotation is not supported on static fields
 * 
 * @author WB
 *
 */
@Configuration
public class ApplicationConfig implements ApplicationContextAware {

	private static Environment env;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		env = applicationContext.getBean(Environment.class);
	}
	public static String getValue(String key) {
		return env.getProperty(key);
	}

	/**
	 * 图片上传地址
	 * 
	 * @return
	 */
	public static String getImageUploadDir() {
		return getValue("wxgame.uploadPath");
	}

	/**
	 * 图片预览地址
	 * 
	 * @return
	 */
	public static String getImageVisitPath() {
		return getValue("wxgame.visitPath");
	}
	
	public static String getHost() {
		return getValue("spring.redis.host");
	}
	
	public static int getPort() {
		return Integer.parseInt(getValue("spring.redis.port"));
	}
	
	public static int getTimeout() {
		return Integer.parseInt(getValue("spring.redis.timeout"));
	}
	
	public static String getPassword() {
		return getValue("spring.redis.password");
	}
	
	public static int getMaxActive() {
		String max = getValue("spring.redis.pool.max-active");
		return Integer.parseInt(max);
	}
	
	public static int getMaxIdle() {
		return Integer.parseInt(getValue("spring.redis.pool.max-idle"));
	}
	
	public static int getMaxWait() {
		return Integer.parseInt(getValue("spring.redis.pool.max-wait"));
	}

}
