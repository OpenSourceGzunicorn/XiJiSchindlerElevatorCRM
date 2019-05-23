function getmachinetype(url){
		 var typeid = document.getElementById("typeID").value;
		 var Request = new AjaxRequest();
		 //var url = '<html:rewrite page="/LiftFparmAction.do?method=AjaxLiftf&typeid="/>'+typeid;
		 url = url+typeid
		 Request.url = url;
		 Request.mehod="POST";
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
								if(arr=="zzid"){
								document.getElementById("zzId").value=colNodes[j].text
										}
								if(arr=="freeid"){
								document.getElementById("freeId").value=colNodes[j].text
										}
								if(arr == 'sdid'){
								document.getElementById("sdId").value=colNodes[j].text
								}
								if(arr == 'loadkg'){
		  					 document.getElementById("loadkg").value=colNodes[j].text;
								}
								if(arr == 'freename'){
								document.getElementById("freename").value=colNodes[j].text
								}
								if(arr == 'speed'){
		  					 document.getElementById("speed").value=colNodes[j].text;
								}
				            }
			             }
			        }
		        }
		 }
		 Request.process();
}
