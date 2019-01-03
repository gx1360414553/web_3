
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		GoodsRecommendController.java
 * Description：
 * 		推荐货品控制器
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

import com.chanxa.linayi.bo.goods.GoodsRecommendBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.goods.GoodsRecommendService;

@Controller
@RequestMapping("/goods/goodsRecommend")
public class GoodsRecommendController extends CommonControllerSupport {
	@Resource
	private GoodsRecommendService goodsRecommendService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("goodsRecommend.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object goodsRecommendList(GoodsRecommendBO goodsRecommend,HttpServletRequest request) {
		return this.goodsRecommendService.getPage( goodsRecommend );
	}

	/**
	 * 新增/更新goodsRecommend
	 * @param goodsRecommend
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( GoodsRecommendBO goodsRecommend ) {
				if( goodsRecommend.getGoodsRecommendId() != null ){
					this.goodsRecommendService.update(goodsRecommend);
				}else{
					this.goodsRecommendService.add(goodsRecommend);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param goodsRecommendId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer goodsRecommendId) {
		this.goodsRecommendService.delete(goodsRecommendId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param goodsRecommendId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer goodsRecommendId) {
		return this.goodsRecommendService.get(goodsRecommendId);
	}

	/**
	 * 编辑页面
	 * @param goodsRecommendId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer goodsRecommendId) {
		ModelAndView mv = new ModelAndView("goods/GoodsRecommendEdit");
		if (goodsRecommendId !=null) {
			GoodsRecommendBO goodsRecommend = this.goodsRecommendService.get(goodsRecommendId);
			mv.addObject("goodsRecommend", goodsRecommend);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param goodsRecommendId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer goodsRecommendId) {
		ModelAndView mv = new ModelAndView("goods/GoodsRecommendShow");
		if (goodsRecommendId !=null) {
			GoodsRecommendBO goodsRecommend = this.goodsRecommendService.get(goodsRecommendId);
			mv.addObject("goodsRecommend", goodsRecommend);
		}
		return mv;
	}

}
