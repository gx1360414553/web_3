<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<form class="form-horizontal ng-pristine ng-valid" role="form">
	<input  type="hidden" ng-model="correct.correctId"/>
<div class="row">
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-4 no-padding-right" style="text-align: right;">商品：</label>
	   <div class="col-sm-8">
	   		{{correct.goodsName}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-4 no-padding-right" style="text-align: right;">分享员：</label>
	   <div class="col-sm-8">
	   		{{correct.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-4 no-padding-right" style="text-align: right;">分享价格：</label>
	   <div class="col-sm-8">
	   		{{correct.price}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-4 no-padding-right" style="text-align: right;">价格类型：</label>
	   <div class="col-sm-8">
	   		{{correct.priceTypeName}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label class="col-sm-4 no-padding-right" style="text-align: right;">价格有效期：</label>
	   <div class="col-sm-8">
	   		{{correct.validStart}} 至  {{correct.validEnd}}
	   </div>
	</div>

</div>
</form>

