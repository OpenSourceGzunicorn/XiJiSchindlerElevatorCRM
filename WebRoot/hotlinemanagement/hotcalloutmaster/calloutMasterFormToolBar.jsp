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

  <logic:present name="isopenshow">
  	AddToolBarItemEx("closeBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
  </logic:present>
  <logic:notPresent name="isopenshow">
  	AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  </logic:notPresent>
  
  <logic:equal name="typejsp" value="add">
 AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod('add','N')");
 AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","","保存提交并返回","120","1","saveMethod('add','Y')");
 </logic:equal>
 <logic:equal name="typejsp" value="mondity">
 AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod('mondity','N')");
 AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","","保存提交并返回","120","1","saveMethod('mondity','Y')");
 </logic:equal>
 <logic:equal name="typejsp" value="sh">
 AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","审核通过","80","1","Auditing('isSendSms','Y')");
 AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","驳回维修单","100","1","RAuditing('isSendSms','R')");
 </logic:equal>
 <logic:equal name="typejsp" value="ps">
 AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","保存","80","1","saveps('N')");
 AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","保存并返回","100","1","saveps('Y')");
 </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

function delselect(){
	document.getElementById("hftId").value="";
	document.getElementById("hftDesc").value="";
}
//关闭
function closeMethod(){
  window.close();
}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/hotphoneSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(typejsp,isreturn){	
	if(checkColumnInput(CalloutMasterjxForm)){
		//var storageid=document.getElementById("storageId").value;
		var assignObject=document.getElementById("assignObject").value;
		if(assignObject==''){
			alert("请选择 派工对象。");
		}else{
			document.CalloutMasterjxForm.typejsp.value = typejsp;
			document.CalloutMasterjxForm.isreturn.value = isreturn;
			document.CalloutMasterjxForm.submit();
		}
	}
}
//驳回急修单
function RAuditing(value,isreturn){
	var bhAuditRem=document.getElementById("bhAuditRem").value;
	if(bhAuditRem==""){
		alert("驳回意见, 不能为空！");
		return;
	}
	
	var calloutMasterNo=document.getElementById("calloutMasterNo").value;
	if(confirm("确认驳回 "+calloutMasterNo+" 急修单？")){
		document.CalloutMasterjxForm.action='<html:rewrite page="/hotphoneAction.do"/>?typejsp=shsave&id='+calloutMasterNo+'&value=N&isreturn='+isreturn;
		document.CalloutMasterjxForm.submit();
		document.CalloutMasterjxForm.action='/hotphoneAction.do';
	}
}
function Auditing(value,isreturn){
	var value1;
	var value2=document.getElementsByName(value);
	var calloutMasterNo=document.getElementById("calloutMasterNo").value;
	var auditRem=document.getElementById("auditRem").value;
	for(var i=0;i<value2.length;i++){
		if(value2[i].checked==true){
			value1=value2[i].value;
		}
	}
	 //window.location = '<html:rewrite page="/hotphoneAction.do"/>?typejsp=shsave&id='+calloutMasterNo+'&value='+value1+'&isreturn='+isreturn+'&auditRem='+auditRem;
	 document.CalloutMasterjxForm.action='<html:rewrite page="/hotphoneAction.do"/>?typejsp=shsave&id='+calloutMasterNo+'&value='+value1+'&isreturn='+isreturn;
	 document.CalloutMasterjxForm.submit();
	 document.CalloutMasterjxForm.action='/hotphoneAction.do';
}

function saveps(isreturn){
	document.CalloutMasterjxForm.isreturn.value=isreturn;
	document.CalloutMasterjxForm.typejsp.value='pssave';
	document.CalloutMasterjxForm.submit();
}


//AJAX动态显示维保站
var phonearr=new Array();
var req;
function Evenmore(storageid){

	 var selectfreeid = document.getElementById("assignObject");

	if(storageid!="" && storageid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/hotphoneAction.do"/>?method=toStorageIDList&storageid='+storageid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("请选择",""));	

				 		for(var i=0;i<rows.length;i++){
							var colNodes = rows[i].childNodes;
							if(colNodes != null){
								var colLen = colNodes.length;
							 	phonearr=new Array(colLen);
							 	
								for(var j=0;j<colLen;j++){
									var freename= colNodes[j].getAttribute("name");
									var freeid= colNodes[j].getAttribute("value");
									var freephone= colNodes[j].getAttribute("phone");
									selectfreeid.add(new Option(freename,freeid));
									
									var objs=new Object;
									objs.id=freeid;
									objs.phone=freephone;
									phonearr[j]=objs;
									
					            }
				             }
				 		}
				 		
					 	//设置下拉框值
					 	var maintpersonnel=document.getElementById("maintPersonnel").value;
					    var aobj=document.getElementById("assignObject");
					    for(var a=0;a<aobj.length;a++){
					    	if(aobj[a].value==maintpersonnel){
					    		aobj[a].selected=true;
					    		break;
					    	}
					    }
					    //alert(phonearr[0].id+","+phonearr[1].phone+"; "+phonearr.length);
					    
				 	}
				
				}
		 };//回调方法
		 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
		selectfreeid.add(new Option("请选择",""));
	}
}

//下拉框发生变化，更改电话
function changeuser(obj){
	var phone=document.getElementById("phone");
	if(obj.value==""){
		phone.value="";
	}else{
		for(var i=0;i<phonearr.length;i++){
			var objs=phonearr[i];
			if(objs.id==obj.value){
				phone.value=objs.phone;
			}
		}
	}
}

//显示维保站下拉框
function showstorageid(val){
	var sel=0;
	
	var selectfreeid = document.getElementById("assignstorageId");
	selectfreeid.options.length=0;
	//if(val==''){
    	selectfreeid.add(new Option("请选择",""));	
	//}
    <logic:iterate id="element" name="storageidList">
	    var sid='<bean:write name="element" property="storageid"/>';
	    var sname='<bean:write name="element" property="storagename"/>';
	    
	    //if(val==''){
	    //	selectfreeid.add(new Option(sname,sid));
	    //}else if(val==sid){
	    //	selectfreeid.add(new Option(sname,sid));
	    //	if(sid!='10098'){
	    //		selectfreeid.add(new Option("厂检站","10098"));
	    //	}
	    //}
	    
	    selectfreeid.add(new Option(sname,sid));

	    sel++;
	    if(val==sid){
	    	selectfreeid[sel].selected=true;//选中下拉框
	    }
	    
    </logic:iterate>
    
    
    
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