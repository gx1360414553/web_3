
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		ProcurementSkuController.java
 * Description：
 * 		#采买任务-商品控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.task.ProcurementSkuBO;
import com.chanxa.linayi.bo.task.ProcurementTaskBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.task.ProcurementTaskPopulator;
import com.chanxa.linayi.service.task.ProcurementSkuService;
import com.chanxa.linayi.service.task.ProcurementTaskService;

@Controller
@RequestMapping("/task/procurementSku")
public class ProcurementSkuController extends CommonControllerSupport {
	@Resource
	private ProcurementSkuService procurementSkuService;
	
	@Resource
	private ProcurementTaskPopulator procurementTaskPopulator;

	@Resource
	private ProcurementTaskService procurementTaskService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("procurementSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object procurementSkuList(ProcurementSkuBO procurementSku,HttpServletRequest request) {
		ProcurementTaskBO procurement = new ProcurementTaskBO();
		procurement.setCommunityId( procurementSku.getCommunityId() );
		procurement.setTaskId( procurementSku.getTaskId() );
		procurementTaskPopulator.setProcurementSkuList( procurement );
		return procurement.getProcurementSkuList();
	}
	/**
	 * 查询采买任务操作记录
	 * @param request
	 * @return
	 */
	@RequestMapping("/listTaskGoodsSkuHist.do")
	@ResponseBody
	public Object listTaskGoodsSkuHist(ProcurementSkuBO procurementSku,HttpServletRequest request) {
		ProcurementTaskBO procurementTask = new ProcurementTaskBO();
		procurementTask.setCommunityId( procurementSku.getCommunityId() );
		procurementTask.setTaskId( procurementSku.getTaskId() );
		//获取任务
		procurementTask = procurementTaskService.get( procurementTask );
		//设置任务商品
		procurementTaskPopulator.setProcurementSkuList( procurementTask );
		//设置商品操作时间
		List<ProcurementSkuBO> procurementSkuList = procurementTask.getProcurementSkuList();
		Date buyTime = procurementTask.getBuyTime();
		Date finishTime = procurementTask.getFinishTime();
		List<Map<String,Object>> resultList = new ArrayList<>();
		for( int i = 0 ; i < procurementSkuList.size(); i++ ){
			ProcurementSkuBO psItem = procurementSkuList.get( i );
			if( buyTime != null ){
				Map<String,Object> resultItem = new HashMap<>();
				resultItem.put( "goodsName", psItem.getGoodsName() );
				resultItem.put( "quantity", psItem.getBuyQuantity() );
				resultItem.put( "statusName", "采买" );
				resultItem.put( "time", buyTime );
				resultList.add( resultItem );
			}
			if( finishTime != null ){
				Map<String,Object> resultItem = new HashMap<>();
				resultItem.put( "goodsName", psItem.getGoodsName() );
				resultItem.put( "quantity", psItem.getReceiveQuantity() );
				resultItem.put( "statusName", "验货" );
				resultItem.put( "time", finishTime );
				resultList.add( resultItem );
			}
		}
		
		return resultList;
	}

	/**
	 * 新增/更新procurementSku
	 * @param procurementSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( ProcurementSkuBO procurementSku ) {
				if( procurementSku.getProcurementSkuId() != null ){
					this.procurementSkuService.update(procurementSku);
				}else{
					this.procurementSkuService.add(procurementSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param procurementSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long procurementSkuId) {
		this.procurementSkuService.delete(procurementSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param procurementSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long procurementSkuId) {
		return this.procurementSkuService.get(procurementSkuId);
	}

	/**
	 * 编辑页面
	 * @param procurementSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long procurementSkuId) {
		ModelAndView mv = new ModelAndView("task/ProcurementSkuEdit");
		if (procurementSkuId !=null) {
			ProcurementSkuBO procurementSku = this.procurementSkuService.get(procurementSkuId);
			mv.addObject("procurementSku", procurementSku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param procurementSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long procurementSkuId) {
		ModelAndView mv = new ModelAndView("task/ProcurementSkuShow");
		if (procurementSkuId !=null) {
			ProcurementSkuBO procurementSku = this.procurementSkuService.get(procurementSkuId);
			mv.addObject("procurementSku", procurementSku);
		}
		return mv;
	}

}
