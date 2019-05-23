<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<script language="javascript" charset="GBK">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nsalarybonusmanage" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/salaryBonusManageSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
  if(verify()){
	  document.salaryBonusManageForm.projects.value=rowsToJSONArray("prb","projects");
	  document.salaryBonusManageForm.isreturn.value="N";
	  document.salaryBonusManageForm.submit();
  }
  
}

//保存返回
function saveReturnMethod(){
  inputTextTrim();  
  if(verify()){
	  document.salaryBonusManageForm.projects.value=rowsToJSONArray("prb","projects");
	  document.salaryBonusManageForm.isreturn.value="Y";
      document.salaryBonusManageForm.submit();
    } 
}

//添加报销报销项目
function addElevators(tableId){

 var maintStation=document.getElementById("maintStation").value;
 if(maintStation==""||maintStation==null){
		alert("请选择维保站!");
		return;
 }
	
  var paramstring = "maintStation="+maintStation+"&maintContractNos=";    
  var maintContractNos = document.getElementsByName("maintContractNo");
  for(i=0;i<maintContractNos.length;i++){
    paramstring += i<maintContractNos.length-1 ? "|"+maintContractNos[i].value+"|," : "|"+maintContractNos[i].value+"|"    
  }

  var returnarray = openWindowWithParams("searchProjectReimbursementAction","toSearchRecord2",paramstring);//弹出框并返回值

  if(returnarray!=null && returnarray.length>0){          
    addRows(tableId,returnarray.length);//增加行
    toSetInputValue(returnarray,"salaryBonusManageForm");//向页面对应输入框赋值
  }    
  
  //setTopRowDateInputsPropertychange(document.getElementById(tableId));
}

function sumValuesByName(tableId,name,outputId){
	 var objs = document.getElementById(tableId);
	 var inputs=objs.getElementsByTagName("input");
	 var sum = 0;
	 var value = 0;
	 for(var i=0;i<inputs.length;i++){
		 if(inputs[i].name==name){
			 value = parseFloat(inputs[i].value);
			 sum =  isNaN(value) ? sum + 0 : accAdd(sum, value);
		 }
	 }
	 document.getElementById(outputId).value = sum;
}

function Average(tableId,name,outputId){
	var objs=document.getElementById(tableId);
	var rows=(objs.rows).length-2;
	var total=document.getElementById(outputId).value;
	
	if(rows>0){
		if(total!=null && total!=""){
			var sum=parseFloat(total);
			var money=(sum/rows).toFixed(2);
			var inputs=objs.getElementsByTagName("input");
			for(var i=0;i<inputs.length;i++){
				if(inputs[i].name==name){
					inputs[i].value=money;
				}
			}
		}else{
			alert("工资、奖金总额为空，请输入工资、奖金总额！");
		}
		
	}else{
		alert("请添加工资、奖金明细！");
	}
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
  var msg ="";

  for(var i=0;i<rows.length;i++){
    var cells = tableObject.rows[i].cells;
    var inputs = rows[i].getElementsByTagName("input");
    var selects = rows[i].getElementsByTagName("select");
    if(rows[i].id == titleRowId){
      titleRowIndex = i; 
    }
    
    for(var j=0;j<inputs.length;j++){
      var cellIndex = getFirstSpecificParentNode("td",inputs[j]).cellIndex;
      
      if(rows[titleRowIndex].cells[cellIndex]!=null&&rows[titleRowIndex].cells[cellIndex].innerHTML.indexOf("*")>=0){
        if(inputs[j].type=="text" && inputs[j].value==""){
          var title = rows[titleRowIndex].cells[cellIndex].innerHTML;            
          msg +="第"+(i-titleRowIndex)+"行 "+title.replace(/\s*<+.*>+\s*/g,"")+"不能为空\n";    
        }        
      }
      
    }  
    
    for(var j=0;j<selects.length;j++){
        var cellIndex = getFirstSpecificParentNode("td",selects[j]).cellIndex;        
        if(rows[titleRowIndex].cells[cellIndex]!=null&&rows[titleRowIndex].cells[cellIndex].innerHTML.indexOf("*")>=0){
          if(selects[j].value==""){
            var title = rows[titleRowIndex].cells[cellIndex].innerHTML;            
            msg +="第"+(i-titleRowIndex)+"行 请选择 "+title.replace(/\s*<+.*>+\s*/g,"")+"\n";    
          }        
        }
        
      }
    
  }
        
  if(msg != ""){
    alert(msg);
    return false;
  } 
  return true;
}

function isNanTable(){
	var prows=document.getElementById("prb").rows;
	var msg="";
	if(prows.length<=2){
		msg+="请添加工资、奖金明细！\n"
	}
	if(msg!=""){
		alert(msg);
		return false;
	}
	return true;
}

function verify(){
	if(isNanTable() && checkColumnInput(salaryBonusManageForm) && checkRowInput("prb","prbT_0")){
		return true;
	}
	return false;
}

function checkPact(obj){
	var rowTr=obj.parentElement.parentElement;
	var inputs=rowTr.getElementsByTagName("input");
	var bustype="";
	var billno="";
	var actionName="";
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].name=="busType"){
			bustype=inputs[i].value;
		}
		if(inputs[i].name=="billno"){
			billno=inputs[i].value;
		}
	}
	//alert(bustype+":"+billno);
	if(bustype=="B"){
		actionName="maintContractAction";
	}else{
		actionName="wgchangeContractAction";
	}
	simpleOpenWindow(actionName,billno,"typejsp=Yes");
}

function displayCheck(billno,bustype){
	var actionName="";
	if(bustype=="B"){
		actionName="maintContractAction";
	}else{
		actionName="wgchangeContractAction";
	}
	simpleOpenWindow(actionName,billno,"typejsp=Yes");
}
</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              CreateToolBar();
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>