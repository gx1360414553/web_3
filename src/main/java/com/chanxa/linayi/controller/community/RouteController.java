
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		RouteController.java
 * Description：
 * 		路线控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.chanxa.linayi.bo.community.CarBO;
import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.bo.community.RouteBO;
import com.chanxa.linayi.bo.community.RouteCommunityBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CarService;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.RouteCommunityService;
import com.chanxa.linayi.service.community.RouteService;
import com.chanxa.linayi.timer.TaskTimerGenerator;

@Controller
@RequestMapping("/community/route")
public class RouteController extends CommonControllerSupport {
	@Resource
	private RouteService routeService;

	@Resource
	private CommunityService communityService;
	
	@Resource
	private RouteCommunityService routeCommunityService;
	
	@Resource
	private CarService carService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("route.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object routeList(RouteBO route,HttpServletRequest request) {
		List<RouteBO> routeList = routeService.listInWeb( route );
		PageResult<RouteBO> page = new PageResult<>();
		page.setRows( routeList );
		page.setTotal( route.getTotal() );
		return page;
	}

	/**
	 * 新增/更新route
	 * @param route
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( @RequestBody String content ) throws Exception {
		JSONObject json = JSONObject.parseObject( content );
		RouteBO route = json.getObject( "route", RouteBO.class );
		RouteCommunityBO[] routeCommunityList = json.getObject( "routeCommunityList", RouteCommunityBO[].class );
		routeService.saveInWeb( route,Arrays.asList( routeCommunityList )  );
		//重新计算任务发布时间
		TaskTimerGenerator taskTimerGenerator = ApplicationContextUtils.getApplicationContext().getBean( TaskTimerGenerator.class );
		taskTimerGenerator.generatorTimers();
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param routeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer routeId) {
		this.routeService.delete(routeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param routeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer routeId) {
		return this.routeService.get(routeId);
	}

	/**
	 * 编辑页面
	 * @param routeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(Integer routeId) {
		Map<String,Object> result = new HashMap<>();
		RouteBO route = new RouteBO();
		if (routeId !=null) {
			route = this.routeService.get(routeId);
		}
		result.put("route", route);
		//获取车辆列表
		CarBO carParam = new CarBO();
		carParam.setRemoved( RemovedEnum.FALSE.getValue() );
		List<CarBO> carList = carService.getList( carParam );
		result.put( "carList", carList );
		//获取社区列表
		CommunityBO community = new CommunityBO();
		community.setRemoved( RemovedEnum.FALSE.getValue() );
		List<CommunityBO> communityList = communityService.getList( community );
		//获取路线社区列表，车辆
		List<CommunityBO> checkedCommunityList = new ArrayList<CommunityBO>();
		if( routeId != null ){
			//获取已选择的路线
			RouteCommunityBO routeCommunity = new RouteCommunityBO();
			routeCommunity.setRouteId( routeId );
			List<RouteCommunityBO> routeCommunityList = routeCommunityService.getList( routeCommunity );
			//排序
			routeCommunityList.sort( ( a, b )->{
				return a.getRank() - b.getRank();
			} ) ;
			List<Integer> routeCommunityIdList = routeCommunityList.stream().map( RouteCommunityBO::getCommunityId ).collect( Collectors.toList() );
			
			CommunityBO communityParam = new CommunityBO();
			communityParam.setCommunityIdList( routeCommunityIdList );
			Map<Integer, List<CommunityBO>> checkedGroupByCommunityList = communityService.getList( communityParam  ).stream().collect( Collectors.groupingBy( CommunityBO::getCommunityId ) );
			routeCommunityIdList.forEach( item->{
				List<CommunityBO> pCheckedList = checkedGroupByCommunityList.getOrDefault( item, Collections.emptyList() );
				if( pCheckedList.size() > 0 ){
					checkedCommunityList.add( pCheckedList.get( 0 ) );
				}
			} );
		}
		result.put( "checkedCommunityList", checkedCommunityList );
		//获取未选择的社区列表
		List<Integer> communityIdList = checkedCommunityList.stream().map( CommunityBO::getCommunityId ).collect( Collectors.toList() );
		List<CommunityBO> unCheckedCommunityList = communityList.stream().filter( item->{
			return !communityIdList.contains( item.getCommunityId() );
		} ).collect( Collectors.toList() );
		result.put( "unCheckedCommunityList", unCheckedCommunityList );
		return AjaxReturnMessage.createMessage( true, result );
	}
	
	/**
	 * 详细页面
	 * @param routeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer routeId) {
		ModelAndView mv = new ModelAndView("community/RouteShow");
		if (routeId !=null) {
			RouteBO route = this.routeService.get(routeId);
			mv.addObject("route", route);
		}
		return mv;
	}

}
