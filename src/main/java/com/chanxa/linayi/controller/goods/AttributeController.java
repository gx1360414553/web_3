
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		AttributeController.java
 * Description：
 * 		属性控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.goods;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.goods.AttributeBO;
import com.chanxa.linayi.bo.goods.ProductionAttributeBO;
import com.chanxa.linayi.bo.goods.ProductionBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.ProductionAttributeSalesEnum;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.goods.AttributePopulator;
import com.chanxa.linayi.service.goods.AttrValueService;
import com.chanxa.linayi.service.goods.AttributeService;
import com.chanxa.linayi.service.goods.ProductionAttributeService;
import com.chanxa.linayi.service.goods.ProductionService;

@Controller
@RequestMapping("/goods/attribute")
public class AttributeController extends CommonControllerSupport {
	@Resource
	private AttributeService attributeService;

	@Resource
	private AttrValueService attrValueService;
	
	@Resource
	private AttributePopulator attributePopulator ;
	
	@Resource
	private ProductionAttributeService productionAttributeService;
	
	@Resource
	private ProductionService productionService;
	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("attribute.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object attributeList(AttributeBO attribute,HttpServletRequest request) {
		attribute.setRemoved( RemovedEnum.FALSE.getValue() );
		PageResult<AttributeBO> page = this.attributeService.getPage( attribute );
		//设置属性值
		attributePopulator.setAttrValueList( page.getRows(),false );
		//设置操作员
		attributePopulator.setAmindAccount( page.getRows() );
		return page;
	}

	
	/**
	 * 新增/更新attribute
	 * @param attribute
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( @RequestBody AttributeBO attribute,HttpServletRequest request ) {
		attribute.setAdminId( getUserId( request ) );
		return this.attributeService.save( attribute );
	}

	/**
	 * 通过主键删除
	 * @param attributeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer attributeId) {
		this.attributeService.delete(attributeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param attributeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer attributeId) {
		return this.attributeService.get(attributeId);
	}

	/**
	 * 编辑页面
	 * @param attributeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer attributeId) {
		ModelAndView mv = new ModelAndView("goods/AttributeEdit");
		if (attributeId !=null) {
			AttributeBO attribute = this.attributeService.get(attributeId);
			attributePopulator.setAttrValueList( Arrays.asList( attribute ), false );
			mv.addObject("attribute", attribute);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param attributeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer attributeId) {
		ModelAndView mv = new ModelAndView("goods/AttributeShow");
		if (attributeId !=null) {
			AttributeBO attribute = this.attributeService.get(attributeId);
			mv.addObject("attribute", attribute);
		}
		return mv;
	}

}
