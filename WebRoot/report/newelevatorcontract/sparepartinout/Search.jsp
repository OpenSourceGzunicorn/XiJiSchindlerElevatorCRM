<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='AJAX'/>"></script>
<style type="text/css">
<!--
.add{font-family:Arial,"宋体";
    font-size:9pt;
	font-weight:bold;
	color: #ffffff;
	background-color:#9E0B0E;
	padding-right:13px;
	padding-bottom:2px;
	padding-left:13px;
	}
body {
	background-color: #F4F2F5;
}
.style5 {
	color: #9e0b0e;
	font-weight: bold;
}
input
{
	border:1px solid #666666;
	height:18px;
	font-size:12px;
}
.leftbottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid; background-image:url(images/botton_bg.gif); padding-top:2px; height:20px;}
.bottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid;padding-top:3px; background-color:#D4D0C8; height:20px;}
-->
</style>
<html:form action="/sparepartInOutReportAction.do?method=toSearchRecord">
	<html:hidden property="property(genReport)" styleId="genReport"value="" />
	<table width="98%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
    <tr>
     <td height="23" colspan="4" background="../../common/images/bg1.gif">&nbsp;</td>
    </tr>
         <tr>
		<td height="30" bgcolor="#81A5C7"><div align="right">物料出入库日期:&nbsp;</div></td>
       	<td colspan="3" bgcolor="#D2E7FC">
        从<html:text property="property(date1)" styleId="date1" onclick="setday(this)"/>
		       到:<html:text property="property(date2)" onclick="setday(this)" styleId="date2" /></td>
        </td>
		</tr>
		<tr>
		<td height="30" bgcolor="#81A5C7"><div align="right">所属仓库:&nbsp;</div></td>
       	<td colspan="3" bgcolor="#D2E7FC">
    	 	<html:select property="property(storageid)">
    	 	<html:option value="">请选择</html:option>
    	<html:options collection="StorageSetupList" property="storageid" labelProperty="storagename"/>
    </html:select>
	</td>  
  </tr>		
   <tr>
    <td height="23" colspan="4"  background="../../common/images/bg.gif">&nbsp;</td>
    </tr>
 </table>
</html:form>
<script language="javascript">
var showid;
function ChangeCenterLine(key,value){
        if(key==""){
        	var mshi=document.getElementById(value);
        	mshi.options.length=0;
        	return null;
        }
       
        showid=value;
		var detailRequest = new AjaxRequest();
		var url = '/XJSCRM/sparepartOutReportAction.do?method=toProductSort';
		//alert(url);
		url+="&key="+key;
		detailRequest.url = url;
		detailRequest.method="POST";
		detailRequest.onComplete = toProductSort;
		detailRequest.process();
}
	
// 读取回调函数。  
function toProductSort(req){
	var xmlDOM = req.responseXML;
 
	var obj=document.getElementById(showid);
 	
	//qingkong chengshiduixiang nrong
	obj.options.length=0;
    var rowNodes=xmlDOM.getElementsByTagName('rows');
 
	if(rowNodes!=null){
		var rowLength=rowNodes.length;
		for(var i=0;i<rowLength;i++){
			var colNodes=rowNodes[i].childNodes;
			if(colNodes!=null){ 
				var  opt =   new   Option(colNodes[1].text,colNodes[0].text);   
				obj.options.add(opt);   
			} 
		}
	}
}
</script>