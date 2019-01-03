
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		ShoppingCarController.java
 * Description：
 * 		购物车控制器
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

import com.chanxa.linayi.bo.user.ShoppingCarBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.ShoppingCarService;

@Controller
@RequestMapping("/user/shoppingCar")
public class ShoppingCarController extends CommonControllerSupport {
	@Resource
	private ShoppingCarService shoppingCarService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shoppingCar.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object shoppingCarList(ShoppingCarBO shoppingCar,HttpServletRequest request) {
		return this.shoppingCarService.getPage( shoppingCar );
	}

	/**
	 * 新增/更新shoppingCar
	 * @param shoppingCar
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( ShoppingCarBO shoppingCar ) {
				if( shoppingCar.getShoppingCarId() != null ){
					this.shoppingCarService.update(shoppingCar);
				}else{
					this.shoppingCarService.add(shoppingCar);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param shoppingCarId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long shoppingCarId) {
		this.shoppingCarService.delete(shoppingCarId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param shoppingCarId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long shoppingCarId) {
		return this.shoppingCarService.get(shoppingCarId);
	}

	/**
	 * 编辑页面
	 * @param shoppingCarId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long shoppingCarId) {
		ModelAndView mv = new ModelAndView("user/ShoppingCarEdit");
		if (shoppingCarId !=null) {
			ShoppingCarBO shoppingCar = this.shoppingCarService.get(shoppingCarId);
			mv.addObject("shoppingCar", shoppingCar);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param shoppingCarId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long shoppingCarId) {
		ModelAndView mv = new ModelAndView("user/ShoppingCarShow");
		if (shoppingCarId !=null) {
			ShoppingCarBO shoppingCar = this.shoppingCarService.get(shoppingCarId);
			mv.addObject("shoppingCar", shoppingCar);
		}
		return mv;
	}

}
