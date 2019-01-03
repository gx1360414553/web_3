
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CategoryGoodsController.java
 * Description：
 * 		货品-类别控制器
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

import com.chanxa.linayi.bo.goods.CategoryGoodsBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.CategoryGoodsService;

@Controller
@RequestMapping("/goods/categoryGoods")
public class CategoryGoodsController extends CommonControllerSupport {
	@Resource
	private CategoryGoodsService categoryGoodsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("categoryGoods.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object categoryGoodsList(CategoryGoodsBO categoryGoods,HttpServletRequest request) {
		return this.categoryGoodsService.getPage( categoryGoods );
	}

	/**
	 * 新增/更新categoryGoods
	 * @param categoryGoods
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CategoryGoodsBO categoryGoods ) {
				if( categoryGoods.getCategoryGoodsId() != null ){
					this.categoryGoodsService.update(categoryGoods);
				}else{
					this.categoryGoodsService.add(categoryGoods);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param categoryGoodsId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer categoryGoodsId) {
		this.categoryGoodsService.delete(categoryGoodsId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param categoryGoodsId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer categoryGoodsId) {
		return this.categoryGoodsService.get(categoryGoodsId);
	}

	/**
	 * 编辑页面
	 * @param categoryGoodsId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer categoryGoodsId) {
		ModelAndView mv = new ModelAndView("goods/CategoryGoodsEdit");
		if (categoryGoodsId !=null) {
			CategoryGoodsBO categoryGoods = this.categoryGoodsService.get(categoryGoodsId);
			mv.addObject("categoryGoods", categoryGoods);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param categoryGoodsId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer categoryGoodsId) {
		ModelAndView mv = new ModelAndView("goods/CategoryGoodsShow");
		if (categoryGoodsId !=null) {
			CategoryGoodsBO categoryGoods = this.categoryGoodsService.get(categoryGoodsId);
			mv.addObject("categoryGoods", categoryGoods);
		}
		return mv;
	}

}
