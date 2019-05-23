function getmachinetype(url){
		 var typeid = document.getElementById("typeId").value;
		 var Request = new AjaxRequest();
		// var url = '<html:rewrite page="/LiftFparmAction.do"/>?method=AjaxLiftf&typeid=';
		 url = url+typeid

		 Request.url = url;
		 Request.mehod="POST";
//alert(url);
		 Request.onComplete = function(req){
		     var req_Obj=req.responseXML;
		     var rowNodes = req_Obj.getElementsByTagName('rows');
		     var machinetype="";
		     var processormodel="";
		     if(rowNodes!=null){
				    var rowLen = rowNodes.length;
				    for(var i=0;i<rowLen;i++){
						var colNodes = rowNodes[i].childNodes;
						if(colNodes != null){
							var colLen = colNodes.length;
							for(var j=0;j<colLen;j++){
								var arr = colNodes[j].getAttribute("name");
								if(arr=="freeid"){
									document.getElementById("freeId").value=colNodes[j].text
								}
								if(arr == 'freename'){
									document.getElementById("freeName").value=colNodes[j].text
								}
								if(arr=="qjid"){
									document.getElementById("qjId").value=colNodes[j].text
								}
								if(arr == 'obliquity'){
		  					 		document.getElementById("obliquity").value=colNodes[j].text;
								}
				            }
			             }
			        }
		        }
		 }
		 Request.process();
}
