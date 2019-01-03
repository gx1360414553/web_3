'use strict';
app.service('communityEarningsItemService', [function() {
	
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
		
	    if( data.communityEarningsId == ""){
			throw new BusinessException("communityEarningsId不能为空");
		}
	    if( data.amount == ""){
			throw new BusinessException("amount不能为空");
		}
	    if( data.type == ""){
			throw new BusinessException("type不能为空");
		}
	    if( data.objectId == ""){
			throw new BusinessException("objectId不能为空");
		}
	    if( data.communityId == ""){
			throw new BusinessException("communityId不能为空");
		}
		
		options.data = {
			communityEarningsItemId:data.communityEarningsItemId,
			communityEarningsId:data.communityEarningsId,
			amount:data.amount,
			type:data.type,
			objectId:data.objectId,
			communityId:data.communityId,
		}
		options.url = urls.ms+"/community/communityEarningsItem/save.do";
		ajax( options );
	}
	
	function getById( options ){
		options.url = urls.ms+"/community/communityEarningsItem/get.do";
		ajax( options );
	}
	
	function remove( options ){
		options.url = urls.ms+"/community/communityEarningsItem/delete.do";
		ajax( options );
	}
	
	return {
		remove:remove,
		getById:getById,
		save:save
	};
}]);