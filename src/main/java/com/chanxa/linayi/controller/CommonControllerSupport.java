 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CommonControllerSupport.java
 * Description：
 * 		控制层共有的继承类
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/
package com.chanxa.linayi.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chanxa.linayi.bo.community.CommunityAccountBO;
import com.chanxa.linayi.common.context.ResponseContext;
import com.chanxa.linayi.common.util.ApplicationContextUtils;

public class CommonControllerSupport{

	
	
	/**
	 * 获取当前系统登陆账号
	 * */
	public static Integer getUserId( HttpServletRequest request ){
		return ( Integer )request.getSession().getAttribute( "accountId" );
	}
	
	/**
	 * 获取用户的权限编码列表
	 * @param request
	 * @return 返回该用户拥有的权限编码列表
	 */
	public static Set<String> getUserPrivilegeCode( HttpServletRequest request ){
		return ( Set<String> )request.getSession().getAttribute( "userPrivilegeCodeList" );
	}
	
	public static Integer getCommunityId(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Object communityId = request.getSession().getAttribute( "communityId" );
		return communityId == null ? null : Integer.valueOf( communityId.toString() );
	}
	
	public static CommunityAccountBO getCommunityAccount(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Object communityAccount = request.getSession().getAttribute( "communityAccount" );
		return communityAccount == null ? null : ( CommunityAccountBO )communityAccount;
	}
	
	/**
	 * 获取用户的权限URL列表
	 * @param request
	 * @return 返回该用户拥有的权限URL列表
	 */
	public static Set<String> getUserPrivilegeUrl( HttpServletRequest request ){
		return ( Set<String> )request.getSession().getAttribute( "privilegeUrlList" );
	}
}
