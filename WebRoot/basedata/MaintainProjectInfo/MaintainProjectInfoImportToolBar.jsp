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

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return" />',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nMaintainProjectInfo" value="Y"> 
    AddToolBarItemEx("ImportBtn2","../../common/images/toolbar/save.gif","","",'导入EXCEL',"85","1","checkMethod()");
    AddToolBarItemEx("DownloadBtn","../../common/images/toolbar/download_theme.gif","","",'下载模版',"80","1","downLoadMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回

function returnMethod(){
  window.location = '<html:rewrite  page="/MaintainProjectInfoSearchAction.do"/>?method=toSearchRecord';
}

function checkMethod(){
var elevatorType=document.getElementById("elevatorType").value;
var maintType=document.getElementById("maintType").value;
  var fileName = document.getElementById("file").value;
  if(elevatorType!=""&&maintType!=""){
  if(fileName==""){
    alert("请选择EXCEL2007或以上版本文件。");
  }else{
    var filestr=fileName.substring(fileName.lastIndexOf("\.")+1,fileName.length).toLowerCase();
    if(filestr!="xlsx"){
      alert("请选择EXCEL2007或以上版本文件。");
    }else{
      if(confirm("若存在维保项目，维保基本要求相同的数据，数据将被替换，确认上传？")){
    	  document.getElementById("ReturnBtn").disabled=true;
    	  document.getElementById("ImportBtn2").disabled=true;
    	  document.getElementById("DownloadBtn").disabled=true;
          document.MaintainProjectInfoForm.submit();
      	}      
   	 }
  	} 
  }else{
  alert("请选择电梯类型和保养类型")
  }
}

//下载模版
function downLoadMethod(){
   window.open("<html:rewrite  page='/uploadFile/Baoyangjiludan.xlsx'/>"); 
}

</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%" >
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