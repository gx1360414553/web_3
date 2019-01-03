
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CarController.java
 * Description：
 * 		车辆控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.community.CarBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.community.CarService;

@Controller
@RequestMapping("/community/car")
public class CarController extends CommonControllerSupport {
	@Resource
	private CarService carService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("car.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object carList(CarBO car,HttpServletRequest request) {
		return this.carService.getPage( car );
	}

	/**
	 * 新增/更新car
	 * @param car
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CarBO car ) {
				if( car.getCarId() != null ){
					this.carService.update(car);
				}else{
					this.carService.add(car);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param carId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer carId) {
		this.carService.delete(carId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param carId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer carId) {
		return this.carService.get(carId);
	}

	/**
	 * 编辑页面
	 * @param carId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer carId) {
		ModelAndView mv = new ModelAndView("community/CarEdit");
		if (carId !=null) {
			CarBO car = this.carService.get(carId);
			mv.addObject("car", car);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param carId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer carId) {
		ModelAndView mv = new ModelAndView("community/CarShow");
		if (carId !=null) {
			CarBO car = this.carService.get(carId);
			mv.addObject("car", car);
		}
		return mv;
	}

}
