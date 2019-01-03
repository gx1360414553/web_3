
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SortingCategoryController.java
 * Description：
 * 		分拣类别控制器
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

import com.chanxa.linayi.bo.goods.SortingCategoryBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SortingCategoryService;

@Controller
@RequestMapping("/goods/sortingCategory")
public class SortingCategoryController extends CommonControllerSupport {
	@Resource
	private SortingCategoryService sortingCategoryService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("sortingCategory.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object sortingCategoryList(SortingCategoryBO sortingCategory,HttpServletRequest request) {
		return this.sortingCategoryService.getPage( sortingCategory );
	}

	/**
	 * 新增/更新sortingCategory
	 * @param sortingCategory
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SortingCategoryBO sortingCategory ) {
				if( sortingCategory.getSortingCategoryId() != null ){
					this.sortingCategoryService.update(sortingCategory);
				}else{
					this.sortingCategoryService.add(sortingCategory);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param sortingCategoryId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer sortingCategoryId) {
		this.sortingCategoryService.delete(sortingCategoryId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param sortingCategoryId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer sortingCategoryId) {
		return this.sortingCategoryService.get(sortingCategoryId);
	}

	/**
	 * 编辑页面
	 * @param sortingCategoryId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer sortingCategoryId) {
		ModelAndView mv = new ModelAndView("goods/SortingCategoryEdit");
		if (sortingCategoryId !=null) {
			SortingCategoryBO sortingCategory = this.sortingCategoryService.get(sortingCategoryId);
			mv.addObject("sortingCategory", sortingCategory);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param sortingCategoryId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer sortingCategoryId) {
		ModelAndView mv = new ModelAndView("goods/SortingCategoryShow");
		if (sortingCategoryId !=null) {
			SortingCategoryBO sortingCategory = this.sortingCategoryService.get(sortingCategoryId);
			mv.addObject("sortingCategory", sortingCategory);
		}
		return mv;
	}

}
