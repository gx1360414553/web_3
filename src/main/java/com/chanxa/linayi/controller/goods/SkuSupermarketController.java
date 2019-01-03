
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SkuSupermarketController.java
 * Description：
 * 		#商品-超市，根据负责超市社区ID一致性hash分库分表，被绑定的比价超市商品复制到绑定该比价超市社区所在库控制器
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

import com.chanxa.linayi.bo.goods.SkuSupermarketBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.goods.SkuSupermarketService;

@Controller
@RequestMapping("/goods/skuSupermarket")
public class SkuSupermarketController extends CommonControllerSupport {
	@Resource
	private SkuSupermarketService skuSupermarketService;
	
	@Resource
	private CommunitySupermarketService communitySupermarketService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("skuSupermarket.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object skuSupermarketList(SkuSupermarketBO skuSupermarket,HttpServletRequest request) {
		return this.skuSupermarketService.getPage( skuSupermarket );
	}

	/**
	 * 新增/更新skuSupermarket
	 * @param skuSupermarket
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SkuSupermarketBO skuSupermarket ) {
				if( skuSupermarket.getSkuSupermarketId() != null ){
					this.skuSupermarketService.update(skuSupermarket);
				}else{
					this.skuSupermarketService.add(skuSupermarket);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param skuSupermarketId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(SkuSupermarketBO skuSupermarket) {
		this.skuSupermarketService.delete(skuSupermarket);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param skuSupermarketId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(SkuSupermarketBO skuSupermarket) {
		return this.skuSupermarketService.get(skuSupermarket);
	}

	/**
	 * 编辑页面
	 * @param skuSupermarketId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(SkuSupermarketBO skuSupermarket) {
		ModelAndView mv = new ModelAndView("goods/SkuSupermarketEdit");
		if (skuSupermarket.getSkuSupermarketId() !=null) {
			skuSupermarket = this.skuSupermarketService.get(skuSupermarket);
			mv.addObject("skuSupermarket", skuSupermarket);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param skuSupermarketId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(SkuSupermarketBO skuSupermarket) {
		ModelAndView mv = new ModelAndView("goods/SkuSupermarketShow");
		if (skuSupermarket.getSupermarketId() !=null) {
			skuSupermarket = this.skuSupermarketService.get(skuSupermarket);
			mv.addObject("skuSupermarket", skuSupermarket);
		}
		return mv;
	}

}
