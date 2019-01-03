
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CorrectController.java
 * Description：
 * 		#商品价格纠错，根据超市所在的社区一致性hash分库分表控制器
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

import com.chanxa.linayi.bo.community.SupermarketBO;
import com.chanxa.linayi.bo.goods.CorrectBO;
import com.chanxa.linayi.bo.goods.GoodsSkuBO;
import com.chanxa.linayi.bo.system.DataDictionaryBO;
import com.chanxa.linayi.bo.user.UserBO;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.enums.CorrectStatusEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.goods.CorrectService;
import com.chanxa.linayi.service.goods.GoodsSkuService;
import com.chanxa.linayi.service.system.DataDictionaryService;
import com.chanxa.linayi.service.user.UserCorrectService;
import com.chanxa.linayi.service.user.UserService;

@Controller
@RequestMapping("/goods/correct")
public class CorrectController extends CommonControllerSupport {
	@Resource
	private CorrectService correctService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GoodsSkuService goodsSkuService;
	
	@Resource
	private SupermarketService supermarketService;
	
	@Resource
	private DataDictionaryService dataDictionaryService;
	
	@Resource
	private UserCorrectService userCorrectService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("correct.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object correctList(CorrectBO correct,HttpServletRequest request) {
		return this.correctService.getPage( correct );
	}

	/**
	 * 新增/更新correct
	 * @param correct
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( CorrectBO correct ) {
				if( correct.getCorrectId() != null ){
					this.correctService.update(correct);
				}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}


	/**
	 * 编辑页面
	 * @param correctId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(CorrectBO correct) {
		ModelAndView mv = new ModelAndView("goods/CorrectEdit");
		if (correct.getCorrectId() !=null) {
			correct = this.correctService.getById(correct);
			mv.addObject("correct", correct);
		}
		return mv;
	}
	
	/***
	 * 分享审核
	 * @param correct
	 * @return
	 */
	@RequestMapping("/correctAudit.do")
	@ResponseBody
	public Object correctAudit(CorrectBO correct) {
		//审核通过更新状态
		Integer status = correct.getStatus();
		CorrectBO lastCorrect = correctService.getGoodsSkuLastCorrect(correct);
		CorrectBO completedLastCorrect = correctService.getCompletedLastCorrect(correct);
		if(CorrectStatusEnum.MAIN_AUDIT_PASS.eq(status)){
			this.correctService.update(correct);
			correctService.updateGoodsSkuPriceNow(correct);
			//发送消息给价格持有人
			if(lastCorrect != null) {
				String title = "您发起的价格分享或纠错结果";
				String content = "您的分享或纠错的价格被系统发现并后台审核通过";
				Integer userId = lastCorrect.getUserId();
				UserBO lastUser = userService.get(userId);
				correctService.pushMsgToRequesterAndSharer(title, content, correct, lastCorrect, lastUser);
			}
			//发送消息给纠错价格发起人
			//如果是分享被审核,没有价格持有人
			if(completedLastCorrect != null && !(completedLastCorrect.getUserId().equals(lastCorrect.getUserId()))) {
				String titleComplete = "您持有的价格被他人纠错的结果";
				String contentComplete = "他人对您的价格进行纠错,已经被后台审核通过,您持有的价格以及收益人已变动";
				Integer completeduserId = completedLastCorrect.getUserId();
				UserBO completeduserIdUser = userService.get(completeduserId);
				correctService.pushMsgToRequesterAndSharer(titleComplete, contentComplete, correct, completedLastCorrect, completeduserIdUser);
			}
			//通过删除分享记录
		}else if(CorrectStatusEnum.AUDIT_NO_PASS.eq(status)){
			correctService.update(correct);
			//发送消息给纠错价格发起人
			if(lastCorrect != null) {
				String title = "您发起的价格分享或纠错结果";
				String content = "您的分享或纠错的价格被系统发现并后台审核,并尚未通过";
				Integer userId = lastCorrect.getUserId();
				UserBO lastUser = userService.get(userId);
				correctService.pushMsgToRequesterAndSharer(title, content, correct, lastCorrect, lastUser);
			}
			//发送消息给价格持有人
			if(completedLastCorrect != null) {
				String titleComplete = "您持有的价格被他人纠错的结果";
				String contentComplete = "他人对您的价格进行纠错,已经被后台审核,并尚未通过";
				Integer completeduserId = completedLastCorrect.getUserId();
				UserBO completeduserIdUser = userService.get(completeduserId);
				correctService.pushMsgToRequesterAndSharer(titleComplete, contentComplete, correct, completedLastCorrect, completeduserIdUser);
			}
			
		}
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}
	
