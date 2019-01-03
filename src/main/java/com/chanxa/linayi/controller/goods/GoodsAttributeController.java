
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		GoodsAttributeController.java
 * Description：
 * 		货品-属性，用于保存新增后商品从货品中带出的属性控制器
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

import com.chanxa.linayi.bo.goods.GoodsAttributeBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.GoodsAttributeService;

@Controller
@RequestMapping("/goods/goodsAttribute")
public class GoodsAttributeController extends CommonControllerSupport {
	@Resource
	private GoodsAttributeService goodsAttributeService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("goodsAttribute.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object goodsAttributeList(GoodsAttributeBO goodsAttribute,HttpServletRequest request) {
		return this.goodsAttributeService.getPage( goodsAttribute );
	}

	/**
	 * 新增/更新goodsAttribute
	 * @param goodsAttribute
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( GoodsAttributeBO goodsAttribute ) {
				if( goodsAttribute.getGoodsAttributeId() != null ){
					this.goodsAttributeService.update(goodsAttribute);
				}else{
					this.goodsAttributeService.add(goodsAttribute);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param goodsAttributeId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer goodsAttributeId) {
		this.goodsAttributeService.delete(goodsAttributeId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param goodsAttributeId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer goodsAttributeId) {
		return this.goodsAttributeService.get(goodsAttributeId);
	}

	/**
	 * 编辑页面
	 * @param goodsAttributeId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer goodsAttributeId) {
		ModelAndView mv = new ModelAndView("goods/GoodsAttributeEdit");
		if (goodsAttributeId !=null) {
			GoodsAttributeBO goodsAttribute = this.goodsAttributeService.get(goodsAttributeId);
			mv.addObject("goodsAttribute", goodsAttribute);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param goodsAttributeId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer goodsAttributeId) {
		ModelAndView mv = new ModelAndView("goods/GoodsAttributeShow");
		if (goodsAttributeId !=null) {
			GoodsAttributeBO goodsAttribute = this.goodsAttributeService.get(goodsAttributeId);
			mv.addObject("goodsAttribute", goodsAttribute);
		}
		return mv;
	}

}
