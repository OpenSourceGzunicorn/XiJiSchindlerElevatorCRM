<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncustomervisitplan" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
	
       window.location = '<html:rewrite page="/customerVisitPlanSearchAction.do"/>?method=toSearchRecord';

}


//保存
function saveMethod(){
	
	if(checkTable())
		{
		  document.customerVisitPlanForm.isreturn.value = "N";
	  		document.customerVisitPlanForm.submit();
		} 
}

//保存返回
function saveReturnMethod(){
	 if(checkTable())
	  {
  	    document.customerVisitPlanForm.isreturn.value = "Y";
        	document.customerVisitPlanForm.submit();
	 } 
}

//增加一行
var index=1;
function addonerows(tbobj,n) {
	if(checkRowInput(tbobj.id,"headrow")){
		var rowobj = tbobj.insertRow(tbobj.rows.length);
		var rows = tbobj.rows;    
		var cells = defaultrowobjs[n];       
		for (var i = 0;i<cells.length;i++) {
			var cell = rowobj.insertCell(rowobj.cells.length);
			cell.align="center";
			cell.innerHTML = defaultrowobjs[n][i].replace("visitStaff_0", "visitStaff_"+index);
		}
		index++;
		tbobj.getElementsByTagName("input").ckAll.checked = false   
	}
}
      
//删除一行
function deleteRow(tbobj){
	var rows = tbobj.rows
	var delrownums = new Array();
	for(var i=rows.length-1;i>0;i--){
		if(rows[i].cells[0].getElementsByTagName("input").cb1.checked == true){
			delrownums.push(i);
		}
	}
	for(var i=0;i<delrownums.length;i++){
		tbobj.deleteRow(delrownums[i]);
	}      
	tbobj.getElementsByTagName("input").ckAll.checked = false
}
    
//列表全选反选
function op(ckall,tbobj){
	var ckvalue=ckall.checked
	var rows = tbobj.rows
	for(var i=1;i<rows.length;i++){
		rows[i].cells[0].getElementsByTagName("input").cb1.checked = ckvalue
	}
}
    
function opleft(cb1,tbobj){  
	if(cb1.checked==false){
		tbobj.getElementsByTagName("input").ckAll.checked = false
	}
}

function switchCell(n) {
	for(i=0;i<navcell.length;i++){
		navcell[i].className="tab-off";
		tb[i].style.display="none";
	}
	navcell[n].className="tab-on";
	tb[n].style.display="";
}

function openNewWindow(action,method){
	
	   var url='query/Search.jsp?path=<html:rewrite page="/'+action+'.do"/>?method='+method;
	   openwindowReturnValueCustomer(url,"customerVisitPlanForm");
	}

function openwindowReturnValueCustomer(url,formname){
	  var obj = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
	  if(obj)
	  {  
	    for(i=0;i<obj.length;i++){
	       
	      for(j=0;j<obj[i].length;j++){     
	        var object = obj[i][j].split("=");
	        var id = object[0];
	        var value = object[1];	
	        
	        if(obj.length > 1){
	          eval(formname+"."+id[i]).value = value;
	        } else {
	          eval(formname+"."+id).value = value;
	        }
	        
	      }
	    } 
	    
	  }
}

function checkRowInput(tableID,titleRowID)
{
	 var tableObject = document.getElementById(tableID);
	  var rows = tableObject.rows; 
	  var titleRowIndex = 0;
	  var msg = "";
	  
	  for(var i=0;i<rows.length;i++){
		    var cells = tableObject.rows[i].cells;
		    var select = rows[i].getElementsByTagName("select");
		    if(rows[i].id == titleRowID){
		      titleRowIndex = i; 
		    }
	
	    for(var j=0;j<select.length;j++){
	        var cellIndex = select[j].parentNode.cellIndex;
	        
	        if(rows[titleRowIndex].cells[cellIndex]!=null&&rows[titleRowIndex].cells[cellIndex].innerHTML.indexOf("*")>=0){
	          if(select[j].value==""){
	            var title = rows[titleRowIndex].cells[cellIndex].innerHTML;            
	              msg += "第"+(i-titleRowIndex)+"行 "+title.replace(/\s*<+.*>+\s*/g,"")+"不能为空\n";    
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


function checkTable()
{
  var idName=document.getElementsByName("companyId");
  var b1 = null;
  var b2 = null;
  var b3 = null;

	if(idName[0]=="")
	  {
		 var b1 = false;
		 alert("拜访客户不能为空！");
		 return;
	  }else
	   {
		  var b1 = true;
	   }
		var cb1 = document.getElementsByName("cb1");
		
		if(cb1.length<1)
	  {			
			var b2 = false;
			alert("至少创建一个拜访计划！");
			return;
	  }else
	   {
		  var b2 = true;
	   }
		
		 if(b1&&b2)
	   {
			
			var b3=checkRowInput("jobHistory","titleRow_0");
	   }
		
	  
	return b3;
}


function change(obj,id){
	var visitStaff=document.getElementById(id);
	if(obj.value!=""){
	var maintDivision=document.getElementById("maintDivision").value;
	var mainStation=document.getElementById("assignedMainStation").value;
	if(maintDivision==null||maintDivision==""){
		alert("请先选着拜访项目!");
		obj.value="";
		return;
	}
    if(mainStation==null||mainStation==""){
    	alert("请先选着拜访项目!");
    	obj.value="";
		return;
    }
	
	var pathName = window.document.location.pathname;
 	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
	var url = projectName+"/customerVisitPlanAction.do?method=getPersonnel" + 
		"&maintDivision="+maintDivision+"&mainStation="+mainStation+"&visitPosition="+obj.value;
	var obj = $.ajax({
			url: url,
			async:false				
		}); 
    var personnels = eval(obj.responseText);
    visitStaff.options.length=0;
 	for(var i=0;i<personnels.length;i++)
 	{
 		var opt = document.createElement("option");
 		opt.value = personnels[i].id;
 		opt.innerHTML = personnels[i].name;
 		visitStaff.appendChild(opt);
 	}
	}else{
		visitStaff.options.length=0;
		var opt = document.createElement("option");
		opt.value = "";
 		opt.innerHTML = "请选择";
 		visitStaff.appendChild(opt);
	}
	
	
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