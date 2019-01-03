'use strict';

app.controller('correctCtrl', function($scope,toaster,correctService,messager,templateform ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.audit = audit;
		$scope.setShareFee = setShareFee;
		$scope.list = list;
		$scope.communityList = JSON.parse($("#listCommunity").text());
		$scope.search={
				communityId:$scope.communityList[0].code,
				mobile:'',
				nickname:'',
				account:'',
				goodsName:'',
				status:"",
				startTime:'',
				endTime:''
		}
		$scope.list();
		$scope.correct = {
				parentId:'',
				correctId:'',
				goodsName:'',
				nickname:'',
				price:'',
				priceType:'',
				priceTypeName:'',
				validStart:'',
				validEnd:'',
				imagePath:''
		};
		$scope.selectCommunity = selectCommunity;
	}
	
	/**选定社区之后的,小区下拉列表的刷新*/
	function selectCommunity(code){
		$scope.address = {};
		var communityId = $scope.search.communityId= code;
		//发送请求,获取下拉小区列表
		var url = urls.ms+"/community/communityLocation/getCommunityLocationList.do";
		$.ajax({
			url : url,
			async : false,
			type : "post",
			data : {"communityId":communityId},
			dataType : "JSON",
			success:function(data){
				$scope.communityLocationList = data;
			}
		})
	}
		
	/**列表查询*/
	function list(){
		var $grid = $("#correctList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/goods/correct/list.do",
			postData:$scope.search,
			pager : "#correctPager",
			colModel : [
	            {name:'correctId',label:'主键',sortable:false,hidden:true},
			    {name:'nickname',label:'昵称',sortable:false},
			    {name:'mobile',label:'手机号',sortable:false},
			    {name:'statusName',label:'状态',sortable:false},
			    {name:'userId',label:'用户id',sortable:false,hidden:true},
			    {name:'goodsSkuId',label:'商品id',sortable:false,hidden:true}, 
				{name:'goodsName',label:'商品',sortable:false}, 
				{name:'supermarketId',label:'超市id',sortable:false,hidden:true}, 
				{name:'supermarketName',label:'超市',sortable:false}, 
				{name:'typeName',label:'类型',sortable:false}, 
				{name:'type',label:'类型',sortable:false,hidden:true}, 
				{name:'price',label:'价格(元)',sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue/100;
				}}, 
				{name:'priceTypeName',label:'价格类型',sortable:false}, 
				{name:'validStart',label:'有效开始时间',sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd") : "";
				}},
				{name:'validEnd',label:'有效结束时间',sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd") : "";
				}},
				{name:'createTime',label:'创建时间',sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd hh:mm:ss") : "";
				}},
	             {label:"操作",name:"opt",width:320,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					opts = opts + "<a href='javascript:void(0);' ng-click='show( "+rowObject.correctId+" )' class='btn btn-primary fa fa-eye btn-sm td-compile'>详情</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='edit( "+rowObject.correctId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>编辑</a> ";
					if(rowObject.status == 10 || rowObject.status == 50 || rowObject.status == 20){
						opts = opts + "<a href='javascript:void(0);' ng-click='audit( "+rowObject._rowId+','+70+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>审核</a> ";
					}
//					opts = opts + "<a href='javascript:void(0);' ng-click='setShareFee( "+rowObject.correctId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>设置分享费</a> ";
            		return opts;
	             }}
			]
		});
	}
	
	/**审核*/
    function audit(rowId){
    	debugger;
    	var rowData = $("#correctList").jqGrid("getRowData",rowId);
    	console.log(rowData)
    	var type = rowData.type;
    	var status = rowData.status;
    	var correctId = rowData.correctId;
    	var goodsSkuId = rowData.goodsSkuId;
    	var supermarketId = rowData.supermarketId;
    	var userId = rowData.userId;
    	/*if(type == 2 && status != 50){
    		toaster.info("纠错的商品无需审核！")
    		return;
    	}*/
    	var url = urls.ms + "/jsp/goods/CorrectAudit.jsp";
    	templateform.open({
			title:"分享审核",
			url:url,
			scope:$scope,
			buttons:[{
				text:"审核通过",
				iconCls:'fa fa-check',
				handle:function( $modalInstance, data ,$scope ){
					$.ajax({
						url : urls.ms + "/goods/correct/correctAudit.do",
						async : false,
						type : "post",
						data : {"userId":userId,"supermarketId":supermarketId,"correctId":correctId,"goodsSkuId":goodsSkuId,"status":70,"communityId":$scope.search.communityId},
						dataType : "JSON",
						success:function(data){
							$("#correctList").trigger("reloadGrid");
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
						url : urls.ms + "/goods/correct/correctAudit.do",
						async : false,
						type : "post",
						data : {"userId":userId,"supermarketId":supermarketId,"correctId":correctId,"status":60,"goodsSkuId":goodsSkuId,"communityId":$scope.search.communityId},
						dataType : "JSON",
						success:function(data){
							$("#correctList").trigger("reloadGrid");
							$modalInstance.close();
							toaster.success( "","操作成功",3000 );
						}
					})
				}
			}],
			onOpen:function( $modalInstance, data ,$scope){
				//获取数据
				$scope.correct = {
						goodsName:rowData.goodsName,
						nickname:rowData.nickname,
						price:rowData.price + "元",
						priceTypeName:rowData.priceTypeName,
						validStart:rowData.validStart,
						validEnd:rowData.validEnd
				}
			}
		},function( $modalInstance,data, $scope ){
		});
    	
    }
    
    /***设置分享费**/
    function setShareFee(id){
    	
    	
    	
    }
    
    
    /**编辑*/
    function edit( id ){
    	var url = urls.ms + "/jsp/goods/CorrectEdit.jsp";
		templateform.open({
			title:"编辑",
			url:url,
			scope:$scope,
			onOpen:function( $modalInstance, data ,$scope){
				$.ajax({
					url:urls.ms + "/goods/correct/getInfo.do",
					async:false,
					type:"post",
					data:{"correctId":id,"communityId":$scope.search.communityId},
					dataType:"json",
					success:function(data){
						var correct = data.data;
						$scope.correct = {
								correctId:correct.correctId,
								goodsName:correct.goodsName,
								nickname:correct.nickname,
								price:correct.price/100,
								priceType:{"code":correct.priceType,"name":correct.priceTypeName},
								validStart:new Date(correct.validStart).format("yyyy-MM-dd"),
								validEnd:new Date(correct.validEnd).format("yyyy-MM-dd"),
						}
						
					}
				})
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,$scope.correct, $scope );
		});
    }
    
	/**保存*/
    function save( $modalInstance,data, $scope ){
    	try{
			$.ajax({
				url:urls.ms + "/goods/correct/save.do",
				type:"post",
				data:{	"correctId":$scope.correct.correctId,
						"communityId":$scope.search.communityId,
						"price":$scope.correct.price * 100,
						"priceType":$scope.correct.priceType.code,
						"validStart":new Date($scope.correct.validStart).format("yyyy-MM-dd")+" 00:00:00",
						"validEnd":new Date($scope.correct.validEnd).format("yyyy-MM-dd")+" 00:00:00",
				},
				dataType:"JSON",
				success:function(data){
					if(data.success){
						$("#correctList").trigger("reloadGrid");
						$modalInstance.close();
						toaster.success( "","操作成功",3000 );
					}
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
    	var url = urls.ms + "/jsp/goods/CorrectShow.jsp";
		templateform.open({
			title:"查看详情",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url,
			scope:$scope,
			onOpen:function( $modalInstance, data ,$scope){
				$.ajax({
					url:urls.ms + "/goods/correct/getInfo.do",
					async:false,
					type:"post",
					data:{"correctId":id,"communityId":$scope.search.communityId},
					dataType:"json",
					success:function(data){
						var correct = data.data;
						var parentId = correct.parentId;
						var share = data.data.share;
						if(parentId == -1){
							$scope.correct = {
									parentId:parentId,
									goodsName:correct.goodsName,
									supermarketName:correct.supermarketName,
									nickname:share.nickname,
									price:share.price/100 + "元",
									priceTypeName:share.priceTypeName,
									validStart:new Date(share.validStart).format("yyyy-MM-dd"),
									validEnd:new Date(share.validEnd).format("yyyy-MM-dd"),
									imagePath:share.imagePath
							}
						}else{
							var corrector = data.data.correct;
							$scope.correct = {
									parentId:parentId,
									goodsName:correct.goodsName,
									supermarketName:correct.supermarketName,
									nickname:share.nickname,
									price:share.price/100 + "元",
									priceTypeName:share.priceTypeName,
									validStart:new Date(share.validStart).format("yyyy-MM-dd"),
									validEnd:new Date(share.validEnd).format("yyyy-MM-dd"),
									imagePath:share.imagePath,
									seccondImagePath:share.seccondImagePath
							}
							$scope.correct.correct = {
									nickname:corrector.nickname,
									price:corrector.price/100 + "元",
									priceTypeName:corrector.priceTypeName,
									validStart:new Date(corrector.validStart).format("yyyy-MM-dd"),
									validEnd:new Date(corrector.validEnd).format("yyyy-MM-dd"),
									imagePath:corrector.imagePath
							}
						}
						
					}
				})
			}
		});
    }
    //初始化
    init();
});