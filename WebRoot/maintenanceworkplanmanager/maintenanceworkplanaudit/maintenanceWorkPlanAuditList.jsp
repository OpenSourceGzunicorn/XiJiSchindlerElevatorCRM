<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
      <td>      
        <bean:message key="maintContract.billNo" />
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>                 
      <td>
       &nbsp;&nbsp;               
        <bean:message key="maintContract.maintContractNo" />
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td> 
      <td>  
        &nbsp;&nbsp;     
        <bean:message key="maintContract.attn" />
        :
      </td>
      <td>
        <html:text property="property(attn)" styleClass="default_input" />
      </td>           
    </tr>
    <tr>   
      <td> 
                    审核状态
        :
      </td>
      <td>
        <html:select property="property(auditStatus)">
          <html:option value="">全部</html:option>
		  <html:option value="Y">已审核</html:option>
		  <html:option value="N">未审核</html:option>
        </html:select>
      </td>                     
      <td> 
        &nbsp;&nbsp;      
        <bean:message key="maintContract.maintDivision" />
        :
      </td>
      <td>
        <html:select property="property(maintDivision)" onchange="Evenmore(this,'mainStation')">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>
      <td>
			&nbsp;&nbsp;所属维保站:
		</td>
		<td>
			<html:select property="property(mainStation)" styleId="mainStation">
			<html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
			</html:select>
		</td>      
    </tr>
  </table>
  <br>
  <table:table id="guiMaintenanceWorkPlanAudit" name="maintenanceWorkPlanAuditList">
    <logic:iterate id="element" name="maintenanceWorkPlanAuditList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billNo}" />
        <html:hidden name="element" property="contractEdate" />
        <html:hidden name="element" property="submitType" />
        <html:hidden name="element" property="contractStatus"/>
        <html:hidden name="element" property="auditStatus" />   
        <html:hidden name="element" property="taskSubFlag" />
        <html:hidden name="element" property="maintContractNo" />      
      </table:define>
      <table:define id="c_billNo">
           <a href="<html:rewrite page='/maintenanceWorkPlanAuditAction.do'/>?method=toPrepareUpdateRecord&id=<bean:write name="element"  property="billNo"/>">
          <bean:write name="element" property="billNo" />
        </a>      
      </table:define>
      <table:define id="c_maintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_contractSdate">
        <bean:write name="element" property="contractSdate" />
      </table:define>
      <table:define id="c_contractEdate">
        <bean:write name="element" property="contractEdate" />
      </table:define>
      <table:define id="c_attn">
        <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_submitType">
       <input type="hidden" name="submitType" value='<bean:write name="element" property="r1"/>'>
        <logic:notEmpty name="element" property="r1">
        <logic:match name="element" property="r1" value="N">未提交</logic:match>
        <logic:match name="element" property="r1" value="Y">已提交</logic:match>
        <logic:match name="element" property="r1" value="null">未提交</logic:match>       
        </logic:notEmpty>
      </table:define>      
      <table:define id="c_auditStatus"> 
        <logic:notEmpty name="element" property="auditStatus">
        <logic:match name="element" property="auditStatus" value="N">未审核</logic:match>
        <logic:match name="element" property="auditStatus" value="Y">已审核</logic:match>
        <logic:match name="element" property="auditStatus" value="null">未审核</logic:match>       
        </logic:notEmpty>
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
       <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:tr />
    </logic:iterate>
    </table:table>
</html:form>
<script>
//AJAX动态显示维保站
var req;
function Evenmore(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/maintenanceReceiptAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("全部","%"));	

				 		for(var i=0;i<rows.length;i++){
							var colNodes = rows[i].childNodes;
							if(colNodes != null){
								var colLen = colNodes.length;
								for(var j=0;j<colLen;j++){
									var freeid = colNodes[j].getAttribute("name");
									var freename = colNodes[j].getAttribute("value");
									selectfreeid.add(new Option(freeid,freename));
					            }
				             }
				 		}
				 	}
				
				}
		 };//回调方法
		 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
   	selectfreeid.add(new Option("全部","%"));
	}
}

</script>