'use strict';

app.controller('deliveryTaskCtrl', function($scope,toaster,deliveryTaskService,messager,templateform ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.remove = remove;
		$scope.list = list;
		$scope.search={
				taskId:"",
				taskNo:"",
				title:"",
				content:"",
				communityId:"",
				orderId:"",
				startTime:"",
				endTime:"",
				dieTime:"",
				receiveTime:"",
				deliveryTime:"",
				finishTime:"",
				serviceFee:"",
				realServiceFee:"",
				userId:"",
				status:"",
				createTime:"",
				createTimeStart:"",
				createTimeEnd:""
		}
		$scope.list();
	}
	
	/**列表查询*/
	function list(){
		if( !$scope.search.communityId ){
			return;
		}
		var $grid = $("#deliveryTaskList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/task/deliveryTask/list.do",
			postData:$scope.search,
			pager : "#deliveryTaskPager",
			colModel : [
				{name:'taskId',label:'主键',sortable:false,hidden:true}, 
				{name:'communityId',label:'社区ID',sortable:false,hidden:true}, 
				{name:'taskNo',label:'任务编号',sortable:false}, 
				{name:'nickname',label:'顾客',sortable:false}, 
				{name:'createTime',label:'下单时间',sortable:false,formatter:function( value, options, row ){
					return value ? new Date( value ).format("yyyy-MM-dd HH:mm:ss") : "";
				}}, 
				{name:'statusName',label:'状态',sortable:false}, 
				{name:'amount',label:'金额(元)',sortable:false,formatter:function( value, options, row ){
					return value ? value / 100 : "";
				}}, 
	             {label:"操作",name:"opt",width:170,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					opts = opts + "<a href='javascript:void(0);' ng-click='show( "+rowObject.taskId+","+rowObject.communityId+" )' class='btn btn-primary fa fa-eye btn-sm td-compile'>详情</a> ";
            		return opts;
	             }}
			]
		});
	}
	
    /**删除*/
    function remove( id ){
    	messager.confirm("确认删除？",function( $modalInstance ){
    		deliveryTaskService.remove({
    			"data":{taskId:id},
    			"success":function( data ){
            		if( data.success ){
            			$("#deliveryTaskList").trigger("reloadGrid");
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
    	var url = urls.ms + "/task/deliveryTask/edit.do?";
		if( id ){
			url = url + $.param( {taskId:id} );
		}
		templateform.open({
			title:"DeliveryTask",
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
			deliveryTaskService.save( {
				data:$scope.deliveryTask,
				success:function( data ){
					if( data.success ){
						$("#deliveryTaskList").trigger("reloadGrid");
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
    
    /**查看*/
    function show( id, communityId ){
    	var url = urls.ms + "/task/deliveryTask/show.do?";
		if( id ){
			url = url + $.param( {taskId:id,communityId:communityId} );
		}
		templateform.open({
			title:"任务详情",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url
		});
    }
    
    //初始化
    init();
});