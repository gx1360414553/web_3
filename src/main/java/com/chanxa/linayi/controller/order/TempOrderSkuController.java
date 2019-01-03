
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		TempOrderSkuController.java
 * Description：
 * 		订单商品临时表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.order;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.order.TempOrderSkuBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.order.TempOrderSkuService;

@Controller
@RequestMapping("/order/tempOrderSku")
public class TempOrderSkuController extends CommonControllerSupport {
	@Resource
	private TempOrderSkuService tempOrderSkuService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("tempOrderSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object tempOrderSkuList(TempOrderSkuBO tempOrderSku,HttpServletRequest request) {
		return this.tempOrderSkuService.getPage( tempOrderSku );
	}

	/**
	 * 新增/更新tempOrderSku
	 * @param tempOrderSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( TempOrderSkuBO tempOrderSku ) {
				if( tempOrderSku.getTempOrderSkuId() != null ){
					this.tempOrderSkuService.update(tempOrderSku);
				}else{
					this.tempOrderSkuService.add(tempOrderSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param tempOrderSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long tempOrderSkuId) {
		this.tempOrderSkuService.delete(tempOrderSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param tempOrderSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long tempOrderSkuId) {
		return this.tempOrderSkuService.get(tempOrderSkuId);
	}

	/**
	 * 编辑页面
	 * @param tempOrderSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long tempOrderSkuId) {
		ModelAndView mv = new ModelAndView("order/TempOrderSkuEdit");
		if (tempOrderSkuId !=null) {
			TempOrderSkuBO tempOrderSku = this.tempOrderSkuService.get(tempOrderSkuId);
			mv.addObject("tempOrderSku", tempOrderSku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param tempOrderSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long tempOrderSkuId) {
		ModelAndView mv = new ModelAndView("order/TempOrderSkuShow");
		if (tempOrderSkuId !=null) {
			TempOrderSkuBO tempOrderSku = this.tempOrderSkuService.get(tempOrderSkuId);
			mv.addObject("tempOrderSku", tempOrderSku);
		}
		return mv;
	}

}
