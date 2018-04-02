/**
 * 当前用户信息
 */

function showusername(obj){		   
	if(obj!="null" && obj!="" )
	{
		$("#login-id").css({display:"none"});
		$("#register-id").css({display:"none"});
		$("#user-id").css({display:"block"});
		$("#logout-id").css({display:"block"});
	}
	else
	{
		$("#login-id").css({display:"block"});
		$("#register-id").css({display:"block"});
		$("#user-id").css({display:"none"});
		$("#logout-id").css({display:"none"});
	}
}

function logout(){	
	$.ajax({
			url:"./servlet/Login",
			type:"POST",
			data:eval("("+"{opertype:\"logout\"}"+")"),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
			    
			    alert(data);
			    location.reload() ;
			}
		});
	
}