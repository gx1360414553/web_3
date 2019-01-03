
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		ProcurementOrderController.java
 * Description：
 * 		#任务订单商品，根据采买任务社区ID一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.task;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.task.ProcurementOrderBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.task.ProcurementOrderService;

@Controller
@RequestMapping("/task/procurementOrder")
public class ProcurementOrderController extends CommonControllerSupport {
	@Resource
	private ProcurementOrderService procurementOrderService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("procurementOrder.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object procurementOrderList(ProcurementOrderBO procurementOrder,HttpServletRequest request) {
		return this.procurementOrderService.getPage( procurementOrder );
	}

	/**
	 * 新增/更新procurementOrder
	 * @param procurementOrder
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( ProcurementOrderBO procurementOrder ) {
				if( procurementOrder.getProcurementOrderId() != null ){
					this.procurementOrderService.update(procurementOrder);
				}else{
					this.procurementOrderService.add(procurementOrder);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param procurementOrderId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long procurementOrderId) {
		this.procurementOrderService.delete(procurementOrderId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param procurementOrderId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long procurementOrderId) {
		return this.procurementOrderService.get(procurementOrderId);
	}

	/**
	 * 编辑页面
	 * @param procurementOrderId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long procurementOrderId) {
		ModelAndView mv = new ModelAndView("task/ProcurementOrderEdit");
		if (procurementOrderId !=null) {
			ProcurementOrderBO procurementOrder = this.procurementOrderService.get(procurementOrderId);
			mv.addObject("procurementOrder", procurementOrder);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param procurementOrderId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long procurementOrderId) {
		ModelAndView mv = new ModelAndView("task/ProcurementOrderShow");
		if (procurementOrderId !=null) {
			ProcurementOrderBO procurementOrder = this.procurementOrderService.get(procurementOrderId);
			mv.addObject("procurementOrder", procurementOrder);
		}
		return mv;
	}

}
