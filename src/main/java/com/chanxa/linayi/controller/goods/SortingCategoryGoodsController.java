
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SortingCategoryGoodsController.java
 * Description：
 * 		分拣类别-货品控制器
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

import com.chanxa.linayi.bo.goods.SortingCategoryGoodsBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.SortingCategoryGoodsService;

@Controller
@RequestMapping("/goods/sortingCategoryGoods")
public class SortingCategoryGoodsController extends CommonControllerSupport {
	@Resource
	private SortingCategoryGoodsService sortingCategoryGoodsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("sortingCategoryGoods.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object sortingCategoryGoodsList(SortingCategoryGoodsBO sortingCategoryGoods,HttpServletRequest request) {
		return this.sortingCategoryGoodsService.getPage( sortingCategoryGoods );
	}

	/**
	 * 新增/更新sortingCategoryGoods
	 * @param sortingCategoryGoods
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SortingCategoryGoodsBO sortingCategoryGoods ) {
				if( sortingCategoryGoods.getSortingCategoryGoodsId() != null ){
					this.sortingCategoryGoodsService.update(sortingCategoryGoods);
				}else{
					this.sortingCategoryGoodsService.add(sortingCategoryGoods);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param sortingCategoryGoodsId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer sortingCategoryGoodsId) {
		this.sortingCategoryGoodsService.delete(sortingCategoryGoodsId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param sortingCategoryGoodsId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer sortingCategoryGoodsId) {
		return this.sortingCategoryGoodsService.get(sortingCategoryGoodsId);
	}

	/**
	 * 编辑页面
	 * @param sortingCategoryGoodsId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer sortingCategoryGoodsId) {
		ModelAndView mv = new ModelAndView("goods/SortingCategoryGoodsEdit");
		if (sortingCategoryGoodsId !=null) {
			SortingCategoryGoodsBO sortingCategoryGoods = this.sortingCategoryGoodsService.get(sortingCategoryGoodsId);
			mv.addObject("sortingCategoryGoods", sortingCategoryGoods);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param sortingCategoryGoodsId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer sortingCategoryGoodsId) {
		ModelAndView mv = new ModelAndView("goods/SortingCategoryGoodsShow");
		if (sortingCategoryGoodsId !=null) {
			SortingCategoryGoodsBO sortingCategoryGoods = this.sortingCategoryGoodsService.get(sortingCategoryGoodsId);
			mv.addObject("sortingCategoryGoods", sortingCategoryGoods);
		}
		return mv;
	}

}
