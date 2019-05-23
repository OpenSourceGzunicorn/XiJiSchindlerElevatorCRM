<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/qualityCheckManagementAction.do?method=toRegistrationRecord" enctype="multipart/form-data">
	<html:hidden property="isreturn" />
	<html:hidden property="isdispose" />
	<html:hidden property="Scores"/>
	<html:hidden property="details"/>
	<html:hidden name="qualityCheckManagementBean" property="billno"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="elevatorNo" styleId="elevatorNo" size="30" styleClass="default_input" /></td>
			<td class="wordtd">电梯类型</td>
			<td>${elevaorTypeName}
	        <input type="hidden" name="elevatorType" id="elevatorType" value="${qualityCheckManagementBean.elevatorType }">
			</td>
			<td class="wordtd">现场督查人员:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="superviseId" style="border:0;" styleClass="default_input" readonly="true"/></td>
			</tr>
		<tr>
			
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="salesContractNo" /></td>
			<td class="wordtd">督查人员联系电话:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="supervisePhone" style="border:0;" styleClass="default_input" readonly="true"/></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.projectName"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="projectName" /></td>
		
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintDivision" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintStation" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.MaintPersonnel"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintPersonnel" /></td>
		
			<td class="wordtd">维保工联系电话:</td>
			<td><bean:write name="qualityCheckManagementBean" property="personnelPhone" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td>
				<html:hidden name="qualityCheckManagementBean" property="totalPoints"/>
				
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td><html:hidden name="qualityCheckManagementBean" property="scoreLevel"/></td>
		
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="checksDateTime" size="25" onfocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" styleClass="Wdate" readonly="true"/></td>
			<td class="wordtd">&nbsp;</td>
			<td>&nbsp;</td>
		</tr> 
		<tr>
			<td class="wordtd">参与维保人员:</td>
			<td colspan="5">
				<table>
					<%int ms=1; %>
	        		<logic:present name="msUserList">
	        		<tr>
						  <logic:iterate id="element" name="msUserList">
								<td width="5%" style="border: none;" >
									<logic:equal name="element" property="isok" value="N">
										<input type="checkbox" name="r5" value="${element.userid }"/>
									</logic:equal>
									<logic:notEqual name="element" property="isok" value="N">
										<input type="checkbox" name="r5" value="${element.userid }" checked="checked"/>
									</logic:notEqual>
									${element.username }
								</td>
							<% if(ms%8==0){ %></tr><tr><%} %>
							<%ms++; %>
						</logic:iterate>
						</tr>
					</logic:present>
	        	</table>
			</td>
		</tr> 
    </table>
    <br>
    <% int i=0; int j=0;%>
    <table id="mtc" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
      	<tr height="23">
          <td colspan="7">            
            &nbsp;<input type="button" value=" + " onclick="addElevators('searchMarkingScoreRegisterAction',this,'mtc','msId','')">
          <input type="button" value=" - " onclick="deleteRow(this)">
          </td>
        </tr>
        <tr class="wordtd_header">
        	<td width="3%"><input type="checkbox" id="checkAll" onclick="checkTableAll(this)"/></td>
			<td width="20%">评分名称</td>
			<td>评分明细</td>
			<td width="15%">备注</td>
			<td width="30%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="markingItemsComplyList">
          <logic:iterate id="element1" name="markingItemsComplyList">
          
          	<tr>
          	 <td align="center"><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
	         <td>
	         	${element1.msName}
	         	<input type="hidden" name="msId" id="msId" value="${element1.msId}"/>
	         	<input type="hidden" name="jnlno" id="jnlno" value="${element1.jnlno }"/>
	         	<input type="hidden" name="msName" id="msName" value="${element1.msName }"/>
	         	<input type="hidden" name="fraction" id="fraction" value="${element1.fraction}"/>
	         </td>
	         <td valign="top"><%-- ${element1.checkItem}<input type="hidden" name="checkItem" id="checkItem" value="${element1.checkItem}"/> --%>
	         	<table width="100%" class="tb" id="msrd<%=j%>">
	         	<thead>
	         		<tr class="wordtd">
	         			<td width="5%" align="center"><input type="checkbox" onclick="checkTableAll(this)"></td>
	         			<td align="left">
	         				&nbsp;&nbsp;<input type="button" value=" + " onclick="addElevators('searchMarkingScoreRegisterDetailAction',this,'msrd<%=j%>','detailId','${element1.msId}')"/>
	         				&nbsp;<input type="button" value=" - " onclick="deleteRow(this)">
	         			</td>
	         		</tr>
	         	</thead>
	         	<tbody>
	         		<logic:present name="element1" property="detailList">
	         			<logic:iterate id="e" name="element1" property="detailList">
	         				<tr>
	         					<td width="5%" align="center"><input type="checkbox" onclick="cancelCheckAll(this)"></td>
	         					<td>
	         						${e.detailName}<input type="hidden" name="detailId" value="${e.detailId}"/>
	         						<input type="hidden" name="detailName" value="${e.detailName}"/>
	         						<input type="hidden" name="msId" value="${e.msId}"/>
	         						<input type="hidden" name="jnlno" value="${e.jnlno}"/>
	         					</td>
	         				</tr>
	         			</logic:iterate>
	         		</logic:present>
	         	</tbody>
	         	</table>
	         </td>
	         <td align="center"><%-- <input type="text" name="fraction" id="fraction" value="${element1.fraction}" size="3" readonly="readonly" class="noborder" /> --%>
	         	<textarea name="rem" id="rem" rows="3" cols="20">${element1.rem}</textarea>
	         </td>
	         <td>
	         	<table width="100%" class="tb">
				<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableAll(this)"></td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="AddRow(this,'${element1.msId}')"/>
				&nbsp;<input type="button" name="todelrow" value=" -  " onclick="deleteRow(this)">
				</td></tr>
				<logic:present name="element1" property="fileList">
					<logic:iterate id="element2" name="element1" property="fileList">
						<tr><td>&nbsp;</td>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}')">${element2.oldFileName}</a>
				      	<a style="cursor:hand;" onclick="deleteFile(this,'${element2.fileSid}')"><img src="../../common/images/toolbar/del_attach.gif" alt="<bean:message key="delete.button.value"/>" /></a>
				      	<%i++; %>
						</td></tr>
					</logic:iterate>
				</logic:present> 
				</table><br>
				<%j++; %>
	         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
      </tbody>    
    </table>
    <div id="caption_1" style="width: 100%;padding-top: 0;padding-bottom: 2;border-top:0 none #ffffff;border-bottom: 1 solid #999999;" class="tb">
</div>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td><html:textarea name="qualityCheckManagementBean" property="supervOpinion" rows="4" cols="100" /></td>
      </tr>
    </table>
    <br>
    <input type="hidden" name="file_length" value="<%=i %>" />
    <input type="hidden" name="table_length" value="<%=j %>"/>
<script language="javascript">
	window.onload=function(){
		var file_length=document.getElementsByName("file_length")[0];
		var table_length=document.getElementsByName("table_length")[0];
		fileGird=parseInt(file_length.value);
		detail=parseInt(table_length.value);
		isQualified();
		partitionGrade();
	}
</script>
</html:form>