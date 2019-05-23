<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/common/javascript/numberToCN_number.js"/>"></script>
<html:errors />
</head>
<br>

<html:form action="/PullDownAction.do?method=toUploadFile" enctype="multipart/form-data">

	<table id="uploadtab_1" width="99%" border="0">	
	<tr><td colspan="2" >
		日期 
		<%-- 
		<html:text property="property(vcdates)" size="12" styleClass="default_input" readonly="true"/>
		<img onclick="WdatePicker({el:'property(vcdates)'})" src="../../common/DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle">
		--%>
		<html:text property="r1" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>

	</td></tr>
	<tr align="left" >
	<td width="30%">分部门:<html:select property="comid" onchange="Evenmore(this,'Storage','td1')">
	<html:option value="">请选择</html:option>
    <html:options collection="CompanyList" property="comid" labelProperty="comname"/>
	</html:select>
	</td>
	<td  style="display:none" id="td1" >
	部门名称:<select name="Storage" id="Storage"></select>
	</td>
	</tr>
	<td>金额转换中文:<input type="text" onblur="Number_to_Chinese2(this.value,'CNnumber')"></td>
	<td><input type="text" id="CNnumber" size="30"></td>
	<tr>
	</tr>
	<tr >	
	<td colspan="2">
		<!-- 已上传的附件 -->
		<%@ include file="UpLoadedFile.jsp" %>
	</td>
	</tr>
	<tr>
	<td colspan="2">
		<!-- 上传的附件 -->
		<%@ include file="UpLoadFile.jsp" %>
	</td>	
	</table>
	<br>
	<table>
	</tr>
	</table>
<script language="javascript">
var req;
function Evenmore(obj,listname,td){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
	 if(window.XMLHttpRequest) {
		 req = new XMLHttpRequest();
	 }else if(window.ActiveXObject) {
		 req = new ActiveXObject("Microsoft.XMLHTTP");
	 }  //生成response
	 var url='<html:rewrite page="/PullDownAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
	 req.open("post",url,true);//post 异步
	 req.onreadystatechange=function getnextlist(){
		
			if(req.readyState==4 && req.status==200){
			 var xmlDOM=req.responseXML;
			 var rows=xmlDOM.getElementsByTagName('rows');
			 if(rows!=null){
			     	selectfreeid.options.length=0; 
			 		selectfreeid.add(new Option("请选择",""));
			 		for(var i=0;i<rows.length;i++){
						var colNodes = rows[i].childNodes;
						if(colNodes != null){
							var colLen = colNodes.length;
							for(var j=0;j<colLen;j++){
								var freeid = colNodes[j].getAttribute("name");
								var freename = colNodes[j].getAttribute("value");
								selectfreeid.add(new Option(freeid,freename));
				            }
			             }
			 		}
			 		}
			
			}
			document.getElementById(td).style.display="";
	 };//回调方法
	 req.send(null);//不发送
	}else{		
		selectfreeid.parentNode.innerHTML="";
	}
}

</script>
</html:form>

