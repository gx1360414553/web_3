'use strict';

app.controller('userCtrl', function($scope,toaster,userService,messager,templateform ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.list = list;
		$scope.auth = auth;
		$scope.batchDisable = batchDisable;
		$scope.disable = disable;
		$scope.search={
				userId:"",
				nickname:"",
				sex:"",
				mobile:"",
				userType:"",
		};
		$scope.checkUserId = new Set();
		$scope.list();
		$scope.user = {
			userId : "",
			nickname : '',
			account : '',
			userType : '',
			email : '',
			headImage : '',
			sexName : '',
			address : '',
			age : '',
			authenticationRemark :'',
			mobile:''
		};
	}
	
	/**列表查询*/
	function list(){
		var $grid = $("#userList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/user/user/list.do",
			postData:$scope.search,
			pager : "#userPager",
			multiselect :true,
			colModel : [
				{name:'nickname',label:'姓名',sortable:false}, 
				{name:'userId',label:'用户id'}, 
				{name:'sex',label:'性别',sortable:false,width:50,formatter:function(cellvalue, options, rowObject){
					if(cellvalue == 1){
						return "男";
					}else if(cellvalue == 2){
						return "女";
					}
					return ""; 
				}}, 
				{name:'mobile',label:'绑定手机号',sortable:false}, 
				{name:'userType',label:'用户类型',sortable:false,formatter:function(cellvalue, options, rowObject){
					if(cellvalue == 10){
						return "用户";
					}else if(cellvalue == 20){
						return "网点配送员";
					}
					return ""; 
				}}, 
				{name:'authentication',label:'认证状态',sortable:false,formatter:function(cellvalue, options, rowObject){
					if(cellvalue == 10){
						return "未认证";
					}else if(cellvalue == 20){
						return "待审核";
					}else if(cellvalue == 30){
						return "认证失败";
					}else if(cellvalue == 40){
						return "认证通过";
					}
					return ""; 
				}}, 
				{name:'valid',label:'禁用状态',sortable:false,formatter:function(cellvalue, options, rowObject){
					return cellvalue == 10 ? "正常" : "禁用"; 
				}}, 
				{name:'createTime',label:'创建时间',sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd hh:mm:ss") : "";
				}},
	             {label:"操作",name:"opt",width:220,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					opts = opts + "<a href='javascript:void(0);' ng-click='show( "+rowObject.userId+" )' class='btn btn-primary fa fa-eye btn-sm td-compile'>查看</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='edit( "+rowObject.userId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>编辑</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='auth( "+rowObject.userId+" )' class='btn btn-primary fa fa-eye btn-sm td-compile'>认证</a> ";
					if(rowObject.valid == 10){
						opts = opts + "<a href='javascript:void(0);' ng-click='disable( "+rowObject.userId+","+ 20 + " )' class='btn btn-primary fa fa-remove btn-sm td-compile'>禁用</a> ";
					}else if(rowObject.valid == 20){
						opts = opts + "<a href='javascript:void(0);' ng-click='disable( "+rowObject.userId+","+ 10 + " )' class='btn btn-primary fa fa-remove btn-sm td-compile'>启用</a> ";
					}
            		return opts;
	             }}
			],
			onSelectRow:function(rowId,status){
				var rows = $( this ).jqGrid( "getRowData" , rowId);
//				alert(rowId + "===========" + status + "========" + rows.userId)
				if(status){
					$scope.checkUserId.add(rows.userId);
				}else{
					$scope.checkUserId.delete(rows.userId);
				}
			}
		});
	}
	
    /**禁用或者启用*/
    function disable( id , valid ){
    	var text = valid == 10 ? "启用" : "禁用"
    	messager.confirm("确认"+ text + "?",function( $modalInstance ){
    		$.ajax({
    			url : urls.ms + '/user/user/disable.do',
    			type : "post",
    			data : {"userId":id,"valid":valid},
    			dataType : "JSON",
    			success : function(data){
    				if( data.success ){
    					$("#userList").trigger("reloadGrid");
    					$scope.$apply(function(){
							$modalInstance.close();
							toaster.success( "","操作成功",3000 );
						});
    				}
    			}
    		})
    		
    	});
    }
    
    /**新增，编辑*/
    function edit( id ){
    	var title = '新增';
    	var url = urls.ms + "/jsp/user/UserEdit.jsp";
    	$scope.user = {
				nickname : "",
				account : "",
				userType : "",
				headImage : "",
				email : "",
				mobile : ""
			}
		if( id ){
			title = '编辑'
			//获取次id的用户信息
			$.ajax({
				url : urls.ms + "/user/user/get.do",
				async : false,
				type : "post",
				data : {"userId":id},
				dataType : "JSON",
				success : function(data){
					console.log(data)
					var user = data.user;
					var userType = data.userType;
					$scope.user = {
						userId : user.userId,
						nickname : user.nickname,
						account : user.account,
						userType : userType,
						headImage : user.headImage,
						email : user.email,
						mobile : user.mobile
					}
				}
			})
		}
		templateform.open({
			title:title + "用户信息",
			url:url,
			scope:$scope,
			data:$scope.user,
			onOpen:function( $modalInstance, data ,$scope){
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		});
    }
    
	/**保存*/
    function save( $modalInstance,data, $scope ){
    	try{
    		console.log(data)
    		$('#userInfoForm').form("submit",{   
    		    url:urls.ms + "/user/user/save.do",
    		    onSubmit: function( param ){   
    		    	for(var key in data){
    		    		param[key] = data[key]
    		    	}
    		    	param.userType = data.userType.code
    		    },   
    		    success:function(data){   
    		    	data = angular.fromJson( data );
    		    	if( data.success ){
						$("#userList").trigger("reloadGrid");
						$scope.$apply(function(){
							$modalInstance.close();
							toaster.success( "","操作成功",3000 );
						});
					}else{
						$scope.$apply(function(){
							toaster.error( "",data.msg,3000 );
						});
					}
    		    }   
    		});
		}catch (e) {
			console.error( e );
			toaster.error( "",typeof e == "string" ? //
					e : e.msg ? //
							e.msg : "出错了",3000 );
		}
    }
    
    /**查看*/
    function show( id ){
    	var url = urls.ms + "/user/user/show.do?";
		if( id ){
			url = url + $.param( {userId:id} );
		}
		templateform.open({
			title:"用户信息显示",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url
		});
    }
    //批量禁用
    function batchDisable(){
    	messager.confirm("确认禁用 ?",function( $modalInstance ){
    	var s = $scope.checkUserId;
    	var size = s.size;
    	if(size == 0){
    		toaster.error( "","请选择需要操作的用户",3000 );
    	}else{
    		var a = new Array();
    		for(var x of s){
    			a.push(x)
    		}
    		var userIdListStr = a.join("-");
    		$modalInstance.close();
    		$.ajax({
    			url:urls.ms + "/user/user/batchDisable.do?",
    			data: { "userIdListStr": userIdListStr} ,
    			type:"post",
    			dataType:"json",
    			success:function(data){
    				$("#userList").trigger("reloadGrid");
					$scope.$apply(function(){
						toaster.success( "","操作成功",3000 );
					});
    			}
    		})
    	}
    	})
    }
    
    function auth(id){
    	var url = urls.ms + "/jsp/user/UserAuth.jsp";
		templateform.open({
			title:"用户认证",
			url:url,
			scope:$scope,
			buttons:[{
				text:"认证通过",
				iconCls:'fa fa-check',
				handle:function( $modalInstance, data ,$scope ){
					$.ajax({
						url : urls.ms + "/user/user/auth.do",
						async : false,
						type : "post",
						data : {"userId":id,"authentication":40},
						dataType : "JSON",
						success:function(data){
							$("#userList").trigger("reloadGrid");
							$modalInstance.close();
							toaster.success( "","操作成功",3000 );
						}
					})
				}
			},{
				text:"不予通过",
				iconCls:'fa fa-times',
				handle:function( $modalInstance, data ,$scope ){
					$.ajax({
						url : urls.ms + "/user/user/auth.do",
						async : false,
						type : "post",
						data : {"userId":id,"authentication":30},
						dataType : "JSON",
						success:function(data){
							$("#userList").trigger("reloadGrid");
							$modalInstance.close();
							toaster.success( "","操作成功",3000 );
						}
					})
				}
			}],
			onOpen:function( $modalInstance, data ,$scope){
				$.ajax({
					url : urls.ms + "/user/user/get.do",
					async : false,
					type : "post",
					data : {"userId":id},
					dataType : "JSON",
					success : function(data){
						console.log(data)
						var user = data.user;
						$scope.user = {
							userId : user.userId,
							nickname : user.nickname,
							account : user.account,
							headImage : user.headImage,
							age : user.age,
							sexName : user.sexName,
							address : user.address,
							authenticationRemark : user.authenticationRemark
						}
					}
				})
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		});
    	
    }
    
    //初始化
    init();
});