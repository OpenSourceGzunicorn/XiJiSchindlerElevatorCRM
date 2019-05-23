<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<%@ page import="com.gzunicorn.common.util.CommonUtil" %>
<HTML>
	<HEAD>
		<TITLE>西继迅达（许昌）电梯有限公司CRM系统</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<LINK href="layout/css/ext-all.css" type=text/css rel=stylesheet>
		<LINK href="layout/css/docs.css" type=text/css rel=stylesheet></LINK>
		<LINK href="layout/css/style.css" type=text/css rel=stylesheet></LINK>
		<LINK href="http://www.jackslocum.com/favicon.ico" rel="shortcut icon">
		<LINK href="http://www.jackslocum.com/favicon.ico" rel=icon>
		<LINK href="layout/css/vtron.css" type=text/css rel=stylesheet></LINK>
		<script language="javascript" src="<html:rewrite forward='AJAX'/>"></script>
		<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
		<STYLE type=text/css>
			.iconlable
			{
				 background-position: left;
				 background-repeat: no-repeat;
				 padding-left: 20px;
				 margin-left: 15px;
				 float:left;				 		
			}
			.fl{float:left;}
			.fr{float:right;}
		</STYLE>
		<!-- GC -->
		<META content="MSHTML 6.00.2900.3395" name=GENERATOR>
	</HEAD>
	
	<BODY id=docs scroll=no>
		<a href="#" id="avf" target="_self"></a>
		<DIV id=loading-mask></DIV>
		<DIV id=loading>
		<DIV class=loading-indicator>
			<IMG style="MARGIN-RIGHT: 8px" height=32 src="layout/images/extanim32.gif" width=32 align=absMiddle>加载中...</DIV>
		</DIV>
		<!-- include everything after the loading indicator -->
	
		<script type="text/javascript" src="layout/ext-base.js"></script>
	    <script type="text/javascript" src="layout/ext-all-debug.js"></script>
	    <script type="text/javascript" src="layout/js/TabCloseMenu.js"></script>
	    <script type="text/javascript" src="layout/js/docs.js"></script>
	    		
		<SCRIPT type=text/javascript>
			Ext.BLANK_IMAGE_URL = "layout/images/s.gif";
			Docs.classData ={<bean:write name="ClassData" filter="false"/>};
			Docs.icons ={<bean:write name="Icons" filter="false"/>};
			function showTodoList(){
				//mainPanel.loadClass('/drp/layout/MainFrame.jsp?NodeId=0404&JumpURL=%2Fdrp%2FmyTaskOaSearchAction.do%3Fmethod%3DtoMain','N0404','null','我的待办工作');./myWorkspaceAction.do?method=toSearchRecord
				//mainPanel.loadClass('/layout/MainFrame.jsp?NodeId=55555&JumpURL=%2Fvtronmbis%2FmyWorkspaceAction.do%3Fmethod%3DtoSearchRecord','N55555','null','欢迎页")');
			}
		</SCRIPT>
	
		<DIV id=header>
			<DIV class=api-title></DIV>
		</DIV>
		<DIV id=classes></DIV>
		<DIV id=main></DIV>
		<DIV id=WelcomePeople>
			<DIV id="welcomeBox" class="fl" style="overflow: hidden; width: 450px;">
				<DIV class="fl">
					<DIV id="welcomeUser" class="iconlable" style="background-image: url('./images/common/icon_user.gif');">欢迎你!&nbsp;<bean:write name="USER_INFO" scope="session" property="userName"/></DIV>	
				</DIV>				
				<DIV class="fr">		
					<DIV id="date" class="iconlable" style="background-image: url('./images/common/date.gif');"><bean:write name="returnServerDateTime"/></DIV>				
					<DIV id="modifyPassword" class="iconlable" style="background-image: url('./images/common/lock.gif');">
						<span style="cursor: hand;" href="<bean:write name="ModifyPwUrl" filter="false"/>" id="<bean:write name="ModifyPwID" filter="false"/>" name="<bean:write name="ModifyPwName"/>" onclick="onClickMenuItem(this)"> 
							<bean:write name="ModifyPwName" filter="false" /> 
						</span>						
					</DIV>
					<DIV id="logout" class="iconlable" style="background-image: url('./images/common/exit.gif');">
						<span style="cursor:hand;" href="<bean:write name="LogonOutUrl"/>" id="<bean:write name="LogonOutID"/>" name="<bean:write name="LogonOutName"/>" onclick="onClickLogonOut(this)"> 
							<bean:write name="LogonOutName" filter="false" /> 
						</span>									
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		
	</BODY>
</HTML>

<script type="text/javascript" >

	//退出系统
	function onClickLogonOut(el){
	  window.location = el.href;
	  return;
	}
	
	window.onload=function(){
		welcomeBoxAdjust();
		window.onresize = function(){
			welcomeBoxAdjust();
		}		
	}
	
	function welcomeBoxAdjust(){			
		if(document.body.offsetWidth - 260 > 450){
			document.getElementById("welcomeBox").style.width = document.body.offsetWidth - 260;
		}else{
			document.getElementById("welcomeBox").style.width = 450;
		}
	}
</script>
