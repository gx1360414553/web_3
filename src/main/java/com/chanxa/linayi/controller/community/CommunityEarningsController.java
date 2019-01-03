
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CommunityEarningsController.java
 * Description：
 * 		社区收益控制器
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

import com.chanxa.linayi.bo.community.CommunityEarningsBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.community.CommunityEarningsService;

@Controller
@RequestMapping("/community/communityEarnings")
public class CommunityEarningsController extends CommonControllerSupport {
	@Resource
	private CommunityEarningsService communityEarningsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("communityEarnings.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communityEarningsList(CommunityEarningsBO communityEarnings,HttpServletRequest request) {
		return this.communityEarningsService.getPage( communityEarnings );
	}

	/**
	 * 新增/更新communityEarnings
	 * @param communityEarnings
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CommunityEarningsBO communityEarnings ) {
				if( communityEarnings.getCommunityEarningsId() != null ){
					this.communityEarningsService.update(communityEarnings);
				}else{
					this.communityEarningsService.add(communityEarnings);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param communityEarningsId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long communityEarningsId) {
		this.communityEarningsService.delete(communityEarningsId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param communityEarningsId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long communityEarningsId) {
		return this.communityEarningsService.get(communityEarningsId);
	}

	/**
	 * 编辑页面
	 * @param communityEarningsId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long communityEarningsId) {
		ModelAndView mv = new ModelAndView("community/CommunityEarningsEdit");
		if (communityEarningsId !=null) {
			CommunityEarningsBO communityEarnings = this.communityEarningsService.get(communityEarningsId);
			mv.addObject("communityEarnings", communityEarnings);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param communityEarningsId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long communityEarningsId) {
		ModelAndView mv = new ModelAndView("community/CommunityEarningsShow");
		if (communityEarningsId !=null) {
			CommunityEarningsBO communityEarnings = this.communityEarningsService.get(communityEarningsId);
			mv.addObject("communityEarnings", communityEarnings);
		}
		return mv;
	}

}
