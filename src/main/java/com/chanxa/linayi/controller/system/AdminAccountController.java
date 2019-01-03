package com.chanxa.linayi.controller.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.linayi.bo.community.CommunityAccountBO;
import com.chanxa.linayi.bo.system.AdminAccountBO;
import com.chanxa.linayi.bo.system.PrivilegeBO;
import com.chanxa.linayi.bo.system.TreeNodeBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.enums.AdminAccountStatu;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.MD5;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.service.community.CommunityAccountService;
import com.chanxa.linayi.service.system.AdminAccountService;
import com.chanxa.linayi.service.system.PrivilegeService;
import com.chanxa.linayi.service.system.RoleService;

/**
 * 用户
 * 
 * @author zy
 * @createTime 2015-09-11
 */
@Controller
@RequestMapping(value = "/backstageUserAction")
public class AdminAccountController {

	@Resource
	private AdminAccountService adminAccountService;

	@Resource
	private PrivilegeService privilegeService;

	@Resource
	private RoleService roleService;

	@Resource
	private CommunityAccountService communityAccountService;

	/** 解除冻结 */
	@RequestMapping(value = "cancelFrozen")
	@ResponseBody
	public AjaxReturnMessage cancelFrozen(AdminAccountBO adminAccount) {
		adminAccountService.updateUser(adminAccount);
		if (adminAccount.getStatus().equals(AdminAccountStatu.ENABLE.getValue() + "")) {
			return AjaxReturnMessage.createMessage("已启用！", true);
		} else {
			return AjaxReturnMessage.createMessage("已禁用！", true);
		}
	}

