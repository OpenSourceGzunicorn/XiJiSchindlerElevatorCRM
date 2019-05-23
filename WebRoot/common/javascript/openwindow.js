/**
 * 弹出指定的查询页面的窗口，并根据返回的数组对象，向页面对应id的输入框赋值 
 * 
 * @param actionName 查询页面的actionName 如searchCustomerAction 开头务必小写
 * @param formName 父页面的表单名称 如<action attribute="serveTableForm" 中的serveTableForm 。当为空时，会自动获取页面中表单的名称。
 */
function openWindowAndReturnValue(actionName,formName){	 
  var obj = openWindow(actionName);
  if(formName == null || formName == ""){
	  var forms = document.getElementsByTagName("form");
	  if(forms !=null && forms.length == 1){
		  formName = document.getElementsByTagName("form")[0].name; //当formName为空时，自动获取页面表单名称
	  }	  
  }
  if(formName != null || !formName == ""){
	  toSetInputValue(obj,formName);  
  }
}

/**
 * 弹出指定的查询页面的窗口，并根据返回的数组对象，向页面对应id的输入框赋值 
 * 
 * @param actionName 查询页面的actionName 如searchCustomerAction 开头务必小写
 * @param flag 例如输入框id="name2", flag就传入2
 */
function openWindowAndReturnValue2(actionName,flag){	 
  var returnvalue = openWindow(actionName);
  toSetInputValue2(returnvalue,flag); 
}


/**
 * 获取弹出的查询页面被选中的所有行包含的所有隐藏域的id和值，存入二维数组并返回。
 * 返回给openWindowAndReturnValue方法往父页面对应id的输入框赋值
 * 
 * 返回值 二维数组 returnarray
 */
function selectReturnMethod(){
  if(serveTableForm.ids){      
    var len = document.serveTableForm.ids.length;
    len = len==null?1:len
    
    var returnarray = new Array();
    var flag = false;
    for(a=0,b=0;a<len;a++){
      var checkBox = len>1?document.serveTableForm.ids[a]:document.serveTableForm.ids

      if(checkBox.checked == true){
        returnarray[b] = new Array();   
         
        parentObj = checkBox.parentNode;
        inputs = parentObj.getElementsByTagName("input");
          
        for(i=0;i<inputs.length;i++){
          if(inputs[i].type == "hidden" && inputs[i].id !=null 
        		  && inputs[i].id !="" && inputs[i].value != null){   
            returnarray[b].push(inputs[i].id+"="+inputs[i].value);         
          }
        }

        b++;
        flag = true;
      }
    }  
    if(!flag){
      alert("请选择至一条记录！");
    }else{
      parent.window.returnuu(returnarray);
    }
    
  }
}

