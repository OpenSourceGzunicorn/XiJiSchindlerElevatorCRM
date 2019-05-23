<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
	<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
  <link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>


<br>
<html:form action="/maintenanceReportAction.do?method=toSearchResults">
<input type="hidden" name="maintDivision" id="maintDivision" value="${rmap.maintDivision }"/>
<input type="hidden" name="mainStation" id="mainStation" value="${rmap.mainStation }"/>
<input type="hidden" name="maintPersonnel" id="maintPersonnel" value="${rmap.maintPersonnel }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />
	
	
	<table width="100%" height="24" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr class="td_2">
      <td width="2" background="/images/line.gif"></td>
      <td width="100" class="tab-on" id="navcell" onclick="switchCell('0')">工作时间</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>
        
      <td width="100" class="tab-off" id="navcell" onclick="switchCell('1')">工作柱形图</td>  
      <td width="800" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>
 </tr>
 </table>
	
	<table id="contents" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">  
    <tr id="tb">
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff" style="text-align: center;">
      <br>
	<%int i=1; %>
	<table class=tb  width="100%" border=0  >
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">维保工</td>
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">项目名称</td>
	<td nowrap="nowrap"  style="text-align: center;">维保合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">销售合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">电梯编号</td>
	<td nowrap="nowrap"  style="text-align: center;">电梯类型</td>
	<td nowrap="nowrap"  style="text-align: center;">规格型号</td>
	<td nowrap="nowrap"  style="text-align: center;">保养时间</td>
	<td nowrap="nowrap"  style="text-align: center;">星期</td>
	<td nowrap="nowrap"  style="text-align: center;">保养类型</td>
	<td nowrap="nowrap"  style="text-align: center;">准保养时间(分钟)</td>
	<td nowrap="nowrap"  style="text-align: center;">作业时间段</td>
	</tr>
	<logic:present name="maintenanceReportList">
	  <logic:iterate id="ele" name="maintenanceReportList">
	  <tr style="display:none;" class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="username"/><html:hidden name="ele" property="username"/>
	  <html:hidden name="ele" property="userid"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="comname"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="storagename"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="projectName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintContractNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="salesContractNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatorNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;">${ele.elevatorType=='T'?'直梯':'扶梯'}</td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatorParam"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintDate"/><html:hidden name="ele" property="maintDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="week"/><html:hidden name="ele" property="week"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintType"/><html:hidden name="ele" property="maintType"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintDateTime"/><html:hidden name="ele" property="maintDateTime"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintStartTime"/><bean:write name="ele" property="maintEndTime"/></td>
	  </tr>
	  </logic:iterate>
	  	</logic:present>
	  	<logic:notPresent name="maintenanceReportList">
	  <tr class="noItems" align="center" height="20"><td colspan="15" >没有记录显示！</td></tr>
	  </logic:notPresent>
	  	</table>
	</td>
	</tr>
	<!-- 柱形图 -->
    <tr id="tb" style="display:none;" >
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff">
        <div id="container" style="min-width: 1000px; height: 550px; margin: 0 auto"></div>
      </td>
    </tr>   
  </table>
	<script>
	var rows = $(".inputtd");
	var i = 0;
	var showRow = setInterval(function(){
		if(i < rows.length){
			for(var j=i;j<rows.length && j<i+20;j++){
				rows[j].style.display="block";	
			}			
			i+=20;
		}else{
			clearInterval(showRow);
		}
	}, 1);

	
  $("document").ready(function () {
  
	  <logic:present name="maintenanceReportList"> 
	  drawChart();
	  </logic:present>
  })
  
  function drawChart() {
  
  	var titleText=document.getElementsByName("username")[0].value;// 维保工名称
  	var maintPersonnel=document.getElementsByName("userid")[0].value;//维保工编号
	
	var weeks = document.getElementsByName("week");//星期
	var maintTypes = document.getElementsByName("maintType");//保养类型
  	var maintDates = document.getElementsByName("maintDate");//保养日期
  	var maintDateTimes = document.getElementsByName("maintDateTime");//准保养时间
	var sumMaintDateTimes=[]; 
  	
  	var maintDates2=[];
  	var weeks2=[];
    for(var i=0;i<maintDateTimes.length;i++)
    {
    	if(i>0&&maintDates[i].value==maintDates[i-1].value)
    	{
    		sumMaintDateTimes[sumMaintDateTimes.length-1] +=  Number(maintDateTimes[i].value);
    	}else
    	{
    		sumMaintDateTimes[sumMaintDateTimes.length] = Number(maintDateTimes[i].value);
    		maintDates2.push(maintDates[i].value);
    		weeks2.push(weeks[i].value)
    	}
    }
   
	var categories= [];
	for(var i=0;i<maintDates2.length;i++){
		categories[i] = maintDates2[i];
 		if(i>0 && maintDates2[i-1].substring(0,4) == maintDates2[i].substring(0,4)){
			categories[i] = maintDates2[i].substring(5);
		}
		categories[i] = categories[i].replace(/-/g,".");
	}
	
	var data = [];
	var overTime = 360;// 大于360分钟时，柱子变成红色
	for(var i=0;i<sumMaintDateTimes.length;i++){
		var y = Number(sumMaintDateTimes[i]);
		/****** 大于360分钟时，柱子变成红色 ******/
		var color = y > overTime ? '#F45B5B' : '#7CB5EC'; 
		data[i] = {color:color,y:y};		
	}
	
	adjustContainer(categories.length);	//图大小自适应

	//柱形图配置
    $("#container").highcharts({
        chart: {
            type: 'column'//柱形图
        },
        title: {//标题
            text: titleText 
        },
        subtitle: {//子标题
            text: ''
        },
		credits: {
         	enabled:false
      	},
        xAxis: {//X轴
            categories: categories, //轴标签数据          
            crosshair: true,
            labels: {//轴标签
            	//staggerLines: 2, //轴标签分两行显示
            	style: {
            		color: "#444444"
            	}
            }
        },
        yAxis: {//Y轴
            min: 0,
            title: {
                text: '单位：分钟'
            }
        },
        tooltip: {//数据提示框
         	//shared: true,
         	useHTML: true,
			formatter: function () {
				
				var index = this.point.index;				
				var objs = eval(getDetailsJson(maintPersonnel, maintDates2[index]));				
				var format = '<b><font color='+this.color+'>' + maintDates2[index] + ' 星期'+weeks2[index]+'</font></b>:<br/>';

				format += "<table style='color: #444444; text-align: center;'><tr>"+
					"<td nowrap='nowrap'><b>电梯编号</b></td>"+
					"<td nowrap='nowrap'><b>标准保养时间</b></td>"+
					"<td nowrap='nowrap'><b>维保合同号</b></td>"+
					"<td nowrap='nowrap'><b>销售合同号</b></td>"+
					"<td nowrap='nowrap'><b>项目名称</b></td>"+
					"<td nowrap='nowrap'><b>保养类型</b></td>"+
					"</tr>";
				
				for(var i=0; i<objs.length; i++){
					format +="<tr>"+
						"<td nowrap='nowrap'>"+objs[i].elevatorNo+"</td>"+
						"<td nowrap='nowrap'>"+objs[i].maintDateTime+"</td>"+
						"<td nowrap='nowrap'>"+objs[i].maintContractNo+"</td>"+
						"<td nowrap='nowrap'>"+objs[i].salesContractNo+"</td>"+
						"<td nowrap='nowrap'>"+objs[i].projectName+"</td>"+
						"<td nowrap='nowrap'>"+getMaintTypeName(objs[i].maintType)+"</td>"+
						"</tr>";
				}
				format += "</table>"

				return format;
            }
		},       
        plotOptions: {//绘图线条控制
            column: {
                pointPadding: 0.2,
                borderWidth: 0,
				colorByPoint: true,
				dataLabels: {
            		enabled: true
        		}
            }
			
        },
        series: [{//Y轴数据
            name: '作业柱形图（红色柱子表示当天总保养时间大于<font color=#F45B5B>'+overTime+'</font>分钟）',
            data: data,
			events: {
            	legendItemClick: function(event) { 
					return false;
            	}
          	}
        }],
        legend: {
        	//enabled: false
        }                               
    });
    
  }
  
  function getMaintTypeName(maintType){
		switch(maintType){
			case "halfMonth": return "半月保养";
			case "quarter": return "季度保养";
			case "halfYear": return "半年保养";
			case "yearDegree": return "年度保养";
		}
	}
	function getDetailsJson (maintPersonnel, maintDates) {
		
		var pathName = window.document.location.pathname;
		var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
		var url = projectName+"/maintenanceReceiptAction.do?method=getMaintPersonnelOneDateDetail" + 
				"&maintPersonnel="+maintPersonnel+"&maintDate="+maintDates

		var obj = $.ajax({
				url: url,
				async:false				
			});
			
		return obj.responseText;	  
	}

  function adjustContainer(rowLen) { 
		$("#container").hide();
		var width = $("#contents").outerWidth();
		width = width >= (rowLen/30) * 1000 ? width : (rowLen/30) * 1000;
		$("#container").width(width);	
		$("#container").show();
		if(window.onresize == null){
			window.onresize = function () {	
				adjustContainer();
			} 
		}		
  }	
</script>
		
</html:form>
