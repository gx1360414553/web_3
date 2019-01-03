
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		SupermarketController.java
* Description：
* 		超市/供应商/中心仓控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.community.CommunitySupermarketBO;
import com.chanxa.linayi.bo.community.SupermarketBO;
import com.chanxa.linayi.bo.goods.SkuSupermarketBO;
import com.chanxa.linayi.bo.system.AreaBO;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.enums.CommunitySupermarketType;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.enums.SupermarketType;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.FileUploadUtil;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.goods.CorrectService;
import com.chanxa.linayi.service.goods.SkuCommunityService;
import com.chanxa.linayi.service.goods.SkuSupermarketService;
import com.chanxa.linayi.service.system.AreaService;
import com.chanxa.linayi.service.system.QuartzService;
import com.chanxa.linayi.util.system.AreaUtil;

@Controller
@RequestMapping("/community/supermarket")
public class SupermarketController extends CommonControllerSupport {
	@Resource
	private SupermarketService supermarketService;

	@Resource
	private AreaService areaService;

	@Resource
	private CommunityService communityService;

	@Resource
	private SkuSupermarketService skuSupermarketService;

	@Resource
	private CorrectService correctService;

	@Resource
	private SkuCommunityService skuCommunityService;

	@Resource
	private CommunitySupermarketService communitySupermarketService;

	@Resource
	private QuartzService quartzService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("supermarket.");
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object supermarketList(SupermarketBO supermarket, HttpServletRequest request) {
		return this.supermarketService.getPage(supermarket);
	}

