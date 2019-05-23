//去掉空格
 String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}
 //检查金额是否为数字
 function f_check_number2(){
    if((event.keyCode>=48 && event.keyCode<=57)){
    }else{
    event.keyCode=0;
     }
 }
 //检查金额是否为数字,不可以输入负号和可以输入点号
 function f_check_number3(){
    if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
    }else{
    event.keyCode=0;
     }
 }
   //检查金额是否为数字,不可以输入点号和可以输入负号
 function f_check_number4(){
    if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==45){
    }else{
    event.keyCode=0;
     }
 }
  //检查金额是否为数字,可以输入负号和点号
 function f_check_number5(){
    if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==45 || event.keyCode==46){
    }else{
    event.keyCode=0;
     }
 }
 //检查输入框是否输入数字
function checkthisvalue(obj){
  if(isNaN(obj.value)){
    alert("请输入数字!");
    obj.value="0";
    obj.focus();
    return false;
  }
}

//页面所有文本输入框值去掉前后空格
function inputTextTrim(){
  var inputs = document.getElementsByTagName("input");
    for(var i=0;i<inputs.length;i++){
      if(inputs[i].type == "text"){
    	  if(inputs[i].value.indexOf(" ")>=0){
    		  inputs[i].value = inputs[i].value.trim();
    	  }       
      }
    }
}

/*
检查所有有*号标记的文本输入框的值是否为空并提示，适用于如下格式
例如: <td>姓名</td><td><input type="text"/>*</td> 
            当空值时提醒   “姓名不能为空”
参数 element 例如div，table，form对象
*/
function checkColumnInput(element){
  var inputs = element.getElementsByTagName("input");
  var textareas = element.getElementsByTagName("textarea");
  var selects = element.getElementsByTagName("select");
  var msg = "";

  for(var i=0;i<inputs.length;i++){
	  
    if(inputs[i].type == "text"
      && getFirstSpecificParentNode("td",inputs[i]) != null && inputs[i].parentNode.innerHTML.indexOf("*")>=0 
      && inputs[i].value.trim() == ""){
      	//searchval，searchvalNo1，为查询框的名称，不需要判断
      	if(inputs[i].name != "searchval" && inputs[i].name != "searchvalNo1"){
      		msg += getFirstSpecificParentNode("td",inputs[i]).previousSibling.innerHTML + "不能为空\n";	
      	}
    }
  }
  
  for(var i=0;i<textareas.length;i++){
      if(textareas[i].parentNode.innerHTML.indexOf("*")>=0 && getFirstSpecificParentNode("td",textareas[i]) != null && textareas[i].value.trim() == ""){          
          msg += getFirstSpecificParentNode("td",textareas[i]).previousSibling.innerHTML + "不能为空\n";                       
      }
  }
  
  for(var i=0;i<selects.length;i++){
		  if(selects[i].value.trim()=="" && selects[i].parentNode.innerHTML.indexOf("*")>0){
				  msg+="请选择"+selects[i].parentNode.previousSibling.innerHTML.replace(/:$/gi,"")+"\n";
			  
		  }
	  }
  
  if(msg != ""){
    alert(msg);
    return false;
  } 
  return true;
}

/*
检查table有*号标记的文本输入框的值是否为空并提示，适用于如下格式
例如: <table id="XX">
   <tr id="XX"><td>姓名*</td><tr>
     <tr><td><input type="text"/></td><tr> 
     <table>
            当空值时提醒   “姓名不能为空”
*/
function checkRowInput(tableId,titleRowId){
  var tableObject = document.getElementById(tableId);
  var rows = tableObject.rows; 
  var titleRowIndex = 0;
  var msg = "";

  for(var i=0;i<rows.length;i++){
    var cells = tableObject.rows[i].cells;
    var inputs = rows[i].getElementsByTagName("input");
    var textareas = rows[i].getElementsByTagName("textarea");
    if(rows[i].id == titleRowId){
      titleRowIndex = i; 
    }
    
    for(var j=0;j<inputs.length;j++){
      var cellIndex = getFirstSpecificParentNode("td",inputs[j]).cellIndex;
      
      if(rows[titleRowIndex].cells[cellIndex]!=null&&rows[titleRowIndex].cells[cellIndex].innerHTML.indexOf("*")>=0){
        if(inputs[j].type=="text" && inputs[j].value==""){
          var title = rows[titleRowIndex].cells[cellIndex].innerHTML;            
          msg += "第"+(i-titleRowIndex)+"行 "+title.replace(/\s*<+.*>+\s*/g,"")+"不能为空\n";    
        }        
      }
      
    }  
    
    for(var j=0;j<textareas.length;j++){
        var cellIndex = getFirstSpecificParentNode("td",textareas[j]).cellIndex;        
        if(rows[titleRowIndex].cells[cellIndex]!=null&&rows[titleRowIndex].cells[cellIndex].innerHTML.indexOf("*")>=0){
          if(textareas[j].value==""){
            var title = rows[titleRowIndex].cells[cellIndex].innerHTML;            
            msg += "第"+(i-titleRowIndex)+"行 "+title.replace(/\s*<+.*>+\s*/g,"")+"不能为空\n";    
          }        
        }
        
      }

    if(msg != ""){
      alert(msg);
      return false;
    } 
    
  }

  return true;
}

function checkEmpty(elementId,titleRowId){	
	if(titleRowId == null || titleRowId == ""){
		return(checkColumnInput(document.getElementById(elementId)));
	}else{
		return(checkRowInput(elementId,titleRowId));
	}				
}

//返回元素指定的父节点对象
function getFirstSpecificParentNode(parentTagName,childObj){
  var parentObj = childObj;  
  while(parentObj){
	if(parentObj.nodeName.toLowerCase() == "form"){
      return null;
	}
    if(parentObj.nodeName.toLowerCase() == parentTagName.toLowerCase()){      
      return parentObj;
    }
    parentObj = parentObj.parentNode;
  }
  return null;
}