
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		TagController.java
 * Description：
 * 		标签控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.goods;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.goods.TagBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.TagService;

@Controller
@RequestMapping("/goods/tag")
public class TagController extends CommonControllerSupport {
	@Resource
	private TagService tagService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("tag.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object tagList(TagBO tag,HttpServletRequest request) {
		return this.tagService.getPage( tag );
	}

	/**
	 * 新增/更新tag
	 * @param tag
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( TagBO tag ) {
				if( tag.getTagId() != null ){
					this.tagService.update(tag);
				}else{
					this.tagService.add(tag);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param tagId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer tagId) {
		this.tagService.delete(tagId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param tagId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer tagId) {
		return this.tagService.get(tagId);
	}

	/**
	 * 编辑页面
	 * @param tagId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer tagId) {
		ModelAndView mv = new ModelAndView("goods/TagEdit");
		if (tagId !=null) {
			TagBO tag = this.tagService.get(tagId);
			mv.addObject("tag", tag);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param tagId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer tagId) {
		ModelAndView mv = new ModelAndView("goods/TagShow");
		if (tagId !=null) {
			TagBO tag = this.tagService.get(tagId);
			mv.addObject("tag", tag);
		}
		return mv;
	}

}
