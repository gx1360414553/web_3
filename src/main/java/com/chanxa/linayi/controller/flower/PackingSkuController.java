
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		PackingSkuController.java
 * Description：
 * 		#配送商品，根据订单所在的社区ID进行一致性hash分库分表控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.flower;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.flower.PackingSkuBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.flower.PackingSkuService;

@Controller
@RequestMapping("/flower/packingSku")
public class PackingSkuController extends CommonControllerSupport {
	@Resource
	private PackingSkuService packingSkuService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("packingSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object packingSkuList(PackingSkuBO packingSku,HttpServletRequest request) {
		return this.packingSkuService.getPage( packingSku );
	}

	/**
	 * 新增/更新packingSku
	 * @param packingSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( PackingSkuBO packingSku ) {
				if( packingSku.getPackingSkuId() != null ){
					this.packingSkuService.update(packingSku);
				}else{
					this.packingSkuService.add(packingSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param packingSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long packingSkuId) {
		this.packingSkuService.delete(packingSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param packingSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long packingSkuId) {
		return this.packingSkuService.get(packingSkuId);
	}

	/**
	 * 编辑页面
	 * @param packingSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long packingSkuId) {
		ModelAndView mv = new ModelAndView("flower/PackingSkuEdit");
		if (packingSkuId !=null) {
			PackingSkuBO packingSku = this.packingSkuService.get(packingSkuId);
			mv.addObject("packingSku", packingSku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param packingSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long packingSkuId) {
		ModelAndView mv = new ModelAndView("flower/PackingSkuShow");
		if (packingSkuId !=null) {
			PackingSkuBO packingSku = this.packingSkuService.get(packingSkuId);
			mv.addObject("packingSku", packingSku);
		}
		return mv;
	}

}
