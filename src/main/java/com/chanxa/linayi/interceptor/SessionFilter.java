package com.chanxa.linayi.interceptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSONObject;

import com.chanxa.linayi.bo.system.AdminAccountBO;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.exception.BusinessException;
import com.chanxa.linayi.common.util.AjaxReturnMessage;

public class SessionFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	
	/**
	 * 为true则校验登陆
	 */
	private static final boolean authLogin = true;
	
	/**
	 * 为ture则校验权限
	 */
	private static final boolean authPrivilage = false;
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader("Expires",0);
        // 不过滤的uri
		String contentPath = request.getContextPath();
        String[] notFilter = new String[] {
        		contentPath,
        		contentPath+"/",
        		contentPath+"/index.jsp",
        		contentPath+"/login.jsp",
    			contentPath+"/html/.{1,}/.{1,}\\.html",
        		contentPath+"/resource/.{0,}",
        		contentPath+"/backstageUserAction/login.do",
        		contentPath+"/backstageUserAction/logout.do"};
        // 请求的uri
        String uri = request.getRequestURI();
        // 是否过滤
        boolean doFilter = true;
        for (String s : notFilter) {
            if (uri.equals(s) || uri.matches(s)) {
                doFilter = false;
                break;
            }
        }
        try{
        	//如果需要过滤该URI则执行if
        	if (doFilter) {
        		//判断是否已登陆且有权限访问，如果都为true,则执行请求
        		if ( (isLogin( request, response ) &&  hashPrivilege( request, response ) ) ) {
        			filterChain.doFilter(request, response);
        		}
        	}else {
        		filterChain.doFilter(request, response);
        	}
        }catch (Exception e) {
        	String contentType = request.getContentType();
        	if( CheckUtil.isNotNullEmpty( contentType ) ){
        		response.setContentType( contentType );
        	}else{
        		response.setContentType( MediaType.APPLICATION_JSON_UTF8.toString() );
        	}
        	logger.error( uri + "->",e );
        	Throwable rootCase = e;
        	while( rootCase.getCause() != null && rootCase.getCause().getClass() != rootCase.getClass() ){
        		rootCase = rootCase.getCause();
        	}
        	if( rootCase instanceof BusinessException ){
        		BusinessException bx = ( BusinessException )rootCase;
        		AjaxReturnMessage ajax = AjaxReturnMessage.createMessage( bx.getMessage(), false );
    			response.getWriter().write( JSONObject.toJSONString( ajax ) );
    			return;
        	}
        	AjaxReturnMessage ajax = AjaxReturnMessage.createMessage( "系统出錯啦！("+ rootCase.getMessage()+")" , false );
			response.getWriter().write( JSONObject.toJSONString( ajax ) );
			return;
		}
    }
	
	/**判断是否有权限*/
	private boolean hashPrivilege( HttpServletRequest request,HttpServletResponse response ) throws IOException{
		if( !authPrivilage ){
			return true;
		}
		AdminAccountBO account = ( AdminAccountBO )request.getSession().getAttribute("adminAccount");
		if( "1".equals( account.getIsAdmin() ) ){
			return true;
		}
		String uri = request.getRequestURI();
		//不验证权限的URL
		String contentPath = request.getContextPath();
		List<String> notFilter = Arrays.asList( 
			contentPath+"/jsp/system/changePWD.jsp",
			contentPath+"/backstageUserAction/home.html",
			contentPath+"/html/.{1,}/.{1,}\\.html",
			contentPath+"/backstageUserAction/changeUserPWD.do");
		if( notFilter.contains( uri ) || CommonControllerSupport.getUserPrivilegeUrl( request ).contains( uri.substring( request.getContextPath().length() ) )){
			return true;
		}
		if(request.getHeader("X-Requested-With") != null){
			response.setStatus( 403 );
			response.getWriter().write( JSONObject.toJSONString( AjaxReturnMessage.createMessage( "无权限！", false)) );
			return false;
		}
		response.getWriter().write( "<div style='font-size:22px;color:red;text-align:center;margin-top: 20px;'>无权限！<div>");
		return false;
	}
	
	/**判断是否已登录*/
	private boolean isLogin( HttpServletRequest request,HttpServletResponse response  ) throws IOException{
		if( !authLogin ){
			return true;
		}
		Object obj = request.getSession().getAttribute("adminAccount");
		if( obj == null ){
			String contentPath = request.getContextPath();
			if(request.getHeader("X-Requested-With") != null){
				response.setStatus(405);
				response.getWriter().write( JSONObject.toJSONString( AjaxReturnMessage.createMessage( "由于您过长时间没有操作，请重新登陆！", false)) );
				return false;
			}
			response.sendRedirect(contentPath+"/login.jsp" );
			return false;
		}
		return true;
	}
}