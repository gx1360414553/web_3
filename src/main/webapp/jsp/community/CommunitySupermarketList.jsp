<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<script type="text/javascript" src="resource/js/community/services/CommunitySupermarketService.js"></script>
<script type="text/javascript" src="resource/js/community/controllers/CommunitySupermarketController.js"></script>
<style>
.modal-dialog{
	width: 70%;
}
</style>
</head>
<body>
<div class="row" ng-controller="communitySupermarketCtrl">
    <div class="col-xs-12 col-md-12">
        <div class="widget">
            <div class="widget-header ">
                <span class="widget-caption"></span>
                <div class="widget-buttons">
                    <a href="" widget-collapse></a>
                    <a href="" widget-maximize></a>
                </div>
            </div>
            <div class="widget-body">
            	<form class="form-inline">
							<div class="form-group form-group-margin">
		                        <div class="input-group">
		                            <span class="input-group-addon">名称</span>
		                            <input ng-model="search.name" type="text" class="form-control">
		                        </div>
		                    </div>
							<div class="form-group form-group-margin">
		                        <div class="input-group">
		                            <span class="input-group-addon">创建时间</span>
		                            <input datetimepicker ng-model="search.startTime" ng-model="search.type" type="text" class="form-control">
		                        </div>
		                       	 至
		                        <div class="input-group">
		                            <input datetimepicker ng-model="search.endTime" type="text" class="form-control">
		                        </div>
		                    </div>
				  <a ng-click="list()" class="btn btn-primary form-group-margin" href="javascript:void(0);">
				  	<i class="fa fa-search"></i>搜索
				  </a>
				</form>
				
				<!-- 表格 -->
                <table id="communitySupermarketList" style="width: 100%"></table>
				<!-- 分页 -->
				<div id="communitySupermarketPager"></div>
            </div>
        </div>
    </div>
</div>
<!--消息框 -->
</body>
</html>