	/**
	 * 新增/更新supermarket
	 * 
	 * @param supermarket
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(SupermarketBO supermarket, @RequestParam("imageFile") CommonsMultipartFile file) {
		if (file != null) {
			String path = Configuration.getConfig().getValue(Propert.IMAGE_PATH);
			String logo = FileUploadUtil.uploadFile(file, path);
			supermarket.setLogo(logo);
		} else {
			supermarket.setLogo(null);
		}
		supermarket.setType(SupermarketType.SUPERMARKET.getValue());
		supermarket.setRemoved(RemovedEnum.FALSE.getValue());

		if (supermarket.getSupermarketId() != null) {
			supermarket.setLastModify(new Date());
			this.supermarketService.update(supermarket);
			Integer communityId = supermarket.getCommunityId();
			//绑定负责社区
			if(supermarket.getCommunityId() != null) {
				if(communityId != null) {
					CommunitySupermarketBO communitySupermarketBO = new CommunitySupermarketBO();
					communitySupermarketBO.setCommunityId(communityId);
					communitySupermarketBO.setSupermarketId(supermarket.getSupermarketId());
					communitySupermarketBO.setType(CommunitySupermarketType.COMPARISON.getValue());
				}
			}
		} else {
			supermarket.setCreateTime(new Date());
			supermarket.setLastModify(new Date());
			this.supermarketService.add(supermarket);
			Integer communityId = supermarket.getCommunityId();
			//绑定负责社区
			if(communityId != null) {
				CommunitySupermarketBO communitySupermarketBO = new CommunitySupermarketBO();
				communitySupermarketBO.setCommunityId(communityId);
				communitySupermarketBO.setSupermarketId(supermarket.getSupermarketId());
				communitySupermarketBO.setType(CommunitySupermarketType.COMPARISON.getValue());
				communitySupermarketService.add(communitySupermarketBO);
			}
		}
			
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param supermarketId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer supermarketId) {
		this.supermarketService.delete(supermarketId);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param supermarketId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer supermarketId) {
		return this.supermarketService.get(supermarketId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param supermarketId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(Integer supermarketId) {
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		SupermarketBO supermarket = this.supermarketService.get(supermarketId);
		String areaCode = supermarket.getAreaCode();
		String areaName = AreaUtil.getAreaName(areaCode, "");
		supermarket.setAreaName(areaName);
		supermarket.setLogo(imageServer + "/" + supermarket.getLogo());
		return AjaxReturnMessage.createMessage(true, supermarket);
	}

	/**
	 * 详细页面
	 * 
	 * @param supermarketId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer supermarketId) {
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		ModelAndView mv = new ModelAndView("community/SupermarketShow");
		if (supermarketId != null) {
			SupermarketBO supermarket = this.supermarketService.get(supermarketId);
			String areaCode = supermarket.getAreaCode();
			String areaName = AreaUtil.getAreaName(areaCode, "");
			supermarket.setAreaName(areaName);
			supermarket.setLogo(imageServer + "/" + supermarket.getLogo());
			mv.addObject("supermarket", supermarket);
		}
		return mv;
	}

	@RequestMapping("/getProvince.do")
	@ResponseBody
	public Object getProvince() {
		AreaBO area = new AreaBO();
		List<AreaBO> areaList = areaService.getProvince(area);
		return AjaxReturnMessage.createMessage(true, areaList);
	}

	@RequestMapping("/getAreaInfo.do")
	@ResponseBody
	public Object getAreaInfo(AreaBO area) {
		List<AreaBO> areaList = areaService.listArea(area);
		return AjaxReturnMessage.createMessage(true, areaList);
	}

	@RequestMapping("/logicDelete.do")
	@ResponseBody
	public Object logicDelete(Integer supermarketId) {
		// 获取删除超市所负责的社区id
		CommunitySupermarketBO CommunityIdBySupermarketId = communitySupermarketService
				.getCommunityIdBySupermarketId(supermarketId);
		// 删除绑定超市
		CommunitySupermarketBO communitySupermarketBO = new CommunitySupermarketBO();
		communitySupermarketBO.setSupermarketId(supermarketId);
		List<CommunitySupermarketBO> list = communitySupermarketService.getList(communitySupermarketBO);
		for (CommunitySupermarketBO communitySupermarketBO2 : list) {
			Long communitySupermarketId = communitySupermarketBO2.getCommunitySupermarketId();
			// 拒绝删除唯一负责超市
			CommunitySupermarketBO communitySupermarketBO3 = new CommunitySupermarketBO();
			communitySupermarketBO3.setCommunitySupermarketId(communitySupermarketId);
			communitySupermarketBO3.setType(10);
			List<CommunitySupermarketBO> CommunitySupermarketBOList = communitySupermarketService
					.getList(communitySupermarketBO3);
			for (CommunitySupermarketBO communitySupermarketBO4 : CommunitySupermarketBOList) {
				Integer communityId = communitySupermarketBO4.getCommunityId();
				communitySupermarketBO4 = new CommunitySupermarketBO();
				communitySupermarketBO4.setCommunityId(communityId);
				communitySupermarketBO4.setType(10);
				if (communitySupermarketService.getList(communitySupermarketBO4).size() == 1) {
					return AjaxReturnMessage.createMessage("该超市已经绑定为某社区的唯一负责超市,无法删除");
				}
			}
			communitySupermarketBO.setType(null);
			communitySupermarketService.delete(communitySupermarketId);
		}
		// 删除超市
		SupermarketBO supermarket = new SupermarketBO();
		supermarket.setSupermarketId(supermarketId);
		supermarket.setRemoved(RemovedEnum.TRUE.getValue());
		supermarketService.update(supermarket);

		SkuSupermarketBO skuSupermarket = new SkuSupermarketBO();
		skuSupermarket.setSupermarketId(supermarketId);
		skuSupermarket.setRemoved(RemovedEnum.TRUE.getValue());

		// 删除商品超市表的关于删除的超市信息
		Integer communityId = CommunityIdBySupermarketId.getCommunityId();
		skuSupermarket.setCommunityId(communityId);
		skuSupermarketService.update(skuSupermarket);
		// 更新商品最高价最低价信息
		correctService.updateSku(communityId);

		return AjaxReturnMessage.createMessage(true);
	}

	/**
	 * 根据社区ID查询社区绑定的负责超市和比价超市
	 */
	@RequestMapping("/listBindSupermarket")
	@ResponseBody
	public Object listBindSupermarket(Integer communityId) {
		List<SupermarketBO> list = supermarketService.listBindSupermarket(communityId);
		// 设置类型名称
		list.forEach(sItem -> {
			sItem.setTypeName(
					DictionaryUtil.getDictionaryName(CommunitySupermarketType.DICT_MAP, sItem.getType() + ""));
			sItem.setType(null);
		});
		return list;
	}

}
