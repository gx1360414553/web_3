'use strict';
app.service('routeService', [function() {
	
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
			throw new BusinessException("name不能为空");
		}
	    if( data.createTime == ""){
			throw new BusinessException("createTime不能为空");
		}
	    if( data.lastModify == ""){
			throw new BusinessException("lastModify不能为空");
		}
		
		options.data = {
			routeId:data.routeId,
			name:data.name,
			createTime:new Date( data.createTime ).format('yyyy-MM-dd HH:mm:ss'),
			lastModify:new Date( data.lastModify ).format('yyyy-MM-dd HH:mm:ss'),
		}
		options.url = urls.ms+"/community/route/save.do";
		ajax( options );
	}
	
	function getById( options ){
		options.url = urls.ms+"/community/route/get.do";
		ajax( options );
	}
	
	function remove( options ){
		options.url = urls.ms+"/community/route/delete.do";
		ajax( options );
	}
	
	return {
		remove:remove,
		getById:getById,
		save:save
	};
}]);