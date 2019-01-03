
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CategoryBrandController.java
 * Description：
 * 		类别-品牌控制器
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

import com.chanxa.linayi.bo.goods.CategoryBrandBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.CategoryBrandService;

@Controller
@RequestMapping("/goods/categoryBrand")
public class CategoryBrandController extends CommonControllerSupport {
	@Resource
	private CategoryBrandService categoryBrandService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("categoryBrand.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object categoryBrandList(CategoryBrandBO categoryBrand,HttpServletRequest request) {
		return this.categoryBrandService.getPage( categoryBrand );
	}

	/**
	 * 新增/更新categoryBrand
	 * @param categoryBrand
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CategoryBrandBO categoryBrand ) {
				if( categoryBrand.getCategoryBrandId() != null ){
					this.categoryBrandService.update(categoryBrand);
				}else{
					this.categoryBrandService.add(categoryBrand);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param categoryBrandId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer categoryBrandId) {
		this.categoryBrandService.delete(categoryBrandId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param categoryBrandId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer categoryBrandId) {
		return this.categoryBrandService.get(categoryBrandId);
	}

	/**
	 * 编辑页面
	 * @param categoryBrandId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer categoryBrandId) {
		ModelAndView mv = new ModelAndView("goods/CategoryBrandEdit");
		if (categoryBrandId !=null) {
			CategoryBrandBO categoryBrand = this.categoryBrandService.get(categoryBrandId);
			mv.addObject("categoryBrand", categoryBrand);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param categoryBrandId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer categoryBrandId) {
		ModelAndView mv = new ModelAndView("goods/CategoryBrandShow");
		if (categoryBrandId !=null) {
			CategoryBrandBO categoryBrand = this.categoryBrandService.get(categoryBrandId);
			mv.addObject("categoryBrand", categoryBrand);
		}
		return mv;
	}

}
