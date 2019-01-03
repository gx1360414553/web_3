<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<form class="form-horizontal ng-pristine ng-valid" role="form">
<div class="row" ng-if="correct.parentId == -1">
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">商品</label>
	   <div class="col-sm-10">
	   		{{correct.goodsName}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">超市</label>
	   <div class="col-sm-10">
	   		{{correct.supermarketName}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享员</label>
	   <div class="col-sm-10">
	   		{{correct.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格</label>
	   <div class="col-sm-10">
	   		{{correct.price}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格类型</label>
	   <div class="col-sm-10">
	   		{{correct.priceTypeName}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格有效期</label>
	   <div class="col-sm-4">
	   		{{correct.validStart}}
	   </div>
	    <div class="col-sm-2">
	   		至	
	   	</div>
	   	<div class="col-sm-4">
	   		{{correct.validStart}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享上传图片</label>
	   <div class="col-sm-10">
	   		<img style="width: 300px;height:200px" alt="无图片" ng-src="{{correct.imagePath}}">
	   </div>
	</div>
</div>
<div class="row" ng-if="correct.parentId != -1">
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-1 no-padding-right" style="text-align: right;">商品</label>
	   <div class="col-sm-10">
	   		{{correct.goodsName}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-1 no-padding-right" style="text-align: right;">超市</label>
	   <div class="col-sm-10">
	   		{{correct.supermarketName}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享员</label>
	   <div class="col-sm-10">
	   		{{correct.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">纠错员</label>
	   <div class="col-sm-10">
	   		{{correct.correct.nickname}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格</label>
	   <div class="col-sm-10">
	   		{{correct.price}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">纠错价格</label>
	   <div class="col-sm-10">
	   		{{correct.correct.price}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格类型</label>
	   <div class="col-sm-10">
	   		{{correct.priceTypeName}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">纠错价格类型</label>
	   <div class="col-sm-10">
	   		{{correct.correct.priceTypeName}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享价格有效期</label>
	   <div class="col-sm-4">
	   		{{correct.validStart}}
	   </div>
	    <div class="col-sm-2">
	   		至	
	   	</div>
	   	<div class="col-sm-4">
	   		{{correct.validStart}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">纠错价格有效期</label>
	   <div class="col-sm-4">
	   		{{correct.correct.validStart}}
	   </div>
	    <div class="col-sm-2">
	   		至	
	   	</div>
	   	<div class="col-sm-4">
	   		{{correct.correct.validStart}}
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">分享上传图片</label>
	   <div class="col-sm-10">
	   		<img style="width: 300px;height:200px" alt="无图片" ng-src="{{correct.imagePath}}">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">纠错上传图片</label>
	   <div class="col-sm-10">
	   		<img style="width: 300px;height:200px" alt="无图片" ng-src="{{correct.correct.imagePath}}">
	   </div>
	</div>
	<div class="form-group col-lg-6 col-xs-6 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">证据图片</label>
	   <div class="col-sm-10">
	   		<img style="width: 300px;height:200px" alt="无图片" ng-src="{{correct.imagePath}}">
	   </div>
	</div>
</div>
</form>