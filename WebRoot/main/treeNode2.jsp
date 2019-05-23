<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>



<html:base/>


    <div align="center"> 
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
    <tr><td width="50%" valign="top">
    <table width="85%">
    <tr bgcolor="#B8C4F4">
      <td nowrap colspan="4" align="center"><b>任务列表</b></td>
    </tr>
    <tr>
      <td nowrap><div align="center">序号</div></td>
      <td nowrap><div align="center">任务名称</div></td>
      <td nowrap><div align="center">任务数量(个)</div></td>
      <td nowrap><div align="center">位置</div></td>
    </tr>
  
    <%int i=1;%>
    <logic:iterate id="duty" name="dutyList">
    
    <tr bgcolor="#FFFFFF" class="title_h3">
    <td nowrap bgcolor="#FFFFFF"><div align="center">&nbsp;<%=i%></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><bean:write name="duty" property="nodename"/></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><bean:write name="duty" property="dutycount"/></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><a href="<bean:write name="duty" property="nodeurl"/>" target="rightFrame">跳转</a></div></td>
    </tr>	
    
    <%i++;%>
    </logic:iterate>
  </table>
  </td>
  
  <td width="40%"  valign="top">
  <table width="80%">
  <tr bgcolor="#B8C4F4">
  	  <td nowrap align="center" colspan="2"><b>信息发布</b></td>
  </tr>
  <tr>
  <td nowrap bgcolor="#FFFFFF">
  <MARQUEE direction=up height=100 onmouseout=this.start() onmouseover=this.stop() scrollAmount=1 scrollDelay=100 width="360">
  <table width="100%">
  <%int k=1;%>
  <logic:iterate id="message" name="messageList">
  <tr>
    <td nowrap><div align="left">&nbsp;<%=k%></div></td>
    <td nowrap title="<bean:write name="message" property="promulgatetitle"/>(<bean:write name="message" property="promulgatedate"/>)"><div align="left"><a href="../dutysearchAction.do?method=toDisplayRecord&messid=<bean:write name="message" property="billno"/>" target="rightFrame"><bean:write name="message" property="promulgatetitle"/>(<bean:write name="message" property="promulgatedate"/>)</div></td>
  </tr>	
  <%k++;%>
  </logic:iterate>
  
  </table>
  </MARQUEE>
  </td>
  </tr>
  </table>

  
  </div>
  