
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		ProcurementTaskController.java
 * Description：
 * 		#采买任务控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.chanxa.dict.domain.DataDictionary;
import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.bo.community.CommunitySupermarketBO;
import com.chanxa.linayi.bo.order.OrderBO;
import com.chanxa.linayi.bo.order.OrderSkuBO;
import com.chanxa.linayi.bo.task.ProcurementOrderBO;
import com.chanxa.linayi.bo.task.ProcurementSkuBO;
import com.chanxa.linayi.bo.task.ProcurementTaskBO;
import com.chanxa.linayi.bo.user.UserBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.constant.SystemTableConstant;
import com.chanxa.linayi.common.enums.CommunitySupermarketType;
import com.chanxa.linayi.common.enums.ProcurementOrderSortingStatusEnum;
import com.chanxa.linayi.common.enums.ProcurementReplenishType;
import com.chanxa.linayi.common.enums.ProcurementTaskInstancyEnum;
import com.chanxa.linayi.common.enums.ProcurementTaskStatusEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.MixedUtil;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.task.ProcurementTaskPopulator;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.task.ProcurementOrderService;
import com.chanxa.linayi.service.task.ProcurementSkuService;
import com.chanxa.linayi.service.task.ProcurementTaskService;
import com.chanxa.linayi.service.user.UserService;
import com.chanxa.linayi.util.system.AreaUtil;
import com.tuya.framework.identity.utils.IdentityUtil;

@Controller
@RequestMapping("/task/procurementTask")
public class ProcurementTaskController extends CommonControllerSupport {
	@Resource
	private ProcurementTaskService procurementTaskService;

	@Resource
	private ProcurementTaskPopulator procurementTaskPopulator;
	
	@Resource
	private UserService userService;
	
	@Resource
	private CommunitySupermarketService communitySupermarketService;
	
	@Resource
	private SupermarketService supermarketService;
	
	@Resource
	private CommunityService communityService;
	
	@Resource
	private ProcurementSkuService procurementSkuService;
	
	@Resource
	private ProcurementOrderService procurementOrderService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("procurementTask.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object procurementTaskList(ProcurementTaskBO procurementTask,HttpServletRequest request) {
		PageResult<ProcurementTaskBO> page = this.procurementTaskService.getPage( procurementTask );
		//设置超市名称
		procurementTaskPopulator.setSupermarket( page.getRows() );
		//计算商品金额
		procurementTaskPopulator.setProcurementSkuList( page.getRows(), procurementTask.getCommunityId() );
		//设置任务状态
		page.getRows().forEach( tItem->{
			tItem.setStatusName( DictionaryUtil.getDictionaryName( ProcurementTaskStatusEnum.DICT_MAP, tItem.getStatus().toString() ) );
		} );
		
		return page;
	}

	/**
	 * 新增/更新procurementTask
	 * @param procurementTask
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( @RequestBody String orderStr ) {
		OrderBO order = JSONObject.parseObject( orderStr, OrderBO.class );
		List<OrderSkuBO> orderSkuList = order.getOrderSkuList();
		Date now = new Date();
		Integer taskCommunityId = order.getCommunityId();
		Integer orderCommunityId = getCommunityId();
		if( CheckUtil.isNullEmpty( orderCommunityId ) ){
			return AjaxReturnMessage.createMessage( "社区账号才能新增采买任务！", false );
		}
		//获取超市ID
		CommunitySupermarketBO communitySupermarket = new CommunitySupermarketBO();
		communitySupermarket.setCommunityId( taskCommunityId );
		communitySupermarket.setType( CommunitySupermarketType.MANAGEMENT.getValue());
		communitySupermarket = communitySupermarketService.getList( communitySupermarket  ).stream().findFirst().orElse( null );
		if( communitySupermarket == null ){
			return AjaxReturnMessage.createMessage( "没有绑定负责超市，不能发布任务", false );
		}
		Integer taskSupermarketId = communitySupermarket.getSupermarketId();
		procurementTaskService.saveInWeb( orderSkuList, now, taskCommunityId, orderCommunityId, taskSupermarketId );
		
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
		this.procurementTaskService.delete(taskId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get( ProcurementTaskBO procurementTask ) {
		return this.procurementTaskService.get(procurementTask);
	}

	/**
	 * 编辑页面
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(Long taskId, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		//获取单前账号社区ID
		Integer userId = getUserId( request );
		if( userId == null ){
			return AjaxReturnMessage.createMessage( "未登陆", false );
		}
		List<CommunityBO> communityList = communityService.listBingCommunity( getCommunityId() );
		CommunityBO community = communityService.get( getCommunityId() );
		communityList.add( 0, community );
		result.put( "communityList", communityList );
		//补货类型
		List<DataDictionary> replenishTypeList = DictionaryUtil.getDictionaries( ProcurementReplenishType.DICT_MAP );
		result.put( "replenishTypeList", replenishTypeList );
		//获取社区工作人员账号
		UserBO user = new UserBO();
		user.setCommunityId( getCommunityId() );
		List<UserBO> userList = userService.getList( user );
		result.put( "userList", userList );
		result.put( "community", community );
		return result;
	}
	
	/**
	 * 详细页面
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show( ProcurementTaskBO procurementTask ) {
		ModelAndView mv = new ModelAndView("task/ProcurementTaskShow");
		if (procurementTask.getTaskId() !=null) {
			
			procurementTask = this.procurementTaskService.get( procurementTask );
			procurementTask.setStatusName( DictionaryUtil.getDictionaryName( ProcurementTaskStatusEnum.DICT_MAP, procurementTask.getStatus().toString() ) );
			//获取采买员
			if( procurementTask.getUserId() != null ){
				UserBO user = userService.get( procurementTask.getUserId() );
				procurementTask.setNickname( user.getNickname() );
				procurementTask.setMobile( user.getAccount() );
				procurementTask.setAddress( AreaUtil.getAreaName( user.getAreaCode() ) + user.getAddress() );
			}
			
			mv.addObject("procurementTask", procurementTask);
		}
		return mv;
	}

}
