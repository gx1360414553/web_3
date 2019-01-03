<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<form class="form-horizontal ng-pristine ng-valid" role="form" id="supermarketInfoForm" method="post" enctype="multipart/form-data">
	<input  type="hidden" ng-model="supermarket.supermarketId"/>
<div class="row">
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">名称</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="supermarket.name" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">地址</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="supermarket.address" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">选择街道</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" readonly="readonly" ng-click="selectArea()" ng-model="supermarket.areaName" class="form-control">
	       <input ng-required="required" type="hidden" readonly="readonly" ng-click="selectArea()" ng-model="supermarket.areaCode" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">超市logo</label>
	   <div class="col-sm-10">
	       <div  name="imageFile" style="width: 100%;" ui-imagebox ng-model="supermarket.logo"></div>
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">任务段号</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="supermarket.sectionNo" class="form-control">
	   </div>
	</div>
	<br/>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12" ng-hide="supermarket.supermarketId">
	<label class="col-sm-2 control-label no-padding-right">负责社区</label>
	<div class="col-sm-10" >
         <ui-select
			on-select="search.communityId = $item.code" 
			ng-init='
			options=<cx:out-sql sqlId="options.listCommunity"></cx:out-sql>;'
			ng-model="supermarket.community" style="min-width: 230px">
			<ui-select-match>{{$select.selected.name}}</ui-select-match>
			<ui-select-choices repeat="item in options | filter:{name: $select.search}">
				<span ng-bind-html="item.name | highlight: $select.search"></span>
			</ui-select-choices>
			</ui-select>
			</div>
	</div>
	
</div>
</form>

