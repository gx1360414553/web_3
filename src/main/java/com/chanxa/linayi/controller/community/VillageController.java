package com.chanxa.linayi.controller.community;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.bo.community.CommunityBO;
import com.chanxa.linayi.bo.community.CommunityLocationBO;
import com.chanxa.linayi.bo.community.VillageBO;
import com.chanxa.linayi.bo.system.AreaBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunityLocationService;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.VillageService;
import com.chanxa.linayi.service.system.AreaService;

/**
 * @author Awake
 * @date 2018年4月26日 下午2:40:57
 */
@Controller
@RequestMapping("/community/village")
public class VillageController extends CommonControllerSupport {
	@Resource
	private CommunityService communityService;

	@Resource
	private CommunityLocationService communityLocationService;

	@Resource
	private VillageService villageService;

	@Resource
	private AreaService areaService;
	
	/**
	 * 分页数据列表
	 * 
	 * @param village
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public PageResult<VillageBO> list(VillageBO village) {
		PageResult<VillageBO> page = villageService.getPageSupermarketAndCommunity(village);
		return page;
	}

	/**
	 * 指定删除
	 * 
	 * @param village
	 * @return
	 */
	@RequestMapping("delete.do")
	public AjaxReturnMessage delete(VillageBO village) {
		Integer villageId = village.getVillageId();
		villageService.delete(villageId);
		return AjaxReturnMessage.createMessage("操作完成", true);
	}

	/**
	 * 跳转编辑界面
	 * 
	 * @return
	 */
	@RequestMapping("edit.do")
	@ResponseBody
	public Object edit(VillageBO village) {
		Map<String, Object> result = new HashMap<>();
		
		/* 获取地区级联列表数据 */
		AreaBO area = new AreaBO();
		area.setLevelStart(1);
		List<AreaBO> areaList = areaService.getList(area);
		Map<String, List<AreaBO>> areaGroupByLevel = areaList.stream()
				.collect(Collectors.groupingBy(AreaBO::getParent));
		result.put("areaList", areaGroupByLevel);
		/* 新增 */
		if (village.getVillageId() == null) { 	
			return AjaxReturnMessage.createMessage(result);
		}
		/* 编辑  */
		List<VillageBO> supermarketAndCommunity = villageService.getSupermarketAndCommunity(village);
		if (supermarketAndCommunity.size() != 1) {
			return AjaxReturnMessage.createMessage("查询失败!",false);
		}
		VillageBO villageBO = supermarketAndCommunity.get(0);
		
		CommunityBO communityBO = new CommunityBO();
		communityBO.setName(villageBO.getCommunityName());
		
		CommunityLocationBO communityLocationBO = new CommunityLocationBO();
		communityLocationBO.setAddress(villageBO.getCommunityLocationName());
		
		CommunityLocationBO communityLocationBO2 = communityLocationService.get(villageBO.getCommunityLocationId());
		AreaBO provinceCityAndRegion = areaService.getProvinceCityAndRegion(communityLocationBO2.getAreaCode());
		result.put("village", villageBO);
		result.put("community", communityBO);
		result.put("communityLocation", communityLocationBO);
		result.put("area", provinceCityAndRegion);
		return AjaxReturnMessage.createMessage(result);
	}

	/**
	 * 新增/编辑
	 * 
	 * @param village
	 * @return
	 */
	@RequestMapping("save.do")
	@ResponseBody
	public Object save(VillageBO village) {
		/* 新增 */
		if (village.getVillageId() == null) {
			village.setRemoved(RemovedEnum.FALSE.getValue());
			int dbResult = villageService.add(village);
			if (dbResult < 1) {
				return AjaxReturnMessage.createMessage("添加失败!", false);
			}
			/* 更新 /编辑 */
		} else {
			int dbResult = villageService.updateByVillageId(village);
			if (dbResult < 1) {
				return AjaxReturnMessage.createMessage("更新失败!", false);
			}
		}
		return AjaxReturnMessage.createMessage("操作完成", true);
	}

	/**
	 * 查看详情
	 * 
	 * @param village
	 * @return
	 */
	@RequestMapping("show.do")
	@ResponseBody
	public Object show(VillageBO village) {
		VillageBO villageDb = villageService.get(village.getVillageId());
		if (villageDb == null) {
			return AjaxReturnMessage.createMessage("查询失败!", false);
		}
		return villageDb;
	}
	
	
	/**根据areaCode查询社区和小区list
	 * @param village
	 * @return
	 */
	@RequestMapping("getVillageListByAreaCode.do")
	@ResponseBody
	public Object getVillageListByAreaCode(AreaBO area) {
		HashMap<Object,Object> result = new HashMap<>();
		result.put("communityLocationList", villageService.getVillageListByAreaCode(area));
		return result;
	}

}
