package net.lv.base.config.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import net.lv.base.service.IResourceService;
/**
 * 定义一系列关于URL的规则和访问权限。
 * 
 * @author lv
 *
 */
@Configuration
@Import(ShiroManager.class)
public class ShiroConfig {
	
	@Resource
	private IResourceService resourceService;
	

	@Bean(name = "realm")
	@DependsOn("lifecycleBeanPostProcessor")
	@ConditionalOnMissingBean
	public Realm realm() {
		return new MyRealm();
	}
	
	 /**
     * 用户授权信息Cache,用户数据注入
     */
    @Bean(name = "shiroCacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 配置管理层。即安全控制层
     * 
     * @return
     */
    @Bean(name = "securityManager")
    @ConditionalOnMissingBean
    public DefaultSecurityManager securityManager() {
        DefaultSecurityManager sm = new DefaultWebSecurityManager();
        sm.setCacheManager(cacheManager());
        return sm;
    }

    /**
     * 
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * @param securityManager管理所有Subject，可以配合内部安全组件。
     * @param realm
     * @return
     */
	@Bean(name = "shiroFilter")
	@DependsOn("securityManager")
	@ConditionalOnMissingBean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultSecurityManager securityManager, Realm realm) {
		// 必须设置 SecurityManager 
		securityManager.setRealm(realm);
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		// 登录界面链接
		shiroFilter.setLoginUrl("/admin/login");
		// 登录成功后要跳转的链接
		shiroFilter.setSuccessUrl("/admin/index");
		// 未授权界面链接;
		shiroFilter.setUnauthorizedUrl("/previlige/no");
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
		// anon:所有url都可以匿名访问
		filterChainDefinitionMap.put("/assets/**", "anon");
		filterChainDefinitionMap.put("/admin/login", "anon");
		
		List<net.lv.base.entity.Resource> list = resourceService.findAll();
		for (net.lv.base.entity.Resource resource : list) {
			filterChainDefinitionMap.put(resource.getSourceUrl(), "perms[" + resource.getSourceKey() + "]");
		}
		
//		filterChainDefinitionMap.put("/admin/user/index", "perms[system:user:index]");
//		filterChainDefinitionMap.put("/admin/user/add", "perms[system:user:add]");
//		filterChainDefinitionMap.put("/admin/user/edit*", "perms[system:user:edit]");
//		filterChainDefinitionMap.put("/admin/user/deleteBatch", "perms[system:user:deleteBatch]");
//		filterChainDefinitionMap.put("/admin/user/grant/**", "perms[system:user:grant]");
		
//		filterChainDefinitionMap.put("/admin/role/index", "perms[system:role:index]");
//		filterChainDefinitionMap.put("/admin/role/add", "perms[system:role:add]");
//		filterChainDefinitionMap.put("/admin/role/edit*", "perms[system:role:edit]");
//		filterChainDefinitionMap.put("/admin/role/deleteBatch", "perms[system:role:deleteBatch]");
//		filterChainDefinitionMap.put("/admin/role/grant/**", "perms[system:role:grant]");
		
//		filterChainDefinitionMap.put("/admin/resource/index", "perms[system:resource:index]");
//		filterChainDefinitionMap.put("/admin/resource/add", "perms[system:resource:add]");
//		filterChainDefinitionMap.put("/admin/resource/edit*", "perms[system:resource:edit]");
//		filterChainDefinitionMap.put("/admin/resource/deleteBatch", "perms[system:resource:deleteBatch]");
		
		// authc:所有url都必须认证通过才可以访问;
		filterChainDefinitionMap.put("/admin/**", "authc");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilter;
	}
}
