<%@ page contentType="text/html;charset=GBK"%>
<script type="text/javascript">

var fileGlide=0;
  //添加附件
  function AddRow(tableid){
	var tbody=tableid.getElementsByTagName("tbody")[0];
	var tr=document.createElement("tr");
	var td=document.createElement("td");
	var inp=createNode("1","checkbox","nodes",fileGlide,"","");
	td.appendChild(inp); 
	td.style.textAlign="center";
	tr.appendChild(td);
	//use del
	
	td=document.createElement("td");
	inp=createNode("2","file","file_"+fileGlide,"","","");
//	inp=createNode("2","file","file_","","","");
	inp.size="60";
	td.appendChild(inp);
	tr.appendChild(td);
	
	//hidden
	inp=createNode("2","hidden","ids",fileGlide,"","","");
	tr.appendChild(inp);
	tbody.appendChild(tr);
	fileGlide++;
  }
  function createNode(flag, type ,name,value ,clas,checked){
	if(flag=="0"){//text node
		return document.createTextNode(value);
	}else if(flag=="1"){//input node
		return document.createElement("<input type=\'"+type+"\' name=\'"+name+"\' value=\'"+value+"\' class=\'"+clas+"\' "+checked+">");
	}else if(flag=="2"){//element node
		return document.createElement("<input type=\'"+type+"\' name=\'"+name+"\' value=\'"+value+"\' "+checked+" class='default_input' >");
	}
  }
  //复选框全选
  function selAllNode(obj,name){
	var idss=document.getElementsByName(name);
		for(var i=idss.length-1;i>=0;i--){
			idss[i].checked=obj.checked;
		}
	}
	
	//删除附件
	function delSelNode2(ptable){
		var pbody=ptable.getElementsByTagName("TBODY")[0];
		var len=pbody.children.length;
		var nodevalue="";
		for(var i=len-1;i>=0;i--){
			if(pbody.children[i].firstChild.firstChild.checked==true){//ids
				pbody.deleteRow(i);
			}
		}
   }
   
	//检查上传信息是否填写完整
	function checkUploadFileField(){
		var returnMsg ="";
			var nodesArr = document.getElementsByName("nodes");
			var alertMsgTitle = new Array("标题","附件");
			if(nodesArr){
				var len = nodesArr.length;
				if(len >0){
					for(var i =0 ; i < len ; i++){
						var nodes_=nodesArr[i].value;
						var file_Arr = document.getElementsByName("file_"+nodes_);
						file_ = file_Arr[0].value;
						if(file_=='' || file_==null){
							returnMsg += alertMsg(i+1,alertMsgTitle[1]);
						}
					}
				}
			}
		if(returnMsg!=""){
			returnMsg="附件信息："+returnMsg;
		}
		return returnMsg;
	}

	function getObjArrByName(fieldName){
		return document.getElementsByName(fieldName);
	}
	function alertMsg(rowno,alertMsgTitle){
		return "\n第 "+rowno+" 行 "+alertMsgTitle+" 为空!";
	}
 //--------------------------添加附件 结束---------------------

	var XMLHttpReq = false;
	  //创建XMLHttpRequest对象       
    function createXMLHttpRequest() {
	  if(window.XMLHttpRequest) { //Mozilla 浏览器
	   XMLHttpReq = new XMLHttpRequest();
	  }else if (window.ActiveXObject) { // IE浏览器
	   XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	  }
	 }
	 
	 //发送请求函数
	 function sendRequestDelFile(url) {
	  createXMLHttpRequest();
	  XMLHttpReq.open("post", url, true);
	  XMLHttpReq.onreadystatechange = processResponseDelFile;//指定响应函数
	  XMLHttpReq.send(null);  // 发送请求
	 }
	 // 处理返回信息函数
	 
	    function processResponseDelFile() {
	    	document.getElementById("filemessagestring").innerHTML="";
	     	if (XMLHttpReq.readyState == 4) { // 判断对象状态
	         	if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息        	
	         	
	            	var res=XMLHttpReq.responseXML.getElementsByTagName("res")[0].firstChild.data;
	            	
	            	//document.getElementById("messagestring").innerHTML=res;
	            	if(res=="Y"){
	            		document.getElementById("filemessagestring").innerHTML="<b><font color='red'>删除成功！</font></b>";
	            		tbs.parentElement.parentElement.style.display='none';
	            	}else{
	            		document.getElementById("filemessagestring").innerHTML="<b><font color='red'>删除失败！</font></b>";
	            	}
	            	
	            	//alert(document.getElementById("messagestring").innerHTML+";123");
	         	} else { //页面不正常
	               window.alert("您所请求的页面有异常。");
	         	}
	       }
	    }

	    //删除
	    var tbs;
	    function deleteFile(value,name,tb){
	   
	 	    tbs=tb;
	 	    if(confirm("确定删除吗？"+name)){
	 			tbs.parentElement.parentElement.style.display='';
	 			var uri = '<html:rewrite page="/maintContractQuoteAction.do"/>?method=toDelFileRecord';
	 			uri +='&filesid='+ value;
	 			sendRequestDelFile(uri);  	
	 			
	 		}
	 	}
	    
		//下载
		function downloadFile(id){
			var uri = '<html:rewrite page="/maintContractQuoteAction.do"/>?method=toDownloadFileRecord';
 			uri +='&filesid='+ id;
			window.location = uri;
			//window.open(url);
		}

  </script>
 
 