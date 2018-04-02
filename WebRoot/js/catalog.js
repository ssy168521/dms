/**
 * 创建目录树 
 */
function GetCatalog(CataTree){		

	CataTree.add(0, -1, '');	
	$.ajax({
		url:"data/catalog.xml",
		dataType:"xml",
		type:"GET",
		timeout:2000,
		async:false,
		error:function(xml){
			alert("加载XML文件出错！");
		},
		success:function(xml){
			$(xml).find("catalog1").each(function(i){
				var id1=$(this).attr("id");
				var name1=$(this).children("value").text();				
				CataTree.add(id1,0,name1);
				$(this).find("catalog2").each(function(i){
					var id2=$(this).attr("id");
					var pid2=$(this).parent().attr("id");
					var name2=$(this).children("value").text();
					var tbname2=$(this).children("table").text();
					CataTree.add(id2,pid2,name2);/*,"javascript:setTableName('"+tbname2+"')"*/
					$(this).find("catalog3").each(function(i){
						var id3=$(this).attr("id");
						var pid3=$(this).parent().attr("id");
						var name3=$(this).children("value").text();
						var tbname3=$(this).children("table").text();
						CataTree.add(id3,pid3,name3,"javascript:setTableName('"+tbname3+"')");
						$(this).find("catalog4").each(function(i){
							var id4=$(this).attr("id");
							var pid4=$(this).parent().attr("id");
							var name4=$(this).children("value").text();
							var tbname4=$(this).children("table").text();
							CataTree.add(id4,pid4,name4,"javascript:setTableName('"+tbname4+"')");
						});
					});						
				});	
			});
		}
	});
	document.write(CataTree);
	$("#iCataTree0").css("display", "none");	
}

function setTableName(tabname){
	if(tabname!=""){
		$("#tbname").val(tabname);
	}
}

function getNodeID(data){
	var tbname="";
	$.ajax({
		url:"data/catalog.xml",
		dataType:"xml",
		type:"GET",
		timeout:2000,
		async:false,
		error:function(xml){
			alert("加载XML文件出错！");
		},
		success:function(xml){
			$(xml).find("catalog1").each(function(i){
				var id1=$(this).attr("id");
				if(id1==data){
					tbname=$(this).children("table").text();
				}
				$(this).find("catalog2").each(function(i){
					var id2=$(this).attr("id");
					if(id2==data){
						tbname=$(this).children("table").text();
					}
					$(this).find("catalog3").each(function(i){
						var id3=$(this).attr("id");
						if(id3==data){
							tbname=$(this).children("table").text();
						}
						$(this).find("catalog4").each(function(i){
							var id4=$(this).attr("id");
							if(id4==data){
								tbname=$(this).children("table").text();
							}
						});
					});
				});
			});
		}
	});
	if(tbname!=""){
		$("#tbname").val(tbname);
	}
}