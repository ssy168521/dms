/**
 * 
 */
function loadXML(fileRoute)
{
    //var fileRoute="books.xml";
    if (window.ActiveXObject)
    {
    	xmlDoc = new ActiveXObject('Msxml2.DOMDocument');
        xmlDoc.async=false;
        xmlDoc.load(fileRoute);
        browse="ie";
        return xmlDoc;
    }
    else if (document.implementation && document.implementation.createDocument)
    {
    	xmlDoc=document.implementation.createDocument('', '', null);
        xmlDoc.load(fileRoute);
        browse="ff";
        return xmlDoc;
    }
    else
    {
    alert( 'δ�����������ļ��ݣ�');
    return;
    }
}

function getmessage()
{
    var msg='<table border="1" cellspacing="0" cellpadding="0" width="500">';
    msg+='<tr><td width="90"></td><td width="100">ͼ����</td><td width="100">������</td><td width="110">ͼ������</td><td width="100">����</td></tr>';
    if(browse=="ff")
    {
        var cNodes = xmlDoc.getElementsByTagName("book");
        for(var j=0;j<cNodes.length;j++)
        {
            var bookID=xmlDoc.getElementsByTagName("book")[j].getAttribute("id");
            var sortID=xmlDoc.getElementsByTagName("book")[j].getAttribute("sortID");
            var bookTitle=xmlDoc.getElementsByTagName("title")[j].childNodes[0].nodeValue;
            var bookAuthor=xmlDoc.getElementsByTagName("author")[j].childNodes[0].nodeValue;
            msg+='<tr><td>'+j+'</td><td>'+bookID+'</td><td width="100">'+sortID+'</td><td width="190">'+bookTitle+'</td><td width="120">'+bookAuthor+'</td></tr>';
        }
    }
    else if(browse=="ie")
    {
        var state = xmlDoc.readyState;
        if (state == 4)
        {
            var oNodes = xmlDoc.selectNodes("//books/book");
            for(j=0;j<oNodes.length;j++)
            {
                var bookID=oNodes[j].getAttribute("id");
                var sortID=oNodes[j].getAttribute("sortID");
                var bookTitle=oNodes[j].childNodes[0].text;
                var bookAuthor=oNodes[j].childNodes[1].text;
                msg+='<tr><td>'+j+'</td><td>'+bookID+'</td><td width="100">'+sortID+'</td><td width="190">'+bookTitle+'</td><td width="120">'+bookAuthor+'</td></tr>';
            }
        }
    }
    msg+='</table>';
    document.getElementById("bookList").innerHTML=msg;
}