	/**
	 * 获得用户列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getUserList")
	@ResponseBody
	public Object getUserList(AdminAccountBO adminAccount) {
		List<AdminAccountBO> list = adminAccountService.getUserList(adminAccount);
		PageResult<AdminAccountBO> page = new PageResult<AdminAccountBO>();
		page.setRows(list);
		page.setTotal(adminAccount.getTotal());
		return page;
	}

	@RequestMapping("/home.html")
	public ModelAndView toHome(HttpServletRequest request, HttpSession session) {
		ModelAndView view = new ModelAndView("forward:/index.jsp");
		// 获取该用户的权限菜单
		List<PrivilegeBO> privilegeTree = privilegeService
				.listUserPrivilege(CommonControllerSupport.getUserId(request));
		List<TreeNodeBO> tree = buildTree(-1, privilegeTree);
		// 设置二级菜单图片显示
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER) + "/";
		for (TreeNodeBO treeNode : tree) {
			treeNode.getChildren().forEach(child -> {
				if (CheckUtil.isNotNullEmpty(child.getIcon())) {
					child.setIcon(imageServer + child.getIcon());
				}
			});
		}
		view.addObject("privilegeTree", tree);
		// 保存用户权限编码,权限URL(不包括禁用的菜单和禁用菜单的子菜单)
		List<PrivilegeBO> rootList = new ArrayList<PrivilegeBO>();
		for (PrivilegeBO privilege : privilegeTree) {
			if (privilege.getUpPrivilegeId() != null && privilege.getUpPrivilegeId().equals(-1)
					&& "1000".equals(privilege.getStatus())) {
				List<PrivilegeBO> childrenList = getChildrenPrivilege(privilegeTree, privilege.getPrivilegeId());
				privilege.setChildren(childrenList);
				rootList.add(privilege);
			}
		}
		Set<String> privilegeCodeList = this.getPrivilegeCodeList(rootList);
		Set<String> privilegeUrlList = this.getPrivilegeUrlList(rootList);
		session.setAttribute("userPrivilegeCodeList", privilegeCodeList);
		session.setAttribute("privilegeUrlList", privilegeUrlList);
		return view;
	}

	private Set<String> getPrivilegeCodeList(List<PrivilegeBO> privilegeTree) {
		Set<String> privilegeCodeList = new HashSet<String>();
		for (PrivilegeBO privilege : privilegeTree) {
			privilegeCodeList.add(privilege.getPrivilegeCode());
			if (privilege.getChildren().size() > 0) {
				privilegeCodeList.addAll(getPrivilegeCodeList(privilege.getChildren()));
			}
		}
		return privilegeCodeList;
	}

	private Set<String> getPrivilegeUrlList(List<PrivilegeBO> privilegeTree) {
		Set<String> privilegeUrlList = new HashSet<String>();
		for (PrivilegeBO privilege : privilegeTree) {
			privilegeUrlList.add(privilege.getAppUrl());
			if (privilege.getChildren().size() > 0) {
				privilegeUrlList.addAll(getPrivilegeUrlList(privilege.getChildren()));
			}
		}
		return privilegeUrlList;
	}

	private List<PrivilegeBO> getChildrenPrivilege(List<PrivilegeBO> privilegeList, Integer upPrivilegeId) {
		List<PrivilegeBO> children = new ArrayList<PrivilegeBO>();
		for (PrivilegeBO privilege : privilegeList) {
			if (upPrivilegeId != null && upPrivilegeId.equals(privilege.getUpPrivilegeId())
					&& "1000".equals(privilege.getStatus())) {
				Integer privilegeId = privilege.getPrivilegeId();
				List<PrivilegeBO> childrenList = getChildrenPrivilege(privilegeList, privilegeId);
				privilege.setChildren(childrenList);
				children.add(privilege);
			}
		}
		return children;
	}

	private List<TreeNodeBO> buildTree(Integer upPrivilegeId, List<PrivilegeBO> privilegeTree) {
		List<TreeNodeBO> rootList = new ArrayList<TreeNodeBO>();
		for (PrivilegeBO privilege : privilegeTree) {
			if (upPrivilegeId.equals(privilege.getUpPrivilegeId())) {
				TreeNodeBO treeNode = new TreeNodeBO();
				treeNode.setId(privilege.getPrivilegeId());
				treeNode.setText(privilege.getPrivilegeName());
				treeNode.setUrl(privilege.getAppUrl());
				if (upPrivilegeId != -1) {
					treeNode.setCommon(privilege.getCommon().toString());
					treeNode.setIcon(privilege.getIcon());
				}
				List<TreeNodeBO> childrenList = buildTree(privilege.getPrivilegeId(), privilegeTree);
				treeNode.setChildren(childrenList);
				rootList.add(treeNode);
			}
		}
		return rootList;
	}

	/**
	 * 登录操作
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public AjaxReturnMessage login(AdminAccountBO adminAccount, HttpSession session) {
		// 检测是否是社区账号登陆
		String account = adminAccount.getAccount();
		String password = adminAccount.getPassword();
		String accountPre = Configuration.getConfig().getValue(Propert.ACCOUNT_PRE);
		if (account.trim().indexOf(accountPre) != 0) {
			adminAccount.setAccount(accountPre + account);
		} else {
			account = account.substring(account.trim().indexOf(accountPre) + accountPre.length());
		}
		// 检测用户是否存在
		adminAccount = this.checkUser(adminAccount);
		if (adminAccount == null) {
			adminAccount = new AdminAccountBO();
			adminAccount.setAccount(account);
			adminAccount.setPassword(password);
			// 检测用户是否存在
			adminAccount = this.checkUser(adminAccount);
		} else {
			CommunityAccountBO communityAccount = communityAccountService.findByAccount(account);
			session.setAttribute("communityAccount", communityAccount);
			session.setAttribute("communityId", communityAccount.getCommunityId());
			session.setAttribute("communityAccountId", communityAccount.getCommunityAccountId());
		}
		if (adminAccount != null) {
			if (adminAccount.getStatus().equals(AdminAccountStatu.ENABLE.getValue() + "")) {
				// 用户ID
				session.setAttribute("accountId", adminAccount.getAccountId());
				// 保存用户信息
				session.setAttribute("adminAccount", adminAccount);

				return AjaxReturnMessage.createMessage(true);
			} else {
				return AjaxReturnMessage.createMessage("账号已被禁用，请联系管理员！", false);
			}
		} else {
			return AjaxReturnMessage.createMessage("账号或者密码不正确！", false);
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "addUser")
	@ResponseBody
	public AjaxReturnMessage addUser(AdminAccountBO adminAccount, HttpServletRequest req)
			throws UnsupportedEncodingException {

		AdminAccountBO account = new AdminAccountBO();
		account.setUserPhone(adminAccount.getUserPhone());
		account.setAccountId(adminAccount.getAccountId());
		account = adminAccountService.checkRegisterUser(account);
		if (account != null) {
			return AjaxReturnMessage.createMessage("该手机号已被使用，请重新输入！", false);
		}
		account = new AdminAccountBO();
		account.setAccount(adminAccount.getAccount());
		account.setAccountId(adminAccount.getAccountId());
		account = adminAccountService.checkRegisterUser(account);
		if (account != null) {
			return AjaxReturnMessage.createMessage("该账号已被使用，请重新输入！", false);
		}
		if (CheckUtil.isNullEmpty(adminAccount.getAccountId())) {
			adminAccountService.addUser(adminAccount);
			return AjaxReturnMessage.createMessage("账号已添加！", true);
		} else {
			adminAccountService.updateUser(adminAccount);
			return AjaxReturnMessage.createMessage("账号已修改！", true);
		}
	}

	/**
	 * 更新用户跳转
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "edit")
	public String edit(Integer accountId, HttpServletRequest req) {
		// 需要更新记录的id
		AdminAccountBO adminAccount = adminAccountService.selectOneUser(accountId);
		req.setAttribute("user", adminAccount);
		return "system/addUsers";
	}

	/**
	 * 修改当前登陆用户密码
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "changeUserPWD")
	@ResponseBody
	public AjaxReturnMessage changeUserPWD(AdminAccountBO account, HttpServletRequest req) {
		Integer accountId = (Integer) req.getSession().getAttribute("accountId");

		AdminAccountBO adminAccount = adminAccountService.getUserById(accountId);
		if (!adminAccount.getPassword().toUpperCase().equals(MD5.MD5(account.getOldPassword()))) {
			return AjaxReturnMessage.createMessage("旧密码不正确！", false);
		}
		;
		if (CheckUtil.isNullEmpty(account.getPassword())) {
			return AjaxReturnMessage.createMessage("请输入新密码！", false);
		}
		adminAccount.setPassword(MD5.MD5(account.getPassword()));
		adminAccountService.updateUser(adminAccount);
		return AjaxReturnMessage.createMessage(true);
	}

	/**
	 * 重置用户密码
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "setUserPWD")
	@ResponseBody
	public AjaxReturnMessage setUserPWD(Integer accountId) {
		AdminAccountBO adminAccount = adminAccountService.getUserById(accountId);
		adminAccount.setPassword(MD5.MD5("123456"));
		adminAccountService.updateUser(adminAccount);
		return AjaxReturnMessage.createMessage("重置密码成功!密码为：123456", true);
	}

	/**
	 * 退出系统
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "logout")
	public void logout(HttpSession session, HttpServletResponse response) {
		// 清空session
		session.invalidate();
	}

	/**
	 * 删除用户
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "delUser")
	@ResponseBody
	public AjaxReturnMessage delUser(Integer accountId) {

		AdminAccountBO adminAccount = adminAccountService.getUserById(accountId);
		if (adminAccount == null) {
			return AjaxReturnMessage.createMessage("账号不存在！", false);
		}
		if (adminAccount.getAccount().equals("admin") || adminAccount.getAccountId().equals(10000000)) {
			return AjaxReturnMessage.createMessage("不能删除管理员账号！", false);
		}
		adminAccountService.delUser(accountId);
		return AjaxReturnMessage.createMessage("已删除！", true);
	}

	/**
	 * 验证用户名是否存在
	 * 
	 * @param map
	 * @return
	 */
	public AdminAccountBO checkUser(AdminAccountBO adminAccount) {
		return adminAccountService.checkLoginUser(adminAccount);
	}

	/**
	 * 验证用户信息是否重复
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "checkUsers")
	@ResponseBody
	public Map checkUsers(HttpServletRequest req) {
		Map map = new HashMap();
		String msg = "";
		String usercode = req.getParameter("usercode");
		String phone = req.getParameter("phone");
		String email = req.getParameter("email");
		// 前台的表单数据

		int name = adminAccountService.checkUserName(usercode);
		int userPhone = adminAccountService.checkUserPhone(phone);
		int userEmail = adminAccountService.checkUserEmail(email);
		if (name > 0) {
			msg = "usercode";
		} else if (userPhone > 0) {
			msg = "phone";
		} else if (userEmail > 0) {
			msg = "email";
		}
		map.put("msg", msg);
		return map;
	}
}
