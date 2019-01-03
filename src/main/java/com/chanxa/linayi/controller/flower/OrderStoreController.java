
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		OrderStoreController.java
 * Description：
 * 		#订单商品收货库，根据订单所在的社区ID进行一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.flower;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.flower.OrderStoreBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.flower.OrderStoreService;

@Controller
@RequestMapping("/flower/orderStore")
public class OrderStoreController extends CommonControllerSupport {
	@Resource
	private OrderStoreService orderStoreService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderStore.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object orderStoreList(OrderStoreBO orderStore,HttpServletRequest request) {
		return this.orderStoreService.getPage( orderStore );
	}

	/**
	 * 新增/更新orderStore
	 * @param orderStore
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( OrderStoreBO orderStore ) {
				if( orderStore.getOrderStoreId() != null ){
					this.orderStoreService.update(orderStore);
				}else{
					this.orderStoreService.add(orderStore);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param orderStoreId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long orderStoreId) {
		this.orderStoreService.delete(orderStoreId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param orderStoreId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long orderStoreId) {
		return this.orderStoreService.get(orderStoreId);
	}

	/**
	 * 编辑页面
	 * @param orderStoreId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long orderStoreId) {
		ModelAndView mv = new ModelAndView("flower/OrderStoreEdit");
		if (orderStoreId !=null) {
			OrderStoreBO orderStore = this.orderStoreService.get(orderStoreId);
			mv.addObject("orderStore", orderStore);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param orderStoreId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long orderStoreId) {
		ModelAndView mv = new ModelAndView("flower/OrderStoreShow");
		if (orderStoreId !=null) {
			OrderStoreBO orderStore = this.orderStoreService.get(orderStoreId);
			mv.addObject("orderStore", orderStore);
		}
		return mv;
	}

}
