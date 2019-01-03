
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		CommunityLocationController.java
* Description：
* 		社区网点配送地址控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.community;

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

import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.bo.community.CommunityLocationBO;
import com.chanxa.linayi.bo.community.SupermarketBO;
import com.chanxa.linayi.bo.system.AreaBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.community.CommunityLocationPopulator;
import com.chanxa.linayi.service.community.CommunityLocationService;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.system.AreaService;

@Controller
@RequestMapping("/community/communityLocation")
public class CommunityLocationController extends CommonControllerSupport {
	@Resource
	private CommunityLocationService communityLocationService;

	@Resource
	private CommunityLocationPopulator communityLocationPopulator;

	@Resource
	private CommunityService communityService;

	@Resource
	private AreaService areaService;

	@Resource
	private SupermarketService SupermarketService;

	@Resource
	private CommunitySupermarketService communitySupermarketService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("communityLocation.");
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCommunityLocationList.do")
	@ResponseBody
	public Object getCommunityLocationList(CommunityLocationBO communityLocation, HttpServletRequest request) {
		List<CommunityLocationBO> list = communityLocationService.getList(communityLocation);
		return list;
	}
	
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communityLocationList(CommunityLocationBO communityLocation, HttpServletRequest request) {
		// 通过小区找超市
		PageResult<SupermarketBO> page = this.communityLocationService.getSupermarketPage(communityLocation);
		return page;
	}

	/**
	 * 新增/更新communityLocation
	 * 
	 * @param communityLocation
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(CommunityLocationBO communityLocation, HttpServletRequest request) {
		CommunityLocationBO communityLocationBO = communityLocationService.getNewCode(communityLocation);
		/* 新增 */
		if (communityLocation.getCommunityLocationId() == null) {
			if (communityLocationBO == null) {
				communityLocation.setAreaCode(communityLocation.getAreaCode() + "0001");
				
			}else {	
				Long newCode = Long.parseLong(communityLocationBO.getAreaCode()) + 1;
				communityLocation.setAreaCode(newCode + "");
			}
			communityLocationService.add(communityLocation);

		} else {
			if (communityLocationBO == null) {
				communityLocation.setAreaCode(communityLocation.getAreaCode() + "0001");
				
			}else {	
				Long newCode = Long.parseLong(communityLocationBO.getAreaCode()) + 1;
				communityLocation.setAreaCode(newCode + "");
			}
			communityLocationService.update(communityLocation);
		}
		// 刷新字典
		DictionaryUtil.reload();
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param communityLocationId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long communityLocationId) {

		this.communityLocationService.delete(communityLocationId);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param communityLocationId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long communityLocationId) {
		return this.communityLocationService.get(communityLocationId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param communityLocationId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(CommunityLocationBO communityLocation) {
		// 获取省市区街道级联下拉列表
		HashMap<String, Object> result = new HashMap<>();
		Map<String, List<AreaBO>> allOverAreaList = areaService.getAllOverAreaList();
		// 查询社区列表
		CommunityBO community = new CommunityBO();
		List<CommunityBO> list = communityService.getList(community);
		
		// 新增
		if (communityLocation.getCommunityLocationId() == null) {
			result.put("areaList", allOverAreaList);
			result.put("communityList", list);
			return AjaxReturnMessage.createMessage(result);
		}

		/* 编辑 */
		// 通过小区id查看所在的省市区街道
		Long communityLocationId = communityLocation.getCommunityLocationId();
		CommunityLocationBO communityLocationBO = communityLocationService.get(communityLocationId);

		String areaCode = communityLocationBO.getAreaCode();
		areaCode = areaCode.substring(0, 14);
		AreaBO provinceCityAndRegion = areaService.getProvinceCityAndRegion(areaCode);
		result = new HashMap<>();
		SupermarketBO supermarket = SupermarketService.get(communityLocation.getSupermarketId());
		communityLocationBO.setSupermarketName(supermarket.getName());
		// communityLocation.setAddress(address);
		// 获取绑定数据的社区名
		CommunityBO communityBO = communityService.get(communityLocationBO.getCommunityId());

		result.put("areaList", allOverAreaList);
		result.put("provinceCityAndRegion", provinceCityAndRegion);
		result.put("communityLocation", communityLocationBO);
		result.put("communityList", list);
		result.put("community", communityBO);
		return AjaxReturnMessage.createMessage(result);
	}

	/**
	 * 详细页面
	 * 
	 * @param communityLocationId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long communityLocationId) {
		ModelAndView mv = new ModelAndView("community/CommunityLocationShow");
		if (communityLocationId != null) {
			CommunityLocationBO communityLocation = this.communityLocationService.get(communityLocationId);
			mv.addObject("communityLocation", communityLocation);
		}
		return mv;
	}

	/**
	 * 删除超市行
	 * 
	 * @param supermarketId
	 * @return
	 */
	@RequestMapping("/remove.do")
	@ResponseBody
	public Object remove(int communityId, int supermarketId) {
		CommunityLocationBO communityLocation = new CommunityLocationBO();
		communityLocation.setCommunityId(communityId);
		communityLocation.setSupermarketId(supermarketId);
		int result = this.communityLocationService.remove(communityLocation);
		if (result < 0) {
			return AjaxReturnMessage.createMessage("删除失败!", false);
		}
		return AjaxReturnMessage.createMessage("删除成功!", true);
	}

}
