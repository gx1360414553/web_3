<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags-cx" prefix="cx" %>
<%@ taglib uri="http://www.chanxa.com/jsp/tag/core" prefix="auth" %>
<style>
.modal-dialog{
	width: 70%;
}
</style>
<form id="goods-edit-form" method="post" enctype="multipart/form-data" class="form-horizontal ng-pristine ng-valid" role="form">
	<input  type="hidden" ng-model="goods.goodsId"/>
<div class="row">
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">商品标题</label>
	   <div class="col-sm-10">
	       <input max-length="30" ng-required="required" type="text" ng-model="goods.goodsName" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">副标题</label>
	   <div class="col-sm-10">
	       <input max-length="30" ng-required="required" type="text" ng-model="goods.subtitle" class="form-control">
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">分拣类别</label>
	   <div class="col-sm-10">
	   		<ui-select ng-model="sortingCategoryCtrl.sortingCategory">
	            <ui-select-match>{{$select.selected.name}}</ui-select-match>
	            <ui-select-choices repeat="category in sortingCategoryCtrl.sortingCategoryList | filter:{name: $select.search}">
	                <span ng-bind-html="category.name | highlight: $select.search"></span>
		        </ui-select-choices>
			</ui-select>
	   </div>
	</div>
	
	
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
		 <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">商品主图</label>
   		<div style="text-align:center;display: inline-block;width: 20%;padding:0 10px"> 			   		
			<div ng-if="goods.goodImageFirst.firstImage == 1 || goods.goodImageFirst.firstImage == null">			
				<div  name="firstImages" style="width: 100%;" ui-imagebox ng-model="goods.goodImageFirst.imagePath"></div>
			</div>	
							
   		</div>
	</div>
	
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
   		<div ng-repeat="item in goods.goodsImageList" style="text-align:center;display: inline-block;width: 20%;padding:0 10px"> 			   					
			<div ng-if="item.firstImage != 1 ">			
				<div  name="imageList" style="width: 100%;" ui-imagebox ng-model="item.imagePath"></div>
			</div>						
   		</div>
	</div>
	
	
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">商品规格</label>
	   <div class="col-sm-10">
	   <div class="row" >
	       <span class="col-sm-6 col-xs-6 col-lg-4" ng-repeat="attr in goods.attributeList">
	      		<div class="input-group form-group form-group-margin">
	                <span class="input-group-addon">{{attr.name}}</span>
			       	<ui-select
					   	ng-model="attr.attrValue">
			            <ui-select-match>{{$select.selected.name}}</ui-select-match>
			            <ui-select-choices repeat="value in attr.attrValueList | filter:{name: $select.search}">
			                <span ng-bind-html="value.name | highlight: $select.search"></span>
			            </ui-select-choices>
				    </ui-select>
	            </div>
	       </span>
	     </div>
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">销售属性</label>
	   <div class="col-sm-10">
	       <div class="row" >
	       <span class="col-sm-12 col-xs-12 col-lg-12" ng-repeat="attr in goods.salesAttributeList">
               <div>{{attr.name}}</div>
		       <div class="checkbox col-sm-3 col-xs-3 col-lg-3" ng-repeat="value in attr.attrValueList">
                    <label >
                        <input ng-click="toggleChecked( value )" type="checkbox" class="colored-primary" ng-checked="{{ value.removed == 2 }}">
                        <span class="text">{{value.name}}</span>
                    </label>
               </div>
	       </span>
	     </div>
		<div class="row" >
		   	<div>销售规格商品</div>
		   	<table class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th ng-repeat="attr in goods.salesAttributeList">
                            {{attr.name}}
                        </th>
                        <th>
                        	商品条码
                        </th>
                        <th>
                        	采买费(元)
                        </th>
                        <th>
                        	配送费(元)
                        </th>
                        <th>
                        	销售服务费(元)
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="goodsSku in goods.goodsSkuList | filter:{'removed':2}">
                    	<td ng-repeat="skuAttribute in ( goodsSku.skuAttributeList | skuAttributeSorter:goods.salesAttributeList )">
                            {{ skuAttribute.valueName }}
                        </td>
                        <td>
                            <input type="text" ng-model="goodsSku.barcode">
                        </td>
                        <td>
                            <input type="number" ng-change="goodsSku.buyFee = toPositive( goodsSku.buyFee )" ng-model="goodsSku.buyFee">
                        </td>
                        <td>
                            <input type="number" ng-blur="goodsSku.deliverFee = toPositive( goodsSku.deliverFee )" ng-model="goodsSku.deliverFee">
                        </td>
                        <td>
                            <input type="number" ng-blur="goodsSku.serviceFee = toPositive( goodsSku.serviceFee )" ng-model="goodsSku.serviceFee">
                        </td>
                    </tr>
                </tbody>
            </table>
		   	
		</div>
	   </div>
	</div>
	<div class="form-group col-lg-12 col-xs-12 col-sm-12">
	   <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">推荐商品</label>
	   <div class="col-lg-10 col-xs-10  col-sm-10">
	       <div ng-repeat="item in goods.recommendGoodsList" style="text-align: center;display: inline-block;width: 20%;padding:0 10px">
   				<img style="width: 100%;" ng-src="{{item.imagePath}}">
   				<div>{{item.goodsName}}</div>
   			</div>
	   </div>
	</div>
	<div class="row">
		 <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right"></label>
		<div class="col-lg-10 col-xs-10  col-sm-10">
			<a ng-click="recommandModal(  )" href="javascript:void(0);" class="btn btn-primary fa fa-plus btn-sm td-compile ng-scope">
				选择推荐商品
			</a>
		</div>
	</div>
	<div class="row">
		 <label for="inputEmail3" class="col-lg-1 col-xs-1 col-sm-2 control-label no-padding-right">商品详情</label>
		<div class="col-lg-10 col-xs-10  col-sm-10">
			<textarea  ng-model="goods.content" kindeditor></textarea>
		</div>
	</div>
</div>
</form>

