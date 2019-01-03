
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		EvaluateController.java
 * Description：
 * 		服务评价控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.order;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.order.EvaluateBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.order.EvaluateService;

@Controller
@RequestMapping("/order/evaluate")
public class EvaluateController extends CommonControllerSupport {
	@Resource
	private EvaluateService evaluateService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("evaluate.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object evaluateList(EvaluateBO evaluate,HttpServletRequest request) {
		return this.evaluateService.getPage( evaluate );
	}

	/**
	 * 新增/更新evaluate
	 * @param evaluate
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( EvaluateBO evaluate ) {
				if( evaluate.getEvaluateId() != null ){
					this.evaluateService.update(evaluate);
				}else{
					this.evaluateService.add(evaluate);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param evaluateId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long evaluateId) {
		this.evaluateService.delete(evaluateId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param evaluateId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long evaluateId) {
		return this.evaluateService.get(evaluateId);
	}

	/**
	 * 编辑页面
	 * @param evaluateId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long evaluateId) {
		ModelAndView mv = new ModelAndView("order/EvaluateEdit");
		if (evaluateId !=null) {
			EvaluateBO evaluate = this.evaluateService.get(evaluateId);
			mv.addObject("evaluate", evaluate);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param evaluateId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long evaluateId) {
		ModelAndView mv = new ModelAndView("order/EvaluateShow");
		if (evaluateId !=null) {
			EvaluateBO evaluate = this.evaluateService.get(evaluateId);
			mv.addObject("evaluate", evaluate);
		}
		return mv;
	}

}
