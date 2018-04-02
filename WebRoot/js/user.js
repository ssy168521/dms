/**
 * user.jsp页面操作
 */

function userPageLoad(){
	$.ajax({
		url:"servlet/PageLoad",
		type:"POST",
		data:eval("("+"{requestSou:\"userPage\"}"+")"),
		async:false,
		error:function(request){
			alert("Network Error 网络异常");
		},
		success:function(data){
			var obj= eval("("+data+")");
			
		}
	});
}