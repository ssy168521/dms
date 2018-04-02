/**
 * 创建目录树 
 */
		var browse = "";
	
	function loadXML(xmlFile) {
		var xmlDoc = null;
		//判断浏览器的类型
		//支持IE浏览器
		if (!window.DOMParser && window.ActiveXObject) {
			var xmlDomVersions = [ 'MSXML.2.DOMDocument.6.0',
					'MSXML.2.DOMDocument.3.0', 'Microsoft.XMLDOM' ];
			for (var i = 0; i < xmlDomVersions.length; i++) {
				try {
					xmlDoc = new ActiveXObject(xmlDomVersions[i]);
					break;
				} catch (e) {
					document.write(e.message);
				}
			}
			xmlDoc.async = false;
			xmlDoc.load(xmlFile);
			browse = "ie";
			return xmlDoc;
		}
		//支持Mozilla浏览器
		else if (document.implementation
				&& document.implementation.createDocument) {
			try {
				/* document.implementation.createDocument('','',null); 方法的三个参数说明
				 * 第一个参数是包含文档所使用的命名空间URI的字符串； 
				 * 第二个参数是包含文档根元素名称的字符串； 
				 * 第三个参数是要创建的文档类型（也称为doctype）
				 */
				xmlDoc = document.implementation.createDocument('', '', null);
				xmlDoc.async = false;
				xmlDoc.load(xmlFile);
				browse = "ff";
				return xmlDoc;
			} catch (e) {
				try {//支持Chrome浏览器
					var xmlhttp = new window.XMLHttpRequest();
					xmlhttp.open("GRT", xmlFile, true);
					xmlhttp.send(null);
					xmlDoc = xmlhttp.responseXML.documentElement;
					//handler(xmlDoc,xmlFile);
					browse = "ff";
					return xmlDoc;
				} catch (e) {
					document.write(e.message);
				}
			}
		} else {
			document.write("未知的浏览器类型");
			return null;
		}
	};
	function GetCatalog() {

		var xmlDoc = loadXML('data/CATA.xml');
		CataTree = new dTree('CataTree');
		CataTree.add(0, -1, 'MENU');
		//CataTree.add(0, -1, '资源三号数据管理系统');
		if (browse == "ff" || browse == "ie") {
			var catalog1 = xmlDoc.getElementsByTagName("catalog1");
			var t1 = 0, t2 = 0, t3 = 0, t4 = 0;
			for (var j = 0; j < catalog1.length; j++) {
				var catalog1vale = catalog1[j].getAttribute("value");
				//document.write(catalog1vale);
				t1++;
				CataTree.add(t1 + t2 + t3 + t4, 0, catalog1vale);
				var catalog2 = catalog1[j].getElementsByTagName("catalog2");
				for (var k = 0; k < catalog2.length; k++) {
					var catalog2vale = catalog2[k].getAttribute("value");
					//document.write(catalog2vale);
					t2++;
					CataTree.add(t1 + t2 + t3 + t4, j + 1, catalog2vale);
					var catalog3 = catalog2[k].getElementsByTagName("catalog3");
					for (var l = 0; l < catalog3.length; l++) {
						var catalog3vale = catalog3[l].getAttribute("value");
						//document.write(catalog3vale);
						t3++;
						CataTree.add(t1 + t2 + t3 + t4, k + 1, catalog3vale);
						var catalog4 = catalog3[l]
								.getElementsByTagName("catalog4");
						var cata4count = catalog4.length;
						var cata4count=0;
						var catalist4=catalog3[l].childNodes;							
						//解决火狐浏览器childnode数量问题
						for(var i=0;i<catalist4.length;i++){
							if(catalist4[i].nodeName=="#text"&&!/\s/.test(catalist4[i].nodevalue)){
								continue;
							}else{
								cata4count++;
							}								
						}
						//去掉最后一级目录
						for (var m = 0; m < cata4count; m++) {
							var catalog4vale = xmlDoc.getElementsByTagName("value")[t4].childNodes[0].nodeValue;
							//document.write(catalog4vale);
							t4++;
							CataTree.add(t1 + t2 + t3 + t4, t1 + t2 + t3 + t4
									- m - 1, catalog4vale);
						}
					}
				}
			}
			document.write(CataTree);
		} else if (browse == "google") {

		}
	}