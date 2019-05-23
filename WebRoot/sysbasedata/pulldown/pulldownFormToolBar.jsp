<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">
  <%--  判断单位类型模块UnitType是否有可写的权限,在property	--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="npulldown" value="Y">
   AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
   AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  <logic:present name="display">
  AddToolBarItemEx("PDFBtn","../../common/images/toolbar/print.gif","","",'导出PDF',"85","1","pdfMethod()");
  </logic:present>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  	 window.location = '<html:rewrite page="/PullDownSearchAction.do"/>?method=toSearchRecord';
 }

//保存
function saveMethod(){
  if(validatePulldownForm(document.all.item("pulldownForm"))==true){
	  var orderby=document.getElementById("orderby").value;
	  if(orderby.trim()==""){
		  alert("排序号 不能为空！");
	  }else{
		  if(isNaN(orderby.trim())){
			  alert("排序号 请输入数字!");
		  }else{
		 	  document.pulldownForm.isreturn.value = "N";
		   	  document.pulldownForm.submit();
	   	  }
   	  }
  }
}

//保存返回
function saveReturnMethod(){
	 if(validatePulldownForm(document.all.item("pulldownForm"))==true){
		 var orderby=document.getElementById("orderby").value;
		  if(orderby.trim()==""){
			  alert("排序号 不能为空！");
		  }else{
			  if(isNaN(orderby.trim())){
				  alert("排序号 请输入数字!");
			  }else{
			 	  document.pulldownForm.isreturn.value = "Y";
			   	  document.pulldownForm.submit();
		   	  }
	   	  }
 	}
}

//去掉空格
 String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}
 //检查金额是否为数字
 function f_check_number(){
  	if((event.keyCode>=48 && event.keyCode<=57)){
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
  
//pdf导出
function pdfMethod(){
 	window.location='<html:rewrite page="/ContractPdfServlet"/>?flag=PullDown&htmlName=PullDown.html';
}
</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              <!--
                CreateToolBar();
                //SetToolBarAttribute();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>