package com.chanxa.linayi.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.service.system.AdminAccountPrivilegeService;
import com.chanxa.linayi.service.system.PrivilegeService;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.bo.system.TreeNodeBO;

@Controller
@RequestMapping(value = "backstageUserPrivilegeAction")
public class AdminAccountPrivilegeController{
	@Resource
	private PrivilegeService privilegeService;
	
	@Resource
	private AdminAccountPrivilegeService adminAccountprivilegeService;
	
	@RequestMapping(value = "getUserRoles")
	@ResponseBody
	public List<TreeNodeBO> getUserRoles(HttpServletRequest request,Integer userId){
		return privilegeService.getRolePrivilege( userId );
	}
	
	@RequestMapping(value = "getUserPrivileges")
	@ResponseBody
	public List<TreeNodeBO> getUserOtherPrivilege(HttpServletRequest request,Integer userId){
		return privilegeService.getUserOtherPrivilege( userId );
	}
	
	/**
	 * 获取所有权限
	 * @param request
	 * @param _response
	 * @return
	 */
	@RequestMapping(value = "getPrivilegesList")
	@ResponseBody
	public List<TreeNodeBO> getPrivilegesList(HttpServletRequest request,Integer userId ){
		List<TreeNodeBO> treeNodeList = privilegeService.getPrivilege();
		List<TreeNodeBO> userPrivilegeList = privilegeService.getUserPrivilege( userId );
		checkedUserPriviliege(treeNodeList, userPrivilegeList);
		return treeNodeList;
	}
	
	private void checkedUserPriviliege(List<TreeNodeBO> treeNodeList,List<TreeNodeBO> userPrivilegeList){
		treeNodeList.forEach( treeNode->{
			userPrivilegeList.forEach( userNode->{
				if( treeNode.getId().equals( userNode.getId() )){
					if( treeNode.getChildren().isEmpty() ){
						treeNode.setChecked("true");
					}
				}
			} );
			this.checkedUserPriviliege(treeNode.getChildren(), userPrivilegeList);
		} );
	}
	
	@RequestMapping(value = "addUserPrivilege")
	@ResponseBody
	public AjaxReturnMessage addUserPrivilege(Integer userId,@RequestParam("privilegeIdList")ArrayList<Integer> privilegeIdList) {
		adminAccountprivilegeService.delAllUserPrivilege( userId );
//		if(privilegeIdList==null||privilegeIdList.size()<=0){
//			return AjaxReturnMessage.createMessage("请选择至少一个权限!",false);
//		}else{
			if( privilegeIdList.size() > 0 ){
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("USERID", userId);
				param.put("privilegeIdLists", privilegeIdList);
				adminAccountprivilegeService.addUserPrivilege(param);
			}
			return AjaxReturnMessage.createMessage( "操作成功",true );
//		}
	}

}
