
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		UserProcurementController.java
 * Description：
 * 		#用户-采买任务，根据用户ID进行一致性hash，该表用于用户查询所抢到的任务控制器
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

import com.chanxa.linayi.bo.task.UserProcurementBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.task.UserProcurementService;

@Controller
@RequestMapping("/task/userProcurement")
public class UserProcurementController extends CommonControllerSupport {
	@Resource
	private UserProcurementService userProcurementService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userProcurement.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object userProcurementList(UserProcurementBO userProcurement,HttpServletRequest request) {
		return this.userProcurementService.getPage( userProcurement );
	}

	/**
	 * 新增/更新userProcurement
	 * @param userProcurement
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( UserProcurementBO userProcurement ) {
				if( userProcurement.getUserProcurementId() != null ){
					this.userProcurementService.update(userProcurement);
				}else{
					this.userProcurementService.add(userProcurement);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param userProcurementId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long userProcurementId) {
		this.userProcurementService.delete(userProcurementId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param userProcurementId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long userProcurementId) {
		return this.userProcurementService.get(userProcurementId);
	}

	/**
	 * 编辑页面
	 * @param userProcurementId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long userProcurementId) {
		ModelAndView mv = new ModelAndView("task/UserProcurementEdit");
		if (userProcurementId !=null) {
			UserProcurementBO userProcurement = this.userProcurementService.get(userProcurementId);
			mv.addObject("userProcurement", userProcurement);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param userProcurementId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long userProcurementId) {
		ModelAndView mv = new ModelAndView("task/UserProcurementShow");
		if (userProcurementId !=null) {
			UserProcurementBO userProcurement = this.userProcurementService.get(userProcurementId);
			mv.addObject("userProcurement", userProcurement);
		}
		return mv;
	}

}
