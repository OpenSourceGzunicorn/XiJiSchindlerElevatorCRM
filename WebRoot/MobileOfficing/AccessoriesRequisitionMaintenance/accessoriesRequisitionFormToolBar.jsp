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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="naccessoriesrequisitionmaintenance" value="Y"> 
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
 </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/accessoriesRequisitionMaintenanceSearchAction.do"/>?method=toSearchRecord';
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//保存返回
function saveReturnMethod(){
	var msir=document.getElementsByName("isAgree");
	if(msir[0].value!=null && msir[0].value!=""){
		
		var isagree=msir[0].value;

		var picauditRem=document.getElementById("picauditRem").value;
		if(isagree=="N" && picauditRem.trim()==""){
			alert("维保负责人审核意见 不能为空！");
			return;
		}else if(isagree=="Y"){
			/*
			var isCharges=document.getElementById("isCharges").value;
			if(isCharges==""){
				alert("请选择 初步判断是否收费！");
				return;
			}
			var isCharges=document.getElementById("isCharges").value;
			if(isCharges==""){
				alert("请选择 是否收费！");
				return;
			}
			*/
			var instock=document.getElementById("instock").value;
			if(instock==""){
				alert("请选择 备件库是否有库存！");
				return;
			}
			/**
			var money1=document.getElementById("money1").value;
			if(isCharges=="Y" && money1.trim()==""){
				alert("初步判定金额 不能为空！");
				return;
			}
			
			if(money1.trim()!="" && isNaN(money1.trim())){
				alert("初步判定金额 只能输入数字！");
				return;
			}
			*/
		}
		
	    document.accessoriesRequisitionMForm.isreturn.value = "Y";
	    document.accessoriesRequisitionMForm.submit();
	}else{
   		alert("是否同意不能为空");
   }
}





//下载
function downloadFile(id){
	var uri = '<html:rewrite page="/accessoriesRequisitionMaintenanceAction.do"/>?method=toDownLoadFiles';
	var name1=encodeURI(id);
	name1=encodeURI(name1);
		uri +='&filename='+ name1;
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