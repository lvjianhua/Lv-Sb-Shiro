package net.lv.base.config.shiro;

import java.util.HashSet;
import java.util.Set;

import net.lv.base.common.utils.MD5Utils;
import net.lv.base.entity.Resource;
import net.lv.base.entity.Role;
import net.lv.base.entity.User;
import net.lv.base.service.IUserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证用户身份和权限，这里Shiro不做权限的管理工作，需要我们自己管理用户权限，
 * 只需要从我们的数据源中把用户和用户的角色权限信息取出来交给Shiro即可，Shiro就会自动的进行权限判断。
 * 
 * @author lv
 *
 */
@Component
public class MyRealm extends AuthorizingRealm {

	public MyRealm(){
		super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);

        //FIXME: 暂时禁用Cache
        setCachingEnabled(false);//启用缓存，默认false；
	}
	
	@Autowired
	private IUserService userService;

    /**
     * 获取身份信息，我们可以在这个方法中，从数据库获取该用户的权限和角色信息
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User dbUser = userService.findByUserName(user.getUserName());
		Set<String> shiroPermissions = new HashSet<>();
		Set<String> roleSet = new HashSet<String>();
		Set<Role> roles = dbUser.getRoles();
		for (Role role : roles) {
			Set<Resource> resources = role.getResources();
			for (Resource resource : resources) {
				shiroPermissions.add(resource.getSourceKey());
				
			}
			roleSet.add(role.getRoleKey());
		}
		// 角色
		authorizationInfo.setRoles(roleSet);
		// 权限
		authorizationInfo.setStringPermissions(shiroPermissions);
		return authorizationInfo;
	}

    /**
     * 在这个方法中，进行身份验证
     * @param token 账户数据
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		
		User user = userService.findByUserName(username);
		
		String password = new String((char[]) token.getCredentials());

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		// 密码错误
		if (!MD5Utils.md5(password).equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
		// 账号锁定
		if (user.getLocked() == 1) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}

		// 身份验证通过,返回一个身份信息
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());

		return info;
	}

}
