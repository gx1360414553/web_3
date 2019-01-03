'use strict';
app.controller('communityLocationCtrl', function($scope,toaster,communityLocationService,messager,templateform,$http ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.remove = remove;
		$scope.list = list;
		$scope.search={
				address:"",
				communityId:""
		}
		//如果是从社区管理编辑页面跳转过来则初始化
		if( $scope.fromCommunity ){
			$scope.search.communityId = $scope.fromCommunity;
		}
//		$scope.list();
	}
	
	/**列表查询*/
	function list(){
		var $grid = $("#communityLocationList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/community/communityLocation/list.do",
			postData:$scope.search,
			pager : "#communityLocationPager",
			colModel : [
	            {name:'communityLocationId',label:'id',sortable:false,hidden:true}, 
				{name:'streetName',label:'街道',sortable:false}, 
				{name:'address',label:'小区',sortable:false}, 
				{name:'communityName',label:'社区',sortable:false}, 
				{name:'supermarketName',label:'超市',sortable:false}, 
	            {label:"操作",name:"opt",width:170,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					opts = opts + "<a href='javascript:void(0);' ng-click='edit( "+rowObject.communityLocationId+","+rowObject.supermarketId+")' class='btn btn-primary fa fa-edit btn-sm td-compile'>编辑</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='remove( "+rowObject.communityLocationId+")' class='btn btn-primary fa fa-remove btn-sm td-compile'>删除</a> ";
            		return opts;
	             }}
			]
		});
	}
	
    /**删除小区*/
    function remove( communityLocationId){
    	messager.confirm("确认删除该小区？",function( $modalInstance ){
    		communityLocationService.remove({
    			"data":{communityLocationId:communityLocationId},
    			"success":function( data ){
            		if( data.success ){
            			$("#communityLocationList").trigger("reloadGrid");
            			$scope.$apply(function(){
            				toaster.error( "",typeof e == "string" ? //
            						e : e.msg ? //
            								e.msg : "出错了",3000 );
						});
            			$modalInstance.close();
            		}else{
            			$scope.$apply(function(){
            				toaster.error( "",typeof e == "string" ? //
            						e : e.msg ? //
            								e.msg : "出错了",3000 );
						});
            		}
            	}
    		});
    	});
    }
    
    /**新增，编辑*/
    function edit( communityLocationId,supermarketId){
    	var url = urls.ms + "/community/communityLocation/edit.do?";
    	if( communityLocationId ){
    		url += $.param( {communityLocationId:communityLocationId,supermarketId:supermarketId} )
    	}
		$http({
		  method: 'post',
		  url: url,
		  responseType:"json"
		}).then(function successCallback(response) {
			openEdit( response.data.data )
		    // this callback will be called asynchronously
		    // when the response is available
		 }, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		 });
    }
    function openEdit( data ){
    	templateform.open({
			title:"编辑",
			url:urls.ms + "/jsp/community/CommunityLocationEdit.jsp",
			scope:$scope,
			data:data,
			onOpen:function( $modalInstance, data ,$scope){
				/*绑定数据edit*/
				var areaList  = data.areaList;
				var areaCtrl = $scope.areaCtrl = {
					provinceList: areaList[1000],
					province:{},
					cityList: data.communityLocation ? areaList[data.province] : [],
					city: {},
					regionList: data.communityLocation ? areaList[data.city] : [],
					region:{}
				}
				
				var communityLocationCtrl = $scope.communityLocationCtrl = {
						communityLocation : data.communityLocation,
						communityList :data.communityList,
				}
				
				
				
				/* 有id初始化表单 */
				if( communityLocationCtrl.communityLocation ){
					areaCtrl.province = data.provinceCityAndRegion;
					areaCtrl.city = data.provinceCityAndRegion.child[0];
					areaCtrl.region = data.provinceCityAndRegion.child[0].child[0];
					areaCtrl.region = data.provinceCityAndRegion.child[0].child[0];
					areaCtrl.street = data.provinceCityAndRegion.child[0].child[0].child[0];
					communityLocationCtrl.community = data.community;
				}
				/*事件绑定  选择省 市 区 街道 小区*/
				var dataChangeInRegion = null;
				var selectCommunity = null;
				$scope.selectedArea = function( $item, level){
				/*改变区域时,后台获取的数据*/
					switch (level) {
					case 1:
						areaCtrl.cityList = areaList[$item.code];
						areaCtrl.city={};
						
						areaCtrl.regionList = [];
						areaCtrl.region={};
						
						areaCtrl.streetList = [];
						areaCtrl.street = {};
						
						break;
					case 2:
						areaCtrl.regionList = [];
						areaCtrl.region={};
						
						areaCtrl.streetList = [];
						areaCtrl.street = {};
						
						areaCtrl.regionList = areaList[$item.code];
						break;
						/*获取区下的社区和小区列表*/
					case 3:
						areaCtrl.streetList = [];
						areaCtrl.street = {};
						if(areaList[$item.code]==undefined||areaList[$item.code]==''){
							toaster.error("","该区暂时没有街道信息,请重新选择",1000);
						}
						areaCtrl.streetList = areaList[$item.code];
						break;
					default :
						break;
					}
				}
				
				
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		});
    }
    
	/**保存*/
    function save( $modalInstance,data, $scope ){
    	try{
    		var areaCtrl = $scope.areaCtrl;
    		var communityLocationCtrl = $scope.communityLocationCtrl;
    		if( !areaCtrl.province.code ){
    			return toaster.error("请选择省！");
    		}
    		if( !areaCtrl.city.code ){
    			return toaster.error("请选择市！");
    		}
    		if( !areaCtrl.region.code ){
    			return toaster.error("请选择区！");
    		}
    		if( !areaCtrl.street.code){
    			return toaster.error("请输入街道！");
    		}
    		if( !communityLocationCtrl.communityLocation.address ){
    			return toaster.error("请输入小区名！");
    		}
    		if($scope.search.communityId){
    			var communityId = $scope.search.communityId;
    		}else{
    			var communityId = communityLocationCtrl.community.communityId;
    		}
    		$.ajax({
    			url:urls.ms+"/community/communityLocation/save.do",
    			data:{
    				communityLocationId:communityLocationCtrl.communityLocation.communityLocationId,
    				areaCode:areaCtrl.street.code,
    				address:communityLocationCtrl.communityLocation.address,
    				communityId:communityId,
    			},
    			dataType:"json",
    			type:"post",
    			success:function( data ){
    				$scope.$apply(function(){
	    				if( data.success ){
							$("#communityLocationList").trigger("reloadGrid");
							$modalInstance.close();
						}else{
								toaster.error( "",data.msg,3000 );
						}
    				});
    			}
    		})
		}catch (e) {
			console.error( e );
			toaster.error( "",typeof e == "string" ? //
					e : e.msg ? //
							e.msg : "出错了",3000 );
		}
    }
    
    /**查看*/
    function show( id ){
    	var url = urls.ms + "/community/communityLocation/show.do?";
		if( id ){
			url = url + $.param( {communityLocationId:id} );
		}
		templateform.open({
			title:"CommunityLocation",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url
		});
    }
    
   
    //初始化
    init();
});