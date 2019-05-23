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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="naccessoriesrequisitionstoreman" value="Y"> 
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/accessoriesRequisitionStoremanSearchAction.do"/>?method=toSearchRecord';
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//保存返回
function saveReturnMethod(){
	
	var msir=document.getElementsByName("wmIsAgree");
	if(msir[0].value!=null&&msir[0].value!=""){
		
		var isagree=msir[0].value;

		var wmRem=document.getElementById("wmRem").value;
		if(isagree=="N" && wmRem.trim()==""){
			alert("配件库管理员审核意见 不能为空！");
			return;
		}else if(isagree=="Y"){
			var newNo=document.getElementById("newNo").value;
			if(newNo.trim()==""){
				alert("新件编号 新件名称/型号！");
				return;
			}
			
			var wmPayment=document.getElementById("wmPayment").value;
			if(wmPayment==""){
				alert("请选择 领取方式！");
				return;
			}
			/*
			var isCharges=document.getElementById("isCharges").value;
			if(isCharges==""){
				alert("请选择 初步判断是否收费！");
				return;
			}
			*/
			var instock=document.getElementById("instock").value;
			if(instock==""){
				alert("请选择 备件库是否有库存！");
				return;
			}
			/*
			var money2=document.getElementById("money2").value;
			if(isCharges=="Y" && money2.trim()==""){
				alert("收费金额 不能为空！");
				return;
			}
			if(money2.trim()!="" && isNaN(money2.trim())){
				alert("收费金额 只能输入数字！");
				return;
			}
			*/
		}
		
	    document.accessoriesRequisitionSForm.isreturn.value = "Y";
	    document.accessoriesRequisitionSForm.submit();
	}else{
   		alert("是否同意 不能为空");
   }
}

//下载
function downloadFile(id){
	var uri = '<html:rewrite page="/accessoriesRequisitionStoremanAction.do"/>?method=toDownLoadFiles';
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