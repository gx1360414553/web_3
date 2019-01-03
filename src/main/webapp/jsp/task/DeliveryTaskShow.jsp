<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<div init-model='deliveryTask'>
<%=JSONObject.toJSONString( request.getAttribute( "deliveryTask" ) )%>
</div>
<div init-model='user'>
<%=JSONObject.toJSONString( request.getAttribute( "user" ) )%>
</div>
<div init-model='nickname'>
<%=JSONObject.toJSONString( request.getAttribute( "nickname" ) )%>
</div>
<div init-model='amount'>
<%=JSONObject.toJSONString( request.getAttribute( "amount" ) )%>
</div>
<form class="form-horizontal ng-pristine ng-valid" role="form">
<div class="row" style="max-height: 500px;overflow: auto;">
<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right" style="text-align: right;">收货人</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{deliveryTask.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right" style="text-align: right;">联系电话</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{deliveryTask.mobile}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
	   <label class="col-lg-1 col-md-1 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">地址</label>
	   <div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="word-break: break-all;">
	   		{{deliveryTask.areaName}}{{deliveryTask.address}}
	   </div>
	</div>
	 <div class="clearfix"></div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">接单人</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{user.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">联系电话</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{user.account}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
	   <label class="col-lg-1 col-md-1 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">地址</label>
	   <div class="col-lg-11 col-md-11 col-sm-9 col-xs-8">
	   		{{user.areaName}}{{user.address}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">任务编号</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{deliveryTask.taskNo}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">顾客</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-md-6 col-sm-12 col-xs-12">
	   <label class="col-lg-2 col-md-2 col-sm-3 col-xs-4 no-padding-right"  style="text-align: right;">金额</label>
	   <div class="col-lg-4 col-md-4 col-sm-9 col-xs-8">
	   		{{ amount / 1000  }}元
	   </div>
	</div>
</div>
</form>