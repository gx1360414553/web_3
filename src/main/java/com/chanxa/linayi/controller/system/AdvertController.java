
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AdvertController.java
 * Description：
 * 		广告控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.system;

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

import com.chanxa.linayi.bo.system.AdvertBO;
import com.chanxa.linayi.bo.system.AdvertItemBO;
import com.chanxa.linayi.common.enums.AdvertStatusEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.system.AdvertItemService;
import com.chanxa.linayi.service.system.AdvertService;

@Controller
@RequestMapping("/system/advert")
public class AdvertController extends CommonControllerSupport {
	@Resource
	private AdvertService advertService;
	
	@Resource
	private AdvertItemService advertItemService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("advert.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object advertList(AdvertBO advert,HttpServletRequest request) {
		return this.advertService.getPage( advert );
	}

	/**
	 * 新增/更新advert
	 * @param advert
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( AdvertBO advert, String advertItemListStr ,@RequestParam("imageFile")List<CommonsMultipartFile> fileList,HttpServletRequest request) {
		Integer adminId = AdvertController.getUserId(request);
		Integer advertId = advert.getAdvertId();
		if( advertId != null ){
			//更新广告
			advert.setLastModify(new Date());
			advertService.update(advert);
			//更新广告项
			Integer state = 1;
			advertItemService.updateOrAddInfo(advertId,advertItemListStr,fileList,state);
		}else{
			//添加广告
			advert.setStatus(AdvertStatusEnum.FALSE.getValue());
			advert.setCreateTime(new Date());
			advert.setAdminId(adminId);
			advertService.add(advert);
			//获取新增的广告id
			advertId = advert.getAdvertId();
			Integer state = 2;
			//新增广告item
			advertItemService.updateOrAddInfo(advertId, advertItemListStr, fileList, state);
		}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}
	
	
	
	/**
	 * 通过主键删除
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer advertId) {
		//删除广告
		this.advertService.delete(advertId);
		//删除广告item
		advertItemService.delete(advertId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer advertId) {
		return this.advertService.get(advertId);
	}

	/**
	 * 编辑页面
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(Integer advertId) {
		AdvertBO advert = this.advertService.get(advertId);
		
		AdvertItemBO adverItem = new AdvertItemBO();
		adverItem.setAdvertId(advertId);
		List<AdvertItemBO> advertItemList = advertItemService.getItemListInfo(adverItem);
		advert.setAdvertItemList(advertItemList);
		return advert;
	}
	
	/**
	 * 发布广告
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/release.do")
	@ResponseBody
	public Object release(Integer advertId) {
		AdvertBO advert = new AdvertBO();
		advert.setAdvertId(advertId);
		advert.setStatus(AdvertStatusEnum.TRUE.getValue());
		advert.setPublishTime(new Date());
		advertService.update(advert);
		return AjaxReturnMessage.createMessage("操作成功",true);
	}
	
	/**
	 * 取消发布
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/unRelease.do")
	@ResponseBody
	public Object unRelease(Integer advertId) {
		AdvertBO advert = new AdvertBO();
		advert.setAdvertId(advertId);
		advert.setStatus(AdvertStatusEnum.CANCEL.getValue());
		advertService.update(advert);
		return AjaxReturnMessage.createMessage("操作成功",true);
	}

}
