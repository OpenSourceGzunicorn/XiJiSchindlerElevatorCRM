<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ page import="com.gzunicorn.common.util.SysConfig"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
    <td>       
     	 急修编号:
      </td>
      <td>
        <html:text property="property(calloutMasterNo)" styleClass="default_input" />
      </td> 
      <td>      
         &nbsp;&nbsp;报修单位名称:
      </td>
      <td>
        <html:text property="property(companyId)"  styleClass="default_input" />
      </td>
        <td>      
       &nbsp;&nbsp;起草人:
      </td>
      <td>
        <html:text property="property(operId)"  styleClass="default_input" />
      </td>     
      
	<td>
         &nbsp;&nbsp;起草时间:
	</td>
   		<td>
   		<html:text property="property(sdate1)" styleClass="Wdate" size="13" onfocus="WdatePicker({isShowClear:true})"/>
		- 
		<html:text property="property(edate1)" styleClass="Wdate" size="13" onfocus="WdatePicker({isShowClear:true})"/>
	</td>
    </tr>
    <tr> 
        <td> 
       	处理状态:
      </td>
      <td>
        <html:select property="property(handleStatus)">
        <html:option value="%">请选择</html:option>
        <html:option value="">&nbsp;</html:option>
        <html:options collection="PulldownList" property="id.pullid" labelProperty="pullname"/>
        </html:select>
      </td>                     
      <td>       
        &nbsp;&nbsp;提交标志:
      </td>
      <td>
        <html:select property="property(SubmitType)">
        <html:option value="%">请选择</html:option>
        <html:option value="Y">已提交</html:option>
        <html:option value="N">未提交</html:option>
        </html:select>
      </td>  
       <td> 
       &nbsp;&nbsp;维保站:
      </td>
      <td>
        <html:select property="property(maintStation)">
        <%--html:option value="%">请选择</html:option--%>
        <html:options collection="storageidList" property="storageid" labelProperty="storagename"/>
        </html:select>
      </td>    
      
      <td>      
         &nbsp;&nbsp;维修人员:
      </td>
      <td>
        <html:text property="property(r5)"  styleClass="default_input" />
      </td>
    </tr>
    <tr> 
        <td> 
       报修方式:
      </td>
      <td>
        <html:select property="property(repairMode)">
        <html:option value="%">请选择</html:option>
        <html:options collection="rmList" property="id.pullid" labelProperty="pullname"></html:options>
        </html:select>
      </td>                     
      <td>       
        &nbsp;&nbsp;服务对象:
      </td>
      <td>
        <html:select property="property(serviceObjects)">
        <html:option value="%">请选择</html:option>
        <html:options collection="soList" property="id.pullid" labelProperty="pullname"></html:options>
        </html:select>
      </td> 
      <td>      
         &nbsp;&nbsp;报修电梯编号:
      </td>
      <td>
        <html:text property="property(elevatorNo)"  styleClass="default_input" />
      </td>     
    </tr>
  </table>
  <br>
  <table:table id="guiCalloutMaster" name="CalloutMasterList">
    <logic:iterate id="element" name="CalloutMasterList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.calloutMasterNo }" /> 
        <input type="hidden"  name="submitType" id="submitType" value="${element.submitType }"> 
        <input type="hidden"  name="calloutMasterNo" id="calloutMasterNo" value="${element.calloutMasterNo }"> 
        <input type="hidden"  name="hisStop" id="hisStop" value="${element.isStop }">    
      </table:define>
      <table:define id="c_calloutMasterNo">
       <a href="<html:rewrite page="/hotphoneAction.do"/>?typejsp=display&id=${element.calloutMasterNo }">${element.calloutMasterNo }</a>     
      </table:define>
      <table:define id="c_companyId">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.companyName }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.companyName }
	  </logic:notEqual>
      </table:define>
      <table:define id="c_elevatorNo">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.elevatorNo }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.elevatorNo }
	  </logic:notEqual>
      </table:define>
      <table:define id="c_operName">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.operName }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.operName }
	  </logic:notEqual>    
      </table:define>
      <table:define id="c_operDateLayout">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.operDate }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.operDate }
	  </logic:notEqual>    
      </table:define>
      <table:define id="c_handleStatus">
      <html:hidden name="element" property="handleStatus"/>
      <logic:equal name="element" property="handle" value="Y"> 
	      <logic:equal name="element" property="handleStatus" value="3">
		  		<font color="red">${element.handleStatusName }</font>
		  </logic:equal>
		  <logic:notEqual name="element" property="handleStatus" value="3">
		  		${element.handleStatusName }
		  </logic:notEqual>          
      </logic:equal>
      
		     <logic:notEqual name="element" property="handle" value="Y">
			     <logic:equal name="element" property="handleStatus" value="5">
			     
      				<logic:equal name='<%=SysConfig.TOOLBAR_RIGHT%>' property="nhotphone" value="Y"> 
					     <input type="button" name="sh" id="sh" value="急修审核" width="30px" onclick="toexamine(this,'sh')" class="default_input"/>
					     <input type="hidden" name="calloutMasterNojx" id="calloutMasterNo" value="${element.calloutMasterNo }"/>
			     	</logic:equal>
			     	<logic:notEqual name='<%=SysConfig.TOOLBAR_RIGHT%>' property="nhotphone" value="Y"> 
			     		${element.handleStatusName }
			     	</logic:notEqual>
			     	
			     </logic:equal>
			     <logic:equal name="element" property="handleStatus" value="6">
			     
      				<logic:equal name='<%=SysConfig.TOOLBAR_RIGHT%>' property="nhotphone" value="Y"> 
					     <input type="button" name="sh" id="sh" value="回访评审" width="30px" onclick="toexamine(this,'ps')" class="default_input"/>
					     <input type="hidden" name="calloutMasterNojx" id="calloutMasterNo" value="${element.calloutMasterNo }"/>
			     	</logic:equal>
			     	<logic:notEqual name='<%=SysConfig.TOOLBAR_RIGHT%>' property="nhotphone" value="Y"> 
			     		${element.handleStatusName }
			     	</logic:notEqual>
			     	
			     </logic:equal>
		     </logic:notEqual>
	     
	     
      </table:define>
      <table:define id="c_isTrap">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.isTrap }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 ${element.isTrap }
	  </logic:notEqual>    
      </table:define>
      <table:define id="c_completeTime">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red"> ${element.completeTime }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.completeTime }
	  </logic:notEqual>    
      </table:define>
      <table:define id="c_isStop">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red"> ${element.isStop }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	  ${element.isStop }
	  </logic:notEqual>   
      </table:define>
      <table:define id="c_submitType">
      <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red"> ${element.submitType }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 ${element.submitType }
	  </logic:notEqual>         
      </table:define>
       <table:define id="c_repairMode">
       <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.repairMode }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 ${element.repairMode }
	  </logic:notEqual>         
      </table:define>
       <table:define id="c_serviceObjects">
       <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.serviceObjects } </font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 ${element.serviceObjects } 
	  </logic:notEqual>
        
      </table:define>
       <table:define id="c_maintStation">
        <logic:equal name="element" property="handleStatus" value="3">
	  <font color="red">${element.maintStation } </font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 ${element.maintStation }
	  </logic:notEqual>
         
      </table:define>
      
      <table:define id="c_R5">
      <logic:equal name="element" property="handleStatus" value="3">
	  		<font color="red">${element.r5name }</font>
	  </logic:equal>
	  <logic:notEqual name="element" property="handleStatus" value="3">
	 		${element.r5name }
	  </logic:notEqual>    
      </table:define>
      
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>
<script type="text/javascript">
function toexamine(obj,value){
	var  shs=document.getElementsByName("sh");
	var  calloutMasterNojxs=document.getElementsByName("calloutMasterNojx");
	for(var i=0;i<shs.length;i++){
		if(shs[i]==obj){
			window.location='<html:rewrite page="/hotphoneAction.do"/>?typejsp='+value+'&id='+calloutMasterNojxs[i].value;
			return;
		}
	}
}
</script>