
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BrowseController.java
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

import com.chanxa.linayi.bo.user.BrowseBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.BrowseService;

@Controller
@RequestMapping("/user/browse")
public class BrowseController extends CommonControllerSupport {
	@Resource
	private BrowseService browseService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("browse.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object browseList(BrowseBO browse,HttpServletRequest request) {
		return this.browseService.getPage( browse );
	}

	/**
	 * 新增/更新browse
	 * @param browse
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BrowseBO browse ) {
				if( browse.getBrowseId() != null ){
					this.browseService.update(browse);
				}else{
					this.browseService.add(browse);
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
	public Object delete(Long browseId) {
		this.browseService.delete(browseId);
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
		return this.browseService.get(browseId);
	}

	/**
	 * 编辑页面
	 * @param browseId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long browseId) {
		ModelAndView mv = new ModelAndView("user/BrowseEdit");
		if (browseId !=null) {
			BrowseBO browse = this.browseService.get(browseId);
			mv.addObject("browse", browse);
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
		ModelAndView mv = new ModelAndView("user/BrowseShow");
		if (browseId !=null) {
			BrowseBO browse = this.browseService.get(browseId);
			mv.addObject("browse", browse);
		}
		return mv;
	}

}
