
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		UserLocationController.java
 * Description：
 * 		用户收货地址控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.user.UserLocationBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.UserLocationService;

@Controller
@RequestMapping("/user/userLocation")
public class UserLocationController extends CommonControllerSupport {
	@Resource
	private UserLocationService userLocationService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userLocation.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object userLocationList(UserLocationBO userLocation,HttpServletRequest request) {
		return this.userLocationService.getPage( userLocation );
	}

	/**
	 * 新增/更新userLocation
	 * @param userLocation
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( UserLocationBO userLocation ) {
				if( userLocation.getUserLocationId() != null ){
					this.userLocationService.update(userLocation);
				}else{
					this.userLocationService.add(userLocation);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param userLocationId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long userLocationId) {
		this.userLocationService.delete(userLocationId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param userLocationId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long userLocationId) {
		return this.userLocationService.get(userLocationId);
	}

	/**
	 * 编辑页面
	 * @param userLocationId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long userLocationId) {
		ModelAndView mv = new ModelAndView("user/UserLocationEdit");
		if (userLocationId !=null) {
			UserLocationBO userLocation = this.userLocationService.get(userLocationId);
			mv.addObject("userLocation", userLocation);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param userLocationId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long userLocationId) {
		ModelAndView mv = new ModelAndView("user/UserLocationShow");
		if (userLocationId !=null) {
			UserLocationBO userLocation = this.userLocationService.get(userLocationId);
			mv.addObject("userLocation", userLocation);
		}
		return mv;
	}

}
