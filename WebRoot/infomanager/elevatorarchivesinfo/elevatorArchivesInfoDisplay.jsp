<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script type="text/javascript">
//下载附件
		function downloadFile(name){
			var uri = '<html:rewrite page="/elevatorArchivesInfoAction.do"/>?method=toDownloadFileRecord';
 			uri +='&filesname='+ name;
 			uri +='&folder=ElevatorArchivesInfo.file.upload.folder';
			window.location = uri;
			//window.open(url);
		}
</script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<br>
<html:errors/>
<logic:present name="elevatorArchivesInfoBean">
  <table width="100%" height="24" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr class="td_2">
      <td width="2" background="/images/line.gif"></td>
      <td width="300" class="tab-on" id="navcell" onclick="switchCell('0')" >基础信息</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>

      <td width="300" class="tab-off" id="navcell" onclick="switchCell('1')">故障记录</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>
      
      <td width="300" class="tab-off" id="navcell" onclick="switchCell('2')">保养记录</td>
      <td background="/images/line_2.gif">&nbsp;</td>

    </tr>
  </table> 
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">  
    <tr id="tb">
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff">
      <br>
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		    <tr>
		      <td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
		      <td>${elevatorArchivesInfoBean.salesContractNo}</td>
		   </tr>
		   <tr>
		      <td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
		      <td>${elevatorArchivesInfoBean.maintContractNo}</td>
		    </tr>  
		    <tr>    
		      <td class="wordtd">购买单位名称:</td>
		      <td>${elevatorArchivesInfoBean.projectName}</td>
		   </tr>
		   <tr>  
		      <td class="wordtd">使用单位名称:</td>
		      <td>${elevatorArchivesInfoBean.projectAddress}</td>
		    </tr>  
		    <tr>    
		      <td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
		      <td>${elevatorArchivesInfoBean.maintDivision}</td>
		    </tr>
		    <tr>
		      <td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
		      <td>${elevatorArchivesInfoBean.maintStation}</td>
		    </tr>  
		    <tr>            
              <td class="wordtd"><bean:message key="elevatorArchivesInfo.elevatorNo"/>:</td>
              <td><a onclick="simpleOpenWindow('elevatorSaleAction','${elevatorArchivesInfoBean.elevatorNo}');" class="link">${elevatorArchivesInfoBean.elevatorNo}</a></td>
           </tr>
           <tr>
              <td class="wordtd"><bean:message key="elevatorArchivesInfo.elevatorType"/>:</td>
              <td>${elevatorArchivesInfoBean.elevatorType}</td>
            </tr> 
             <tr>
               <td class="wordtd"><bean:message key="elevatorArchivesInfo.elevatorParam"/>:</td>
               <td>${elevatorArchivesInfoBean.elevatorParam}</td>
             </tr>
             <tr>
               <td class="wordtd">层/站/门/提升高度:</td>
               <td>${elevatorArchivesInfoBean.floor}/${elevatorArchivesInfoBean.stage}/${elevatorArchivesInfoBean.door}/${elevatorArchivesInfoBean.high}</td>
             </tr>
		  </table>
		</td>
	  </tr>

      <!-- 故障记录 -->
      <tr id="tb" style="display:none;" >
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
        <td bgcolor="#ffffff">
        <br>
           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr class="td_3_1">            
              <td>故障日期</td>
              <td>维修完成日期</td>
              <td>报修描述</td>
              <td>维修描述</td>
              <td>故障分类</td>
              <td>维修人</td>
              <td nowrap="nowrap">坏件品牌型号或其它备注</td>
            </tr> 
            <logic:present name="calloutList">
			<logic:iterate id="element" name="calloutList" >
              <tr>
                <td align="center">${element.operDate}</td>
                <td align="center">${element.completeTime}</td>
                <td width="200px" style="text-align: center;word-wrap:break-word;">${element.repairDesc}</td>
                <td width="200px" style="text-align: center;word-wrap:break-word;">${element.processDesc}</td>
                <td align="center">${element.hfcId}</td>
                <td align="center">${element.assignUser}</td>
                <td width="200px" style="text-align: center;word-wrap:break-word;">${element.serviceRem}</td>                    
              </tr>
            </logic:iterate>
           </logic:present>
          </table>
        </td>
      </tr>
      
      <!-- 保养记录 -->
      <tr id="tb" style="display:none;" >
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
        <td bgcolor="#ffffff">
        <br>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr class="td_3_1">            
            <td>保养时间</td>
              <td>保养类型</td>
              <td>保养人</td>
              <td nowrap="nowrap">保养备注</td>
            </tr> 
            <logic:present name="mwpList">
            <logic:iterate id="element" name="mwpList" >
              <tr id="tr_0">
                <td align="center">${element.maintDate}</td>
                <td align="center">
                <logic:match name="element" property="maintType" value="halfMonth" >
				半月保养
		       </logic:match>
		       <logic:match name="element" property="maintType" value="quarter" >
				季度保养 
		       </logic:match>
		       <logic:match name="element" property="maintType" value="halfYear" >
				半年保养
		       </logic:match>
		       <logic:match name="element" property="maintType" value="yearDegree" >
				年度保养
		       </logic:match>
             	</td>
                <td align="center">${element.maintPersonnel}</td>
                <td width="400px" style="text-align: center;word-wrap:break-word;">${element.rem}</td>
              </tr> 
          </logic:iterate>
          </logic:present>      
          </table>
        </td>
      </tr>
	  
	</table>
</logic:present>