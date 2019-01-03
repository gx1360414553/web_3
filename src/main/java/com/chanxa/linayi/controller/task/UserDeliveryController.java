
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		UserDeliveryController.java
 * Description：
 * 		#用户-配送任务，根据用户ID进行一致性hash，该表用于用户查询所抢到的任务控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.task;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.task.UserDeliveryBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.task.UserDeliveryService;

@Controller
@RequestMapping("/task/userDelivery")
public class UserDeliveryController extends CommonControllerSupport {
	@Resource
	private UserDeliveryService userDeliveryService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userDelivery.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object userDeliveryList(UserDeliveryBO userDelivery,HttpServletRequest request) {
		return this.userDeliveryService.getPage( userDelivery );
	}

	/**
	 * 新增/更新userDelivery
	 * @param userDelivery
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( UserDeliveryBO userDelivery ) {
				if( userDelivery.getUserDeliveryId() != null ){
					this.userDeliveryService.update(userDelivery);
				}else{
					this.userDeliveryService.add(userDelivery);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param userDeliveryId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long userDeliveryId) {
		this.userDeliveryService.delete(userDeliveryId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param userDeliveryId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get( UserDeliveryBO userDelivery ) {
		return this.userDeliveryService.get(userDelivery);
	}

	/**
	 * 编辑页面
	 * @param userDeliveryId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long userDeliveryId) {
		ModelAndView mv = new ModelAndView("task/UserDeliveryEdit");
//		if (userDeliveryId !=null) {
//			UserDeliveryBO userDelivery = this.userDeliveryService.get(userDeliveryId);
//			mv.addObject("userDelivery", userDelivery);
//		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param userDeliveryId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long userDeliveryId) {
		ModelAndView mv = new ModelAndView("task/UserDeliveryShow");
//		if (userDeliveryId !=null) {
//			UserDeliveryBO userDelivery = this.userDeliveryService.get(userDeliveryId);
//			mv.addObject("userDelivery", userDelivery);
//		}
		return mv;
	}

}
