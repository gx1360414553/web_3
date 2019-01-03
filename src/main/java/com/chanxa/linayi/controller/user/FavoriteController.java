
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		FavoriteController.java
 * Description：
 * 		浏览历史记录控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.user.FavoriteBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.FavoriteService;

@Controller
@RequestMapping("/user/favorite")
public class FavoriteController extends CommonControllerSupport {
	@Resource
	private FavoriteService favoriteService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("favorite.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object favoriteList(FavoriteBO favorite,HttpServletRequest request) {
		return this.favoriteService.getPage( favorite );
	}

	/**
	 * 新增/更新favorite
	 * @param favorite
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( FavoriteBO favorite ) {
				if( favorite.getFavoriteId() != null ){
					this.favoriteService.update(favorite);
				}else{
					this.favoriteService.add(favorite);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param browseId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete( FavoriteBO favorite ) {
		this.favoriteService.delete(favorite);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param browseId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long browseId) {
		return this.favoriteService.get(browseId);
	}

	/**
	 * 编辑页面
	 * @param browseId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long browseId) {
		ModelAndView mv = new ModelAndView("user/FavoriteEdit");
		if (browseId !=null) {
			FavoriteBO favorite = this.favoriteService.get(browseId);
			mv.addObject("favorite", favorite);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param browseId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long browseId) {
		ModelAndView mv = new ModelAndView("user/FavoriteShow");
		if (browseId !=null) {
			FavoriteBO favorite = this.favoriteService.get(browseId);
			mv.addObject("favorite", favorite);
		}
		return mv;
	}

}
