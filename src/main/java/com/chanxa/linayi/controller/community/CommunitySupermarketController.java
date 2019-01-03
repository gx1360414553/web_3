
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		CommunitySupermarketController.java
* Description：
* 		负责超市/比价超市控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.community.CommunitySupermarketBO;
import com.chanxa.linayi.bo.community.SupermarketBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.CommunitySupermarketType;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.goods.CorrectService;
import com.chanxa.linayi.service.goods.GoodsService;

@Controller
@RequestMapping("/community/communitySupermarket")
public class CommunitySupermarketController extends CommonControllerSupport {
	@Resource
	private CommunitySupermarketService communitySupermarketService;

	@Resource
	private SupermarketService supermarketService;

	@Resource
	private CorrectService correctService;
	
	@Resource
	private GoodsService goodsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("communitySupermarket.");
	}

	@RequestMapping("/bindSupermarket.do")
	@ResponseBody
	public Object bindSupermarket(CommunitySupermarketBO communitySupermarket) {
		communitySupermarketService.bindSupermarket(communitySupermarket);
		return AjaxReturnMessage.createMessage("操作完成", true);
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object communitySupermarketList(SupermarketBO supermarket, HttpServletRequest request) {
		Integer communityId = supermarket.getCommunityId();
		PageResult<SupermarketBO> page = supermarketService.getPage(supermarket);
		List<SupermarketBO> supermarketList = page.getRows();
		List<Integer> supermarketIdList = page.getRows().stream().map(SupermarketBO::getSupermarketId)
				.collect(Collectors.toList());
		if (supermarketIdList.isEmpty()) {
			return page;
		}
		// 处理超市是否是其他社区的负责超市，当前社区的负责超市，当前社区的比较超市
		CommunitySupermarketBO communitySupermarket = new CommunitySupermarketBO();
		communitySupermarket.setSupermarketIdList(supermarketIdList);
		List<CommunitySupermarketBO> communitySupermarketList = communitySupermarketService
				.getList(communitySupermarket);
		// 前端按钮显示处理,
		// 10：本社区已绑定为负责超市，
		// 20：其他社区已绑定负责超市且当前社区已绑定为比价超市，
		// 30：其他社区已绑定负责超市且当前社区未绑定为比价超市，
		// 40：其他社区未绑定负责超市且当前社区已绑定为比价超市，
		// 50：其他社区未绑定为负责超市且当前社区未绑定为比价超市，
		// 60: 该超市为待解绑状态
		
		supermarketList.forEach(sItem -> {
			List<CommunitySupermarketBO> csList = communitySupermarketList.stream().filter(item -> {
				return item.getSupermarketId().equals(sItem.getSupermarketId());
			}).collect(Collectors.toList());
			Integer type = 50;
			for (CommunitySupermarketBO csItem : csList) {
				// 是否已绑定负责超市
				CommunitySupermarketBO communitySupermarketBO = new CommunitySupermarketBO();
				communitySupermarketBO.setSupermarketId(csItem.getSupermarketId());
//				if(csItem.getSupermarketBindStatus().equals(SupermarketBindStatusEnum.WAITDEBINDMANAGEMENTSUPERMARKET.getValue())){
//					sItem.setSupermarketBindStatus(SupermarketBindStatusEnum.WAITDEBINDMANAGEMENTSUPERMARKET.getValue());
//				}
				if (communitySupermarketService.isbind(communitySupermarketBO)) {
					sItem.setBind(true);
				} else {
					sItem.setBind(false);
				}
				// 负责超市
				if (CommunitySupermarketType.MANAGEMENT.eq(csItem.getType())) {
					if (csItem.getCommunityId().equals(communityId)) {
						type = 10;
						break;
					} else {
						type = type == 40 ? 20 : 30;
					}
					// 比价超市
				} else if (csItem.getCommunityId().equals(communityId)) {
					type = (type == 30 || type == 20) ? 20 : 40;
				}
			}
			sItem.setType(type);
		});
		return page;
	}

	/**
	 * 新增/更新communitySupermarket
	 * 
	 * @param communitySupermarket
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(CommunitySupermarketBO communitySupermarket) {
		if (communitySupermarket.getCommunitySupermarketId() != null) {
			this.communitySupermarketService.update(communitySupermarket);
		} else {
			this.communitySupermarketService.add(communitySupermarket);
		}
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param communitySupermarketId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long communitySupermarketId) {
		this.communitySupermarketService.delete(communitySupermarketId);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param communitySupermarketId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long communitySupermarketId) {
		return this.communitySupermarketService.get(communitySupermarketId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param communitySupermarketId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long communitySupermarketId) {
		ModelAndView mv = new ModelAndView("community/CommunitySupermarketEdit");
		if (communitySupermarketId != null) {
			CommunitySupermarketBO communitySupermarket = this.communitySupermarketService.get(communitySupermarketId);
			mv.addObject("communitySupermarket", communitySupermarket);
		}
		return mv;
	}

	/**
	 * 详细页面
	 * 
	 * @param communitySupermarketId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long communitySupermarketId) {
		ModelAndView mv = new ModelAndView("community/CommunitySupermarketShow");
		if (communitySupermarketId != null) {
			CommunitySupermarketBO communitySupermarket = this.communitySupermarketService.get(communitySupermarketId);
			mv.addObject("communitySupermarket", communitySupermarket);
		}
		return mv;
	}
	
	/**
	 * 换绑社区页面
	 * 
	 * @param communitySupermarketId
	 * @return
	 */
	@RequestMapping("/selectCommunity.do")
	public ModelAndView selectCommunity(Integer supermarketId) {
		ModelAndView mv = new ModelAndView("community/selectCommunity");
		mv.addObject("supermarketId", supermarketId);
		return mv;
	}
}
