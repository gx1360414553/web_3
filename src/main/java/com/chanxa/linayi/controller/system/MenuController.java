package com.chanxa.linayi.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chanxa.linayi.common.util.CheckUtil;

import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.FileUploadUtil;

import org.springframework.web.bind.annotation.RequestBody;

import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.FileUploadUtil;

import com.chanxa.linayi.bo.system.PrivilegeBO;
import com.chanxa.linayi.service.system.MenuService;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.bo.system.TreeNodeBO;
import com.chanxa.linayi.service.system.Impl.PrivilegeServiceImpl;

@Controller
@RequestMapping("/menuController/")
public class MenuController {
	@Resource
	private MenuService menuServiceImpl;

	@Resource
	private PrivilegeServiceImpl privilegeServiceImpl;
	/**
	 * 获取权限树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPrivilegeTree")
	@ResponseBody
	public List<TreeNodeBO> getPrivilegeTree(HttpServletRequest request) {
		List<TreeNodeBO> tree = privilegeServiceImpl.getPrivilege();
		TreeNodeBO node = new TreeNodeBO();
		node.setText("root");
		node.setState("open");
		node.setChildren(tree);
		//设置二级菜单图片显示
		String imageServer = Configuration.getConfig().getValue( Propert.IMAGE_SERVER )+"/";
		for( TreeNodeBO treeNode : tree ) {
			treeNode.getChildren().forEach( child->{
				if( CheckUtil.isNotNullEmpty( child.getIcon() ) ){
					child.setIcon( imageServer + child.getIcon() );
				}
			} );
		}
		return Arrays.asList(node);
	}

	/**
	 * 修改节点关系
	 * */
	@RequestMapping("resetNodeIndex")
	@ResponseBody
	public AjaxReturnMessage resetNodeIndex( @RequestBody TreeNodeBO tree ){
		privilegeServiceImpl.resetNodeIndex( tree );
		return AjaxReturnMessage.createMessage("操作成功！", true);
	}

	/**
	 * 
	 * @Title: addMenu @Description: 添加菜单 @return List 返回类型 @throws
	 */
	@RequestMapping(value = "addMenu")
	@ResponseBody
	public AjaxReturnMessage addMenu(HttpServletRequest request,PrivilegeBO privilege,@RequestParam(value="file",required=false)CommonsMultipartFile file) {
		PrivilegeBO privilegeParam = new PrivilegeBO();
		privilegeParam.setPrivilegeCode( privilege.getPrivilegeCode() );
		if( file != null ){
			if( file.getSize() > 1024*10 ){
				return AjaxReturnMessage.createMessage( "上传的首页图标文件大小不能超过10k！", false );
			}
			String contentType = file.getContentType();
			if( contentType.indexOf( "image" ) == 0 ){
				String path = FileUploadUtil.uploadImageOriginal( file, Configuration.getConfig().getValue( Propert.IMAGE_PATH ) );
				privilege.setIcon( path );
			}  
		}
		privilegeParam.setPrivilegeCode( privilege.getPrivilegeCode() );
		List<PrivilegeBO> privilegeList =  privilegeServiceImpl.listPrivilege( privilegeParam );
		if ( privilege.getPrivilegeId() != null ) {
			//判断菜单编码是否已使用
			for( PrivilegeBO privilegeBO : privilegeList ) {
				if( privilegeBO.getPrivilegeCode() .equals(  privilege.getPrivilegeCode()) &&  !privilegeBO.getPrivilegeId().equals( privilege.getPrivilegeId() ) ){
					return AjaxReturnMessage.createMessage( "操作失败，菜单编码已被使用！", false );
				}
			}
			privilegeServiceImpl.update( privilege );
		} else {
			
			//判断菜单编码是否已使用
			if( privilegeList.size() > 0 ){
				return AjaxReturnMessage.createMessage( "操作失败，菜单编码已被使用！", false );
			}
			privilegeServiceImpl.add(privilege);
		}
		return AjaxReturnMessage.createMessage( "操作成功！", true );
	}

	/**
	 * 
	 * @Title: addMenu @Description: 添加菜单 @return List 返回类型 @throws
	 */
	@RequestMapping(value = "deleteMenu")
	@ResponseBody
	public AjaxReturnMessage deleteMenu(Integer privilegeId) {
		privilegeServiceImpl.delete(privilegeId);
		return AjaxReturnMessage.createMessage("操作成功！",true);
	}
}
