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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustContractModify" value="Y">
  		AddToolBarItemEx("PassBtn","../../common/images/toolbar/digital_confirm.gif","","",'保 存',"65","1","passMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/entrustContractModifySearchAction.do"/>?method=toSearchRecord';
}

//审核通过
function passMethod(){
	
	var contractSdate=document.getElementById("contractSdate").value;
	if(contractSdate.trim()==""){
		alert("合同开始日期  不能为空！");
		return;
	}
	
	var contractEdate=document.getElementById("contractEdate").value;
	if(contractEdate.trim()==""){
		alert("合同结束日期  不能为空！");
		return;
	}
	
	var mainEdate=document.getElementsByName("mainEdate");
	var iserrstr="";
	for(var i=0;i<mainEdate.length;i++){
		if(mainEdate[i].value.trim()==""){
			iserrstr="维保结束日期 不能为空！";
			break;
		}
	}
	
	if(iserrstr!=""){
		alert(iserrstr);
		return;
	}
	
	var contractTotal=document.getElementById("contractTotal").value;
	if(contractTotal.trim()==""){
		alert("合同总额 不能为空！");
		return;
	}
	document.entrustContractMasterForm.submit();
	
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

function pickStartDay(endDateId){
  var endDate = document.getElementById(endDateId).value;
  endDate = endDate == null || endDate == ""　? "2099-12-31" : endDate    
  var d = endDate.split('-');
  WdatePicker({isShowClear:false,maxDate:d[0]+'-'+d[1]+'-'+(Number(d[2])-1),readOnly:true});
}
function pickEndDay(startDateId){
	//var date = new Date();
	var startDate = document.getElementById(startDateId).value;
	//var todayDate = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	//var d = startDate < todayDate ? todayDate.split('-') : startDate.split('-');
	var d=startDate.split('-');
	WdatePicker({isShowClear:false,minDate:d[0]+'-'+d[1]+'-'+(Number(d[2])+1),readOnly:true});
}
//计算合同有效期
function setContractPeriod(){
  var sd = entrustContractMasterForm.contractSdate.value.split("-");
  var ed = entrustContractMasterForm.contractEdate.value.split("-");
  if(sd != "" && ed != ""){  
    var years = Number(ed[0])-Number(sd[0]);
    var months = years>0 ? 12*years - (Number(sd[1]) - Number(ed[1])) : Number(ed[1]) - Number(sd[1]);
    months = Number(ed[2])-Number(sd[2])>0 ? months+1 : months;  
    entrustContractMasterForm.contractPeriod.value = months;     
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