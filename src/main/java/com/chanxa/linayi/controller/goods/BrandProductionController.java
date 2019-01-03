
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BrandProductionController.java
 * Description：
 * 		品牌-产品控制器
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

import com.chanxa.linayi.bo.goods.BrandProductionBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.BrandProductionService;

@Controller
@RequestMapping("/goods/brandProduction")
public class BrandProductionController extends CommonControllerSupport {
	@Resource
	private BrandProductionService brandProductionService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("brandProduction.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object brandProductionList(BrandProductionBO brandProduction,HttpServletRequest request) {
		return this.brandProductionService.getPage( brandProduction );
	}

	/**
	 * 新增/更新brandProduction
	 * @param brandProduction
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BrandProductionBO brandProduction ) {
				if( brandProduction.getBrandProductionId() != null ){
					this.brandProductionService.update(brandProduction);
				}else{
					this.brandProductionService.add(brandProduction);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param brandProductionId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer brandProductionId) {
		this.brandProductionService.delete(brandProductionId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param brandProductionId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer brandProductionId) {
		return this.brandProductionService.get(brandProductionId);
	}

	/**
	 * 编辑页面
	 * @param brandProductionId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer brandProductionId) {
		ModelAndView mv = new ModelAndView("goods/BrandProductionEdit");
		if (brandProductionId !=null) {
			BrandProductionBO brandProduction = this.brandProductionService.get(brandProductionId);
			mv.addObject("brandProduction", brandProduction);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param brandProductionId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer brandProductionId) {
		ModelAndView mv = new ModelAndView("goods/BrandProductionShow");
		if (brandProductionId !=null) {
			BrandProductionBO brandProduction = this.brandProductionService.get(brandProductionId);
			mv.addObject("brandProduction", brandProduction);
		}
		return mv;
	}

}
