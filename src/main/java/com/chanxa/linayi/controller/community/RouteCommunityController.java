
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		RouteCommunityController.java
 * Description：
 * 		路线-社区控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.bo.community.RouteCommunityBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.RouteCommunityService;

@Controller
@RequestMapping("/community/routeCommunity")
public class RouteCommunityController extends CommonControllerSupport {
	@Resource
	private RouteCommunityService routeCommunityService;

	@Resource
	private CommunityService communityService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("routeCommunity.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object routeCommunityList(RouteCommunityBO routeCommunity,HttpServletRequest request) {
		List<RouteCommunityBO> routeCommunityList = routeCommunityService.getList( routeCommunity );
		if( routeCommunityList.size() > 0 ){
			CommunityBO community = new CommunityBO();
			community.setCommunityIdList( routeCommunityList.stream().map( RouteCommunityBO::getCommunityId ).collect( Collectors.toList() ) );
			Map<Integer, List<CommunityBO>> communityGroupByCommunityId = communityService.getList( community  ).stream().collect( Collectors.groupingBy( CommunityBO::getCommunityId ) );
			routeCommunityList.forEach( item->{
				item.setCommunityName( communityGroupByCommunityId.getOrDefault( item.getCommunityId(), Collections.emptyList() ).stream().findFirst().map( CommunityBO::getName ).orElse( null ) );
			} );
		}
		routeCommunityList.sort( ( a, b )->{
			return a.getRank() - b.getRank();
		} ) ;
		return routeCommunityList;
	}

	/**
	 * 新增/更新routeCommunity
	 * @param routeCommunity
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( RouteCommunityBO routeCommunity ) {
				if( routeCommunity.getRouteCommunityId() != null ){
					this.routeCommunityService.update(routeCommunity);
				}else{
					this.routeCommunityService.add(routeCommunity);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param routeCommunityId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer routeCommunityId) {
		this.routeCommunityService.delete(routeCommunityId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param routeCommunityId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer routeCommunityId) {
		return this.routeCommunityService.get(routeCommunityId);
	}

	/**
	 * 编辑页面
	 * @param routeCommunityId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer routeCommunityId) {
		ModelAndView mv = new ModelAndView("community/RouteCommunityEdit");
		if (routeCommunityId !=null) {
			RouteCommunityBO routeCommunity = this.routeCommunityService.get(routeCommunityId);
			mv.addObject("routeCommunity", routeCommunity);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param routeCommunityId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer routeCommunityId) {
		ModelAndView mv = new ModelAndView("community/RouteCommunityShow");
		if (routeCommunityId !=null) {
			RouteCommunityBO routeCommunity = this.routeCommunityService.get(routeCommunityId);
			mv.addObject("routeCommunity", routeCommunity);
		}
		return mv;
	}

}
