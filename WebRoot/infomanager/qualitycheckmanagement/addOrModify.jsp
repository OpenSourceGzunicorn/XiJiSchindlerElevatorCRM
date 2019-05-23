<%@ page contentType="text/html;charset=GBK"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
      
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr height="12">
          <td colspan="4">            
          
          </td>
        </tr>
        <%-- tr class="wordtd_header">
			<td width="10%">顺序</td>
			<td>检查项目</td>
			<td>分值</td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr--%>
        <tr class="wordtd_header">
			<td width="20%">评分名称</td>
			<td>评分明细</td>
			<logic:notPresent name="dispose"><td width="5%">分值</td></logic:notPresent>
			<td width="20%">备注</td>
			<td width="8%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tfoot>
        <tr height="12"><td colspan="4"></td></tr>
      </tfoot>
        
    </table>
    
  
  <script type="text/javascript">  
  
    
  </script>