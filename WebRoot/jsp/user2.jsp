<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">    
    <title></title>   
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">
    <script src="js/jquery.js"></script>
    <script src="js/pintuer.js"></script>
    <script src="js/user.js"></script>

  </head> 
<body onload="userPageLoad()">
    <form method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 用户列表</strong> <a href="" style="float:right; display:none;">添加字段</a></div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li> <a class="button border-main icon-plus-square-o" href="./jsp/adduser.html"> 添加用户</a> </li>
        <li><h4>搜索：</h4></li>
        <li style="font-size:16px;">用户名 
            <input type="text" name="s_ishome" class="input" onchange="" style="width:100px; line-height:17px; display:inline-block"/>
        </li>
        <li style="font-size:16px;">角色名称 
            <select name="s_ishome" class="input" onchange="" style="width:100px; line-height:17px; display:inline-block">
                <option value=""></option>
                <option value="">角色名1</option>
                <option value="">角色名2</option>
                <option value="">角色名3</option>
                <option value="">角色名4</option> 
            </select>
        </li>       
        <li style="font-size:16px;">有效性
            <select name="s_ishome" class="input" onchange="" style="width:100px; line-height:17px; display:inline-block">
                <option value=""></option>
                <option value="">有效</option>
                <option value="">无效</option> 
            </select>
        </li>
        <li style="font-size:16px;">注册时间 
            <input type="text" name="s_ishome" class="input" onchange="" style="width:120px; line-height:17px; display:inline-block"/> — <input type="text" name="s_ishome" class="input" onchange="" style="width:120px; line-height:17px; display:inline-block"/>
        </li>
        <li>
            <a href="javascript:void(0)" class="button border-main icon-search" onclick="changesearch()" > 搜索</a>
        </li>
     </ul>
    </div>
   <table class="table table-hover text-center">
      <tr>
        <th width="100" style="text-align:left; padding-left:20px;">用户ID</th>  
        <th>用户名</th>
        <th>角色名称</th>
        <th>有效性</th>
        <th>在线状态</th>
        <th width="10%">注册时间</th>
        <th width="310">操作</th>
      </tr>
     <!--  <volist name="list" id="vo"> -->
        <tr>
         <%
           for (int j=1;j<7;j++){
         %>
          <td style="text-align:left; padding-left:20px;">
          	<input type="checkbox" name="id[]" value="" /><%=j %>
          </td>
          
          <td>用户<%=j %></td>
          <td>角色<%=j %></td>  
          <td>有效</td>
          <td>离线</td>
          <td>2017-01-01</td>
          <td>
              <div class="button-group"> 
              <a class="button border-main" href="./jsp/userinfo.html"><span class="icon-book"></span>详情</a>
              <a class="button border-main" href="./jsp/userchange.html"><span class="icon-edit"></span> 修改</a> 
              <a class="button border-red" href="javascript:void(0)" onclick="return del(1,1,1)"><span class="icon-trash-o"></span> 删除</a> 
              </div>
          </td>
        </tr>
        <% }  %>  
      <tr>
        <td style="text-align:left; padding:19px 0;padding-left:20px;">
            <input type="checkbox" id="checkall"/>全选
        </td>
        <td colspan="7" style="text-align:left;padding-left:20px;">
            <a href="javascript:void(0)" class="button border-red icon-trash-o" style="padding:5px 15px;" onclick="DelSelect()"> 删除</a>  
        </td>
      </tr>
      <tr>
        <td colspan="8">
            <div class="pagelist"> 
                <a >上一页</a> 
                <span class="current">1</span>
                <a >下一页</a>
                <a >尾页</a> 
            </div>
        </td>
      </tr>
    </table>
  </div>
</form>
<script type="text/javascript">

//搜索
function changesearch(){	
		
}

//单个删除
function del(id,mid,iscid){
	if(confirm("您确定要删除吗?")){
		
	}
}

//全选
$("#checkall").click(function(){ 
  $("input[name='id[]']").each(function(){
	  if (this.checked) {
		  this.checked = false;
	  }
	  else {
		  this.checked = true;
	  }
  });
});

//批量删除
function DelSelect(){
	var Checkbox=false;
	 $("input[name='id[]']").each(function(){
	  if (this.checked==true) {		
		Checkbox=true;	
	  }
	});
	if (Checkbox){
		var t=confirm("您确认要删除选中的内容吗？");
		if (t==false) return false;		
		$("#listform").submit();		
	}
	else{
		alert("请选择您要删除的内容!");
		return false;
	}
}
</script>
</body>
</html>
