
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		MessageController.java
 * Description：
 * 		系统消息控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.system.MessageBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.system.MessageService;

@Controller
@RequestMapping("/system/message")
public class MessageController extends CommonControllerSupport {
	@Resource
	private MessageService messageService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("message.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object messageList(MessageBO message,HttpServletRequest request) {
		return this.messageService.getPage( message );
	}

	/**
	 * 新增/更新message
	 * @param message
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( MessageBO message ) {
				if( message.getMessageId() != null ){
					this.messageService.update(message);
				}else{
					this.messageService.add(message);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param messageId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long messageId) {
		this.messageService.delete(messageId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param messageId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long messageId) {
		return this.messageService.get(messageId);
	}

	/**
	 * 编辑页面
	 * @param messageId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long messageId) {
		ModelAndView mv = new ModelAndView("system/MessageEdit");
		if (messageId !=null) {
			MessageBO message = this.messageService.get(messageId);
			mv.addObject("message", message);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param messageId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long messageId) {
		ModelAndView mv = new ModelAndView("system/MessageShow");
		if (messageId !=null) {
			MessageBO message = this.messageService.get(messageId);
			mv.addObject("message", message);
		}
		return mv;
	}

}
