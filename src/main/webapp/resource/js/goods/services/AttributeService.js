'use strict';
app.service('attributeService', [function() {
	
	function ajax( options ){
		$.ajax({
			url:options.url,
			data:options.data,
			dataType: options.dataType ? options.dataType : "json",
			type: options.type ? options.type : "post",
			success: options.success,
	    	contentType : options.contentType ? options.contentType : "application/x-www-form-urlencoded",
			error: options.error ? options.error : function(){}
		}).then(function( data ){
			if( options.then ){
				options.then.call( this,data );
			}
		});
	}
	
	function save( options ){
		var data = options.data;
		
	    if( data.name == ""){
			throw new BusinessException("属性名不能为空");
		}
	    debugger
	    //判断是否填写规格值
		if( !data.attrValueList || !data.attrValueList.length ){
			throw new BusinessException("请添加规格值！");
		}else{
			angular.forEach( data.attrValueList,function( item,index ){
				if( !item.name ){
					throw "规格值不能为空！";
				}
			} );
		}
		options.data =  angular.toJson({
			attributeId:data.attributeId,
			name:data.name,
			attrValueList:data.attrValueList,
		});
		options.url = urls.ms+"/goods/attribute/save.do";
		options.contentType="application/json";
		ajax( options );
	}
	
	function getById( options ){
		options.url = urls.ms+"/goods/attribute/get.do";
		ajax( options );
	}
	
	function remove( options ){
		options.url = urls.ms+"/goods/attribute/delete.do";
		ajax( options );
	}
	
	return {
		remove:remove,
		getById:getById,
		save:save
	};
}]);