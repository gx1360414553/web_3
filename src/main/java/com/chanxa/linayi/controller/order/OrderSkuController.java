
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		OrderSkuController.java
 * Description：
 * 		#订单-商品，根据订单社区ID一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.community.CommunitySupermarketBO;
import com.chanxa.linayi.bo.community.SupermarketBO;
import com.chanxa.linayi.bo.order.OrderSkuBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.CommunitySupermarketType;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.order.OrderSkuPopulator;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.order.OrderSkuService;

@Controller
@RequestMapping("/order/orderSku")
public class OrderSkuController extends CommonControllerSupport {
	@Resource
	private OrderSkuService orderSkuService;
	
	@Resource
	private OrderSkuPopulator orderSkuPopulator;

	@Resource
	private SupermarketService supermarketService;
	
	@Resource
	private CommunitySupermarketService communitySupermarketService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("orderSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object orderSkuList(OrderSkuBO orderSku,HttpServletRequest request) {
		List<OrderSkuBO> orderSkuList = orderSkuService.getList( orderSku );
		//设置超市名称
		List<Integer> supermarketIdList = orderSkuList.stream().map( OrderSkuBO::getSupermarketId ).collect( Collectors.toList() );
		if( supermarketIdList.size() > 0 ){
			SupermarketBO supermarket = new SupermarketBO();
			supermarket.setSupermarketIdList( supermarketIdList );
			Map<Integer, List<SupermarketBO>> supermarketGroupBySupermarketId = supermarketService.getList( supermarket  )
					.stream()//
					.collect( //
							Collectors.groupingBy( SupermarketBO::getSupermarketId ) //
					);
			orderSkuList.forEach( item->{
				item.setCommunityId( orderSku.getCommunityId() );
				item.setSupermarketName( supermarketGroupBySupermarketId.get( //
						item.getSupermarketId() )//
						.stream()//
						.findFirst()//
						.map( SupermarketBO::getName ).orElse( null ) //
					);
				item.setCreateTime( supermarketGroupBySupermarketId.get( //
						item.getSupermarketId() )//
						.stream()//
						.findFirst()//
						.map( SupermarketBO::getCreateTime ).orElse( null ) //
					);
			} );
				
		}
		return orderSkuList;
	}
	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/listOrderSkuByCommunityId.do")
	@ResponseBody
	public Object listOrderSkuByCommunityId(OrderSkuBO orderSku,HttpServletRequest request) {
		//获取当前社区
		Integer communityId = orderSku.getCommunityId();
		if( communityId == null || orderSku.getCommunityId() == null ){
			return Collections.emptyList();
		}
		//获取超市ID
		CommunitySupermarketBO communitySupermarket = new CommunitySupermarketBO();
		communitySupermarket.setCommunityId( orderSku.getCommunityId() );
		communitySupermarket.setType( CommunitySupermarketType.MANAGEMENT.getValue() );
		communitySupermarket = communitySupermarketService.getList( communitySupermarket ).stream().findFirst().orElse( null );
		if( communitySupermarket == null ){
			return Collections.emptyList();
		}
		orderSku.setCommunityId( communityId );
		orderSku.setSupermarketId( communitySupermarket.getSupermarketId() );
		//获取当前社区ID
		List<OrderSkuBO> orderSkuList = orderSkuService.listOrderSkuByCommunityId( orderSku );
		PageResult<OrderSkuBO> page = new PageResult<OrderSkuBO>();
		page.setRows( orderSkuList );
		page.setTotal( orderSku.getTotal() );
		//设置超市
		List<Integer> supermarketIdList = orderSkuList.stream().map( OrderSkuBO::getSupermarketId ).collect( Collectors.toList());
		if( supermarketIdList.size() > 0 ){
			SupermarketBO supermarket = new SupermarketBO();
			supermarket.setSupermarketIdList( supermarketIdList );
			Map<Integer, List<SupermarketBO>> supermarketGroupBySupermarketId = supermarketService.getList( supermarket  ).stream().collect( Collectors.groupingBy( SupermarketBO::getSupermarketId ) );
			for(  OrderSkuBO item : orderSkuList ) {
				item.setSupermarketName( supermarketGroupBySupermarketId.getOrDefault( item.getSupermarketId(), Collections.emptyList() ).stream().findFirst().map( SupermarketBO::getName ).orElse( null ) );
			}
		}
		return page;
	}

	@RequestMapping("/modifyPrice.do")
	@ResponseBody
	public Object modifyPrice( OrderSkuBO orderSku ){
		orderSkuService.modifyPrice( orderSku );
		return AjaxReturnMessage.createMessage( true );
	}
	
	/**
	 * 新增/更新orderSku
	 * @param orderSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( OrderSkuBO orderSku ) {
				if( orderSku.getOrderSkuId() != null ){
					this.orderSkuService.update(orderSku);
				}else{
					this.orderSkuService.add(orderSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param orderSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long orderSkuId) {
		this.orderSkuService.delete(orderSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param orderSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long orderSkuId) {
		OrderSkuBO orderSku = new OrderSkuBO();
		orderSku.setOrderSkuId(orderSkuId);
		return this.orderSkuService.get(orderSku);
	}

	/**
	 * 编辑页面
	 * @param orderSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long orderSkuId) {
		ModelAndView mv = new ModelAndView("order/OrderSkuEdit");
//		if (orderSkuId !=null) {
//			OrderSkuBO orderSku = this.orderSkuService.get(orderSkuId);
//			mv.addObject("orderSku", orderSku);
//		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param orderSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long orderSkuId) {
		ModelAndView mv = new ModelAndView("order/OrderSkuShow");
//		if (orderSkuId !=null) {
//			OrderSkuBO orderSku = this.orderSkuService.get(orderSkuId);
//			mv.addObject("orderSku", orderSku);
//		}
		return mv;
	}

}
