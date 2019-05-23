<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	用户控制工具栏的显示
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"85","1","excelMethod()");
 AddToolBarItemEx("xlsBtn","../../common/images/toolbar/xls.gif","","",'按合同导出EXCEL',"125","1","excelMethod2()");
 window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//查询
function searchMethod(){
	document.serveTableForm.genReport.value="N";
	document.serveTableForm.target = "_blank";
	document.serveTableForm.submit();
}


//导出Excel
function excelMethod(){
	document.serveTableForm.genReport.value="Y";
	document.serveTableForm.target = "_blank";
	document.serveTableForm.submit();
}

//按合同号导出EXCEL
function excelMethod2(){
    serveTableForm.genReport.value="C";
	serveTableForm.target = "_blank";
	document.serveTableForm.submit();
}
</script>
  
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
  <td height="22" class="table_outline3" valign="bottom" width="100%">

    <div id="toolbar" align="center">
    <script language="javascript">
      <!--
        CreateToolBar();
      //-->
    </script>
    </div>
    </td>
</tr>
</table>
 
