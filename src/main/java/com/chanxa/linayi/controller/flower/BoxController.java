
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BoxController.java
 * Description：
 * 		#流转箱/配送箱，流转箱根据任务社区ID进行一致性hash分库分表，配送箱跟单订单所在社区进行一致性hash分库分表控制器
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

import com.chanxa.linayi.bo.flower.BoxBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.flower.BoxService;

@Controller
@RequestMapping("/flower/box")
public class BoxController extends CommonControllerSupport {
	@Resource
	private BoxService boxService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("box.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object boxList(BoxBO box,HttpServletRequest request) {
		return this.boxService.getPage( box );
	}

	/**
	 * 新增/更新box
	 * @param box
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BoxBO box ) {
				if( box.getBoxId() != null ){
					this.boxService.update(box);
				}else{
					this.boxService.add(box);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param boxId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long boxId) {
		this.boxService.delete(boxId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param boxId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(BoxBO box) {
		return this.boxService.get(box);
	}

	/**
	 * 编辑页面
	 * @param boxId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(BoxBO box) {
		ModelAndView mv = new ModelAndView("flower/BoxEdit");
		if (box.getBoxId() !=null) {
			box = this.boxService.get(box);
			mv.addObject("box", box);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param boxId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(BoxBO box) {
		ModelAndView mv = new ModelAndView("flower/BoxShow");
		if (box.getBoxId() !=null) {
			box = this.boxService.get(box);
			mv.addObject("box", box);
		}
		return mv;
	}

}
