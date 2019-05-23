<%@ page contentType="text/html;charset=GBK" %>

<html:hidden property="tokenid" styleId="tokenid"/>
<html:hidden property="taskid"/>
<html:hidden property="taskname"/>
<html:hidden property="tasktype"/>
<a href="" id="avf" target="_blank"></a>
<html:hidden property="status" styleId="status"/>
<html:hidden property="flowname" styleId="flowname"/>

<logic:notPresent name="display"> 
  <br>
  <table width="100%" border="0" align="center"  class="tb" cellpadding="2" cellspacing="0">
    <tr height="23"><td colspan="2">&nbsp;<b>审批</b></td></tr>
    <tr>
      <td class="wordtd" nowrap ><div align="right">审批结果</div>
      </td>
      <td>
        <html:select property="approveresult" styleId="approveresult" styleClass="default_input">
          <html:options collection="ResultList" property="tranname" labelProperty="tranname"/>
        </html:select>
      </td>
    </tr>
    <tr>
      <td class="wordtd" nowrap ><div align="right">审批意见</div></td>
      <td>
        <html:textarea property="approverem" styleId="approverem" styleClass="default_textarea" cols="90" rows="3"/>
      </td>
    </tr>
  </table>
</logic:notPresent>