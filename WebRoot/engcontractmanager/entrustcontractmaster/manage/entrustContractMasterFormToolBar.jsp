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
  
  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay">  
	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  <logic:notPresent name="display">  
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustcontractmaster" value="Y"> 
	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
	    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
	  </logic:equal>
	  </logic:notPresent>
  </logic:notPresent> 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//关闭
function closeMethod(){
  window.close();
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/entrustContractMasterSearchAction.do"/>?method=${returnMethod}';
}

//保存
function saveMethod(){

  if(checkInput()){
    document.entrustContractMasterForm.maintContractDetails.value=rowsToJSONArray("dynamictable_0","details");
    document.entrustContractMasterForm.submitType.value = "N";
    document.entrustContractMasterForm.isreturn.value = "N";
    document.entrustContractMasterForm.submit();
  }
  
}

//保存返回
function saveReturnMethod(){
  inputTextTrim();  
  if(checkInput()){
	  document.entrustContractMasterForm.maintContractDetails.value=rowsToJSONArray("dynamictable_0","details");
	  document.entrustContractMasterForm.submitType.value = "Y";
      document.entrustContractMasterForm.isreturn.value = "Y";
      document.entrustContractMasterForm.submit();
    } 
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

function checkInput(){
  inputTextTrim();// 页面所有文本输入框去掉前后空格
  
  var boo = false;
  var elevatorNos = document.getElementsByName("elevatorNo"); 
  if(elevatorNos !=null && elevatorNos.length>0){
    boo = checkColumnInput(entrustContractMasterForm) && checkRowInput("dynamictable_0","titleRow_0");
  }else{
    alert("不能保存，请添加合同明细。");
  }
  return boo;
}


function pickStartDay(endDate){
  var d = document.getElementById(endDate).value.split('-');
  WdatePicker({maxDate:d[0]+'-'+d[1]+'-'+(Number(d[2])-1),readOnly:true});
}


function pickEndDay(startDate){
  var d = document.getElementById(startDate).value.split('-');
  WdatePicker({minDate:d[0]+'-'+d[1]+'-'+(Number(d[2])+1),readOnly:true});
}


function setDatesByName(name,newDate){
  if(newDate && newDate!=""){
    var dates = document.getElementsByName(name);
          
    for ( var i = 0; i < dates.length; i++) { 
    if(dates[i].value == ""){
      dates[i].value = newDate;
    }  
    }  
  }
}


function setTopRowDateInputsPropertychange(table){
  
  var tbody = table.getElementsByTagName("tbody")[0]; 
  var rows = tbody.childNodes;
  
  for(var i=0;i<rows.length;i++){
    if(!rows[i].id){
      var inputs = rows[i].getElementsByTagName("input");
      for(var j=0;j<inputs.length;j++){
       
        if(inputs[j].className.indexOf("Wdate")>=0){
           inputs[j].onpropertychange = function(){setDatesByName(this.name,this.value);};
        }
      }
      break;
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