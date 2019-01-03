
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		GoodsSkuController.java
 * Description：
 * 		商品控制器
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

import com.chanxa.linayi.bo.goods.GoodsSkuBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.GoodsSkuService;

@Controller
@RequestMapping("/goods/goodsSku")
public class GoodsSkuController extends CommonControllerSupport {
	@Resource
	private GoodsSkuService goodsSkuService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("goodsSku.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object goodsSkuList(GoodsSkuBO goodsSku,HttpServletRequest request) {
		return this.goodsSkuService.getPage( goodsSku );
	}

	/**
	 * 新增/更新goodsSku
	 * @param goodsSku
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( GoodsSkuBO goodsSku ) {
				if( goodsSku.getGoodsSkuId() != null ){
					this.goodsSkuService.update(goodsSku);
				}else{
					this.goodsSkuService.add(goodsSku);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param goodsSkuId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long goodsSkuId) {
		this.goodsSkuService.delete(goodsSkuId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param goodsSkuId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long goodsSkuId) {
		return this.goodsSkuService.get(goodsSkuId);
	}

	/**
	 * 编辑页面
	 * @param goodsSkuId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long goodsSkuId) {
		ModelAndView mv = new ModelAndView("goods/GoodsSkuEdit");
		if (goodsSkuId !=null) {
			GoodsSkuBO goodsSku = this.goodsSkuService.get(goodsSkuId);
			mv.addObject("goodsSku", goodsSku);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param goodsSkuId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long goodsSkuId) {
		ModelAndView mv = new ModelAndView("goods/GoodsSkuShow");
		if (goodsSkuId !=null) {
			GoodsSkuBO goodsSku = this.goodsSkuService.get(goodsSkuId);
			mv.addObject("goodsSku", goodsSku);
		}
		return mv;
	}

}
