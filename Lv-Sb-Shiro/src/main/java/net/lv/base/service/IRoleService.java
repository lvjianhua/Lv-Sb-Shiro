package net.lv.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.lv.base.entity.Role;
import net.lv.base.service.support.IBaseService;

/**
 * <p>
 * 角色服务类
 * </p>
 *
 * @author lv
 */
public interface IRoleService extends IBaseService<Role,Integer> {

	/**
	 * 添加或者修改角色
	 * @param role
	 */
	void saveOrUpdate(Role role);

	/**
	 * 给角色分配资源
	 * @param id 角色ID
	 * @param resourceIds 资源ids
	 */
	void grant(Integer id, String[] resourceIds);

	/**
	 * 根据关键字查询分页
	 * @param searchText
	 * @param pageRequest
	 * @return
	 */
	Page<Role> findAllByLike(String searchText, PageRequest pageRequest);
	
}
