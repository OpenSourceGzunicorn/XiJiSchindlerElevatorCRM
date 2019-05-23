<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
	<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
	<script language="javascript" src="<html:rewrite page="/common/javascript/DigitalToChinese.js"/>"></script>
  <link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>

<html:form action="/factoryPassRateReportAction.do?method=toSearchResults">

<input type="hidden" name="departmentstr" id="departmentstr" value="${rmap.departmentstr }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<input type="hidden" name="projectprovince" id="projectprovince" value="${rmap.projectprovince }"/>
<input type="hidden" name="ishuizong" id="ishuizong" value="${rmap.ishuizong }"/>
<html:hidden property="genReport" styleId="genReport" />
 
 ${searchcontent }
 <br/><br/>

<table class="tb"  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">

<logic:present name="resultlist">
<logic:iterate id="e1eresult" name="resultlist">

	<logic:present name="e1eresult" property="totalList">
		<tr>
			<td nowrap="nowrap" rowspan="16" align="center">&nbsp;${e1eresult.departmentName}&nbsp;</td>
		</tr>
		
		<tr>
			<td nowrap="nowrap" rowspan="5" align="center">&nbsp;直梯&nbsp;</td>
		</tr>
		<tr class="wordtd_header" id="thead">
			<td nowrap="nowrap"  style="text-align: center;">合同性质</td>	
			<td nowrap="nowrap"  style="text-align: center;">厂检项目数</td>
			<td nowrap="nowrap"  style="text-align: center;">厂检总台量</td>
			<%int j=1; %>
		  	<logic:present name="maxList">
		   		<logic:iterate id="e1" name="maxList">
		   		<%if(j==1){ %>
		   			<td nowrap="nowrap"  style="text-align: center;">初检台量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检不合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">监改合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检总合格率</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格率</td>
		   		<%}else{ %>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=j%>)+'检台量');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=j%>)+'检合格量');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=j%>)+'检不合格量');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=j%>)+'检合格率');</script></td>
		   		<%} %>
		   		<%j++; %>
		   		</logic:iterate>
		   	</logic:present>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">大包合同</td>
			<logic:present name="e1eresult" property="tDList">
				<logic:iterate id="element1" name="e1eresult" property="tDList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element1" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element1" property="elevator"/></td>
					<logic:present name="element1" property="factoryList">
						<%int jg=1; %>
						  <logic:iterate id="e1" name="element1" property="factoryList" indexId="a">
						  <%if(jg==1){ %>
						      <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="factory${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="nofactory${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="unqualified${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="nojgfactory${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="rate${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="ratejg${a}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="factory${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="nofactory${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="unqualified${a}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e1" property="rate${a}"/></td>
						  <%} %>
		   				  <%jg++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="tDList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
		
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">设备合同</td>
			<logic:present name="e1eresult" property="tSList">
				<logic:iterate id="element2" name="e1eresult" property="tSList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element2" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element2" property="elevator"/></td>
					<logic:present name="element2" property="factoryList">
						<%int jg1=1; %>
						  <logic:iterate id="e2" name="element2" property="factoryList" indexId="b">
						  	<%if(jg1==1){ %>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="factory${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="nofactory${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="unqualified${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="nojgfactory${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="rate${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="ratejg${b}"/></td>
						 	<%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="factory${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="nofactory${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="unqualified${b}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e2" property="rate${b}"/></td>
						 	<%} %>
		   				  	<%jg1++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="tSList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm1=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm1==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm1++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
		
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">合计</td>
			<logic:present name="e1eresult" property="tList">
				<logic:iterate id="element3" name="e1eresult" property="tList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element3" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element3" property="elevator"/></td>
					<logic:present name="element3" property="factoryList">
						<%int jg2=1; %>
						  <logic:iterate id="e3" name="element3" property="factoryList" indexId="c">
						  	<%if(jg2==1){ %>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="factory${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="nofactory${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="unqualified${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="nojgfactory${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="rate${c}"/></td>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="ratejg${c}"/></td>
						  	<%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="factory${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="nofactory${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="unqualified${c}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e3" property="rate${c}"/></td>
						  	<%} %>
		   				  	<%jg2++; %>
		   				  	</logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="tList">
				<td>0</td><td>0</td>
	 				<logic:present name="maxList">
					<%int jgm2=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm2==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm2++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
	 	</tr>
		
		<tr>
			<td nowrap="nowrap" rowspan="5" align="center">&nbsp;扶梯&nbsp;</td>
		</tr>
		<tr class="wordtd_header" id="thead">
			<td nowrap="nowrap"  style="text-align: center;">合同性质</td>	
			<td nowrap="nowrap"  style="text-align: center;">厂检项目数</td>
			<td nowrap="nowrap"  style="text-align: center;">厂检总台量</td>
			<%int k=1; %>
		  	<logic:present name="maxList">
		   		<logic:iterate id="e1" name="maxList">
		   		<%if(k==1){ %>
		   			<td nowrap="nowrap"  style="text-align: center;">初检台量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检不合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">监改合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检总合格率</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格率</td>
		   		<%}else{ %>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=k%>)+'检台数');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=k%>)+'检合格台数');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=k%>)+'检不合格量');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=k%>)+'检合格率');</script></td>
		   		<%} %>
		   		<%k++; %>
		   		</logic:iterate>
		   	</logic:present>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">大包合同</td>
			<logic:present name="e1eresult" property="fDList">
				<logic:iterate id="element4" name="e1eresult" property="fDList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element4" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element4" property="elevator"/></td>
					<logic:present name="element4" property="factoryList">
					<%int jg3=1; %>
						  <logic:iterate id="e4" name="element4" property="factoryList" indexId="d">
						  <%if(jg3==1){ %>
						    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="factory${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="nofactory${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="unqualified${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="nojgfactory${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="rate${d}"/></td>
						    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="ratejg${d}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="factory${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="nofactory${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="unqualified${d}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e4" property="rate${d}"/></td>
						  <%} %>
						  <%jg3++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="fDList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm3=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm3==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm3++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">设备合同</td>
			<logic:present name="e1eresult" property="fSList">
			<logic:iterate id="element5" name="e1eresult" property="fSList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element5" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element5" property="elevator"/></td>
					<logic:present name="element5" property="factoryList">
					<%int jg4=1; %>
						  <logic:iterate id="e5" name="element5" property="factoryList" indexId="f">
						  <%if(jg4==1){ %>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="factory${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="nofactory${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="unqualified${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="nojgfactory${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="rate${f}"/></td>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="ratejg${f}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="factory${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="nofactory${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="unqualified${f}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e5" property="rate${f}"/></td>
						  <%} %>
						  <%jg4++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="fSList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm4=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm4==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm4++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">合计</td>
			<logic:present name="e1eresult" property="fList">
				<logic:iterate id="element6" name="e1eresult" property="fList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element6" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element6" property="elevator"/></td>
					<logic:present name="element6" property="factoryList">
					<%int jg5=1; %>
						  <logic:iterate id="e6" name="element6" property="factoryList" indexId="g">
						  <%if(jg5==1){ %>
						      <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="factory${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="nofactory${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="unqualified${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="nojgfactory${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="rate${g}"/></td>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="ratejg${g}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="factory${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="nofactory${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="unqualified${g}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e6" property="rate${g}"/></td>
						  <%} %>
						  <%jg5++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="fList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm5=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm5==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm5++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
	  	</tr>
	  	
		<tr>
			<td nowrap="nowrap" rowspan="5"  align="center">&nbsp;合计&nbsp;</td>
		</tr>
		<tr class="wordtd_header" id="thead">
			<td nowrap="nowrap"  style="text-align: center;">合同性质</td>	
			<td nowrap="nowrap"  style="text-align: center;">厂检项目数</td>
			<td nowrap="nowrap"  style="text-align: center;">厂检总台量</td>
			<%int l=1; %>
		  	<logic:present name="maxList">
		   		<logic:iterate id="e1" name="maxList">
		   		<%if(l==1){ %>
		   			<td nowrap="nowrap"  style="text-align: center;">初检台量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检不合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">监改合格量</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检总合格率</td>
		   			<td nowrap="nowrap"  style="text-align: center;">初检合格率</td>
		   		<%}else{ %>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=l%>)+'检台数');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=l%>)+'检合格台数');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=l%>)+'检不合格量');</script></td>
		   			<td nowrap="nowrap"  style="text-align: center;"><script>document.write(NumberToChinese(<%=l%>)+'检合格率');</script></td>
		   		<%} %>
		   		<%l++; %>
		   		</logic:iterate>
		   	</logic:present>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">大包合同</td>
			<logic:present name="e1eresult" property="dList">
				<logic:iterate id="element7" name="e1eresult" property="dList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element7" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element7" property="elevator"/></td>
					<logic:present name="element7" property="factoryList">
					<%int jg6=1; %>
						  <logic:iterate id="e7" name="element7" property="factoryList" indexId="h">
						  <%if(jg6==1){ %>
						    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="factory${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="nofactory${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="unqualified${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="nojgfactory${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="rate${h}"/></td>
						    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="ratejg${h}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="factory${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="nofactory${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="unqualified${h}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e7" property="rate${h}"/></td>
						  <%} %>
						  <%jg6++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="dList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm6=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm6==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm6++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">设备合同</td>
			<logic:present name="e1eresult" property="sList">
				<logic:iterate id="element8" name="e1eresult" property="sList">
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element8" property="salesContractNo"/></td>
					<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element8" property="elevator"/></td>
					<logic:present name="element8" property="factoryList">
					<%int jg7=1; %>
						  <logic:iterate id="e8" name="element8" property="factoryList" indexId="i">
						  <%if(jg7==1){ %>
						    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="factory${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="nofactory${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="unqualified${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="nojgfactory${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="rate${i}"/></td>
						  	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="ratejg${i}"/></td>
						  <%}else{ %>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="factory${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="nofactory${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="unqualified${i}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e8" property="rate${i}"/></td>
						  <%} %>
						  <%jg7++; %>
						  </logic:iterate>
					  </logic:present>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="e1eresult" property="sList">
				<td>0</td><td>0</td>
				<logic:present name="maxList">
					<%int jgm7=1; %>
		 				<logic:iterate id="e1" name="maxList">
		 				<%if(jgm7==1){ %>
		 					<td>0</td><td>0</td><td>0</td><td>0</td><td>0.0%</td><td>0.0%</td>
		 				<%}else{ %>
		 					<td>0</td><td>0</td><td>0</td><td>0.0%</td>
		 				<%} %>
		   				<%jgm7++; %>
		 				</logic:iterate>
		 		</logic:present>
			</logic:notPresent>
		</tr>
	
		<tr class="inputtd" align="center" height="20">
			<td nowrap="nowrap"  style="text-align: center;">总计</td>
			<logic:iterate id="element9" name="e1eresult" property="totalList">
				<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element9" property="salesContractNo"/></td>
				<td nowrap="nowrap"  style="text-align: center;"><bean:write name="element9" property="elevator"/></td>
				<logic:present name="element9" property="factoryList">
				<%int jg8=1; %>
					  <logic:iterate id="e9" name="element9" property="factoryList" indexId="n">
					  <%if(jg8==1){ %>
					    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="factory${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="nofactory${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="unqualified${n}"/></td>
							  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="nojgfactory${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="rate${n}"/></td>
					    <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="ratejg${n}"/></td>
					  <%}else{ %>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="factory${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="nofactory${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="unqualified${n}"/></td>
						  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="e9" property="rate${n}"/></td>
					  <%} %>
					  <%jg8++; %>
					  </logic:iterate>
				  </logic:present>
			</logic:iterate>
		</tr>
	</logic:present>

</logic:iterate>
</logic:present>

<logic:notPresent name="resultlist">
	 <tr class="noItems" align="center" height="20"><td>没有记录显示！</td></tr>
</logic:notPresent>

</table>
		
</html:form>


