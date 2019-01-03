
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BrandController.java
 * Description：
 * 		品牌，类别->品牌->产品控制器
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

import com.chanxa.linayi.bo.goods.BrandBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.BrandService;

@Controller
@RequestMapping("/goods/brand")
public class BrandController extends CommonControllerSupport {
	@Resource
	private BrandService brandService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("brand.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object brandList(BrandBO brand,HttpServletRequest request) {
		return this.brandService.getPage( brand );
	}

	/**
	 * 新增/更新brand
	 * @param brand
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BrandBO brand ) {
				if( brand.getBrandId() != null ){
					this.brandService.update(brand);
				}else{
					this.brandService.add(brand);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param brandId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer brandId) {
		this.brandService.delete(brandId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param brandId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer brandId) {
		return this.brandService.get(brandId);
	}

	/**
	 * 编辑页面
	 * @param brandId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer brandId) {
		ModelAndView mv = new ModelAndView("goods/BrandEdit");
		if (brandId !=null) {
			BrandBO brand = this.brandService.get(brandId);
			mv.addObject("brand", brand);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param brandId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer brandId) {
		ModelAndView mv = new ModelAndView("goods/BrandShow");
		if (brandId !=null) {
			BrandBO brand = this.brandService.get(brandId);
			mv.addObject("brand", brand);
		}
		return mv;
	}

}
