
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		PasswordLogController.java
 * Description：
 * 		支付密码日志控制器
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

import com.chanxa.linayi.bo.user.PasswordLogBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.PasswordLogService;

@Controller
@RequestMapping("/user/passwordLog")
public class PasswordLogController extends CommonControllerSupport {
	@Resource
	private PasswordLogService passwordLogService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("passwordLog.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object passwordLogList(PasswordLogBO passwordLog,HttpServletRequest request) {
		return this.passwordLogService.getPage( passwordLog );
	}

	/**
	 * 新增/更新passwordLog
	 * @param passwordLog
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( PasswordLogBO passwordLog ) {
				if( passwordLog.getPasswordLogId() != null ){
					this.passwordLogService.update(passwordLog);
				}else{
					this.passwordLogService.add(passwordLog);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param passwordLogId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long passwordLogId) {
		this.passwordLogService.delete(passwordLogId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param passwordLogId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long passwordLogId) {
		return this.passwordLogService.get(passwordLogId);
	}

	/**
	 * 编辑页面
	 * @param passwordLogId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long passwordLogId) {
		ModelAndView mv = new ModelAndView("user/PasswordLogEdit");
		if (passwordLogId !=null) {
			PasswordLogBO passwordLog = this.passwordLogService.get(passwordLogId);
			mv.addObject("passwordLog", passwordLog);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param passwordLogId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long passwordLogId) {
		ModelAndView mv = new ModelAndView("user/PasswordLogShow");
		if (passwordLogId !=null) {
			PasswordLogBO passwordLog = this.passwordLogService.get(passwordLogId);
			mv.addObject("passwordLog", passwordLog);
		}
		return mv;
	}

}
