
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SkuAttributeController.java
 * Description：
 * 		商品-属性值控制器
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

import com.chanxa.linayi.bo.goods.SkuAttributeBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SkuAttributeService;

@Controller
@RequestMapping("/goods/skuAttribute")
public class SkuAttributeController extends CommonControllerSupport {
	@Resource
	private SkuAttributeService skuAttributeService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("skuAttribute.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object skuAttributeList(SkuAttributeBO skuAttribute,HttpServletRequest request) {
		return this.skuAttributeService.getPage( skuAttribute );
	}

	/**
	 * 新增/更新skuAttribute
	 * @param skuAttribute
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SkuAttributeBO skuAttribute ) {
				if( skuAttribute.getSkuAttributeId() != null ){
					this.skuAttributeService.update(skuAttribute);
				}else{
					this.skuAttributeService.add(skuAttribute);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param skuAttributeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer skuAttributeId) {
		this.skuAttributeService.delete(skuAttributeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param skuAttributeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer skuAttributeId) {
		return this.skuAttributeService.get(skuAttributeId);
	}

	/**
	 * 编辑页面
	 * @param skuAttributeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer skuAttributeId) {
		ModelAndView mv = new ModelAndView("goods/SkuAttributeEdit");
		if (skuAttributeId !=null) {
			SkuAttributeBO skuAttribute = this.skuAttributeService.get(skuAttributeId);
			mv.addObject("skuAttribute", skuAttribute);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param skuAttributeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer skuAttributeId) {
		ModelAndView mv = new ModelAndView("goods/SkuAttributeShow");
		if (skuAttributeId !=null) {
			SkuAttributeBO skuAttribute = this.skuAttributeService.get(skuAttributeId);
			mv.addObject("skuAttribute", skuAttribute);
		}
		return mv;
	}

}
