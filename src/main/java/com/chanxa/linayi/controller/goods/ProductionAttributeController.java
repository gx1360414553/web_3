
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		ProductionAttributeController.java
 * Description：
 * 		产品-属性控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.goods;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.goods.ProductionAttributeBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.ProductionAttributeService;

@Controller
@RequestMapping("/goods/productionAttribute")
public class ProductionAttributeController extends CommonControllerSupport {
	@Resource
	private ProductionAttributeService productionAttributeService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productionAttribute.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object productionAttributeList(ProductionAttributeBO productionAttribute,HttpServletRequest request) {
		return this.productionAttributeService.getPage( productionAttribute );
	}

	/**
	 * 新增/更新productionAttribute
	 * @param productionAttribute
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( ProductionAttributeBO productionAttribute ) {
				if( productionAttribute.getProductionAttributeId() != null ){
					this.productionAttributeService.update(productionAttribute);
				}else{
					this.productionAttributeService.add(productionAttribute);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param productionAttributeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer productionAttributeId) {
		this.productionAttributeService.delete(productionAttributeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param productionAttributeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer productionAttributeId) {
		return this.productionAttributeService.get(productionAttributeId);
	}

	/**
	 * 编辑页面
	 * @param productionAttributeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer productionAttributeId) {
		ModelAndView mv = new ModelAndView("goods/ProductionAttributeEdit");
		if (productionAttributeId !=null) {
			ProductionAttributeBO productionAttribute = this.productionAttributeService.get(productionAttributeId);
			mv.addObject("productionAttribute", productionAttribute);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param productionAttributeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer productionAttributeId) {
		ModelAndView mv = new ModelAndView("goods/ProductionAttributeShow");
		if (productionAttributeId !=null) {
			ProductionAttributeBO productionAttribute = this.productionAttributeService.get(productionAttributeId);
			mv.addObject("productionAttribute", productionAttribute);
		}
		return mv;
	}

}
