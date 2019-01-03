'use strict';

app.controller('communityCtrl', function($scope,toaster,communityService,messager,templateform ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.remove = remove;
		$scope.list = list;
		$scope.showCommunityLocation = showCommunityLocation;
		$scope.search={
				communityId:"",
				name:"",
				address:"",
				areaCode:"",
				createTime:"",
				amount:"",
				remark:"",
				lastModify:"",
				removed:"",
				sectionNo:"",
				longitude:"",
				latitude:"",
		}
		$scope.list();
		$scope.openBindSupermarket= openBindSupermarket;
	}
	
	/**列表查询*/
	function list(){
		var $grid = $("#communityList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/community/community/list.do",
			postData:$scope.search,
			pager : "#communityPager",
			subGrid: true,
			subGridRowExpanded: showChildGrid,
		    subGridOptions : {
				reloadOnExpand :false,
				selectOnExpand : true 
			},
			colModel : [
				{name:'communityId',label:'社区ID',sortable:false,width:80}, 
				{name:'sectionNo',label:'社区编号',sortable:false,width:80}, 
				{name:'name',label:'社区名称',sortable:false,width:160}, 
				{name:'nickname',label:'社区负责人名称',sortable:false,width:80}, 
				{name:'mobile',label:'手机号',sortable:false,width:80}, 
				{name:'address',label:'地址',sortable:false,width:200}, 
	             {label:"操作",name:"opt",width:300,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					opts = opts + "<a href='javascript:void(0);' ng-click='edit( "+rowObject.communityId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>修改</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='showCommunityLocation( "+rowObject.communityId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>配送区域</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='openBindSupermarket( "+rowObject.communityId+")' class='btn btn-primary fa fa-edit btn-sm td-compile'>绑定超市</a> ";
            		return opts;
	             }}
			]
		});
	}
	
	/**查询负责超市或者比价超市*/
	 function showChildGrid(parentRowID, parentRowKey) {
		 var row = $(this).jqGrid("getRowData",parentRowKey);
		 debugger
	     var childGridID = parentRowID + "_table";
	     var childGridPagerID = parentRowID + "_pager";
	     $('#' + parentRowID).append('<table id=' + childGridID + '></table><div id=' + childGridPagerID + ' class=scroll></div>');
	     $("#" + childGridID).jqGrid({
	         url: urls.ms+"/community/supermarket/listBindSupermarket.do?communityId="+row.communityId,
	         mtype: "GET",
	         datatype: "json",
	         colModel: [
	             { name: 'name', label: '超市名称', key: true, width: 60,sortable:false },
	             { name: 'typeName', label: '超市类型', width: 60,sortable:false },
	             { name: 'communityName', label: '所属社区', key: true, width: 60,sortable:false },
	             { name: 'address', label: '地址', width: 200 ,sortable:false},
	             {name:'createTime',label:'创建时间',sortable:false,formatter:function( cellvalue, options, rowObject ){
						return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd hh:mm:ss") : "";
				}},
	         ],
			loadonce: true,
	         width: 500,
	         rownumbers:false,
	         height: '100%',
	     });
	
	 }
	
	 /**社区配送地址*/
	 function showCommunityLocation( communityId ){
		 var url = urls.ms + "/jsp/community/CommunityLocationList.jsp?";
		 if( communityId ){
			url = url + $.param( {communityId:communityId} );
		 }
		 $scope.fromCommunity = communityId;
		 $scope.width="120px"
		 templateform.open({
			title:"社区编辑",
			url:url,
			scope:$scope,
			buttons:[],
			onOpen:function( $modalInstance, data ,$scope){
				
			}
		 },function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		 });
	 }
	 
    /**删除*/
    function remove( id ){
    	messager.confirm("确认删除？",function( $modalInstance ){
    		communityService.remove({
    			"data":{communityId:id},
    			"success":function( data ){
            		if( data.success ){
            			$("#communityList").trigger("reloadGrid");
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
    function edit( id ){
    	var url = urls.ms + "/community/community/edit.do?";
		if( id ){
			url = url + $.param( {communityId:id} );
		}
		templateform.open({
			title:"社区编辑",
			url:url,
			scope:$scope,
			onOpen:function( $modalInstance, data ,$scope){
					
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		});
    }
    
	/**保存*/
    function save( $modalInstance,data, $scope ){
    	try{
			communityService.save( {
				data:$scope.community,
				success:function( data ){
					if( data.success ){
						$("#communityList").trigger("reloadGrid");
						$modalInstance.close();
					}else{
						$scope.$apply(function(){
							toaster.error( "",data.msg,3000 );
						});
					}
				}
			} )
		}catch (e) {
			console.error( e );
			toaster.error( "",typeof e == "string" ? //
					e : e.msg ? //
							e.msg : "出错了",3000 );
		}
    }
    
    /**
     * 绑定负责超市或者比较超市
     * @param type 10:负责超市，20:比价超市
     */
    function openBindSupermarket( communityId ){
    	var url = urls.ms + "/jsp/community/CommunitySupermarketList.jsp?";
		 if( communityId ){
			url = url + $.param( {communityId:communityId} );
		 }
		 templateform.open({
			title:"超市信息列表",
			url:url,
			scope:$scope,
			buttons:[],
			onOpen:function( $modalInstance, data ,$scope){
				$scope.communityId = communityId;
			}
		 },function( $modalInstance,data, $scope ){
			
		 });
    }
    
    /**查看*/
    function show( id ){
    	var url = urls.ms + "/community/community/show.do?";
		if( id ){
			url = url + $.param( {communityId:id} );
		}
		templateform.open({
			title:"Community",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url
		});
    }
    
    //初始化
    init();
});