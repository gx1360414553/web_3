
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		CommunityAccountController.java
* Description：
* 		社区网点账号控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.community.CommunityAccountBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.MD5;
import com.chanxa.linayi.service.community.CommunityAccountService;

@Controller
@RequestMapping("/community/communityAccount")
public class CommunityAccountController extends CommonControllerSupport {
	@Resource
	private CommunityAccountService communityAccountService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("communityAccount.");
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communityAccountList(CommunityAccountBO communityAccount, HttpServletRequest request) {
		return this.communityAccountService.getPage(communityAccount);
	}

	/**
	 * 新增/更新communityAccount
	 * 
	 * @param communityAccount
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(CommunityAccountBO communityAccount, @RequestParam("toChangePSW") String toChangePSW) {
		Integer communityAccountId = communityAccount.getCommunityAccountId();

		if (communityAccountId != null) {
			/* 加密密码 */
			communityAccount.setPassword(MD5.MD5(communityAccount.getPassword()));
			List<CommunityAccountBO> communityAccountBOs = communityAccountService.getList(communityAccount);
			
			if (communityAccountBOs.size() < 1) {
				return AjaxReturnMessage.createMessage("密码错误", false);
			}
			communityAccount.setPassword(MD5.MD5(toChangePSW));
			this.communityAccountService.update(communityAccount);

		} else {
			this.communityAccountService.add(communityAccount);
		}
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param communityAccountId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer communityAccountId) {
		this.communityAccountService.delete(communityAccountId);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param communityAccountId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer communityAccountId) {
		return this.communityAccountService.get(communityAccountId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param communityAccountId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer communityAccountId) {
		ModelAndView mv = new ModelAndView("community/CommunityAccountEdit");
		if (communityAccountId != null) {
			CommunityAccountBO communityAccount = this.communityAccountService.get(communityAccountId);
			mv.addObject("communityAccount", communityAccount);
		}
		return mv;
	}

	/**
	 * 详细页面
	 * 
	 * @param communityAccountId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer communityAccountId) {
		ModelAndView mv = new ModelAndView("community/CommunityAccountShow");
		if (communityAccountId != null) {
			CommunityAccountBO communityAccount = this.communityAccountService.get(communityAccountId);
			mv.addObject("communityAccount", communityAccount);
		}
		return mv;
	}

}
