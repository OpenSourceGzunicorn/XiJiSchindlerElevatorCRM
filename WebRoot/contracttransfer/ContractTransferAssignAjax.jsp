<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<html:errors/>
<br>
	<html:form action="/ServeTable.do">
	<table>
	  <tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流水号:</td><td><html:text property="property(billno)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;维保合同号:</td><td><html:text property="property(maintcontractno)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;销售合同号:</td><td><html:text property="property(salescontractno)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;电梯编号:</td><td><html:text property="property(elevatorno)" styleClass="default_input"/></td>
	  </tr>
	  <tr>
		<td>所属维保分部:</td><td>
		<html:select property="property(maintdivision)" onchange="Evenmore(this,'maintstation')">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
		</td>
		<td>&nbsp;&nbsp;所属维保站:</td><td>
        <html:select property="property(maintstation)" styleId="maintstation">
			<html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
		</html:select>
		</td>
		<td>&nbsp;&nbsp;甲方单位名称:</td><td><html:text property="property(companyid)" styleClass="default_input"/></td>
	  </tr>
	</table>
	<br>
    <table:table id="guiContractTransferAssigAjax" name="contractTransferAssignAjax">
      <logic:iterate id="element" name="contractTransferAssignAjax">
		<table:define id="c_cb">
        	<html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="billno"/>
        	</html:multibox>
		        <html:hidden name="element" property="companyname" styleId="companyname"/>
				<html:hidden name="element" property="maintcontractno" styleId="maintcontractno"/>
				<html:hidden name="element" property="salescontractno" styleId="salescontractno"/> 
				<html:hidden name="element" property="elevatorno" styleId="elevatorno"/> 
				<html:hidden name="element" property="maintdivisionname" styleId="maintdivisionname"/> 
				<html:hidden name="element" property="maintstationname" styleId="maintstationname"/> 
				<html:hidden name="element" property="contractsdate" styleId="contractsdate"/> 
				<html:hidden name="element" property="contractedate" styleId="contractedate"/>
				<html:hidden name="element" property="companyid" styleId="companyid"/>
				<html:hidden name="element" property="maintdivision" styleId="maintdivision"/>
				<html:hidden name="element" property="maintstation" styleId="maintstation"/>
		</table:define>
		
		<table:define id="c_BillNo">
        	<!-- <a href="<html:rewrite page='/EngUnitInfoAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="engunitid"/>"><bean:write name="element"  property="engunitid"/></a> -->
         		<bean:write name="element" property="billno"/>
		</table:define>
		<table:define id="c_CompanyName">
        	<bean:write name="element" property="companyname"/>
		</table:define>
		<table:define id="c_MaintContractNo">
        	<bean:write name="element" property="maintcontractno"/>
		</table:define>
		<table:define id="c_SalesContractNo">
        	<bean:write name="element" property="salescontractno"/>
		</table:define>
		<table:define id="c_ElevatorNo">
        	<bean:write name="element" property="elevatorno"/>
		</table:define>
		<table:define id="c_MaintDivision">
        	<bean:write name="element" property="maintdivisionname"/>
		</table:define>
		<table:define id="c_MaintStation">
        	<bean:write name="element" property="maintstationname"/>
		</table:define>
		<table:define id="c_ContractSDate">
        	<bean:write name="element" property="contractsdate"/>
		</table:define>
		<table:define id="c_ContractEDate">
        	<bean:write name="element" property="contractedate"/>
		</table:define>
        <table:tr/>
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