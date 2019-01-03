
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BankController.java
 * Description：
 * 		银行控制器
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

import com.chanxa.linayi.bo.user.BankBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.BankService;

@Controller
@RequestMapping("/user/bank")
public class BankController extends CommonControllerSupport {
	@Resource
	private BankService bankService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("bank.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object bankList(BankBO bank,HttpServletRequest request) {
		return this.bankService.getPage( bank );
	}

	/**
	 * 新增/更新bank
	 * @param bank
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BankBO bank ) {
				if( bank.getBankId() != null ){
					this.bankService.update(bank);
				}else{
					this.bankService.add(bank);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param bankId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer bankId) {
		this.bankService.delete(bankId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param bankId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer bankId) {
		return this.bankService.get(bankId);
	}

	/**
	 * 编辑页面
	 * @param bankId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer bankId) {
		ModelAndView mv = new ModelAndView("user/BankEdit");
		if (bankId !=null) {
			BankBO bank = this.bankService.get(bankId);
			mv.addObject("bank", bank);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param bankId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer bankId) {
		ModelAndView mv = new ModelAndView("user/BankShow");
		if (bankId !=null) {
			BankBO bank = this.bankService.get(bankId);
			mv.addObject("bank", bank);
		}
		return mv;
	}

}
