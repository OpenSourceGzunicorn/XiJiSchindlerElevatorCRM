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
  AddToolBarItemEx("ImportBtn2","../../common/images/toolbar/save.gif","","",'导入EXCEL',"85","1","checkMethod()");
  AddToolBarItemEx("DownloadBtn","../../common/images/toolbar/download_theme.gif","","",'下载模版',"80","1","downLoadMethod()");

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//返回
function returnMethod(){
	window.location = '<html:rewrite page="/personnelManageSearchAction.do"/>?method=toSearchRecord';
}

function checkMethod(){
  var fileName = document.getElementById("file").value;
  if(fileName==""){
    alert("请选择EXCEL2007或以上版本文件。");
  }else{
    var filestr=fileName.substring(fileName.lastIndexOf("\.")+1,fileName.length).toLowerCase();
    if(filestr!="xlsx"){
      alert("请选择EXCEL2007或以上版本文件。");
    }else{
    	//if(confirm("若存在相同的维保分部，维保站，月份 将被替换，确认上传？")){
    	  document.getElementById("ImportBtn2").disabled=true;
          document.personnelManageForm.submit();  
    	//}
    }
  }
}

//下载模版
function downLoadMethod(){
	var flag = document.getElementsByName('flag')[0].value;
	if(flag==""){
		flag=document.getElementsByName('pflag')[0].value;
	}
	
	if(flag=="U"){
		window.open("<html:rewrite page='/uploadFile/personnelImportInfo.xlsx'/>");
	}else if(flag=="P"){
		window.open("<html:rewrite page='/uploadFile/traininghistory.xlsx'/>");
	}else if(flag=="Z"){
		window.open("<html:rewrite page='/uploadFile/certificateexam.xlsx'/>");
	}else if(flag=="T"){
		window.open("<html:rewrite page='/uploadFile/toolreceive.xlsx'/>");
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