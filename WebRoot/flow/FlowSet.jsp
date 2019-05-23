<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.bean.*" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<logic:present name="flow">
<html:form action="/flowAction.do?method=toSetRecord">
<html:hidden property="isreturn"/>
<input type="hidden" name="id" value="<bean:write name="flow" property="processdefineid"/>">
<html:hidden name="flow" property="processdefineid"/>
<table width="99%" class="tb">
  <tr>
    <td width="10%" class="wordtd">流程ID</td>
    <td width="15%"><html:hidden name="flow" property="processdefinename" write="true"/></td>
    <td width="10%" class="wordtd">流程名称</td>
    <td width="18%"><html:text name="flow" property="processdefinealiasname" styleClass="default_input" size="30"/></td>
    <td width="10%" class="wordtd">版本</td>
    <td width="12%"><html:hidden name="flow" property="version" write="true"/></td>
    <%--td width="10%" class="wordtd">跳级</td>
    <td width="15%">&nbsp;
    	<logic:equal name="flow" property="jump" value="1">
    		<input type="radio" name="jump" value="1" checked>允许
    		<input type="radio" name="jump" value="0">不允许
    	</logic:equal>
    	<logic:notEqual name="flow" property="jump" value="1">
    		<input type="radio" name="jump" value="1">允许
    		<input type="radio" name="jump" value="0" checked>不允许
    	</logic:notEqual>
    </td--%>
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
  	<td class="wordtd" width="15%" nowrap><div align="center">结点ID</div></td><td width="30%"><html:hidden name="node" property="nodeid" write="true"/><html:hidden name="node" property="nodeclass"/></td>
    <td class="wordtd" width="15%" nowrap><div align="center">结点名称</div></td><td width="40%"><html:hidden name="node" property="nodename" write="true"/></td>
    </tr>
    <tr>
    <td class="wordtd" nowrap><div align="center">组织关系过滤</div></td>
    <td>
    	<logic:equal name="node" property="orgfilter" value="1"><input type="checkbox" name="orgfilter" value="<bean:write name="node" property="nodeid"/>" checked/></logic:equal>
    	<logic:notEqual name="node" property="orgfilter" value="1"><input type="checkbox" name="orgfilter" value="<bean:write name="node" property="nodeid"/>" /></logic:notEqual>
    </td>
    <td class="wordtd" nowrap><div align="center">级别</div></td>
    <td><%--logic:equal name="node" property="jumpflag" value="1"><input type="checkbox" name="jumpflag" value="<bean:write name="node" property="nodeid"/>" checked/></logic:equal>
    	<logic:notEqual name="node" property="jumpflag" value="1"><input type="checkbox" name="jumpflag" value="<bean:write name="node" property="nodeid"/>" /></logic:notEqual--%>
		<html:text name="node" property="jumpflag" size="8" styleClass="default_input" />
    </td>
    </tr>
    <tr>
    <td class="wordtd" nowrap><div align="center">备注</div></td><td colspan="3"><html:text name="node" property="rem1" styleClass="default_input" size="50"/></td>
    </tr>
    </table>
    <html:hidden name="node" property="des"/><html:hidden name="node" property="ext1"/><html:hidden name="node" property="ext2"/>
    <html:hidden name="node" property="ext3"/><html:hidden name="node" property="ext4"/><html:hidden name="node" property="ext5"/>
    </td>
    <td valign="top">
    <logic:equal name="node" property="nodeclass" value="K">
    <div style="height:102px; width:100%; overflow:auto">
    <table width="100%" class="tb" id="act_<bean:write name="node" property="nodeid"/>">
      <thead>
      <tr>
      	<td class="wordtd">
       		<!--<input type="button" name="adduser" class="default_bottom" id="adduser" style="width:30px" value="＋" onclick="setSel('query/Search.jsp?path=<html:rewrite page="/query/UserPurviewSetIndex.jsp"/>',act_<bean:write name='node' property='nodeid'/>)" /> 
			<input type="button" name="deluser" class="default_bottom" id="deluser" style="width:30px" value="－" onclick="del(act_<bean:write name="node" property="nodeid"/>)" />-->
		</td>
        <td class="wordtd"><div align="center">用户</div></td>
        <td class="wordtd"><div align="center">类型</div></td>
      </tr>
      <tr style="display:none">
      	<td><input type="checkbox">
      		<input type="hidden" name="actext3_<bean:write name="node" property="nodeid"/>" id="actext3" value="">
    		<input type="hidden" name="actext4_<bean:write name="node" property="nodeid"/>" id="actext4" value="">
    		<input type="hidden" name="actext5_<bean:write name="node" property="nodeid"/>" id="actext5" value=""></td>
        <td><input type="hidden" id="actors" name="actactors_<bean:write name="node" property="nodeid"/>" styleClass="default_input">
        	<input type="text" id="ext1" name="actext1_<bean:write name="node" property="nodeid"/>" style="border=0;" readonly="true"/></td>
        <td><input type="hidden" id="typeid" name="acttypeid_<bean:write name="node" property="nodeid"/>"/>
        	<input type="text" id="ext2" name="actext2_<bean:write name="node" property="nodeid"/>" style="border=0;" readonly="true"/></td>
      </tr>
      </thead>
      <tbody>
      <logic:iterate name="node" property="actlist" id="act">
      <tr>
      	<td><input type="checkbox"><input type="hidden" name="actext3_<bean:write name="node" property="nodeid"/>" id="actext3" value="">
    		<input type="hidden" name="actext4_<bean:write name="node" property="nodeid"/>" id="actext4" value="">
    		<input type="hidden" name="actext5_<bean:write name="node" property="nodeid"/>" id="actext5" value=""></td>
        <td><input type="hidden" name="actactors_<bean:write name="node" property="nodeid"/>" id="actactors" value="<bean:write name="act" property="actors"/>" >
        	<input type="text" name="actext1_<bean:write name="node" property="nodeid"/>" id="ext1" value="<bean:write name="act" property="actorsname"/>" style="border=0;" readonly="true"></td>
        <td><input type="hidden" name="acttypeid_<bean:write name="node" property="nodeid"/>" id="acttypeid" value="<bean:write name="act" property="typeid"/>" >
        	<input type="text" name="actext2_<bean:write name="node" property="nodeid"/>" id="ext2" value="<bean:write name="act" property="typename"/>" style="border=0;" readonly="true"></td>
      </tr>
      </logic:iterate>
      </tbody>
    </table></div>
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
        <td><input type="text" name="tarurl1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="url1"/>" size="55"></td>
        <td style="display:none"><input type="text" name="tarparam1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="param1"/>"></td>
        <td><input type="text" name="tartarget1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="target1"/>"></td>
      </tr>
	  <tr>
        <td><input type="text" name="tarurl2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="url2"/>" size="55"></td>
        <td style="display:none"><input type="text" name="tarparam2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="param2"/>"></td>
        <td><input type="text" name="tartarget2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="target2"/>"></td>
      </tr>
      <tr style="display:none">
        <td><input type="text" name="tarurl3_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="url3"/>"></td>
        <td><input type="text" name="tarparam3_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="param3"/>"></td>
        <td><input type="text" name="tartarget3_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="target3"/>"></td>
      </tr>
      <input type="hidden" name="taractionname_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="actionname"/>">
      <input type="hidden" name="tardes_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="des"/>">
      <input type="hidden" name="tarenabledflag_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="enabledflag"/>">
      <input type="hidden" name="tarext1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="ext1"/>">
      <input type="hidden" name="tarext2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="ext2"/>">
      <input type="hidden" name="tarext3_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="ext3"/>">
      <input type="hidden" name="tarext4_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="ext4"/>">
      <input type="hidden" name="tarext5_<bean:write name="node" property="nodeid"/>" value="<bean:write name="tar" property="ext5"/>">
      </logic:iterate>
    </table></div>
    </logic:equal>
    <logic:equal name="node" property="nodeclass" value="D">
    <div id="dec" style="display:">
    <table width="100%" class="tb" id="dec_<bean:write name="node" property="nodeid"/>">
      <thead>
      <tr>
      	<td width="5%" class="wordtd_k2">&nbsp;</td>
        <td width="65%" class="wordtd_k2">
        	<div align="center">
        	<!--table width="100%" border="0"><tr>
        	<td-->
        		<input type="button" name="adddec" class="default_bottom" style="width:30px" value="＋" onclick="add(dec_<bean:write name="node" property="nodeid"/>)" />
				<input type="button" name="deldec" class="default_bottom" style="width:30px" value="－" onclick="del(dec_<bean:write name="node" property="nodeid"/>)" />
        	<!--/td-->
        	<!--td-->
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分流设置：
        	<select name="decflag_<bean:write name="node" property="nodeid"/>" onchange="decChange(this,'<bean:write name="node" property="nodeid"/>')">
        		<logic:equal name="node" property="decflag" value="0">
            		<option value="0" selected>固定值</option>
          			<option value="1">范围</option>
            	</logic:equal>
            	<logic:notEqual name="node" property="decflag" value="0">
            		<option value="0">固定值</option>
          			<option value="1" selected>范围</option>
            	</logic:notEqual>
              </select>
              </div>
            <!--/td> 
            </tr>
        	</table--> 
        </td>
        <td width="30%" class="wordtd_k2"><div align="center">流向</div></td>
      </tr>
      <tr style="display:none">
        <td><input type="checkbox" name="dec"/></td>
        <td>
        	<logic:equal name="node" property="decflag" value="0">
        	<div style="display:compact" name="dec_app_<bean:write name="node" property="nodeid"/>">分流选择：<input type="text" name="decselpath_<bean:write name="node" property="nodeid"/>" value=""></div>
        	<div style="display:none" name="dec_sco_<bean:write name="node" property="nodeid"/>">最小值：<input type="text" name="decminqty_<bean:write name="node" property="nodeid"/>" value="" size="15" style="text-align:right">&nbsp;最大值：<input type="text" name="decmaxqty_<bean:write name="node" property="nodeid"/>" value="" size="15" style="text-align:right"></div>
        	</logic:equal>
            <logic:notEqual name="node" property="decflag" value="0">
            <div style="display:none" name="dec_app_<bean:write name="node" property="nodeid"/>">分流选择：<input type="text" name="decselpath_<bean:write name="node" property="nodeid"/>" value=""></div>
        	<div style="display:compact" name="dec_sco_<bean:write name="node" property="nodeid"/>">最小值：<input type="text" name="decminqty_<bean:write name="node" property="nodeid"/>" value="" size="15" style="text-align:right">&nbsp;最大值：<input type="text" name="decmaxqty_<bean:write name="node" property="nodeid"/>" value="" size="15" style="text-align:right"></div>
            </logic:notEqual>	
          </td>
          <td> 
        	<select name="dectranpath_<bean:write name="node" property="nodeid"/>">
        		<logic:iterate name="node" property="tranlist" id="tran">
      			<option value="<bean:write name="tran" property="tranname"/>"><bean:write name="tran" property="tranname"/></option>
		    	</logic:iterate> 
            </select>
          </td>
      <input type="hidden" name="decext1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="node" property="nodename"/>">
      <input type="hidden" name="decext2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="flow" property="processdefinename"/>">
      <input type="hidden" name="decext3_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext4_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext5_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext6_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext7_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext8_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext9_<bean:write name="node" property="nodeid"/>" value="">
      <input type="hidden" name="decext10_<bean:write name="node" property="nodeid"/>" value="">
      </tr>
      </thead>
      <tbody>
      <logic:iterate name="node" property="declist" id="dec">     
      <tr>
       <td><input type="checkbox" name="dec"/></td>
        <td>
        	<logic:equal name="node" property="decflag" value="0">
        	<div style="display:compact" name="dec_app_<bean:write name="node" property="nodeid"/>">分流选择：<input type="text" name="decselpath_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="selpath" />"></div>
        	<div style="display:none" name="dec_sco_<bean:write name="node" property="nodeid"/>">最小值：<input type="text" name="decminqty_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="minqty" />" size="15" style="text-align:right">&nbsp;最大值：<input type="text" name="decmaxqty_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="maxqty"/>" size="15" style="text-align:right"></div>
        	</logic:equal>
            <logic:notEqual name="node" property="decflag" value="0">
            <div style="display:none" name="dec_app_<bean:write name="node" property="nodeid"/>">分流选择：<input type="text" name="decselpath_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="selpath" />"></div>
        	<div style="display:compact" name="dec_sco_<bean:write name="node" property="nodeid"/>">最小值：<input type="text" name="decminqty_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="minqty" />" size="15" style="text-align:right">&nbsp;最大值：<input type="text" name="decmaxqty_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="maxqty" />" size="15" style="text-align:right"></div>
            </logic:notEqual>	
          </td>
          <td> 
        	<select name="dectranpath_<bean:write name="node" property="nodeid"/>">
        		<option value="<bean:write name="dec" property="tranpath"/>"><bean:write name="dec" property="tranpath"/></option>	
        		<logic:iterate name="dec" property="tranlist" id="tran">
      			<option value="<bean:write name="tran" property="tranname"/>"><bean:write name="tran" property="tranname"/></option>
		    	</logic:iterate> 
            </select>
          </td>
      <input type="hidden" name="decext1_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext1"/>">
      <input type="hidden" name="decext2_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext2"/>">
      <input type="hidden" name="decext3_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext3"/>">
      <input type="hidden" name="decext4_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext4"/>">
      <input type="hidden" name="decext5_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext5"/>">
      <input type="hidden" name="decext6_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext6"/>">
      <input type="hidden" name="decext7_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext7"/>">
      <input type="hidden" name="decext8_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext8"/>">
      <input type="hidden" name="decext9_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext9"/>">
      <input type="hidden" name="decext10_<bean:write name="node" property="nodeid"/>" value="<bean:write name="dec" property="ext10"/>">
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
</html:form>
</logic:present>