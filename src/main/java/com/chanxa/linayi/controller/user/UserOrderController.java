
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		UserOrderController.java
 * Description：
 * 		#用户-订单，根据用户ID进行一致性hash分库分表控制器
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

import com.chanxa.linayi.bo.user.UserOrderBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.UserOrderService;

@Controller
@RequestMapping("/user/userOrder")
public class UserOrderController extends CommonControllerSupport {
	@Resource
	private UserOrderService userOrderService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userOrder.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object userOrderList(UserOrderBO userOrder,HttpServletRequest request) {
		return this.userOrderService.getPage( userOrder );
	}

	/**
	 * 新增/更新userOrder
	 * @param userOrder
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( UserOrderBO userOrder ) {
				if( userOrder.getUserOrderId() != null ){
					this.userOrderService.update(userOrder);
				}else{
					this.userOrderService.add(userOrder);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param userOrderId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long userOrderId) {
		this.userOrderService.delete(userOrderId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param userOrderId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long userOrderId) {
		return this.userOrderService.get(userOrderId);
	}

	/**
	 * 编辑页面
	 * @param userOrderId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long userOrderId) {
		ModelAndView mv = new ModelAndView("user/UserOrderEdit");
		if (userOrderId !=null) {
			UserOrderBO userOrder = this.userOrderService.get(userOrderId);
			mv.addObject("userOrder", userOrder);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param userOrderId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long userOrderId) {
		ModelAndView mv = new ModelAndView("user/UserOrderShow");
		if (userOrderId !=null) {
			UserOrderBO userOrder = this.userOrderService.get(userOrderId);
			mv.addObject("userOrder", userOrder);
		}
		return mv;
	}

}
