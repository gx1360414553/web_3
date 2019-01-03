
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AfterSaleController.java
 * Description：
 * 		售后申请控制器
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

import com.chanxa.linayi.bo.order.AfterSaleBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.order.AfterSaleService;

@Controller
@RequestMapping("/order/afterSale")
public class AfterSaleController extends CommonControllerSupport {
	@Resource
	private AfterSaleService afterSaleService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("afterSale.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object afterSaleList(AfterSaleBO afterSale,HttpServletRequest request) {
		return this.afterSaleService.getPage( afterSale );
	}

	/**
	 * 新增/更新afterSale
	 * @param afterSale
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( AfterSaleBO afterSale ) {
				if( afterSale.getAfterSaleId() != null ){
					this.afterSaleService.update(afterSale);
				}else{
					this.afterSaleService.add(afterSale);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param afterSaleId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long afterSaleId) {
		this.afterSaleService.delete(afterSaleId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param afterSaleId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long afterSaleId) {
		return this.afterSaleService.get(afterSaleId);
	}

	/**
	 * 编辑页面
	 * @param afterSaleId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long afterSaleId) {
		ModelAndView mv = new ModelAndView("order/AfterSaleEdit");
		if (afterSaleId !=null) {
			AfterSaleBO afterSale = this.afterSaleService.get(afterSaleId);
			mv.addObject("afterSale", afterSale);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param afterSaleId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long afterSaleId) {
		ModelAndView mv = new ModelAndView("order/AfterSaleShow");
		if (afterSaleId !=null) {
			AfterSaleBO afterSale = this.afterSaleService.get(afterSaleId);
			mv.addObject("afterSale", afterSale);
		}
		return mv;
	}

}
