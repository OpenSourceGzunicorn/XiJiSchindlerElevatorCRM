<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

  <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>审核信息</td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核人:</td>
      <td width="20%">
        ${delayBean.auditOperid}
      </td>     
      <td class="wordtd" nowrap="nowrap">审核状态:</td>
      <td width="20%">
        ${delayBean.auditStatus == 'Y' ? '已审核' : '未审核'}
      </td>         
      <td class="wordtd" nowrap="nowrap">审核时间:</td>
      <td width="20%">
        ${delayBean.auditDate}
      </td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核意见:</td>
      <td colspan="5">
          <span class="audit show">${delayBean.auditRem}</span>         
          <span class="audit hide">
            <logic:equal name="delayBean" property="auditStatus" value="N">
              <html:textarea name="delayBean" property="auditRem" rows="3" cols="100" styleClass="default_textarea"/>
            </logic:equal>
            <logic:equal name="delayBean" property="auditStatus" value="Y">
            	${delayBean.auditRem}
            </logic:equal>
          </span>
      </td> 
    </tr> 
  </table>
<%--   <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>审核信息</td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核人:</td>
      <td width="25%">
        ${maintContractBean.auditOperid}
      </td>     
      <td class="wordtd" nowrap="nowrap">审核状态:</td>
      <td width="30%">
        ${maintContractBean.auditStatus == 'Y' ? '已审核' : '未审核'}
      </td>         
      <td class="wordtd" nowrap="nowrap">审核时间:</td>
      <td width="15%">
        ${maintContractBean.auditDate}
      </td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核意见:</td>
      <td colspan="5">
        ${maintContractBean.auditRem}     
      </td> 
    </tr> 
  </table> 
  
  <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>任务下达信息</td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">任务下达人:</td>
      <td width="25%">
        ${maintContractBean.taskUserId}
      </td>     
      <td class="wordtd" nowrap="nowrap">任务下达标志:</td>
      <td width="30%">
        ${maintContractBean.taskSubFlag == 'Y' ? '已下达' : ''}
        ${maintContractBean.taskSubFlag == 'N' ? '未下达' : ''}
        ${maintContractBean.taskSubFlag == 'R' ? '已退回' : ''}
      </td>         
      <td class="wordtd" nowrap="nowrap">任务下达日期:</td>
      <td width="15%">
        ${maintContractBean.taskSubDate}
      </td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">任务下达备注:</td>
      <td colspan="5">
        ${maintContractBean.taskRem}
      </td> 
    </tr> 
  </table>  --%>
