
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SkuCommunityController.java
 * Description：
 * 		商品-社区网点控制器
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

import com.chanxa.linayi.bo.goods.SkuCommunityBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SkuCommunityService;

@Controller
@RequestMapping("/goods/skuCommunity")
public class SkuCommunityController extends CommonControllerSupport {
	@Resource
	private SkuCommunityService skuCommunityService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("skuCommunity.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object skuCommunityList(SkuCommunityBO skuCommunity,HttpServletRequest request) {
		return this.skuCommunityService.getPage( skuCommunity );
	}

	/**
	 * 新增/更新skuCommunity
	 * @param skuCommunity
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SkuCommunityBO skuCommunity ) {
				if( skuCommunity.getSkuCommunityId() != null ){
					this.skuCommunityService.update(skuCommunity);
				}else{
					this.skuCommunityService.add(skuCommunity);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param skuCommunityId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long skuCommunityId) {
		this.skuCommunityService.delete(skuCommunityId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param skuCommunityId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long skuCommunityId) {
		return this.skuCommunityService.get(skuCommunityId);
	}

	/**
	 * 编辑页面
	 * @param skuCommunityId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long skuCommunityId) {
		ModelAndView mv = new ModelAndView("goods/SkuCommunityEdit");
		if (skuCommunityId !=null) {
			SkuCommunityBO skuCommunity = this.skuCommunityService.get(skuCommunityId);
			mv.addObject("skuCommunity", skuCommunity);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param skuCommunityId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long skuCommunityId) {
		ModelAndView mv = new ModelAndView("goods/SkuCommunityShow");
		if (skuCommunityId !=null) {
			SkuCommunityBO skuCommunity = this.skuCommunityService.get(skuCommunityId);
			mv.addObject("skuCommunity", skuCommunity);
		}
		return mv;
	}

}
