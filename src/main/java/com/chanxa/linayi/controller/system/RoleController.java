package com.chanxa.linayi.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.system.RoleService;
import com.chanxa.linayi.bo.system.RoleBO;
import com.chanxa.linayi.bo.system.AccountRoleRelBO;
import com.chanxa.linayi.bo.system.RolePrivilegeRelBO;
import com.chanxa.linayi.bo.system.PrivilegeBO;
import com.chanxa.linayi.bo.system.TreeNodeBO;

@Controller
@RequestMapping(value = "roleAction")
public class RoleController{

	@Resource
	private RoleService roleService;

	@RequestMapping(value = "getRoleList")
	@ResponseBody
	public List<RoleBO> getRoleList(HttpServletRequest request) {
		return roleService.getRoleList();
	}


	@RequestMapping(value = "getRoleListByUser")
	@ResponseBody
	public List<RoleBO> getRoleListByUser( Integer accountId ) {
		return roleService.getRoleListByUser( accountId  );
	}

	@RequestMapping(value = "getRolePrivilege")
	@ResponseBody
	public List<PrivilegeBO> getRolePrivilege( Integer roleId ) {
		return roleService.getRolePrivilege( roleId );
	}

	/**
	 * 获取权限树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPrivilegeTree")
	@ResponseBody
	public List<TreeNodeBO> getPrivilegeTree( Integer roleId ) {
		return roleService.getPrivilegeList( roleId  );
	}

	@RequestMapping(value = "addRole")
	@ResponseBody
	public AjaxReturnMessage addRole( RoleBO role ) throws UnsupportedEncodingException {
		roleService.addRole( role );
		return AjaxReturnMessage.createMessage("操作成功！", true);
	}

	@RequestMapping(value = "addUserRole")
	@ResponseBody
	public AjaxReturnMessage addUserRole( @RequestBody ArrayList<Integer> roleIdList,Integer accountId ) throws UnsupportedEncodingException {
			try {
				// 先删除此用户的所有角色 在添加新角色
				roleService.delUserAllRole( accountId );
				roleIdList.forEach(roleId->{
					AccountRoleRelBO accountRoleRel = new AccountRoleRelBO();
					accountRoleRel.setRoleId(roleId);
					accountRoleRel.setAccountId(accountId);
					roleService.addUserRole( accountRoleRel );
				});
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxReturnMessage.createMessage("操作失败！",false);
			}
		return AjaxReturnMessage.createMessage("操作成功！",true);
	}

	/**
	 * 添加角色权限
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "addRolePrivilege")
	@ResponseBody
	public AjaxReturnMessage addRolePrivilege( Integer roleId,@RequestBody ArrayList<Integer> privilegeIdList ) throws UnsupportedEncodingException {
		roleService.delRolePrivilege( roleId );
		privilegeIdList.forEach(privilegeId->{
			RolePrivilegeRelBO privilegeRel = new RolePrivilegeRelBO();
			privilegeRel.setRoleId(roleId);
			privilegeRel.setPrivilegeId(privilegeId);
			roleService.addRolePrivilege( privilegeRel );
		});
		return AjaxReturnMessage.createMessage("操作成功！",true);
	}

	/**
	 * 更新用户跳转
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "updateRole")
	public String updateUser(HttpServletRequest req) {

		// 需要更新记录的id
		String id = req.getParameter("ID");
		// Map map = roleService.selectOneUser(id);
		// req.setAttribute("user", map);
		// System.out.println("=====updateUser");
		return "system/addUsers";
	}

	/**
	 * 删除角色
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "delRole")
	@ResponseBody
	public AjaxReturnMessage delRole( Integer roleId ) {
		
		// 查询此角色是否有关联用户和权限
		List<RolePrivilegeRelBO> rpList = roleService.getRolePrivilegeListByRoleID( roleId );
		List<AccountRoleRelBO> urList = roleService.getUserRoleListByRoleID( roleId );

		if (rpList.size() == 0 && urList.size() == 0) {
			roleService.delRole( roleId );
			return AjaxReturnMessage.createMessage("已删除！", true);
		} else {
			return AjaxReturnMessage.createMessage("该角色有关联用户，不能删除！",false);
		}
	}
}
