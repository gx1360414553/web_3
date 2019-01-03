<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/tags-cx" prefix="cx"%>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth"%>
<div init-model='community'>
	<%=JSONObject.toJSONString(request.getAttribute("community"))%>
</div>
<form class="form-horizontal ng-pristine ng-valid" role="form">
	<input type="hidden" ng-model="community.communityId" />
	<div class="row" style="max-height: 500px; overflow: auto;">
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">社区编号</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text" placeholder="社区名的前两个字的拼音第一位再大写+00"
					ng-model="community.sectionNo" class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">社区名称</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text" ng-model="community.name"
					class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">负责人名称</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text"
					ng-model="community.nickname" class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">负责人手机</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text"
					ng-model="community.mobile" class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">微信号</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text"
					ng-model="community.weixingAccount" class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">QQ号</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text"
					ng-model="community.qqAccount" class="form-control">
			</div>
		</div>
		<div class="form-group col-lg-6 col-xs-6 col-sm-12">
			<label class="col-sm-3 control-label no-padding-right">地址</label>
			<div class="col-sm-9">
				<input ng-required="required" type="text"
					ng-model="community.address" class="form-control">
			</div>
		</div>
	</div>
</form>

