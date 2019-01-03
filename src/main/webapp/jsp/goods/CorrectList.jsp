<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<script type="text/javascript" src="resource/js/goods/services/CorrectService.js"></script>
<script type="text/javascript" src="resource/js/goods/controllers/CorrectController.js"></script>
</head>
<body>
<div class="row" ng-controller="correctCtrl">
    <div class="col-xs-12 col-md-12">
        <div class="widget">
            <div class="widget-header ">
                <span class="widget-caption"></span>
                <div class="widget-buttons">
                    <a href="" widget-collapse></a>
                    <a href="" widget-maximize></a>
                </div>
            </div>
            <div style="display: none;" id="listCommunity">
            	<cx:out-sql sqlId="options.listCommunity" />
            </div>
            <div class="widget-body">
            	<form class="form-inline" id="community">
            				<div class="form-group form-group-margin">
            				    <div class="input-group">
		                            <span class="input-group-addon">社区</span>
		                            <ui-select
								   		on-select="selectCommunity($item.code)"
								   		ng-init="temp=communityList[0]"
								   		ng-model="temp"
									   	style="min-width: 210px">
							            <ui-select-match >{{$select.selected.name}}</ui-select-match>
							            <ui-select-choices repeat="item in communityList | filter: {name:$select.search} track by $index">
							                <span ng-bind-html="item.name | highlight: $select.search"></span>
							            </ui-select-choices>
							        </ui-select>
		                        </div>
		                    </div>
		                    <div class="form-group form-group-margin">
            				    <div class="input-group">
		                            <span class="input-group-addon">小区</span>
		                            <ui-select
								   		on-select="search.communityLocationId=$item.communityLocationId"
								   		ng-init='communityLocationList.splice(0,0,{address:"全部",communityLocationId:""})'
								   		ng-model="communityLocation"
									   	style="min-width: 210px">
							            <ui-select-match >{{$select.selected.address}}</ui-select-match>
							            <ui-select-choices repeat="communityLocation in communityLocationList">
							                <span ng-bind-html="communityLocation.address | highlight:$select.search"></span>
							            </ui-select-choices>
							        </ui-select>
		                        </div>
		                    </div>
		                    <div class="form-group form-group-margin">
                       			<div class="input-group">
                           			<span class="input-group-addon">手机号</span>
                           			<input ng-model="search.mobile" type="text" class="form-control">
                       			</div>
                   			</div>
                   			
							<div class="form-group form-group-margin">
		                        <div class="input-group">
		                            <span class="input-group-addon">状态</span>
		                            <ui-select
								   		on-select="search.status=$item.code"
								   		ng-init='
								   			options=<cx:out-options dictMapCd="correctStatus" />;
								   			options.splice(0,0,{name:"全部",code:""});
								   		'
									   	ng-model="temp" style="min-width: 200px">
							            <ui-select-match>{{$select.selected.name}}</ui-select-match>
							            <ui-select-choices repeat="item in options | filter: {name:$select.search}">
							                <span ng-bind-html="item.name | highlight: $select.search"></span>
							            </ui-select-choices>
							        </ui-select>
		                        </div>
		                    </div>
		                    
		                    	 <div class="form-group form-group-margin">
                       			<div class="input-group">
                           			<span class="input-group-addon">商品</span>
                           			<input ng-model="search.goodsName" type="text" class="form-control">
                       			</div>
                   			</div>
                   			<div class="form-group form-group-margin">
                       			<div class="input-group">
                           			<span class="input-group-addon">有效开始时间</span>
                           			<input ng-model="search.validStart" type="text" class="form-control" datetimepicker>
                       			</div>
                   			</div>
                   			
			    			<div class="form-group form-group-margin">
		                        <div class="input-group">
		                            <span class="input-group-addon"> 创建时间</span>
					                <input type="text" class="form-control" datetimepicker ng-model="search.startTime"/>
		                     	</div>
		                    </div>
		                     <div class="form-group form-group-margin">
                            		至
		                    </div>
		                    <div class="form-group form-group-margin">
		                        <div class="input-group">
					                <input type="text" class="form-control" datetimepicker ng-model="search.endTime"/>
		                     	</div>
		                    </div>
				  <a ng-click="list()" class="btn btn-primary form-group-margin" href="javascript:void(0);">
				  	<i class="fa fa-search"></i>搜索
				  </a>
				  
				</form>
				
				<!-- 表格 -->
                <table id="correctList" style="width: 100%"></table>
				<!-- 分页 -->
				<div id="correctPager"></div>
				
            </div>
        </div>
    </div>
</div>
<!--消息框 -->
<toaster-container toaster-options="{'position-class': 'toast-top-center', 'close-button':true,'limit':1}"></toaster-container>
</body>
</html>