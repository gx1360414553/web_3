<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>

<form class="form-horizontal ng-pristine ng-valid" role="form" id="supermarketInfoForm" method="post" enctype="multipart/form-data">
	<input  type="hidden" ng-model="correct.correctId"/>
<div class="row">
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">商品</label>
	   <div class="col-sm-9">
	       <input ng-required="required" type="text" readonly="readonly" ng-model="correct.goodsName" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">姓名</label>
	   <div class="col-sm-9">
	       <input ng-required="required" type="text" readonly="readonly" ng-model="correct.nickname" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">分享价格(元)</label>
	   <div class="col-sm-9">
	       <input ng-required="required" type="text" ng-model="correct.price" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">价格类型</label>
	   <div class="col-sm-9">
	   <ui-select
	   		on-select="correct.priceType.code=$item.code"
	   		ng-init='
	   			options=<cx:out-options dictMapCd="correctPriceType" />;
	   		'
		   	ng-model="correct.priceType" style="min-width: 200px">
	           <ui-select-match>{{$select.selected.name}}</ui-select-match>
	           <ui-select-choices repeat="item in options | filter: {name:$select.search}">
	               <span ng-bind-html="item.name | highlight: $select.search"></span>
	           </ui-select-choices>
	       </ui-select>
	    </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">价格有效期</label>
	   <div class="col-sm-9">
	       <input ng-required="required" type="text" datetimepicker options="timepicker:false" ng-model="correct.validStart" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-3 control-label no-padding-right">至</label>
	   <div class="col-sm-9">
	       <input ng-required="required" type="text" datetimepicker options="timepicker:false" ng-model="correct.validEnd" class="form-control">
	   </div>
	</div>
	
</div>
</form>

