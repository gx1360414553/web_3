
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		OrderController.java
 * Description：
 * 		#订单，根据社区ID进行一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.order;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.xb.xsdschema.impl.PublicImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.flower.BoxBO;
import com.chanxa.linayi.bo.order.OrderBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.OrderSeccondStatusEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.order.OrderService;
import com.chanxa.linayi.util.system.AreaUtil;
import com.mchange.util.ObjectCache;

@Controller
@RequestMapping("/order/order")
public class OrderController extends CommonControllerSupport {
	@Resource
	private OrderService orderService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("order.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object orderList(OrderBO order,HttpServletRequest request) {
		PageResult<OrderBO> page = this.orderService.getPage( order );
		//设置订单状态
		page.getRows().forEach( item->{
			item.setSeccondStatusName( DictionaryUtil.getDictionaryName( OrderSeccondStatusEnum.DICT_MAP, item.getSeccondStatus().toString() ) );
			item.setAddress( AreaUtil.getAreaName( item.getAreaCode() ) + item.getAddress() );
		} );
		return page;
	}
	
	@RequestMapping("/orderList.do")
	@ResponseBody
	public Object orderList1(OrderBO order){
		PageResult<OrderBO> page = orderService.getPage(order);
		page.setTotal(order.getTotal());
		page.setRows(orderService.getList(order));
		return page;
		
	}

	/**
	 * 新增/更新order
	 * @param order
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( OrderBO order ) {
				if( order.getOrderId() != null ){
					this.orderService.update(order);
				}else{
					this.orderService.add(order);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long orderId) {
		this.orderService.delete(orderId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(OrderBO order) {
		return this.orderService.get(order);
	}

	/**
	 * 编辑页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(OrderBO order) {
		ModelAndView mv = new ModelAndView("order/OrderEdit");
		if (order.getOrderId() !=null) {
			order = this.orderService.get(order);
			mv.addObject("order", order);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(OrderBO order) {
		ModelAndView mv = new ModelAndView("order/OrderShow");
		if (order.getOrderId() !=null) {
			order = this.orderService.get(order);
			mv.addObject("order", order);
		}
		return mv;
	}

}
