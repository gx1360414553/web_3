
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		EarningsController.java
 * Description：
 * 		#用户收益，根据用户ID一致性hash分库分表控制器
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

import com.chanxa.linayi.bo.user.EarningsBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.EarningsService;

@Controller
@RequestMapping("/user/earnings")
public class EarningsController extends CommonControllerSupport {
	@Resource
	private EarningsService earningsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("earnings.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object earningsList(EarningsBO earnings,HttpServletRequest request) {
		return this.earningsService.getPage( earnings );
	}

	/**
	 * 新增/更新earnings
	 * @param earnings
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( EarningsBO earnings ) {
				if( earnings.getEarningsId() != null ){
					this.earningsService.update(earnings);
				}else{
					this.earningsService.add(earnings);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param earningsId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long earningsId) {
		this.earningsService.delete(earningsId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param earningsId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long earningsId) {
		return this.earningsService.get(earningsId);
	}

	/**
	 * 编辑页面
	 * @param earningsId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long earningsId) {
		ModelAndView mv = new ModelAndView("user/EarningsEdit");
		if (earningsId !=null) {
			EarningsBO earnings = this.earningsService.get(earningsId);
			mv.addObject("earnings", earnings);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param earningsId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long earningsId) {
		ModelAndView mv = new ModelAndView("user/EarningsShow");
		if (earningsId !=null) {
			EarningsBO earnings = this.earningsService.get(earningsId);
			mv.addObject("earnings", earnings);
		}
		return mv;
	}

}
