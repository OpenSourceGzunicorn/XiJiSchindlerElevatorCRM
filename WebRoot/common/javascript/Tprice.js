function getTypeid(obj,url){
	
	  var selectzzid = document.getElementById("zzId");
	
	 if(obj.value==""){	 			
 		selectzzid.options.length=0; 
 		selectzzid.add(new Option("请选择",""));
 		
 	 }else{
 		getLoad(obj,url); ;//电梯倾角表(扶梯)
 		
 	 }

 }
 function getTypeid22(obj,url){
 		getLoad(obj,url); ;//电梯倾角表(扶梯)
 }
 function getTypeid2(obj,url2){
	
	 var selectfreeid = document.getElementById("freeId");
	 if(obj.value==""){	 			
 		selectfreeid.options.length=0; 
 		selectfreeid.add(new Option("请选择",""));
 	 }else{
 		getFreeItem(obj,url2);
 	 }

 }
 function getTypeid3(obj,url3){
	
	 var selectsdid = document.getElementById("sdId");
	 if(obj.value==""){	 			
 		selectsdid.options.length=0; 
 		selectsdid.add(new Option("请选择",""));
 	 }else{
 		getSpeed(obj,url3);
 	 }

 }
  function getTypeid33(obj,url3){
 		getSpeed(obj,url3);
 }
 function getFreeItem(obj,url2){
	 var selectfreeid = document.getElementById("freeId");

	 var Request = new AjaxRequest();
	 //var url = '<html:rewrite page="/searchContractElevator2Action.do"/>?method=toAjaxFreeItem&typeid='+obj.value;
	url2 = url2+obj.value;
	 Request.url = url2;
	 Request.mehod="POST";
	 Request.async = false; //fasle 表示同步，true 表示异步
	 Request.onComplete = function(req){
	     var req_Obj=req.responseXML;
	     var rowNodes = req_Obj.getElementsByTagName('rows');
	     if(rowNodes!=null){
	     	selectfreeid.options.length=0; 
	 		selectfreeid.add(new Option("请选择",""));	

		    var rowLen = rowNodes.length;
		    for(var i=0;i<rowLen;i++){
				var colNodes = rowNodes[i].childNodes;
				if(colNodes != null){
					var colLen = colNodes.length;
					for(var j=0;j<colLen;j++){
						var freeid = colNodes[j].getAttribute("name");
						var freename = colNodes[j].getAttribute("value");
						selectfreeid.add(new Option(freename,freeid));
		            }
	             }
	        }
        }
	 }
	 Request.process("getFreeItem");
 }
function getSpeed(obj,url3){
	 var selectsdid = document.getElementById("sdId");
	 
	 var Request = new AjaxRequest();
	url3 = url3+obj.value;
	 Request.url = url3;
	 Request.mehod="POST";
	 Request.async = false; //fasle 表示同步，true 表示异步
	 Request.onComplete = function(req){
	     var req_Obj=req.responseXML;
	     var rowNodes = req_Obj.getElementsByTagName('rows');
	     if(rowNodes!=null){
	 		selectsdid.options.length=0; 
	 		selectsdid.add(new Option("请选择",""));
	 		
		    var rowLen = rowNodes.length;
		    for(var i=0;i<rowLen;i++){
				var colNodes = rowNodes[i].childNodes;
				if(colNodes != null){
					var colLen = colNodes.length;
					for(var j=0;j<colLen;j++){
						var sdid = colNodes[j].getAttribute("name");
						var speed = colNodes[j].getAttribute("value");
						selectsdid.add(new Option(speed,sdid));
		            }

	             }
	        }
        }
	 }
	 Request.process("getSpeed");
 }
 
 function getLoad(obj,url){

	 var selectzzid = document.getElementById("zzId");

	 var Request = new AjaxRequest();
	url = url+obj.value;
	 Request.url = url;
	 Request.mehod="POST";
	 Request.async = false; //fasle 表示同步，true 表示异步
	 Request.onComplete = function(req){
	     var req_Obj=req.responseXML;
	     var rowNodes = req_Obj.getElementsByTagName('rows');
	     if(rowNodes!=null){
	 		selectzzid.options.length=0; 
	 		selectzzid.add(new Option("请选择",""));

		    var rowLen = rowNodes.length;
		    for(var i=0;i<rowLen;i++){
				var colNodes = rowNodes[i].childNodes;
				if(colNodes != null){
					var colLen = colNodes.length;
					for(var j=0;j<colLen;j++){
						var zzid = colNodes[j].getAttribute("name");
						var loadkg = colNodes[j].getAttribute("value");
						selectzzid.add(new Option(loadkg,zzid));
		            }

	             }
	        }
        }
	 }
	 Request.process("getLoad");
 }