
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi.webwar
 * All rights reserved.
 * 
 * Filename：
 *		AreaController.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2018年1月2日下午2:21:31 
 ********************************************************/
package com.chanxa.linayi.controller.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.service.system.AreaService;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *
 * @author jobd huang
 * @date 2018年1月2日 下午2:21:31
 */
@Controller
@RequestMapping("/system/area/")
public class AreaController {

	@Resource
	private AreaService areaService;
	/**省市区街道
	 * @return
	 */
	@RequestMapping("listAreaTree.do")
	@ResponseBody
	public Object listAreaTree(){
		return areaService.getAllOverAreaList();
	}

}
