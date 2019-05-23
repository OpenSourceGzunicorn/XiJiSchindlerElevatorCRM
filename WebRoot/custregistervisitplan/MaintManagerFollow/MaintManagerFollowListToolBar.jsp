  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
/*   AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()"); */
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nMaintManagerFollow" value="Y"> 
  AddToolBarItemEx("UpdateBtn","../../common/images/toolbar/edit.gif","","","跟 进","65","1","updateMethod()");  
  </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	var edate1 = document.getElementById("edate1").value;
	var sdate1 =document.getElementById("sdate1").value;
	if(sdate1!=""&&edate1!=""){	
       if(sdate1>edate1)
     	{
      	alert("开始日期必须小于结束日期！");
      	document.getElementById("edate1").value="";
	     return;
	   }
    
}
	serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//查看
function viewMethod(){
	var index = getIndex();	
	if(index >= 0){	
	toDoMethod(index,"toDisplayRecord","");
	}else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

function updateMethod(){
	var index = getIndex();	
	if(index >= 0){	
		
		/* var auditStatus=document.getElementById("auditStatus").value;	
	if(auditStatus=="已审核"){
		alert("该记录已审核,不能重复审核!");
		return;
	} */
	toDoMethod(index,"toPrepareUpdateRecord","");
	}else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

function referMethod(){
	var index = getIndex();	
	if(index >= 0){	
	var submitType=getVal("submitType",index)
	if(submitType=="是"){
		alert("该记录已提交，不能重复提交!");
		return;
	}
	toDoMethod(index,"toSubmitRecord","");
	}else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	} 
}



//跳转方法
function toDoMethod(index,method,params){
		window.location = '<html:rewrite page="/MaintManagerFollowAction.do"/>?id='+getVal('ids',index)+'&method=toPrepareUpdateRecord';
}

//获取选中记录下标
function getIndex(){
	if(document.getElementsByName("ids").length){	
		var ids = document.getElementsByName("ids");
		for(var i=0;i<ids.length;i++){
		  if(ids[i].checked == true){
		    return i;
		  }
		}				
	}
	return -1;	
}

//根据name和选中下标获取元素的值
function getVal(name,index){
	var obj = eval("serveTableForm."+name);
	if(obj && obj.length){
		obj = obj[index];
	}
	return obj ? obj.value : null;
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