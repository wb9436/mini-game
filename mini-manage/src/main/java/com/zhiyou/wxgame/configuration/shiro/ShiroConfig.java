package com.zhiyou.wxgame.configuration.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import net.sf.ehcache.CacheManager;

@Configuration
public class ShiroConfig {

	/**
	 * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
	 * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
	 * 主要是AuthorizingRealm类的子类，以及CacheManager类。
	 */
	@Bean("lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean("shiroRealm")
	@DependsOn("lifecycleBeanPostProcessor")
	public ShiroRealm shiroRealm() {
		ShiroRealm realm = new ShiroRealm();
		return realm;
	}

	/**
	 * ehCacheManager缓存管理器
	 * 
	 * @return
	 */
	@Bean
	public EhCacheManager cacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManager(CacheManager.create());
		return em;
	}

	/**
	 * 安全管理器
	 * 
	 * @return
	 */
	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(shiroRealm());
		// 设置cacheManager,默认MemoryConstrainedCacheManager
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	@Bean("shiroFilterFactoryBean")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());

		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl("/login");
		filters.put("logout", logoutFilter);
		shiroFilterFactoryBean.setFilters(filters);

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/docs/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/upload/**", "anon");
		filterChainDefinitionMap.put("/files/**", "anon");
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/index");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		return shiroFilterFactoryBean;
	}

	/**
	 * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
	 */
	@Bean
	@ConditionalOnMissingBean // 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public EnterpriseCacheSessionDAO enterpriseCacheSessionDAO() {
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		enterpriseCacheSessionDAO.setCacheManager(cacheManager());
		return enterpriseCacheSessionDAO;
	}

	/**
	 * AbstractSessionDAO 提供了SessionDAO的基础实现，如生成会话ID等； 
	 * CachingSessionDAO 提供了对开发者透明的会话缓存的功能，只需要设置相应的CacheManager即可； 
	 * MemorySessionDAO 直接在内存中进行会话维护；
	 * EnterpriseCacheSessionDAO 提供了缓存功能的会话维护，继承CachingSessionDAO，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 * 
	 * shiro session的管理
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(120 * 1000);
		// 默认sessionDAO为memorySessionDAO
		sessionManager.setSessionDAO(enterpriseCacheSessionDAO());
		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
		sessionManager.setSessionListeners(listeners);
		return sessionManager;
	}

}
