<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/DatePicker/WdatePicker.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<input type="hidden" name="isreturn" id="isreturn" />
<input type="hidden" name="method" id="method" />
<table width="100%" border="0">
<tr>
<td>业务类别:<select name="busType" id="busType">
		<option value="%">全部</option>
		<option value="W">维修</option>
		<option value="G">改造</option>
	</select>
&nbsp;&nbsp;合同号：
<input type="text" name="maintContractNo" id="maintContractNo" class="default_input" >
&nbsp;&nbsp;所属维保分部:
<html:select property="maintDivision">
<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
</html:select>
</td>
</tr>
</table>
<br>
<table width="100%" class="tb" id="table_1">
	<tr>
		<td nowrap class="wordtd_header" width="3%"><div align="center"><input type="checkbox" name="selAll" onclick="checkedallbox(this)"></div></td>	
	    <td nowrap class="wordtd_header" width="3%"><div align="center">序号</div></td>
		<td nowrap class="wordtd_header" width="5%"><div align="center">业务类别</div></td>
		<td nowrap class="wordtd_header" width="9%"><div align="center">合同号</div></td>
	    <td nowrap class="wordtd_header" width="15%"><div align="center">客户名称</div></td>
	    <td nowrap class="wordtd_header" width="10%"><div align="center">预计到货日期</div></td>
	    <td nowrap class="wordtd_header" width="10%"><div align="center">确认到货日期</div></td>
	    <td nowrap class="wordtd_header" width="10%"><div align="center">任务下达人</div></td>
	    <td nowrap class="wordtd_header" width="10%"><div align="center">任务下达日期</div></td>
	    <td nowrap class="wordtd_header" width="20%"><div align="center">任务下达备注</div></td>
	    <td nowrap class="wordtd_header" width="5%"><div align="center">所属维保分部</div></td>
	</tr>
	<logic:empty name="ServicingContractMasterList">
	<tr><td colspan="11" align="center">没记录显示！</td></tr>
	</logic:empty>
	<logic:present name="ServicingContractMasterList">
	<logic:iterate id="element" name="ServicingContractMasterList" indexId="seid">
	<tr>
		<td align="center">
		<input type="checkbox" name="checkboxids" onclick="sethiddenvalue(this,'isbox',${seid})">
		<input type="hidden" name="isbox" id="isbox" value="N">
		<input type="hidden" name="wgbillno" id="wgbillno" value="<bean:write name="element"  property="wgbillno"/>">
		</td>
		<td align="center">${seid+1}</td>
		<td nowrap>
		<bean:write name="element"  property="bustype"/>
		</td>
		<td nowrap>		
		<a href="<html:rewrite page='/ServicingContractMasterTaskSearchAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="wgbillno"/>" target="_blnk">
		<bean:write name="element"  property="maintcontractno"/></a>
		
		</td>
		<td nowrap><bean:write name="element" property="companyid"/></td>
		<td><input type="text" name="forecastdate" id="forecastdate" size="12" Class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" readonly="readonly" value="<bean:write name="element" property="forecastdate"/>"/></td>
		<td><input type="text" name="comfirmdate" id="comfirmdate" size="12" Class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" readonly="readonly" value="<bean:write name="element" property="comfirmdate"/>"/></td>
		<td nowrap><bean:write name="element" property="taskuserid"/></td>
		<td nowrap><bean:write name="element" property="tasksubdate"/></td>
        <td nowrap><bean:write name="element" property="taskrem"/></td>
		<td nowrap><bean:write name="element" property="maintdivision"/></td>
	</tr>
	</logic:iterate>
	</logic:present>
</table>
<script type="text/javascript">
$("#busType").val('${busType}');
$("#maintContractNo").val('${maintContractNo}');

</script>



