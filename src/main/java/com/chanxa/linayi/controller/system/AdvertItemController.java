
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AdvertItemController.java
 * Description：
 * 		广告项控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.system.AdvertItemBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.system.AdvertItemService;

@Controller
@RequestMapping("/system/advertItem")
public class AdvertItemController extends CommonControllerSupport {
	@Resource
	private AdvertItemService advertItemService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("advertItem.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object advertItemList(AdvertItemBO advertItem,HttpServletRequest request) {
		return this.advertItemService.getPage( advertItem );
	}

	/**
	 * 新增/更新advertItem
	 * @param advertItem
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( AdvertItemBO advertItem ) {
				if( advertItem.getAdvertId() != null ){
					this.advertItemService.update(advertItem);
				}else{
					this.advertItemService.add(advertItem);
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
		this.advertItemService.delete(advertId);
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
		return this.advertItemService.get(advertId);
	}

	/**
	 * 编辑页面
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer advertId) {
		ModelAndView mv = new ModelAndView("system/AdvertItemEdit");
		if (advertId !=null) {
			AdvertItemBO advertItem = this.advertItemService.get(advertId);
			mv.addObject("advertItem", advertItem);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param advertId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer advertId) {
		ModelAndView mv = new ModelAndView("system/AdvertItemShow");
		if (advertId !=null) {
			AdvertItemBO advertItem = this.advertItemService.get(advertId);
			mv.addObject("advertItem", advertItem);
		}
		return mv;
	}

}
