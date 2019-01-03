
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		OrderSectionController.java
 * Description：
 * 		下单时间段控制器
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

import com.chanxa.linayi.bo.task.OrderSectionBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.task.OrderSectionService;

@Controller
@RequestMapping("/task/orderSection")
public class OrderSectionController extends CommonControllerSupport {
	@Resource
	private OrderSectionService orderSectionService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderSection.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object orderSectionList(OrderSectionBO orderSection,HttpServletRequest request) {
		return this.orderSectionService.getPage( orderSection );
	}

	/**
	 * 新增/更新orderSection
	 * @param orderSection
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( OrderSectionBO orderSection ) {
				if( orderSection.getOrderSectionId() != null ){
					this.orderSectionService.update(orderSection);
				}else{
					this.orderSectionService.add(orderSection);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param orderSectionId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer orderSectionId) {
		this.orderSectionService.delete(orderSectionId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param orderSectionId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer orderSectionId) {
		return this.orderSectionService.get(orderSectionId);
	}

	/**
	 * 编辑页面
	 * @param orderSectionId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer orderSectionId) {
		ModelAndView mv = new ModelAndView("task/OrderSectionEdit");
		if (orderSectionId !=null) {
			OrderSectionBO orderSection = this.orderSectionService.get(orderSectionId);
			mv.addObject("orderSection", orderSection);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param orderSectionId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer orderSectionId) {
		ModelAndView mv = new ModelAndView("task/OrderSectionShow");
		if (orderSectionId !=null) {
			OrderSectionBO orderSection = this.orderSectionService.get(orderSectionId);
			mv.addObject("orderSection", orderSection);
		}
		return mv;
	}

}
