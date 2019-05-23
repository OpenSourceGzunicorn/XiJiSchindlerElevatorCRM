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
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","",'保 存',"65","0","saveMethod()"); 
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save_back.gif","","",'保存并返回',"90","0","saveReturnMethod()"); 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/${action}.do"/>?method=toPrepareUpdateRecord&id=${id}';
}

//保存
function saveMethod(){
	var newfile=document.getElementById("newfile").value;
	var oldNo=document.getElementById("oldNo").value
	if(oldNo==""||oldNo==null){
		alert("请输入旧件编号!!");
		return;
	}
	if(newfile==""||newfile==null){
		alert("请上传旧件图片!!");
		return;
	}
	
	document.getElementById("isreturn").value = "N";
    document.getElementById("Addform").submit();
}

//保存返回
function saveReturnMethod(){
	var newfile=document.getElementById("newfile").value;
	var oldNo=document.getElementById("oldNo").value
	if(oldNo==""||oldNo==null){
		alert("请输入旧件编号!!");
		return;
	}
	if(newfile==""||newfile==null){
		alert("请上传旧件图片!!");
		return;
	}
	document.getElementById("isreturn").value = "Y";
    document.getElementById("Addform").submit();
}

//下载
function downloadFile(id){
	var uri = '<html:rewrite page="/accessoriesRequisitionAction.do"/>?method=toDownLoadFiles';
		uri +='&filename='+ id;
		uri +='&folder=AccessoriesRequisition.file.upload.folder';
	    
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
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>