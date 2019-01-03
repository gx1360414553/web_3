
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		BankCardController.java
 * Description：
 * 		银行卡控制器
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

import com.chanxa.linayi.bo.user.BankCardBO;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.service.user.BankCardService;

@Controller
@RequestMapping("/user/bankCard")
public class BankCardController extends CommonControllerSupport {
	@Resource
	private BankCardService bankCardService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("bankCard.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object bankCardList(BankCardBO bankCard,HttpServletRequest request) {
		return this.bankCardService.getPage( bankCard );
	}

	/**
	 * 新增/更新bankCard
	 * @param bankCard
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( BankCardBO bankCard ) {
				if( bankCard.getBankCardId() != null ){
					this.bankCardService.update(bankCard);
				}else{
					this.bankCardService.add(bankCard);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键删除
	 * @param bankCardId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Long bankCardId) {
		this.bankCardService.delete(bankCardId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param bankCardId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Long bankCardId) {
		return this.bankCardService.get(bankCardId);
	}

	/**
	 * 编辑页面
	 * @param bankCardId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Long bankCardId) {
		ModelAndView mv = new ModelAndView("user/BankCardEdit");
		if (bankCardId !=null) {
			BankCardBO bankCard = this.bankCardService.get(bankCardId);
			mv.addObject("bankCard", bankCard);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param bankCardId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Long bankCardId) {
		ModelAndView mv = new ModelAndView("user/BankCardShow");
		if (bankCardId !=null) {
			BankCardBO bankCard = this.bankCardService.get(bankCardId);
			mv.addObject("bankCard", bankCard);
		}
		return mv;
	}

}
