
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		SortingSkuController.java
 * Description：
 * 		#流转商品，根据订单所在的社区ID进行一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.flower;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.flower.SortingSkuBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.flower.SortingSkuService;

@Controller
@RequestMapping("/flower/sortingSku")
public class SortingSkuController extends CommonControllerSupport {
	@Resource
	private SortingSkuService sortingSkuService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("sortingSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object sortingSkuList(SortingSkuBO sortingSku,HttpServletRequest request) {
		return this.sortingSkuService.getPage( sortingSku );
	}

	/**
	 * 新增/更新sortingSku
	 * @param sortingSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( SortingSkuBO sortingSku ) {
				if( sortingSku.getSortingSkuId() != null ){
					this.sortingSkuService.update(sortingSku);
				}else{
					this.sortingSkuService.add(sortingSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param sortingSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long sortingSkuId) {
		this.sortingSkuService.delete(sortingSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param sortingSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(SortingSkuBO sortingSku) {
		return this.sortingSkuService.get(sortingSku);
	}

	/**
	 * 编辑页面
	 * @param sortingSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(SortingSkuBO sortingSku) {
		ModelAndView mv = new ModelAndView("flower/SortingSkuEdit");
		if (sortingSku.getSortingSkuId() !=null) {
			sortingSku = this.sortingSkuService.get(sortingSku);
			mv.addObject("sortingSku", sortingSku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param sortingSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(SortingSkuBO sortingSku) {
		ModelAndView mv = new ModelAndView("flower/SortingSkuShow");
		if (sortingSku.getSortingSkuId() !=null) {
			sortingSku = this.sortingSkuService.get(sortingSku);
			mv.addObject("sortingSku", sortingSku);
		}
		return mv;
	}

}
