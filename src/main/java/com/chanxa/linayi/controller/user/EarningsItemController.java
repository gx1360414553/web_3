
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		EarningsItemController.java
 * Description：
 * 		#收益明细，根据用户ID一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.user.EarningsItemBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.EarningsItemService;

@Controller
@RequestMapping("/user/earningsItem")
public class EarningsItemController extends CommonControllerSupport {
	@Resource
	private EarningsItemService earningsItemService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("earningsItem.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object earningsItemList(EarningsItemBO earningsItem,HttpServletRequest request) {
		return this.earningsItemService.getPage( earningsItem );
	}

	/**
	 * 新增/更新earningsItem
	 * @param earningsItem
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( EarningsItemBO earningsItem ) {
				if( earningsItem.getEarningsItemId() != null ){
					this.earningsItemService.update(earningsItem);
				}else{
					this.earningsItemService.add(earningsItem);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param earningsItemId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long earningsItemId) {
		this.earningsItemService.delete(earningsItemId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param earningsItemId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long earningsItemId) {
		return this.earningsItemService.get(earningsItemId);
	}

	/**
	 * 编辑页面
	 * @param earningsItemId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long earningsItemId) {
		ModelAndView mv = new ModelAndView("user/EarningsItemEdit");
		if (earningsItemId !=null) {
			EarningsItemBO earningsItem = this.earningsItemService.get(earningsItemId);
			mv.addObject("earningsItem", earningsItem);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param earningsItemId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long earningsItemId) {
		ModelAndView mv = new ModelAndView("user/EarningsItemShow");
		if (earningsItemId !=null) {
			EarningsItemBO earningsItem = this.earningsItemService.get(earningsItemId);
			mv.addObject("earningsItem", earningsItem);
		}
		return mv;
	}

}
