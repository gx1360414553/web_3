'use strict';

angular.module('app', ["toaster","ngTouch"]).controller("AppCtrl",function( $rootScope,$scope,$window,toaster){
	
	$scope.user={
	}
	$scope.login=function(){
		$.ajax({ 
			url: urls.ms+'/backstageUserAction/login.do',
			data:$scope.user,
			dataType : "json",
			type: "post", 
            error:function(){
            	toaster.error( "","服务器出错！",3000 );
            },
			success: function(result){
				if (result.success){
					window.location.href=urls.testMs+"/backstageUserAction/home.html";
				}else{
					$scope.$apply(function(){
						toaster.error( "",result.msg,3000 );
					})
				}
			}
		});
	}
	var startSref = 1000;
	function setSref( menuTree ){
		for( var  i = 0 ; i < menuTree.length; i++ ){
			menuTree[i].sref = "app."+ ( ++startSref );
			if( menuTree[i].children ){
				setSref( menuTree[i].children );
			}
		}
	}
	
	document.onkeypress = function( event ) {
		var e = event ? event : (window.event ? window.event : null);
		if (e.keyCode == 13) {
			$scope.login();
		}
	}
});