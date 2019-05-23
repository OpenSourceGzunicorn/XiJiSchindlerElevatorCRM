<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.bean.*" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<logic:present name="flow">
<table width="99%" class="tb">
  <tr>
    <td width="10%" class="wordtd">流程ID</td>
    <td width="15%"><bean:write name="flow" property="processdefinename" filter="true"/></td>
    <td width="10%" class="wordtd">流程名称</td>
    <td width="18%"><bean:write name="flow" property="processdefinealiasname" /></td>
    <td width="10%" class="wordtd">版本</td>
    <td width="12%"><bean:write name="flow" property="version" /></td>
    <td width="15%">
    	<logic:equal name="flow" property="jump" value="1">
    		允许
    	</logic:equal>
    	<logic:notEqual name="flow" property="jump" value="1">
    		不允许
    	</logic:notEqual>
    </td>
  </tr>
</table>
<br/>
<table width="99%" class="tb">
  <tr>
    <td class="wordtd" width="50%"><div align="center">结点信息</div></td>
    <td class="wordtd" width="50%"><div align="center">扩展设置</div></td>
  </tr>
  <logic:iterate name="flow" property="nodelist" id="node">
  <tr>
  	<td valign="top">
  	<table><tr>
  	<td class="wordtd" width="15%"><div align="center">结点ID</div></td><td width="30%"><bean:write name="node" property="nodeid" /></td>
    <td class="wordtd" width="15%"><div align="center">结点名称</div></td><td width="40%"><bean:write name="node" property="nodename" /></td>
    </tr>
    <tr>
    <td class="wordtd"><div align="center">组织关系过滤</div></td>
    <td>
    	<logic:equal name="node" property="orgfilter" value="1"><input type="checkbox" name="orgfilter" value="<bean:write name="node" property="nodeid"/>" checked/></logic:equal>
    	<logic:notEqual name="node" property="orgfilter" value="1"><input type="checkbox" name="orgfilter" value="<bean:write name="node" property="nodeid"/>" /></logic:notEqual>
    </td>
    <%--td class="wordtd" ><div align="center">级别</div></td>
    <td><logic:equal name="node" property="jumpflag" value="1"><input type="checkbox" name="jumpflag" value="<bean:write name="node" property="nodeid"/>" checked/></logic:equal>
    	<logic:notEqual name="node" property="jumpflag" value="1"><input type="checkbox" name="jumpflag" value="<bean:write name="node" property="nodeid"/>" /></logic:notEqual>
    	<bean:write name="node" property="jumpflag"/>
    </td--%>
    </tr>
    <tr>
    <td class="wordtd"><div align="center">备注</div></td><td colspan="3"><bean:write name="node" property="rem1" />&nbsp;</td>
    </tr>
    </table>
    <%--bean:write name="node" property="des"/><bean:write name="node" property="ext1"/><bean:write name="node" property="ext2"/>
    <bean:write name="node" property="ext3"/><bean:write name="node" property="ext4"/><bean:write name="node" property="ext5"/--%>
    </td>
    <td valign="top">
    <logic:equal name="node" property="nodeclass" value="K">
    <div id="act" style="height:100px; width:100%; overflow:auto">
    <table width="100%" class="tb" id="act_<bean:write name="node" property="nodeid"/>">
      <thead>
      <tr style="position: relative; top: expression(this.offsetParent.scrollTop);">
        <td class="wordtd"><div align="center">用户</div></td>
        <td class="wordtd"><div align="center">类型</div></td>
      </tr>
      </thead>
      <tbody>
      <logic:iterate name="node" property="actlist" id="act">
      <tr>
        <td><bean:write name="act" property="actorsname"/></td>
        <td><bean:write name="act" property="typename"/></td>
      </tr>
      </logic:iterate>
      </tbody>
    </table>
    </div>
    <br/>
    <div id="tar" style="display:">

    <table width="100%" class="tb">
      <tr>
        <td class="wordtd" width="65%"><div align="center">链接［URL］</div></td>
        <!--td class="wordtd">param</td-->
        <td class="wordtd"><div align="center">目标［Target］</div></td>
      </tr>
      <logic:iterate name="node" property="tarlist" id="tar" length="1">
      <tr>
        <td><bean:write name="tar" property="url1"/></td>
        <td><bean:write name="tar" property="target1"/></td>
      </tr>
	  <tr>
        <td><bean:write name="tar" property="url2"/></td>
        <td><bean:write name="tar" property="target2"/></td>
      </tr>
      </logic:iterate>
    </table></div>
    </logic:equal>
    <logic:equal name="node" property="nodeclass" value="D">
    <div id="dec" style="display:">
    <table width="100%" class="tb" id="dec_<bean:write name="node" property="nodeid"/>">
      <thead>
      <tr>
        <td width="65%" class="wordtd">
        	<div align="center">
        	分流设置：
        		<logic:equal name="node" property="decflag" value="0">
            		固定值
            	</logic:equal>
            	<logic:notEqual name="node" property="decflag" value="0">
            		范围
            	</logic:notEqual>
              </div>
            <!--/td> 
            </tr>
        	</table--> 
        </td>
        <td width="35%" class="wordtd"><div align="center">流向</div></td>
      </tr>
      </thead>
      <tbody>
      <logic:iterate name="node" property="declist" id="dec">     
      <tr>
        <td>
        	<logic:equal name="node" property="decflag" value="0">
        	<div style="display:compact" name="dec_app_<bean:write name="node" property="nodeid"/>">分流选择：<bean:write name="dec" property="selpath" /></div>
        	</logic:equal>
            <logic:notEqual name="node" property="decflag" value="0">
        	<div style="display:compact" name="dec_sco_<bean:write name="node" property="nodeid"/>">最小值：<bean:write name="dec" property="minqty" />&nbsp;最大值：<bean:write name="dec" property="maxqty" /></div>
            </logic:notEqual>	
          </td>
          <td> 
        	<bean:write name="dec" property="tranpath"/>
          </td>
      </tr>
      </logic:iterate>
      </tbody>
    </table></div>
    </logic:equal>
    <br/>
    </td>
  </tr>
  </logic:iterate>
</table>
<br/>
<br/>
</logic:present>