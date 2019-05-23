<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite forward="jq.js"/>"></script>
<br>
<html:errors/>
<html:form action="/markingScoreAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table id="table_mt" width="100%" class="tb">

  <tr>
    <td width="20%" class="wordtd"><bean:message key="markingitems.mtId"/>:</td>
    <td width="80%" id="td1"><html:text property="msId" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="markingitems.mtName"/>:</td>
   <td width="80%" id="td2"><html:textarea property="msName" rows="2" cols="60" styleClass="default_textarea"/><font color="red">*</font></td>
  </tr>
   <tr>
    <td width="20%" class="wordtd"><bean:message key="markingitems.fraction"/>:</td>
   <td width="80%" id="td3"><html:text property="fraction" maxlength="255" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  </tr>
     <tr>
    <td width="20%" class="wordtd">电梯类型:</td>
   <td width="80%" id="td3">
   <html:select property="elevatorType">
   <html:option value="">请选择</html:option>
    <html:options collection="elevaorTypeList" property="id.pullid" labelProperty="pullname"/>
   </html:select>
 <font color="red">*</font></td>
  </tr>
   <tr>
    <td class="wordtd">排序号:</td>
    <td nowrap="nowrap">
    <input type="text" id="orderby"  name="orderby" onpropertychange="checkthisvalue(this);" Class="default_input"/><font color="red">*（只能填数字）</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
<br>
<table width="70%" border="0" cellpadding="0" cellspacing="0" class="tb" id="table_ms">
    <thead>
    <tr height="23">
    <td colspan="3">&nbsp;
    <input type="button" class="default_input" value=" + " onclick="addOneRow('table_ms');" />
    <input type="button" class="default_input" value=" - " onclick="deleteRow('table_ms');"/>
    <b>评分明细</b>
    </td>
    </tr>
    <tr class="wordtd_header" id="tr0">
    <td width="5%"><input type="checkbox" onclick="checkTableAll('table_ms',this)"/></td>
    <td width="30%">评分明细代码<font color="red">*</font></td>
    <td width="50%">评分明细名称<font color="red">*</font></td>
    </tr>
    </thead>
    <tfoot>
        <tr height="23"><td colspan="3"></td></tr>
     </tfoot>
    <tbody>
    <tr id="tr1" >
    <td align="center"><input type="checkbox" style="text-align:center" size="1"/></td>
    <td align="center"><input type="text" name="detailId" id="detailId" size="30"/></td>
    <td align="center"><textarea  rows="2" cols="63" name="detailName" id="detailName"></textarea></td>
    </tr>
    </tbody>
    </table >
    <script type="text/javascript">
    $("document").ready(function(){

    	setDynamicTable("table_ms","tr1");

	}); 
    </script>
<html:javascript formName="markingScoresForm"/>
</html:form>
   