
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		DeliveryTaskController.java
 * Description：
 * 		#配送任务，根据社区ID一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.task;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.task.DeliveryTaskPopulator;
import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.order.OrderBO;
import com.chanxa.linayi.bo.task.DeliveryTaskBO;
import com.chanxa.linayi.bo.user.UserBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.DeliveryTaskStatusEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.order.OrderService;
import com.chanxa.linayi.service.task.DeliveryTaskService;
import com.chanxa.linayi.service.user.UserService;
import com.chanxa.linayi.util.system.AreaUtil;

@Controller
@RequestMapping("/task/deliveryTask")
public class DeliveryTaskController extends CommonControllerSupport {
	@Resource
	private DeliveryTaskService deliveryTaskService;

	@Resource
	private DeliveryTaskPopulator deliveryTaskPopulator;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private UserService userService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("deliveryTask.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object deliveryTaskList(DeliveryTaskBO deliveryTask,HttpServletRequest request) {
		List<DeliveryTaskBO> list = this.deliveryTaskService.listDeliveryTaskInWeb( deliveryTask );
		//设置状态
		list.forEach( item->{
			item.setStatusName( DictionaryUtil.getDictionaryName( DeliveryTaskStatusEnum.DICT_MAP, item.getStatus().toString() ) );
		} );
		//获取订单金额
		
		PageResult<DeliveryTaskBO> page = new PageResult<>();
		page.setRows( list );
		page.setTotal( deliveryTask.getTotal() );
		return page;
	}

	/**
	 * 新增/更新deliveryTask
	 * @param deliveryTask
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( DeliveryTaskBO deliveryTask ) {
				if( deliveryTask.getTaskId() != null ){
					this.deliveryTaskService.update(deliveryTask);
				}else{
					this.deliveryTaskService.add(deliveryTask);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long taskId) {
		this.deliveryTaskService.delete(taskId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get( DeliveryTaskBO deliveryTask ) {
		return this.deliveryTaskService.get(deliveryTask);
	}

	/**
	 * 编辑页面
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long taskId) {
		ModelAndView mv = new ModelAndView("task/DeliveryTaskEdit");
//		if (taskId !=null) {
//			DeliveryTaskBO deliveryTask = this.deliveryTaskService.get(taskId);
//			mv.addObject("deliveryTask", deliveryTask);
//		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(DeliveryTaskBO deliveryTask) {
		ModelAndView mv = new ModelAndView("task/DeliveryTaskShow");
		if (deliveryTask.getTaskId() !=null) {
			deliveryTask = this.deliveryTaskService.get(deliveryTask);
			//设置订单联系人
			OrderBO order = new OrderBO();
			order.setOrderId( deliveryTask.getOrderId() );
			order.setCommunityId( deliveryTask.getCommunityId() );
			order = orderService.getOrder( order );
			deliveryTask.setNickname( order.getNickname() );
			deliveryTask.setAreaName( AreaUtil.getAreaName( order.getAreaCode() ) );
			deliveryTask.setAddress( order.getAddress() );
			deliveryTask.setMobile( order.getMobile() );
			mv.addObject("deliveryTask", deliveryTask);
			//获取客户名称
			UserBO user = userService.get( order.getUserId() );
			mv.addObject("nickname", user.getNickname());
			mv.addObject("amount", order.getAmount());
			//设置配送员
			if( deliveryTask.getUserId() != null ){
				UserBO user2 = userService.get( deliveryTask.getUserId() );
				user.setAreaName( AreaUtil.getAreaName( user.getAreaCode() ) );
				mv.addObject("user", user2);
			}
			mv.addObject("deliveryTask", deliveryTask);
		}
		return mv;
	}

}
