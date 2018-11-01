package net.lv.base.config;

import javax.annotation.PostConstruct;

import net.lv.base.config.shiro.freemarker.ShiroTags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 配置ftl页面加上shiro-tag组件
 * 
 * 控制页面中ftl某些按钮的显示或隐藏功能
 * @author lv
 *
 */
@Configuration
public class FreeMarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() {
    	try {
			configuration.setSharedVariable("shiro", new ShiroTags());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