	/**
	 * 获取信息
	 * @param correct
	 * @return
	 */
	@RequestMapping("/getInfo.do")
	@ResponseBody
	public Object getInfo(CorrectBO correct) {
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		CorrectBO result = this.correctService.getById(correct);
		//昵称
		Integer userId = result.getUserId();
		UserBO user = userService.get(userId); 
		String nickname = user.getNickname();
		result.setNickname(nickname);
		//商品名称
		Long goodsSkuId = result.getGoodsSkuId();
		GoodsSkuBO goodsSKu = goodsSkuService.get(goodsSkuId);
		String goodsName = goodsSKu.getGoodsName();
		result.setGoodsName(goodsName);
		//超市名称
		Integer supermarketId = result.getSupermarketId();
		SupermarketBO supermarket = supermarketService.get(supermarketId);
		String supermarketName = supermarket.getName();
		result.setSupermarketName(supermarketName);
		
		Long parentId = result.getParentId();
		if(parentId == -1){
			//设置分享信息
			CorrectBO share = new CorrectBO();
			share.setNickname(nickname);
			share.setPrice(result.getPrice());
			share.setPriceTypeName(result.getPriceTypeName());
			share.setValidStart(result.getValidStart());
			share.setValidEnd(result.getValidEnd());
			share.setImagePath(imageServer + "/" + result.getImagePath());
			result.setShare(share);
		}else{
			//纠错信息，获取该纠错信息对应的分享信息
			CorrectBO correctTemp =  new CorrectBO();
			correctTemp.setNickname(nickname);
			correctTemp.setPrice(result.getPrice());
			correctTemp.setPriceTypeName(result.getPriceTypeName());
			correctTemp.setValidStart(result.getValidStart());
			correctTemp.setValidEnd(result.getValidEnd());
			correctTemp.setImagePath(imageServer + "/" + result.getImagePath());
			result.setCorrect(correctTemp);
			
			//获取当前纠错对应的分享
			CorrectBO param = new CorrectBO();
			param.setCorrectId(parentId);
			param.setCommunityId(correct.getCommunityId());
			CorrectBO share = this.correctService.getById(param);
			Integer uId = share.getUserId();
			UserBO u = userService.get(uId);
			share.setNickname(u.getNickname());
			share.setImagePath(imageServer + "/" + share.getImagePath());
			result.setShare(share);
		}
		return AjaxReturnMessage.createMessage(true,result);
	}
	
	@RequestMapping("/getProfitInfo.do")
	@ResponseBody
	public Object getProfitInfo(CorrectBO correct){
		//从字典表获取已有的收益规则信息
		String sharerPercent = dataDictionaryService.getProfitByCode("sharerPercent");
		String supplierPercent = dataDictionaryService.getProfitByCode("supplierPercent");
		String communityPercent = dataDictionaryService.getProfitByCode("communityPercent");
		String cardServiesPercent = dataDictionaryService.getProfitByCode("cardServiesPercent");
		
		correct.setSharerPercent(sharerPercent);
		correct.setSupplierPercent(supplierPercent);
		correct.setCommunityPercent(communityPercent);
		correct.setCardServiesPercent(cardServiesPercent);
		
		return AjaxReturnMessage.createMessage(true, correct);
	}
	
	/**
	 * 保存收益规则信息
	 * @param correct
	 * @return
	 */
	@RequestMapping("/saveProfit.do")
	@ResponseBody
	public Object saveProfit(CorrectBO correct){
		//将设置的收益百分比保存到字典表
		String sharerPercent = correct.getSharerPercent();
		String supplierPercent = correct.getSupplierPercent();
		String communityPercent = correct.getCommunityPercent();
		String cardServiesPercent = correct.getCardServiesPercent();
		
		DataDictionaryBO ddb = new DataDictionaryBO();
		ddb.setDictMapCd("sharerPercent");
		ddb.setName(sharerPercent);
		dataDictionaryService.updateNameByCode(ddb);
		
		ddb.setDictMapCd("supplierPercent");
		ddb.setName(supplierPercent);
		dataDictionaryService.updateNameByCode(ddb);
		
		ddb.setDictMapCd("communityPercent");
		ddb.setName(communityPercent);
		dataDictionaryService.updateNameByCode(ddb);
		
		ddb.setDictMapCd("cardServiesPercent");
		ddb.setName(cardServiesPercent);
		dataDictionaryService.updateNameByCode(ddb);
		
		return AjaxReturnMessage.createMessage(true, "操作成功");
	}
	

}
