/**
 * 侧边栏控制
 */
$(document).ready(function(){
	
	$("#aFloatTools_Show").click(function(){
		$('#divFloatToolsView').animate({width: 'show', opacity: 'show'}, 'normal',function(){
			$('#divFloatToolsView').show();
		});
		$('#aFloatTools_Show').attr('style','display:none');
		$('#aFloatTools_Hide').attr('style','display:block');
	});
	
	$("#aFloatTools_Hide").click(function(){
		$('#divFloatToolsView').animate({width: 'hide', opacity: 'hide'}, 'normal',function(){
			$('#divFloatToolsView').hide();
		});
		$('#aFloatTools_Show').attr('style','display:block');
		$('#aFloatTools_Hide').attr('style','display:none');
	});
	
});

/**
 * 侧边栏加载数据
 */
function initisidebar2(data){
    /*$.getJSON("data/temp.geojson",function(data){*/
	   	$("#pageindex").val(1);
	   	var listnum=data.features.length;
	   	/* $("#sumnotes").val("共"+listnum+"条"); */
	   	var pageidx=Number($("#pageindex").val());
	   	var perpagenum=Number($("#perpagenum").val());
	   	document.getElementById("sumnotes").innerHTML="共"+listnum+"条";
	   	document.getElementById("sumpages").innerHTML="共"+Math.ceil(listnum/perpagenum)+"页";
	   	var $jsontip = $("#jsonTip");
       	$jsontip.empty();
       	var startidx=perpagenum*(pageidx-1);
       	var endidx=perpagenum*(pageidx);
       	if(endidx>listnum) endidx=listnum; 
       	var strHtml =listhtml(data,startidx,endidx);
       	$jsontip.html(strHtml);
   	/*});*/
}