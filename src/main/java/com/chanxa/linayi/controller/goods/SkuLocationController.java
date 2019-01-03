
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SkuLocationController.java
 * Description：
 * 		商品-社区网点索引表，存放该规格商品的超市所在的社区ID和商品ID对应关系控制器
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

import com.chanxa.linayi.bo.goods.SkuLocationBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SkuLocationService;

@Controller
@RequestMapping("/goods/skuLocation")
public class SkuLocationController extends CommonControllerSupport {
	@Resource
	private SkuLocationService skuLocationService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("skuLocation.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object skuLocationList(SkuLocationBO skuLocation,HttpServletRequest request) {
		return this.skuLocationService.getPage( skuLocation );
	}

	/**
	 * 新增/更新skuLocation
	 * @param skuLocation
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SkuLocationBO skuLocation ) {
				if( skuLocation.getSkuLocationId() != null ){
					this.skuLocationService.update(skuLocation);
				}else{
					this.skuLocationService.add(skuLocation);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param skuLocationId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long skuLocationId) {
		this.skuLocationService.delete(skuLocationId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param skuLocationId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long skuLocationId) {
		return this.skuLocationService.get(skuLocationId);
	}

	/**
	 * 编辑页面
	 * @param skuLocationId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long skuLocationId) {
		ModelAndView mv = new ModelAndView("goods/SkuLocationEdit");
		if (skuLocationId !=null) {
			SkuLocationBO skuLocation = this.skuLocationService.get(skuLocationId);
			mv.addObject("skuLocation", skuLocation);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param skuLocationId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long skuLocationId) {
		ModelAndView mv = new ModelAndView("goods/SkuLocationShow");
		if (skuLocationId !=null) {
			SkuLocationBO skuLocation = this.skuLocationService.get(skuLocationId);
			mv.addObject("skuLocation", skuLocation);
		}
		return mv;
	}

}
