
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		test.webwar
 * All rights reserved.
 * 
 * Filename：
 *		PrivilegeTag.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2016年11月28日上午10:49:36 
 ********************************************************/
package com.chanxa.linayi.tags;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chanxa.linayi.bo.system.PrivilegeBO;
import com.chanxa.linayi.controller.CommonControllerSupport;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *	用于控制按钮是否显示标签
 * @author jobd huang
 * @date 2016年11月28日 上午10:49:36
 */
public class PrivilegeTag extends BodyTagSupport{

	/**  */
	private static final long serialVersionUID = 1L;
	
	/**权限编码*/
	private String privilegeCode;
	
	@Override
	public int doStartTag() throws JspException {
		//获取当前session中的权限列表
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Set<String> privilegeCodeList = CommonControllerSupport.getUserPrivilegeCode( request );
		//判断该权限编码是否存在于session中，如果存在则输出标签，否则不输出标签
		for( String privilegeCode : privilegeCodeList ) {
			if( privilegeCode.equals( this.privilegeCode ) ){
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}

	
	public String getPrivilegeCode() {
		return privilegeCode;
	}

	
	public void setPrivilegeCode( String privilegeCode ) {
		this.privilegeCode = privilegeCode;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
