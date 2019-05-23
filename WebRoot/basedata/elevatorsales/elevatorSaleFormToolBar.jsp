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
  <logic:notPresent name="isOpen">
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
 </logic:notPresent>
 
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatorSale" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  
  <logic:present name="isOpen">
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/close.gif","","",'关闭',"65","0","window.close();");
  </logic:present>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/elevatorSaleSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
  document.elevatorSaleForm.isreturn.value = "N";
  document.elevatorSaleForm.submit();
}

//保存返回
function saveReturnMethod(){
  document.elevatorSaleForm.isreturn.value = "Y";
  document.elevatorSaleForm.submit();
}

//删除
var tbs;
function deleteFile(tablename,elevatorNo,value,td){
	  tbs=td;
	    if(confirm("确定删除吗")){
	    	tbs.parentElement.style.display='';
			var uri = '<html:rewrite page="/elevatorSaleAction.do"/>?method=toDeleteFileRecord';
			uri +='&filesid='+ value;
			uri +='&folder=ElevatorArchivesInfo.file.upload.folder';
			uri +='&tablename='+tablename;
			uri +='&elevatorNo='+elevatorNo;
			sendRequestDelFile(uri);  	
			//partitionGrade();
		}
	}

//下载附件
function downloadFile(name,tablename){
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	var tablename1=encodeURI(tablename);
	tablename1=encodeURI(tablename1);
	var uri = '<html:rewrite page="/elevatorSaleAction.do"/>?method=toDownloadFileRecord';
		uri +='&filesname='+ name1;
		uri +='&folder=ElevatorArchivesInfo.file.upload.folder';
		uri +='&tablename='+tablename1;
	window.location = uri;
	//window.open(url);
}

var XMLHttpReq = false;
//创建XMLHttpRequest对象       
function createXMLHttpRequest() {
if(window.XMLHttpRequest) { //Mozilla 浏览器
XMLHttpReq = new XMLHttpRequest();
}else if (window.ActiveXObject) { // IE浏览器
XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
}
}

//发送请求函数
function sendRequestDelFile(url) {
createXMLHttpRequest();
XMLHttpReq.open("post", url, true);
XMLHttpReq.onreadystatechange = processResponseDelFile;//指定响应函数
XMLHttpReq.send(null);  // 发送请求
}
// 处理返回信息函数

  function processResponseDelFile() {
   	if (XMLHttpReq.readyState == 4) { // 判断对象状态
       	if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息        	
       	
          	var res=XMLHttpReq.responseXML.getElementsByTagName("res")[0].firstChild.data;	
          	//document.getElementById("messagestring").innerHTML=res;
          	if(res=="Y"){
          		tbs.parentElement.parentElement.removeChild(tbs.parentElement);
          		partitionGrade();
          	}else{
          		alert("删除失败！");
          	}        	
          	//alert(document.getElementById("messagestring").innerHTML+";123");
       	} else { //页面不正常
             window.alert("您所请求的页面有异常。");
       	}
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