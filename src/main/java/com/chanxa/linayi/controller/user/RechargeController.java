
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		RechargeController.java
 * Description：
 * 		充值/提现记录控制器
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

import com.chanxa.linayi.bo.user.RechargeBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.RechargeService;

@Controller
@RequestMapping("/user/recharge")
public class RechargeController extends CommonControllerSupport {
	@Resource
	private RechargeService rechargeService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("recharge.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object rechargeList(RechargeBO recharge,HttpServletRequest request) {
		return this.rechargeService.getPage( recharge );
	}

	/**
	 * 新增/更新recharge
	 * @param recharge
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( RechargeBO recharge ) {
				if( recharge.getRechargeId() != null ){
					this.rechargeService.update(recharge);
				}else{
					this.rechargeService.add(recharge);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param rechargeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long rechargeId) {
		this.rechargeService.delete(rechargeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param rechargeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long rechargeId) {
		return this.rechargeService.get(rechargeId);
	}

	/**
	 * 编辑页面
	 * @param rechargeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long rechargeId) {
		ModelAndView mv = new ModelAndView("user/RechargeEdit");
		if (rechargeId !=null) {
			RechargeBO recharge = this.rechargeService.get(rechargeId);
			mv.addObject("recharge", recharge);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param rechargeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long rechargeId) {
		ModelAndView mv = new ModelAndView("user/RechargeShow");
		if (rechargeId !=null) {
			RechargeBO recharge = this.rechargeService.get(rechargeId);
			mv.addObject("recharge", recharge);
		}
		return mv;
	}

}
