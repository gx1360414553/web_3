
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		DeliverySkuController.java
 * Description：
 * 		#配送任务-商品，根据订单社区ID一致性hash分库分表控制器
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

import com.chanxa.linayi.bo.task.DeliverySkuBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.task.DeliverySkuService;

@Controller
@RequestMapping("/task/deliverySku")
public class DeliverySkuController extends CommonControllerSupport {
	@Resource
	private DeliverySkuService deliverySkuService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("deliverySku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object deliverySkuList(DeliverySkuBO deliverySku,HttpServletRequest request) {
		return this.deliverySkuService.getPage( deliverySku );
	}

	/**
	 * 新增/更新deliverySku
	 * @param deliverySku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( DeliverySkuBO deliverySku ) {
				if( deliverySku.getDeliverySkuId() != null ){
					this.deliverySkuService.update(deliverySku);
				}else{
					this.deliverySkuService.add(deliverySku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param deliverySkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long deliverySkuId) {
		this.deliverySkuService.delete(deliverySkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param deliverySkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long deliverySkuId) {
		return this.deliverySkuService.get(deliverySkuId);
	}

	/**
	 * 编辑页面
	 * @param deliverySkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long deliverySkuId) {
		ModelAndView mv = new ModelAndView("task/DeliverySkuEdit");
		if (deliverySkuId !=null) {
			DeliverySkuBO deliverySku = this.deliverySkuService.get(deliverySkuId);
			mv.addObject("deliverySku", deliverySku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param deliverySkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long deliverySkuId) {
		ModelAndView mv = new ModelAndView("task/DeliverySkuShow");
		if (deliverySkuId !=null) {
			DeliverySkuBO deliverySku = this.deliverySkuService.get(deliverySkuId);
			mv.addObject("deliverySku", deliverySku);
		}
		return mv;
	}

}
