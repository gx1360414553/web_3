
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		DriverController.java
 * Description：
 * 		司机控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.community;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;

import com.chanxa.linayi.bo.community.DriverBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.community.DriverService;

@Controller
@RequestMapping("/community/driver")
public class DriverController extends CommonControllerSupport {
	@Resource
	private DriverService driverService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("driver.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object driverList(DriverBO driver,HttpServletRequest request) {
		driver.setRemoved( RemovedEnum.FALSE.getValue() );
		List<DriverBO> driverList = driverService.listJoinCar( driver );
		PageResult<DriverBO> page = new PageResult<>();
		page.setRows( driverList );
		page.setTotal( driver.getTotal() );
		return page;
	}

	/**
	 * 新增/更新driver
	 * @param driver
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( DriverBO driver ) {
		driverService.saveInWeb( driver );
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param driverId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer driverId) {
		driverService.deleteInWeb( driverId );
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param driverId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer driverId) {
		return this.driverService.get(driverId);
	}

	/**
	 * 编辑页面
	 * @param driverId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer driverId) {
		ModelAndView mv = new ModelAndView("community/DriverEdit");
		DriverBO driver = new DriverBO();
		if (driverId !=null) {
			driver = this.driverService.get(driverId);
		}
		mv.addObject("driver", driver);
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param driverId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer driverId) {
		ModelAndView mv = new ModelAndView("community/DriverShow");
		if (driverId !=null) {
			DriverBO driver = this.driverService.get(driverId);
			mv.addObject("driver", driver);
		}
		return mv;
	}

}
