<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<form id="category-edit-form" method="post" enctype="multipart/form-data" class="form-horizontal ng-pristine ng-valid" role="form">
<div class="row" style="max-height: 500px;overflow: auto;">
	<div ng-show="!category.categoryId" class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-sm-2 no-padding-right" style="text-align: right;">父类别名称</label>
	   <div class="col-sm-10">
	   		{{category.parentName}}
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">类别名称</label>
	   <div class="col-sm-10">
	       <input ng-required="required" type="text" ng-model="category.name" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label class="col-sm-2 control-label no-padding-right">类别图</label>
	   <div class="col-sm-10">
	       	<div ng-model="category.logo" ui-imagebox name="logo" data-options=" width:'300px'"></div>
	   </div>
	</div>
</div>
</form>

