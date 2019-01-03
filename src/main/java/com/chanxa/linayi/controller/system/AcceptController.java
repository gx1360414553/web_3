
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AcceptController.java
 * Description：
 * 		消息接收对象控制器
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

import com.chanxa.linayi.bo.system.AcceptBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.system.AcceptService;

@Controller
@RequestMapping("/system/accept")
public class AcceptController extends CommonControllerSupport {
	@Resource
	private AcceptService acceptService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("accept.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object acceptList(AcceptBO accept,HttpServletRequest request) {
		return this.acceptService.getPage( accept );
	}

	/**
	 * 新增/更新accept
	 * @param accept
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( AcceptBO accept ) {
				if( accept.getAcceptId() != null ){
					this.acceptService.update(accept);
				}else{
					this.acceptService.add(accept);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param acceptId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long acceptId) {
		this.acceptService.delete(acceptId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param acceptId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long acceptId) {
		return this.acceptService.get(acceptId);
	}

	/**
	 * 编辑页面
	 * @param acceptId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long acceptId) {
		ModelAndView mv = new ModelAndView("system/AcceptEdit");
		if (acceptId !=null) {
			AcceptBO accept = this.acceptService.get(acceptId);
			mv.addObject("accept", accept);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param acceptId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long acceptId) {
		ModelAndView mv = new ModelAndView("system/AcceptShow");
		if (acceptId !=null) {
			AcceptBO accept = this.acceptService.get(acceptId);
			mv.addObject("accept", accept);
		}
		return mv;
	}

}
