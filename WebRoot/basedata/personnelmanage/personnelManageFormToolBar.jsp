<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="npersonnelmanage" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/personnelManageSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	inputTextTrim();	
	if(checkInput()){
		document.personnelManageForm.isreturn.value = "N";
  		document.personnelManageForm.submit();
	}
}

//保存返回
function saveReturnMethod(){
	inputTextTrim();	
	if(checkInput()){
    	document.personnelManageForm.isreturn.value = "Y";
    	document.personnelManageForm.submit();
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

//增加一行
function addonerows(tbobj,n) {
	if(checkRowInput(tbobj.id,"headrow")){
		var rowobj = tbobj.insertRow(tbobj.rows.length);
		var rows = tbobj.rows;    
		var cells = defaultrowobjs[n];       
		for (var i = 0;i<cells.length;i++) {
			var cell = rowobj.insertCell(rowobj.cells.length);
			cell.align="center";
			cell.innerHTML = defaultrowobjs[n][i];
		}   
		tbobj.getElementsByTagName("input").ckAll.checked = false   
		
		setfilename();//设置file的name
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

//检查输入框空值
function checkInput(){
	return checkColumnInput(table1) 
	    && checkRowInput("jobHistory","titleRow_1") 
		&& checkRowInput("trainingHistory","titleRow_2") 
		&& checkRowInput("certificateExam","titleRow_3")
		&& checkRowInput("toolReceive","titleRow_4");
}

//下载附件
function downloadFile(numno){
	var uri = '<html:rewrite page="/personnelManageAction.do"/>?method=toDownloadFileDispose';
	    uri +='&numno='+ numno;
	window.location = uri;
}

//设置file的name
function setfilename(){
	  var list = document.getElementsByTagName("input");
	  var kk=1;
	  for(i=0;i<list.length;i++){
		if(list[i].type=='file'){
			//alert(list[i].name);
			list[i].name = kk;// 这是修改这个值
			//alert(list[i].name);
			kk++;
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