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
<html:form action="/flowAction.do?method=toSetShrRecord">
<html:hidden property="isreturn"/>
<input type="hidden" name="id" value="<bean:write name="pid" />">
<table width="99%" class="tb">
<tr>
<td width="10%" class="wordtd">流程ID</td>
    <td width="15%"><input type="hidden" name="processdefinename" id="processdefinename" value="${processdefinename }"/>${processdefinename }</td>
    <td width="10%" class="wordtd">流程名称</td>
    <td width="18%">
    <input type="hidden" name="processdefinealiasname" value="${processdefinealiasname }">${processdefinealiasname }
    </td>
    <td width="10%" class="wordtd">版本</td>
    <td width="12%"><input type="hidden" name="version" value="${version }">${version }</td>
</tr>
  <tr><td width="10%" class="wordtd"><input type="checkbox" name="qxz" id="qxz" onclick="setall(this,'comid');">全选&nbsp;</input>流程配置公司:</td>
  <td colspan="5">
  <logic:iterate id="ele" name="Companylist"><input type="checkbox" name="comid" id="comid" value="${ele.comid}"/>${ele.comfullname} </logic:iterate>
  </td>
  </tr>
</table>
<br/>
<table width="99%" class="tb">
  <tr>
    <td class="wordtd" width="50%"><div align="center">结点信息</div></td>
    <td class="wordtd" width="50%"><div align="center">扩展设置</div></td>
  </tr>
  <logic:iterate name="nodeext" id="node">
  <tr>
  	<td valign="top">
  	<table><tr>
  	<td class="wordtd" width="15%" nowrap><div align="center">结点ID</div></td><td width="30%"><html:hidden name="node" property="nodeid" write="true"/></td>
    <td class="wordtd" width="15%" nowrap><div align="center">结点名称</div></td><td width="40%"><html:hidden name="node" property="nodename" write="true"/></td>
    </tr>
    </table>
    </td>
    <td valign="top">

    <%-- div style="height:102px; width:100%; overflow:auto"--%>
    <table width="100%" class="tb" id="act_<bean:write name="node" property="nodeid"/>">
      <thead>
      <%-- tr style="position: relative; top: expression(this.offsetParent.scrollTop);"--%>
      <tr>
      	<td class="wordtd">
      	    <input type="checkbox" name="qxr" id="qxr" onclick="setall(this,'username_<bean:write name="node" property="nodeid"/>')" width="10%"/>全选&nbsp;
      		<input type="button" name="adduser" class="default_bottom" id="adduser" style="width:30px" value="＋" onclick="setSel('act_<bean:write name="node" property="nodeid"/>')" />
			<input type="button" name="deluser" class="default_bottom" id="deluser" style="width:30px" value="－" onclick="del(act_<bean:write name="node" property="nodeid"/>)" />
		</td>
        <td class="wordtd"><div align="center">用户</div></td>
        <td class="wordtd"><div align="center">所属分部</div></td>
      </tr>
      <tr style="display:none">
      	<td><input type="checkbox" name="username_<bean:write name="node" property="nodeid"/>"></td>
        <td>
        	<input type="text" name="username_<bean:write name="node" property="nodeid"/>" id="username" style="border=0;" readonly="readonly"/>
        	<input type="hidden" name="userid_<bean:write name="node" property="nodeid"/>" id="userid"/>
        </td>
        <td>
        	<input type="text" name="grcname_<bean:write name="node" property="nodeid"/>" id="grcname" style="border=0;" readonly="readonly" size="30"/>
        	<input type="hidden" name="grcid_<bean:write name="node" property="nodeid"/>" id="grcid"/>
        </td>
      </tr>
      </thead>
      <tbody>
      <logic:iterate name="node" property="list3"  id="ele">
      <logic:notEqual name="ele" property="username_${ node.nodeid}" value="">
      <tr>
      	<td>
      	    <input type="checkbox" name="username_<bean:write name="node" property="nodeid"/>">
      	</td>
      	<td>
        	<input type="text" name="username_<bean:write name="node" property="nodeid"/>" id="username" readonly="readonly" style="border=0;" value='<bean:write name="ele"   property="username_${ node.nodeid}"/>'/>
        	<input type="hidden" name="userid_<bean:write name="node" property="nodeid"/>" id="userid" value='<bean:write name="ele" property="userid_${ node.nodeid}"/>'/>
        </td>
        <td>
            <input type="hidden" name="grcid_<bean:write name="node" property="nodeid"/>" id="grcid" value='<bean:write name="ele" property="grcid_${ node.nodeid}"/>'>
        	<input type="text" name="grcname_<bean:write name="node" property="nodeid"/>" id="grcname" value='<bean:write name="ele"  property="grcname_${ node.nodeid}"/>'  style="border=0;" readonly="readonly" size="30"/>
        </td>	
      </tr>
      </logic:notEqual>
      </logic:iterate>
      
      </tbody>
    </table>
    <%-- /div--%>
    <br/>

    <br/>
    </td>
  </tr>
  </logic:iterate>
</table>
</html:form>
 <script type="text/javascript">
 function settypeid(){

	 var iddds=document.getElementsByName("comid");
	<logic:present name="list2">
    <logic:iterate id="eleid" name="list2" >            
		var typeid2='<bean:write name="eleid"/>';
		for(var j=0;j<iddds.length;j++){
			 //alert(typeid2+"=="+iddds[j].value);
			  if(typeid2==iddds[j].value){
				  iddds[j].checked=true;
				  break;
			  }
		  }
	</logic:iterate>
	</logic:present>	
 }
 function setall(obj,name){
	 var bb=document.getElementsByName(name);
	 for(var i=bb.length-1;i>=0;i--){
		 bb[i].checked=obj.checked;
	 }
 }
 settypeid();
</script>





