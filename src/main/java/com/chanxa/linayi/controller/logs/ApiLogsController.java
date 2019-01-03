package com.chanxa.linayi.controller.logs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.logs.ApiLogs;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.logs.ApiLogsService;

@Controller
@RequestMapping("/logs/apiLogs")
public class ApiLogsController extends CommonControllerSupport {
	@Resource
	private ApiLogsService apiLogsService;

	@InitBinder("apiLogs")
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("apiLogs.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object apiLogsList(ApiLogs apiLogs) {
		return this.apiLogsService.getList( apiLogs );
	}

	/**
	 * 新增apiLogs
	 * @param apiLogs
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(@ModelAttribute ApiLogs apiLogs) {
		this.apiLogsService.add(apiLogs);
		return AjaxReturnMessage.createMessage("success");
	}


	/**
	 * 更新apiLogs
	 * @param apiLogs
	 * @return
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public Object update(@ModelAttribute ApiLogs apiLogs) {
		this.apiLogsService.update(apiLogs);
		return AjaxReturnMessage.createMessage("success");
	}

	/**
	 * 通过主键删除
	 * @param logId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer logId) {
		this.apiLogsService.delete(logId);
		return AjaxReturnMessage.createMessage("success");
	}

	/**
	 * 通过主键获取对象
	 * @param logId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer logId) {
		return this.apiLogsService.get(logId);
	}

	/**
	 * 编辑页面
	 * @param logId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer logId) {
		ModelAndView mv = new ModelAndView("logs/ApiLogsEdit");
		if (logId !=null) {
			ApiLogs apiLogs = this.apiLogsService.get(logId);
			mv.addObject("apiLogs", apiLogs);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param logId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer logId) {
		ModelAndView mv = new ModelAndView("logs/ApiLogsShow");
		if (logId !=null) {
			ApiLogs apiLogs = this.apiLogsService.get(logId);
			mv.addObject("apiLogs", apiLogs);
		}
		return mv;
	}

}
