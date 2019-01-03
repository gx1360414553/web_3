
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		GoodsImageController.java
 * Description：
 * 		货品图片控制器
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

import com.chanxa.linayi.bo.goods.GoodsImageBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.GoodsImageService;

@Controller
@RequestMapping("/goods/goodsImage")
public class GoodsImageController extends CommonControllerSupport {
	@Resource
	private GoodsImageService goodsImageService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("goodsImage.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object goodsImageList(GoodsImageBO goodsImage,HttpServletRequest request) {
		return this.goodsImageService.getPage( goodsImage );
	}

	/**
	 * 新增/更新goodsImage
	 * @param goodsImage
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( GoodsImageBO goodsImage ) {
				if( goodsImage.getGoodsImageId() != null ){
					this.goodsImageService.update(goodsImage);
				}else{
					this.goodsImageService.add(goodsImage);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param goodsImageId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long goodsImageId) {
		this.goodsImageService.delete(goodsImageId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param goodsImageId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long goodsImageId) {
		return this.goodsImageService.get(goodsImageId);
	}

	/**
	 * 编辑页面
	 * @param goodsImageId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long goodsImageId) {
		ModelAndView mv = new ModelAndView("goods/GoodsImageEdit");
		if (goodsImageId !=null) {
			GoodsImageBO goodsImage = this.goodsImageService.get(goodsImageId);
			mv.addObject("goodsImage", goodsImage);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param goodsImageId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long goodsImageId) {
		ModelAndView mv = new ModelAndView("goods/GoodsImageShow");
		if (goodsImageId !=null) {
			GoodsImageBO goodsImage = this.goodsImageService.get(goodsImageId);
			mv.addObject("goodsImage", goodsImage);
		}
		return mv;
	}

}
