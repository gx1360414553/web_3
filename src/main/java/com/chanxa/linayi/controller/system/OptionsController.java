
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		fdaSystem.webwar
 * All rights reserved.
 * 
 * Filename：
 *		OptionsController.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2017年3月10日下午3:01:31 
 ********************************************************/
package com.chanxa.linayi.controller.system;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.system.OptionsService;


/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *
 * @author jobd huang
 * @date 2017年3月10日 下午3:01:31
 */
@Controller
@RequestMapping("/system/options/")
public class OptionsController extends CommonControllerSupport{
	
	@Resource
	private OptionsService optionsService;
	
	private Logger logger = LoggerFactory.getLogger( OptionsController.class );
	
	@RequestMapping("listOptions")
	@ResponseBody
	public Object listOptions( HttpServletRequest request,@RequestParam Map<String,Object> params ){
		try{
			return optionsService.listOptions( params );
		}catch( Exception ex ){
			logger.error( "出错了->",ex );
			return AjaxReturnMessage.createMessage( false );
		}
	}
	
}
