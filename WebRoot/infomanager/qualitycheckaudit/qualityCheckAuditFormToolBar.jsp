<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualitycheckaudit" value="Y"> 
    AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/digital_confirm.gif","","",'<bean:message key="tollbar.referdata"/>',"80","1","saveSubmitMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toSearchRecordAudit';
}

//保存提交
function saveSubmitMethod(){
	if(confirm("是否确定已审核完成信息，提交后将不能对该信息进行处理！")){
		document.qualityCheckManagementForm.isreturn.value = "Y";
		document.qualityCheckManagementForm.submit();
	}
}

//下载附件
function downloadFile(name,oldName){
	var uri = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toDownloadFileAudit';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	oledName=encodeURI(oldName);
	oledName=encodeURI(oldName);
		uri +='&filesname='+ name1;
		uri +='&folder=MTSComply.file.upload.folder';
		uri+='&fileOldName='+oldName;
	window.location = uri;
	//window.open(url);
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