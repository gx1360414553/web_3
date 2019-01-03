
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SkuSupermarketTagController.java
 * Description：
 * 		商品-标签控制器
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

import com.chanxa.linayi.bo.goods.SkuSupermarketTagBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SkuSupermarketTagService;

@Controller
@RequestMapping("/goods/skuSupermarketTag")
public class SkuSupermarketTagController extends CommonControllerSupport {
	@Resource
	private SkuSupermarketTagService skuSupermarketTagService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("skuSupermarketTag.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object skuSupermarketTagList(SkuSupermarketTagBO skuSupermarketTag,HttpServletRequest request) {
		return this.skuSupermarketTagService.getPage( skuSupermarketTag );
	}

	/**
	 * 新增/更新skuSupermarketTag
	 * @param skuSupermarketTag
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SkuSupermarketTagBO skuSupermarketTag ) {
				if( skuSupermarketTag.getSkuSupermarketTagId() != null ){
					this.skuSupermarketTagService.update(skuSupermarketTag);
				}else{
					this.skuSupermarketTagService.add(skuSupermarketTag);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param skuSupermarketTagId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer skuSupermarketTagId) {
		this.skuSupermarketTagService.delete(skuSupermarketTagId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param skuSupermarketTagId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer skuSupermarketTagId) {
		return this.skuSupermarketTagService.get(skuSupermarketTagId);
	}

	/**
	 * 编辑页面
	 * @param skuSupermarketTagId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer skuSupermarketTagId) {
		ModelAndView mv = new ModelAndView("goods/SkuSupermarketTagEdit");
		if (skuSupermarketTagId !=null) {
			SkuSupermarketTagBO skuSupermarketTag = this.skuSupermarketTagService.get(skuSupermarketTagId);
			mv.addObject("skuSupermarketTag", skuSupermarketTag);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param skuSupermarketTagId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer skuSupermarketTagId) {
		ModelAndView mv = new ModelAndView("goods/SkuSupermarketTagShow");
		if (skuSupermarketTagId !=null) {
			SkuSupermarketTagBO skuSupermarketTag = this.skuSupermarketTagService.get(skuSupermarketTagId);
			mv.addObject("skuSupermarketTag", skuSupermarketTag);
		}
		return mv;
	}

}
