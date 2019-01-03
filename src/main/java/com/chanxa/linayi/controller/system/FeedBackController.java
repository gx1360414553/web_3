package com.chanxa.linayi.controller.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.bo.system.FeedbackBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.system.FeedbackService;

@Controller
@RequestMapping("/feedBack")
public class FeedBackController extends CommonControllerSupport{
	
	@Resource
	private FeedbackService feedbackService;
	 
	@InitBinder("feedBack")
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("feedBack.");
	}
	/**
	 * 分页查询反馈意见列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/feedBackList.do")
	@ResponseBody
	public PageResult<FeedbackBO> feedBackList(FeedbackBO feedBack) {
		List<FeedbackBO> feedBacks=feedbackService.getFeedBacks( feedBack );
		PageResult<FeedbackBO> pageResult = new PageResult<FeedbackBO>();
		pageResult.setRows(feedBacks);
		pageResult.setTotal(feedBack.getTotal());
		return pageResult;
	}
}
