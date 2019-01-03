<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<div init-model='communityEarnings'>
<%=JSONObject.toJSONString( request.getAttribute( "communityEarnings" ) )%>
</div>
<form class="form-horizontal ng-pristine ng-valid" role="form">
	<input  type="hidden" ng-model="communityEarnings.communityEarningsId"/>
<div class="row" style="max-height: 500px;overflow: auto;">
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">主键</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="communityEarnings.communityEarningsId" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">社区网点ID</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="communityEarnings.communityId" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">金额</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="communityEarnings.amount" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">收益类型</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="communityEarnings.type" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">收益日期</label>
	   <div class="col-sm-10">
	   		<input type="text" class="form-control" datetimepicker ng-model="communityEarnings.earningsTime"/>
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">创建时间</label>
	   <div class="col-sm-10">
	   		<input type="text" class="form-control" datetimepicker ng-model="communityEarnings.createTime"/>
	   </div>
	</div>

</div>
</form>

