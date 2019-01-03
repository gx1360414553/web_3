
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CommunityController.java
 * Description：
 * 		社区网点控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunityService;

@Controller
@RequestMapping("/community/community")
public class CommunityController extends CommonControllerSupport {
	@Resource
	private CommunityService communityService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("community.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communityList(CommunityBO community,HttpServletRequest request) {
		List<CommunityBO> list = this.communityService.listCommunityInWeb( community );
		PageResult<CommunityBO> page = new PageResult<>();
		page.setTotal( community.getTotal() );
		page.setRows( list );
		return page;	
	}

	/**
	 * 新增/更新community
	 * @param community
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CommunityBO community ) {
		this.communityService.saveInWeb(community);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param communityId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer communityId) {
		this.communityService.delete(communityId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param communityId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer communityId) {
		return this.communityService.get(communityId);
	}

	/**
	 * 编辑页面
	 * @param communityId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(CommunityBO community) {
		ModelAndView mv = new ModelAndView("community/CommunityEdit");
		if (community.getCommunityId() !=null) {
			community = this.communityService.listCommunityInWeb( community ).stream().findFirst().orElse( null );
			mv.addObject("community", community);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param communityId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer communityId) {
		ModelAndView mv = new ModelAndView("community/CommunityShow");
		if (communityId !=null) {
			CommunityBO community = this.communityService.get(communityId);
			mv.addObject("community", community);
		}
		return mv;
	}
	
}
