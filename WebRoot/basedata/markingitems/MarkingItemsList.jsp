<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr>
			<td>
				<bean:message key="markingitems.mtId" />
				:
			</td>
			<td>
				<html:text property="property(msId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="markingitems.mtName" />
				:
			</td>
			<td>
				<html:text property="property(msName)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				电梯类型
				:
			</td>
			<td>
				<html:select property="property(elevatorType)">
					<html:option value="">请选择</html:option>
					<html:options collection="elevaorTypeList" property="id.pullid" labelProperty="pullname"/>
				</html:select>
			</td>
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="markingitems.enabledflag" />
				:
			</td>
			<td>
				<html:select property="property(enabledFlag)">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select>
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiMarkingScore" name="markingScoreList">
		<logic:iterate id="element" name="markingScoreList">
			<table:define id="c_cb">
			<bean:define id="msId" name="element" property="msId" />
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox>  --%>
				
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=msId.toString()%>" />
			</table:define>
			<table:define id="c_msId">
				<a href="<html:rewrite page='/markingScoreAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="msId"/>">
				<bean:write name="element" property="msId" />
				</a>
			</table:define>
			<table:define id="c_msName">
				<bean:write name="element" property="msName"/>
			</table:define>
			<table:define id="c_fraction">
				<bean:write name="element" property="fraction" />
			</table:define>
			<table:define id="c_elevatorType">
				<bean:write name="element" property="elevatorType" />
			</table:define>
			
			<table:define id="c_enabledFlag">
				<logic:match name="element" property="enabledFlag" value="Y">
					<bean:message key="pageword.yes" />
				</logic:match>
				<logic:match name="element" property="enabledFlag" value="N">
					<bean:message key="pageword.no" />
				</logic:match>
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
	
	<script type="text/javascript">
		$("document").ready(function(){
			//防止标题换行
			$($(".headerRow3")[0].cells).each(function(){				
				$(this).html("<nobr>"+$(this).html()+"</nobr>");
			});			
		})
	</script>
</html:form>