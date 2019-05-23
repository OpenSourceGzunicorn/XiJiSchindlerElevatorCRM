<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
	<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<html:errors />
<br>
<html:form action="/maintenanceWorkPlanTurnAction.do">
<input type="hidden" name="method" id="method"/>

	<table>
		<tr >
		    <td>
		          维保合同号:
			</td>
		   <td>
             <html:text property="maintContractNo" styleId="maintContractNo" styleClass="default_input" size="20"></html:text>
			</td>
		    <td>
		        &nbsp;&nbsp;电梯编号:
			</td>
		   <td>
             <html:text property="elevatorNo" styleId="elevatorNo" styleClass="default_input" size="20"></html:text>
			</td>
		     
		     <td>
		         &nbsp;&nbsp; 项目名称:
			</td>
		   <td>
             <html:text property="projectName" styleId="projectName" styleClass="default_input" size="30"></html:text>
			</td>
			<td>
		          &nbsp;&nbsp;所属维保站:
			</td>
		   <td>
			  <html:select property="mainStation" styleId="mainStation">
			     <html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
			  </html:select>
			</td>
			</tr>
			<tr>
			<td>
		          销售合同号:
			</td>
		   <td>
			  <html:text property="salesContractNo" styleId="salesContractNo" styleClass="default_input"></html:text>
			</td>
			<td>
		          &nbsp;&nbsp;原维保工:
			</td>
		   <td>
			  <html:text property="maintPersonnel" styleId="maintPersonnel" styleClass="default_input"></html:text>
			</td>
					<td>
				         &nbsp;&nbsp;下达日期:
					</td>
				   <td>
				   		<html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="12" onfocus="WdatePicker({isShowClear:true})"></html:text>
						- 
						<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="12" onfocus="WdatePicker({isShowClear:true})"></html:text>
						<font color="red">*</font>
					</td>
			
		</tr>
	</table>
<br>	
 <div  id="divid" style="overflow-y:auto;width:100%; height:340px;">
	<table class=tb width="100%" id="dynamictable_0" >  
	 <thead>
	<tr>
	<td class=wordtd_header style="text-align: center;"><div align="center"><input type="checkbox" name="selAll" onclick="checkTableAll('dynamictable_0',this);changeisBox();" /></div></td>
	<td nowrap="nowrap" class=wordtd_header style="text-align: center;" >&nbsp;序号&nbsp;</td>		
	<td nowrap="nowrap" class=wordtd_header>维保合同号</td>
	<td nowrap="nowrap" class=wordtd_header>销售合同号</td>
	<td nowrap="nowrap" class=wordtd_header >项目名称</td>
	<td nowrap="nowrap" class=wordtd_header>电梯编号</td>
	<td nowrap="nowrap" class=wordtd_header>维保开始时间</td>
	<td nowrap="nowrap" class=wordtd_header>维保计划开始时间</td>
	<td nowrap="nowrap" class=wordtd_header>维保结束时间</td>
	<td nowrap="nowrap" class=wordtd_header>下达日期</td>
	<td nowrap="nowrap" class=wordtd_header>维保站</td>
	<td nowrap="nowrap" class=wordtd_header>原维保工</td>
	<td nowrap="nowrap" class=wordtd_header>作业转派日期</td>
	<td nowrap="nowrap" class=wordtd_header>分配维保工</td>		
	</tr>
	</thead>
	<logic:present name="maintenanceTurnListSize">
	<logic:equal name="maintenanceTurnListSize" value="0">
			<TR class=noItems ><TD colSpan=13>没记录显示! </TD></TR>
	</logic:equal>
	<logic:notEqual name="maintenanceTurnListSize"  value="0">
				<logic:present name="maintenanceTurnList">
	<logic:iterate id="element" name="maintenanceTurnList" indexId="i">
	
	
	<tr align="center" >
	<td nowrap="nowrap" ><input type="checkbox" name="checkboxMcds" onclick="changeisBox();cancelCheckAll('dynamictable_0','selAll');">
	</td>
	<td>${i+1 }</td>     
	<td nowrap="nowrap">
	<bean:write name="element"  property="maintContractNo"/>
	<input type="hidden" name="isBox" value="N">
	<input type="hidden" name="rowid" value="${element.rowid}" >
	<input type="hidden" name="elevator" value="'${element.elevatorNo}'">
	</td>
	<td nowrap="nowrap"><bean:write name="element" property="salesContractNo" /></td>
	<td nowrap="nowrap"><bean:write name="element" property="projectName" /></td>
	<td nowrap="nowrap">
	<a onclick="simpleOpenWindow('elevatorSaleAction','${element.elevatorNo}');" class="link">${element.elevatorNo}</a>	
	</td>
	<td nowrap="nowrap"><bean:write name="element" property="mainSdate" /></td>
	<td nowrap="nowrap"><bean:write name="element" property="shippedDate" /></td>
	<td nowrap="nowrap"><bean:write name="element" property="mainEdate" /> </td>
	<td nowrap="nowrap"><bean:write name="element" property="taskSubDate" /></td>
	<td nowrap="nowrap"><bean:write name="element" property="assignedMainStation" /></td>
	<td nowrap="nowrap"><bean:write name="element" property="maintPersonnel" /></td>
	<td nowrap="nowrap">
		<html:text property="zpdate" styleId="zpdate" value="${element.zpdate }" styleClass="Wdate" size="12"  onfocus="WdatePicker({isShowClear:true})"/>
		<input type="hidden" name="hzpdate" value="${element.zpdate}">
	</td>
	<td nowrap="nowrap">
		<select style="width: 90px;" name="zpoperid"  onmouseover="getmaintPersonnel(this,'${element.mainStation}');"  >
		     <option value="">请选择</option>
        </select>		
	  <input type="hidden" name="mainStationId" value="${element.mainStation}">
	</td>
	</tr>	
	</logic:iterate>
	
	
	
	</logic:present>

	</logic:notEqual>


    </logic:present>
	

	</table>
	</div>
</html:form>
<script type="text/javascript">
$("document").ready(function() {
	  setScrollTable("divid","dynamictable_0",16);
});
</script>
