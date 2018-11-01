package net.lv.base.controller.web;

import java.util.List;

import net.lv.base.controller.BaseController;
import net.lv.base.entity.User;
import net.lv.base.service.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController{
	
	@Autowired
	private IUserService userService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/","/index"})
	public String index(){
		List<User> users = userService.findAll();
		logger.debug(users.toString());
		return "index";
	}
}
