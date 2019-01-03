
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		UserController.java
 * Description：
 * 		用户控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.dict.domain.DataDictionary;
import com.chanxa.dict.util.DictionaryUtil;
import com.chanxa.linayi.bo.user.UserBO;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.enums.AuthenticationType;
import com.chanxa.linayi.common.enums.ErrorType;
import com.chanxa.linayi.common.enums.UserState;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.FileUploadUtil;
import com.chanxa.linayi.common.util.MD5;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.user.UserService;

@Controller
@RequestMapping("/user/user")
public class UserController extends CommonControllerSupport {
	@Resource
	private UserService userService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object userList(UserBO user,HttpServletRequest request) {
		return this.userService.getPage( user );
	}

	/**
	 * 新增/更新user
	 * @param user
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( UserBO user,@RequestParam("imageFile")CommonsMultipartFile file ) {
		if(file != null){
			String path = Configuration.getConfig().getValue(Propert.IMAGE_PATH);
			String headImage = FileUploadUtil.uploadFile(file, path);
			user.setHeadImage(headImage);
		}else{
			user.setHeadImage(null);
		}
		UserBO userbo = new UserBO();
		userbo.setMobile(user.getMobile());
		List<UserBO> list = this.userService.getUserByMobile(userbo);
		if( user.getUserId() != null ){
			if (null != list && list.size() > 0) {
				String mobile = this.userService.get(user.getUserId()).getMobile();
				if (!mobile.equals(list.get(0).getMobile()) ) {
					return AjaxReturnMessage.createMessage(ErrorType.MOBILE_BINDED.getErrorMsg(),false);					
				}
			}
			this.userService.update(user);
		}else{
			//设置初始密码
			user.setPassword(MD5.MD5(Configuration.getConfig().getValue(Propert.DEFAULTPAW)));
			user.setAuthentication(AuthenticationType.NO_AUTH.getValue());			
			user.setCreateTime(new Date());
			if (null != list && list.size() > 0) {
				return AjaxReturnMessage.createMessage(ErrorType.MOBILE_BINDED.getErrorMsg(),false);
			}
			this.userService.add(user);
		}
		return AjaxReturnMessage.createMessage("操作成功!",true);
		
	}

	/**
	 * 通过主键删除
	 * @param userId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer userId) {
		this.userService.delete(userId);
		return AjaxReturnMessage.createMessage("操作成功!",true);
	}

	/**
	 * 通过主键获取对象
	 * @param userId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer userId) {
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		Map<String, Object> map = new HashMap<String, Object>();
		UserBO user = this.userService.get(userId);
		String headImage = user.getHeadImage();
		if(headImage != null){
			headImage = headImage.indexOf("http") == -1 ? imageServer + "/" + user.getHeadImage() : headImage;
		}
		user.setHeadImage(headImage);
		//计算用户年龄
		Date birthday = user.getBirthDay();
		if(birthday != null){
			Calendar c = Calendar.getInstance();
			c.setTime(birthday);
			int birthYear = c.get(Calendar.YEAR);
			Date today = new Date();
			c.setTime(today);
			int currentYear = c.get(Calendar.YEAR);
			int age = currentYear - birthYear;
			
			user.setAge(age);
		}
		Integer userType = user.getUserType();
		//获取用户类型的字典表对象
		DataDictionary dict = DictionaryUtil.getDictionary("userUserType", userType+"");
		map.put("user", user);
		map.put("userType", dict);
		return map;
	}

	/**
	 * 编辑页面
	 * @param userId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer userId) {
		ModelAndView mv = new ModelAndView("user/UserEdit");
		if (userId !=null) {
			UserBO user = this.userService.get(userId);
			mv.addObject("user", user);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param userId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer userId) {
		ModelAndView mv = new ModelAndView("user/UserShow");
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		if (userId !=null) {
			UserBO user = this.userService.get(userId);
			String headImage = user.getHeadImage();
			if(headImage != null){
				headImage = headImage.indexOf("http") == -1 ? imageServer + "/" + headImage : headImage;
			}
			user.setHeadImage(headImage);
			mv.addObject("user", user);
		}
		return mv;
	}
	
	/***
	 * 批量禁用
	 * @param userId
	 * @return
	 */
	@RequestMapping("/batchDisable.do")
	@ResponseBody
	public Object batchDisable( String	userIdListStr) {
		String[] userIdList = userIdListStr.split("-");
		for (String s : userIdList) {
			Integer userId = Integer.parseInt(s);
			UserBO user = new UserBO();
			user.setUserId(userId);
			user.setValid(UserState.DISABLE.getValue());
			userService.update(user);
		}
		return AjaxReturnMessage.createMessage("操作成功",true);
	}
	
	/**
	 * 认证
	 * @param user
	 * @return
	 */
	@RequestMapping("/auth.do")
	@ResponseBody
	public Object auth( UserBO user ) {
		userService.update(user);
		return AjaxReturnMessage.createMessage("操作成功",true);
	}
	
	/**
	 * 禁用或者启用
	 * @param user
	 * @return
	 */
	@RequestMapping("/disable.do")
	@ResponseBody
	public Object disable( UserBO user ) {
		userService.update(user);
		return AjaxReturnMessage.createMessage("操作成功",true);
	}

}
