
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CommunityEarningsItemController.java
 * Description：
 * 		#社区收益明细，根据社区ID一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.community.CommunityEarningsItemBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.community.CommunityEarningsItemService;

@Controller
@RequestMapping("/community/communityEarningsItem")
public class CommunityEarningsItemController extends CommonControllerSupport {
	@Resource
	private CommunityEarningsItemService communityEarningsItemService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("communityEarningsItem.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communityEarningsItemList(CommunityEarningsItemBO communityEarningsItem,HttpServletRequest request) {
		return this.communityEarningsItemService.getPage( communityEarningsItem );
	}

	/**
	 * 新增/更新communityEarningsItem
	 * @param communityEarningsItem
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CommunityEarningsItemBO communityEarningsItem ) {
				if( communityEarningsItem.getCommunityEarningsItemId() != null ){
					this.communityEarningsItemService.update(communityEarningsItem);
				}else{
					this.communityEarningsItemService.add(communityEarningsItem);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param communityEarningsItemId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long communityEarningsItemId) {
		this.communityEarningsItemService.delete(communityEarningsItemId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param communityEarningsItemId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long communityEarningsItemId) {
		return this.communityEarningsItemService.get(communityEarningsItemId);
	}

	/**
	 * 编辑页面
	 * @param communityEarningsItemId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long communityEarningsItemId) {
		ModelAndView mv = new ModelAndView("community/CommunityEarningsItemEdit");
		if (communityEarningsItemId !=null) {
			CommunityEarningsItemBO communityEarningsItem = this.communityEarningsItemService.get(communityEarningsItemId);
			mv.addObject("communityEarningsItem", communityEarningsItem);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param communityEarningsItemId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long communityEarningsItemId) {
		ModelAndView mv = new ModelAndView("community/CommunityEarningsItemShow");
		if (communityEarningsItemId !=null) {
			CommunityEarningsItemBO communityEarningsItem = this.communityEarningsItemService.get(communityEarningsItemId);
			mv.addObject("communityEarningsItem", communityEarningsItem);
		}
		return mv;
	}

}
