
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AttrValueController.java
 * Description：
 * 		属性值控制器
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

import com.chanxa.linayi.bo.goods.AttrValueBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.AttrValueService;

@Controller
@RequestMapping("/goods/attrValue")
public class AttrValueController extends CommonControllerSupport {
	@Resource
	private AttrValueService attrValueService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("attrValue.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object attrValueList(AttrValueBO attrValue,HttpServletRequest request) {
		return this.attrValueService.getPage( attrValue );
	}

	/**
	 * 新增/更新attrValue
	 * @param attrValue
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( AttrValueBO attrValue ) {
				if( attrValue.getAttrValueId() != null ){
					this.attrValueService.update(attrValue);
				}else{
					this.attrValueService.add(attrValue);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param attrValueId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer attrValueId) {
		this.attrValueService.delete(attrValueId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param attrValueId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer attrValueId) {
		return this.attrValueService.get(attrValueId);
	}

	/**
	 * 编辑页面
	 * @param attrValueId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer attrValueId) {
		ModelAndView mv = new ModelAndView("goods/AttrValueEdit");
		if (attrValueId !=null) {
			AttrValueBO attrValue = this.attrValueService.get(attrValueId);
			mv.addObject("attrValue", attrValue);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param attrValueId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer attrValueId) {
		ModelAndView mv = new ModelAndView("goods/AttrValueShow");
		if (attrValueId !=null) {
			AttrValueBO attrValue = this.attrValueService.get(attrValueId);
			mv.addObject("attrValue", attrValue);
		}
		return mv;
	}

}
