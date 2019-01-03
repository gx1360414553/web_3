'use strict';
app.filter('skuAttributeSorter', function( ) {
	
	return function( skuAttributeList,salesAttributeList ) {
		// 根据商品属性排序
		angular.forEach( salesAttributeList, function( attribute,attributeIndex ){
			var attributeId = attribute.attributeId;
			var attrValueList = attribute.attrValueList;
			
			var currentSkuAttribute = skuAttributeList[attributeIndex];
			// 交换位置
			angular.forEach(skuAttributeList, function( skuAttribute, skuAttributeIndex){
				var skuAttributeId = skuAttribute.attributeId;
				if( attributeId == skuAttributeId ){
					var temp = skuAttributeList[skuAttributeIndex];
					skuAttributeList[attributeIndex] = temp;
					skuAttributeList[skuAttributeIndex] = currentSkuAttribute;
					// 获取属性值名称
					angular.forEach( attrValueList,function( valueItem,valueIndex ){
						if( temp.attrValueId == valueItem.attrValueId ){
							temp.valueName = valueItem.name;
						}
					} );
					return;
				}
			});
			
		});
		
		
		return skuAttributeList;
	};
});
app.controller('goodsCtrl', function($q,$http,$scope,toaster,goodsService,messager,templateform,$templateCache ) {
	
	function init(){
		$scope.show = show;
		$scope.edit = edit;
		$scope.remove = remove;
		$scope.list = list;
		$scope.addUI = addUI;
		$scope.checkedCategory = checkedCategory;
		$scope.brandList = new Array( {name:"全部",code:""} );
		$scope.selectedBarand = $scope.brandList[0];
		$scope.removeList = removeList;
		$scope.changeStatusList = changeStatusList;
		$scope.search={
				goodsName:"全部",
				brandId:"",
				categoryId:"",
				status:"",
		}
		$scope.changeStatus=changeStatus;
		$scope.importUI = importUI;
	}
	
	function importUI(){
		templateform.open({
			title:"商品导入",
			url:"jsp/goods/GoodsImportUI.jsp",
			buttons:[],
			onOpen:function( $modalInstance,data, $scope ){
				$scope.importResult = {
						data:[]
				}
				
				$scope.downloadErrorData = function(){
					if( $scope.importResult.file ){
						$.download("goods/goods/downloadErrorData.do",{
							"fileName":$scope.importResult.file
						});
					}
					
				}
				
				$scope.confirmImport=function(){
					var defer = $q.defer();
						$("<input name='file' type='file' style='visibility: hidden;'/>").click().change( function( value ){
						// 提交表单
						var $body = $("body:eq(0)");
						var $form = $("<form action='#' enctype='multipart/form-data' method='post'>");
						$body.append( $form );
						$form.append( $( this ) ).form("submit",{
							onSubmit:function(param){
								$scope.promise = defer.promise;
							},
							success:function (data){
								data = angular.fromJson( data );
								$scope.$apply(function(){
									if( data.success ){
										toaster.info( "",data.msg,3000 );
										list();
									}
									debugger
									$scope.importResult = data.data;
									defer.resolve( data );
									if( data.success && $scope.importResult.data.length == 0 ){
										$modalInstance.close();
									}
								})
							},
							url:"goods/goods/importGoods.do"
						});
					});
				}
			}
		},function( $modalInstance,data, $scope ){
			$modalInstance.close()
		})
		
	}
	
	/***************************************************************************
	 * 上架下架
	 */
	function changeStatus(goodsId,status ){
		var message = status == 2 ? "确认下架？":"确认上架？";
		messager.confirm( message ,function( $modalInstance ){
			$.ajax({
				url:"goods/goods/changeStatus.do",
				data:{
					goodsId:goodsId,
					status:status
				},
				dataType:"json",
				type:"post",
				success:function( data ){
					if( data.success ){
						$("#goodsList").trigger("reloadGrid");
					}else{
						toaster.error( "",data.msg,3000 );
					}
					$modalInstance.close();
				}
			})
    	});
	}
	
	function changeStatusList(status){
		// 确认操作
		var selarrrow=$('#goodsList').jqGrid('getGridParam','selarrrow');
		if(selarrrow.length==0){
			toaster.error("",'请选择行',3000 ); 
			return;
		}
		var message = status == 2 ? "确认下架？":"确认上架？";
		messager.confirm( message ,function( $modalInstance ){
			for (var i = 0; i < selarrrow.length; i++) {
				var obj = $('#goodsList').jqGrid('getRowData',selarrrow[i]);
				var goodsId = obj.goodsId;
				  $.ajax({ url:"goods/goods/changeStatus.do", 
					  data:{
						  goodsId:goodsId, status:status }, dataType:"json", type:"post",
						  success:function( data ){ 
							  if( data.success ){
							     $("#goodsList").trigger("reloadGrid");
							  }
						  	  else{ 
						  		toaster.error("",data.msg,3000 ); 
						  		  } 
							  $modalInstance.close(); 
						  		  } 
						  })
				 
			}
			
    	});
	}
	
	
	/** 列表查询 */
	function list(){
		if($scope.search.goodsName=="全部"){
			$scope.search.goodsName="";
		}
		var $grid = $("#goodsList");
		if( $grid[0].grid ){
			$grid.jqGrid('setGridParam', {
				page : 1,
				postData:$scope.search,
			}).trigger("reloadGrid");
			return;
		}
		$grid.jqGrid({
			url : urls.ms+"/goods/goods/list.do",
			postData:$scope.search,
			pager : "#goodsPager",
			multiselect:true,
			colModel : [
				{name:'goodsName',label:'商品名称',width:120,sortable:false}, 
				{name:'attributeString',label:'销售属性',width:140,sortable:false}, 
				{name:'goodsId',label:'id',width:200,sortable:false,hidden:true}, 
				{name:'categoryName',label:'类别',width:60,sortable:false}, 
				{name:'brandName',label:'品牌',width:60,sortable:false}, 
				{name:'statusName',label:'上架状态',width:60,sortable:false}, 
				{name:'createTime',label:'创建时间',width:80,sortable:false,formatter:function( cellvalue, options, rowObject ){
					return cellvalue ? new Date( cellvalue ).format("yyyy-MM-dd hh:mm:ss") : "";
				}},
				{name:'adminName',label:'操作员',width:50,sortable:false}, 
	             {label:"操作",name:"opt",width:180,sortable:false,formatter:function(cellvalue, options, rowObject){
					var opts = "";
					if( rowObject.status == 1 || rowObject.status==2 ){
						opts = opts + "<a href='javascript:void(0);' ng-click='changeStatus( "+rowObject.goodsId+","+3+")' class='btn btn-primary fa fa-eye btn-sm td-compile'>上架</a> ";
					}else{
						opts = opts + "<a href='javascript:void(0);' ng-click='changeStatus( "+rowObject.goodsId+","+2+" )' class='btn btn-primary fa fa-eye btn-sm td-compile'>下架</a> ";
					}
					opts = opts + "<a href='javascript:void(0);' ng-click='edit( "+rowObject.goodsId+" )' class='btn btn-primary fa fa-edit btn-sm td-compile'>编辑</a> ";
					opts = opts + "<a href='javascript:void(0);' ng-click='remove( "+rowObject.goodsId+" )' class='btn btn-primary fa fa-remove btn-sm td-compile'>删除</a> ";
            		return opts;
	             }}
			]
		});
			
	}
	
    /** 列表查询页面选择类别 */
	   function checkedCategory( $item ){
		   try{
			   $scope.search.categoryId=$item.code;
			   $scope.brandList.splice(1,$scope.brandList.length-1);
			   
			   angular.element("#brandId").scope().$select.select($scope.brandList[0]);
			   if( !$item.code ){
				   return;
			   }
			   // 获取品牌信息
			   goodsService.listBrand({
				   data:{categoryId:$scope.search.categoryId},
				   success:function( data ){
					   if( data instanceof Array ){
						   $scope.brandList = $scope.brandList.concat( data );
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
	   
	   /** 列表查询页面选择品牌 */
	   function checkedbrand( $item ){
		   try{
			   $scope.search.brandId=$item.code;
			   $scope.brandList.splice(1,$scope.brandList.length-1);
			   
			   angular.element("#brandId").scope().$select.select($scope.brandList[0]);
			   if( !$item.code ){
				   return;
			   }
			   // 获取品牌信息
			   goodsService.listBrand({
				   data:{categoryId:$scope.search.categoryId},
				   success:function( data ){
					   if( data instanceof Array ){
						   $scope.brandList = $scope.brandList.concat( data );
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
	   
	   
	   
	   /** 删除 */
	    function remove( id ){
	    	messager.confirm("确认删除？",function( $modalInstance ){
	    		goodsService.remove({
	    			"data":{goodsId:id},
	    			"success":function( data ){
	            		if( data.success ){
	            			$("#goodsList").trigger("reloadGrid");
	            			$scope.$apply(function(){
	            				toaster.success( "", data.msg ,3000 );
							});
	            			$modalInstance.close();
	            		}else{
	            			$scope.$apply(function(){
	            				toaster.error( "", data.msg ,3000 );
							});
	            		}
	            	}
	    		});
	    	});
	    }
	   
	
    /** 删除 */
    function removeList( ){
    	var selarrrow=$("#goodsList").jqGrid('getGridParam','selarrrow');
    	if(selarrrow.length==0){
			toaster.error("",'请选择行',3000 ); 
			return;
		}
    	messager.confirm("确认删除？",function( $modalInstance ){
    		for(var i = 0;i < selarrrow.length; i++){
    			var obj=$("#goodsList").jqGrid('getRowData',selarrrow[i]);
    			var goodsId = obj.goodsId;
    		goodsService.remove({
    			"data":{goodsId:goodsId},
    			"success":function( data ){
            		if( data.success ){
            			$("#goodsList").trigger("reloadGrid");
            			$scope.$apply(function(){
            				toaster.success( "", data.msg ,3000 );
						});
            			$modalInstance.close();
            		}else{
            			$scope.$apply(function(){
            				toaster.error( "", data.msg ,3000 );
						});
            		}
            	}
    		});
    		}
    	});
    }
    
    /**
	 * 编辑
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param productionId
	 *            产品ID
	 */
    function edit( goodsId,productionId ){
    	// 获取商品信息
    	$.ajax({
    		url:"goods/goods/edit.do",
    		data:{
    			goodsId: goodsId,
    			productionId:productionId
    		},
    		dataType:"json",
    		type:"post"
    	}).then( function( data ){
    		openEdit( data.data );
    	} )
    }
    
    /***************************************************************************
	 * 新增编辑页面
	 */
    function addUI(){
    	templateform.open({
			title:"产品搜索",
			url:"jsp/goods/GoodsCategorySearch.jsp",
			onOpen:function( $modalInstance, data ,$scope){
				$.ajax({
					url:"system/options/listOptions",
					data:{sqlId:"listCategory",level:3},
					dataType:"json",
					type:"post"
				}).then( function( data ){
					init( data );
				} );
				
				var search = $scope.search={
					categoryName:"",
					category:{},
					brandName:"",
					brand:{},
					productionName:"",
					production:{},
					
				}
				
				
				function init( data ){
					$scope.categoryList = data;
				}
				
				// 类别查询品牌
				$scope.listBrandList = function( category ){
					if( search.category == category ){
						return;
					}
					search.category = category;
					$scope.brandList = [];
					$scope.search.brand = {};
					$scope.productionList = [];
					$scope.search.production = {};
					$.ajax({
						url:"system/options/listOptions",
						data:{sqlId:"listBrand",categoryId:category.code},
						dataType:"json",
						type:"post"
					}).then( function( data ){
						$scope.$apply( function(){
							$scope.brandList = data;
						} );
					} );
					$.ajax({
						url:"system/options/listOptions",
						data:{sqlId:"listProduction",categoryId:category.code},
						dataType:"json",
						type:"post"
					}).then( function( data ){
						$scope.$apply( function(){
							$scope.productionList = data;
						} );
					} );
				}
				
				// 品牌查询产品
				$scope.listProduction = function( brand ){
					if( search.brand == brand ){
						return;
					}
					search.brand = brand;
					$scope.productionList = [];
					$scope.production = {};
					$.ajax({
						url:"system/options/listOptions",
						data:{
							sqlId:"listProduction",
							categoryId:search.category.code,
							brandId:search.brand.code,
						},
						dataType:"json",
						type:"post"
					}).then( function( data ){
						$scope.$apply( function(){
							$scope.productionList = data;
						} );
					} );
				}
				
				/** 选择产品 */
				$scope.selectedProduction = function( production ){
					search.production = production;
				}
				
				$scope.confirmPorudction = function(){
					
				}
			}
		},function( $modalInstance,data, $scope ){
			var production = $scope.search.production;
			if( ! production.code ){
				return toaster.error( "","请选择产品",3000 );
			}
			// 获取产品信息
			edit( null,production.code );
		});
    }
    /** 新增，编辑 */
    function openEdit( data ){
    	var url = urls.ms + "/jsp/goods/GoodsEdit.jsp";
		templateform.open({
			title:"商品信息",
			url:url,
			scope:$scope,
			data:data,
			dataName:"data",
			onOpen:function( $modalInstance, data ,$scope){
				var  goods = $scope.goods = data.goods;
				debugger
				var sortingCategoryCtrl = $scope.sortingCategoryCtrl = {
						sortingCategoryList : data.sortingCategoryList,
						sortingCategory : data.sortingCategoryList.filter( item => item.sortingCategoryId == goods.sortingCategoryId )[0]
				}
				$scope.toPositive=function( input ){
					try{
						if( input < 0 ){
							return -parseFloat(new Number( input ).toFixed( 2 ));
						}else{
							return parseFloat(new Number( input ).toFixed( 2 ));
						}
					}catch (e) {
						return 0;
					}
				}
				// 销售属性
				var salesAttributeList = goods.salesAttributeList;
				// 销售商品
				var goodsSkuList = goods.goodsSkuList;
				for( var index in goodsSkuList ){
					var goodsSku = goodsSkuList[index];
					goodsSku.buyFee = goodsSku.buyFee ? goodsSku.buyFee / 100 : 0;
					goodsSku.deliverFee = goodsSku.deliverFee ? goodsSku.deliverFee / 100 : 0;
					goodsSku.serviceFee = goodsSku.serviceFee ? goodsSku.serviceFee / 100 : 0;
				} 
				
				// 获取已有的规格属性
				var salesAttributeList = goods.salesAttributeList;
				if( !salesAttributeList ){
					salesAttributeList = [];
				}
				
				// 销售属性值选中事件
				$scope.toggleChecked=function( value ){
					
					value.removed = value.removed == 2 ? 1 : 2;
					var salesAttributeCount = salesAttributeList.length;
					var newGoodsSkuList = [];
					// 遍历第一个销售属性的属性值
					var newAttrValueList = [];
					var attrValueList = salesAttributeList[ 0 ].attrValueList;
					
					for( var i = 0; i < attrValueList.length; i++ ){
						if( attrValueList[i].removed != 2 ){
							continue;
						}
						newAttrValueList.push( attrValueList[i] );
						// 存在多个销售属性执行
						if( salesAttributeCount > 1 ){
							group( newAttrValueList, 1 );
						// 如果只存在一个销售属性则执行
						}else{
							newGoodsSkuList[newGoodsSkuList.length] = {
									skuAttributeList:angular.copy( newAttrValueList ),
									removed:2,
									buyFee:0,
									deliverFee:0,
									serviceFee:0
							}
							newAttrValueList.pop();
						}
					}
					
					// 从第二个属性的属性值开始组合
					function group( newAttrValueList,salesAttributeIndex ){
						
						var attrValueList = salesAttributeList[ salesAttributeIndex ].attrValueList;
						for( var i = 0; i < attrValueList.length; i++ ){
							if( attrValueList[i].removed != 2 ){
								continue;
							}
							newAttrValueList.push( attrValueList[i] );
							// 如果构成一个组合则执行if
							if( newAttrValueList.length == salesAttributeCount ){
								newGoodsSkuList[newGoodsSkuList.length] = {
										skuAttributeList:angular.copy( newAttrValueList ),
										removed:2,
										buyFee:0,
										deliverFee:0,
										serviceFee:0
								}
								newAttrValueList.pop();
							}else{
								group( newAttrValueList,salesAttributeIndex+1 );
							}
						}
						newAttrValueList.pop();
						return;
					}
					// 如果已存在的则将removed设置为1否则加入goodsSkuList
					var goodsSkuList = goods.goodsSkuList;
					lable:
					for( var i = 0; i < goodsSkuList.length; i++ ){
						// 获取所有的属性值ID并从小到大排序
						var attrValueIdList = goodsSkuList[i].skuAttributeList.map( item => item.attrValueId ).sort().join( "," );
						for( var j = 0 ; j < newGoodsSkuList.length; j++ ){
							var newValueIdList = newGoodsSkuList[j].skuAttributeList.map( item => item.attrValueId ).sort().join( "," );
							if( attrValueIdList == newValueIdList ){
								goodsSkuList[i].removed = 2;
								newGoodsSkuList.splice( j, 1 );
								continue lable;
							}
						}
						goodsSkuList[i].removed = 1;
					}
					
					goods.goodsSkuList = goodsSkuList.concat( newGoodsSkuList );
				}
				
				// 选择推荐商品
				$scope.recommandModal = function(){
					recommandModal(  $modalInstance, goods ,$scope );
				};
				
			}
		},function( $modalInstance,data, $scope ){
			save( $modalInstance,data, $scope );
		});
    }
    
    
    /**
	 * 
	 * 推荐商品列表查询
	 */
    function recommandModal( $modalInstance,data, $scope ){
    	templateform.open({
			title:"商品信息",
			data:data,
			url:"jsp/goods/GoodsRecommandList.jsp",
    	},function( $modalInstance,data, $scope ){
    		var rowData =  $("#goodsRecommandList").jqGrid('getRowData');
    		var rowIndexArr =  $("#goodsRecommandList").jqGrid('getGridParam', 'selarrrow');
    		var selectedRows = [];
    		for( var i = 0 ; i < rowIndexArr.length; i++ ){
    			selectedRows.push( rowData[rowIndexArr[i]] );
    		}
    		data.recommendGoodsList = selectedRows;
    		console.info( selectedRows );
    		$modalInstance.close();
		})
    }
    
	/** 保存 */
    function save( $modalInstance,data, $scope ){
    	var goods = data.goods;
    	var sortingCategory = $scope.sortingCategoryCtrl.sortingCategory;
    	try{
    		debugger
    		var goodsSkuLis = goods.goodsSkuList.filter( item => {
    			return item.removed == 2;
    		} );
    		// 判断配送费和采买费是否已填
    		if( !goods.goodsName ){
    			toaster.error( "", "请填写商品标题", 3000 );
    			return;
    		}
    		if( !goods.subtitle ){
    			toaster.error( "", "请填写商品副标题", 3000 );
    			return;
    		}
    		if( !sortingCategory || !sortingCategory.sortingCategoryId ){
    			toaster.error( "", "请选择分拣类别", 3000 );
    			return;
    		}else{
    			goods.sortingCategoryId = sortingCategory.sortingCategoryId;
    		}
    		
    		if( goodsSkuLis.length == 0 ){
    			toaster.error( "", "请添加销售商品,且属性不得为空", 3000 );
    			return;
    		}
    		
// for( var i = 0 ; i < goodsSkuLis.length; i++ ){
// if( !goodsSkuLis[i].buyFee ){
// toaster.error( "", "请填写完所有的商品采买费", 3000 );
// return;
// }
// if( !goodsSkuLis[i].deliverFee ){
// toaster.error( "", "请填写完所有的配送费", 3000 );
// return;
// }
// if( !goodsSkuLis[i].serviceFee ){
// toaster.error( "", "请填写完所有的销售服务费", 3000 );
// return;
// }
// }
    		
    		if( !goods.content ){
    			toaster.error( "", "请填写商品图文详情", 3000 );
    			return;
    		}
    		// 转换配送费和采买费为分
    		var goods = angular.copy( goods );
    		debugger
    		// 设置配送费和采买费
    		var goodsSkuList = goods.goodsSkuList;
    		angular.forEach( goodsSkuList, function( goodsSku, index ){
    			goodsSku.buyFee = goodsSku.buyFee * 100;
    			goodsSku.deliverFee = goodsSku.deliverFee * 100;
    			goodsSku.serviceFee = goodsSku.serviceFee * 100;
    		} )
    		// 处理图片
    		angular.forEach( goods.goodsImageList, function( item, index ){
    			if( item.imagePath == "resource/image/addImg.png" ){
    				item.imagePath = "";
    			}
    		})
    		
    		$('#goods-edit-form').form("submit",{   
    		    url:"goods/goods/save.do",
    		    onSubmit: function( param ){   
    		    	param.goods = angular.toJson( goods );
    		    },   
    		    success:function(data){   
    		    	data= data.substring(0,data.indexOf('}')+1);
    		    	console.log(data);
    		    	data = angular.fromJson( data );
    		    	if( data.success ){
						$("#goodsList").trigger("reloadGrid");
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
    
    /** 查看 */
    function show( id ){
    	var url = urls.ms + "/goods/goods/show.do?";
		if( id ){
			url = url + $.param( {goodsId:id} );
		}
		templateform.open({
			title:"Goods",
			buttons:[],
			backdrop: true,
		    keyboard: true,
			url:url
		});
    }
    
    // 初始化
    init();
});