//弹出指定的查询页面的窗口
function openWindow(actionName){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+projectName+actionName+".do";  
	return window.showModalDialog(url,window,'dialogWidth:970px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
}

//弹出指定的查询页面的窗口，带参数
function openWindowWithParams(actionName,method,paramstring){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+encodeURIComponent(projectName+actionName+".do?method="+method+"&"+paramstring);
	return window.showModalDialog(url,window,'dialogWidth:970px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
}

function simpleOpenWindow(actionName,id,params){
	var params = params;
	if(params == null || params == ""){
		params = "isOpen=Y";
	}
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url=projectName+actionName+".do?method=toDisplayRecord&"+params+"&id="+id;
	window.open(url,'','height=500px, width=1000px,scrollbars=yes, resizable=yes,directories=no');
}

//根据数组对象，向父页面对应id的输入框赋值 
function toSetInputValue(arr,formName){	
  if(arr){  
    for(i=0;i<arr.length;i++){		       
      for(j=0;j<arr[i].length;j++){     
        var object = arr[i][j].split("=");
        var id = object[0];
        var value = object[1];
        var inputObj = eval(formName+"."+id);
        if(inputObj != null){
          if(inputObj.length > 1 ){
            var startIndex =  inputObj.length - arr.length;
            eval(formName+"."+id)[i+startIndex].value = value;
		  }else{
		    eval(formName+"."+id).value = value;
		  }
        }
        
      }		      
    } 		    
  }  
}

//根据数组对象，向父页面对应id的输入框或元素的innerHTML赋值
function toSetInputValue2(arr,flag){
  if(arr && arr.length == 1){  
    flag=flag?flag:'';
    for(i=0;i<arr.length;i++){		       
      for(j=0;j<arr[i].length;j++){
      	
        var object = arr[i][j].split("=");
        var id = object[0];
        var value = object[1];
        if(document.getElementById(id+flag)){
          var element = document.getElementById(id+flag);
         
          if(element.value != null){
          	element.value = value;
          } else if(element.innerHTML != null){
            element.innerHTML = value;
          }
          
        }
      }		      
    } 		    
  }  
}

//根据数组对象，向父页面对应id的输入框赋值 
function toSetInputValue3(arr,formName,flag){	
  if(arr){  
  	flag=flag?flag:'';
    for(i=0;i<arr.length;i++){		       
      for(j=0;j<arr[i].length;j++){     
        var object = arr[i][j].split("=");
        var id = object[0];
        var value = object[1];
        var inputObj = eval(formName+"."+id+flag);
        
        if(inputObj != null){
          if(inputObj.length > 1 ){
            var startIndex =  inputObj.length - arr.length;
            if(eval(formName+"."+id+flag)[i+startIndex].type=="hidden"){
            	eval(formName+"."+id+flag)[i+startIndex].parentNode.innerHTML+=value;
            }else{
            	eval(formName+"."+id+flag)[i+startIndex].value = value;
            }
            
            //eval(formName+"."+id+flag)[i+startIndex].value = value;
            
		  }else{
			  if(eval(formName+"."+id+flag).type=="hidden"){
				  eval(formName+"."+id+flag).parentNode.innerHTML+=value;
	            }else{
	            	eval(formName+"."+id+flag).value = value;
	            }
		    //eval(formName+"."+id+flag).value = value;
		  }
          
        }
        
        
      }		      
    } 		    
  }  
}

function openWindowAndReturnValue3(actionName,method,paramstring,flag){	 
	  var returnvalue = openWindowWithParams(actionName,method,paramstring);
	  toSetInputValue2(returnvalue,flag); 
}
function openWindowAndReturnValue4(actionName,method,paramstring,flag){	 
	  var returnvalue = openWindowWithParams(actionName,method,paramstring);
	  toSetInputValue4(returnvalue,flag); 
}
//根据数组对象，向父页面对应id的输入框或元素的innerHTML赋值
function toSetInputValue4(arr,flag){
  if(arr && arr.length > 0){
    flag=flag?flag:'';

    for(i=0;i<arr.length;i++){
    	//alert(arr[i]);
      for(j=0;j<arr[i].length;j++){
      	
        var object = arr[i][j].split("=");
        var id = object[0];
        var value = object[1];
        
        if(i==0){
          	if(document.getElementById(id+flag)){
	          var element = document.getElementById(id+flag);
	         
	          if(element.value != null){
	          	element.value = value;
	          } else if(element.innerHTML != null){
	            element.innerHTML = value;
	          }
	        }
        }else{
        	if(document.getElementById(id+flag)){
	          var element = document.getElementById(id+flag);
	         
	          if(element.value != null){
	          	element.value += ','+value;
	          } else if(element.innerHTML != null){
	            element.innerHTML += ','+value;
	          }
	        }
        }
      }		      
    }
  }
  
}

function openWindowAndReturnValue5(actionName,method,paramstring,flag){	 
	  var returnvalue = openWindowWithParams(actionName,method,paramstring);
	  toSetInputValue5(returnvalue,flag); 
}
//根据数组对象，向父页面对应id的输入框或元素的innerHTML赋值
function toSetInputValue5(arr,flag){
  if(arr && arr.length > 0){
    flag=flag?flag:'';

    for(i=0;i<arr.length;i++){
    	//alert(arr[i]);
      for(j=0;j<arr[i].length;j++){
      	
        var object = arr[i][j].split("=");
        var id = object[0];
        var value = object[1];
        
    	if(document.getElementById(id+flag)){
          var element = document.getElementById(id+flag);
         
          if(element.value != null){
          	if(element.value==''){
          		element.value = value;
          	}else{
          		element.value += ','+value;
          	}
          } else if(element.innerHTML != null){
            element.innerHTML += ','+value;
          }
          
          
        }
      }		      
    }
  }
  
}




