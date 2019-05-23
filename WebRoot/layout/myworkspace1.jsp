<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<LINK href="http://www.jackslocum.com/favicon.ico" rel="shortcut icon">
<LINK href="http://www.jackslocum.com/favicon.ico" rel=icon>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='j_extallCSS'/>">
<script language="javascript" src="<html:rewrite forward='j_rowexpanderJS'/>"></script>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<style>
body{ margin:5px;}
</style>

<BODY>

<SCRIPT LANGUAGE="JavaScript">
var store;

Ext.onReady(function(){
    Ext.QuickTips.init();
    var xg = Ext.grid;
//工具
 var tools = [
 {
       id:'refresh',
       handler: function(event, toolEl, panel){
            var id = panel.getId();
            panel.getStore().baseParams.id=id; 
       		panel.getStore().load();
       }

    },{
        id:'close',
        handler: function(e, target, panel){
            panel.remove(panel, true);
        }
  }		        
  
  ];
    

var colM=new Ext.grid.ColumnModel([
            new xg.RowNumberer(),
            {header: "sid", width: 40, sortable: true, dataIndex: 'sid', align:'center',hidden :true,hideable :false},
            {header: "title", sortable: true, dataIndex: 'title',hidden :true,hideable :false},
            {header: "url", sortable: true, dataIndex: 'url',hidden :true,hideable :false},
            //{header: "日期", sortable: true, dataIndex: 'date',width: 40,align:'center'},
            {header: "主题", sortable: true, dataIndex: 'title'}
	]);	

<logic:present name="divList">
	<logic:iterate id="element" name="divList" >
	  store = new Ext.data.Store({
						url:'/XJSCRM/ajaxWorkspaceAction.do?method=toAjaxRecord',
						autoLoad:true,
						baseParams:{id:'<bean:write name="element" property="wskey"  filter="false"/>'}, 
						reader:new Ext.data.XmlReader({		
							record:'row',
							id:'sid'},
							Ext.data.Record.create([
							   {name: 'sid'},
					           {name: 'title'},           
					           {name: 'url'},
					           //{name: 'date'},
					           {name: 'title2'}
					           
					           ])
					    )
					});
					
	//var pagewsid = '<bean:write name="element" property="wsid"  filter="false"/>';				
	var grid= new Ext.grid.GridPanel({ 
		 		id:'<bean:write name="element" property="wskey"  filter="false"/>', 
				store:store,	
				cm:colM,	
				stripeRows: true, //间隔线
		        viewConfig: {
		         forceFit:true
		        },
		       	bodyStyle : 'width:100%', 
		        //height:450,
		        height:230, //设置首页Panel高度
				layout:'fit',
				style:'padding:6px 0 5px 5px',
		        collapsible: true,
				tools: tools,
		        //title:'<div id ="'+pagewsid+'"> </div>',	
		        title:"<div id ='<bean:write name="element" property="wsid"  filter="false"/>'> </div>",	
		        renderTo: '<bean:write name="element" property="divid"  filter="false"/>',
		        listeners: { 
		        	"rowclick" : function( Grid ,  rowIndex, e ){
						var Record  = Grid.getStore().getAt(rowIndex);
						var url = Record.get('url');
						if(url.indexOf("../../")!=-1){
						   url=url.substring(6,url.length);
						}
						var avf=document.getElementById("avf");
						if(avf!=null){
							//avf.href="../../dayAndFeeAction.do?id=0800002166_1&method=toPrepareUpdateRecord&pathsource=workspace";
							avf.href="../../"+escapeXML(url);
							avf.click();
						}
		        	}
		        }
		});
		
	store.on('load',function(){
		var div = document.getElementById('<bean:write name="element" property="wsid"  filter="false"/>');
		//div.innerHTML="<a href='<bean:write name="element" property="link"  filter="false"/>'  target='_blank'> <bean:write name="element" property="title"  filter="false"/> </a>";
		div.innerHTML='<bean:write name="element" property="title"  filter="false"/>';
	});
	
	store.on('beforeload',function(){
		var div = document.getElementById('<bean:write name="element" property="wsid"  filter="false"/>');
		div.innerHTML="<img src='<html:rewrite page="/"/>/common/images/progressbar_green.gif'>";
	});
	</logic:iterate>
</logic:present>

});

function escapeXML(dangerous){
	if(dangerous==null){
		return dangerous;
	}
	if( dangerous.indexOf("&amp;")  == -1 && 
	    dangerous.indexOf("&quot;") == -1 && 
          dangerous.indexOf("&apos;") == -1 && 
          dangerous.indexOf("&lt;")  == -1 && 
          dangerous.indexOf("&gt;") == -1    
      ){
          return dangerous;
      } else {
          dangerous = dangerous.replace(/\&amp;/g,"&");
          dangerous = dangerous.replace(/\&quot;/g,"/");
          dangerous = dangerous.replace(/\&apos;/g,"'");
          dangerous = dangerous.replace(/\&lt;/g,"<");
          dangerous = dangerous.replace(/\&gt;/g,">");
          return dangerous;
      }
  }


//自动刷新,最后一个store
/**
window.setInterval(function() {
   store.reload();  //store 的变量名
}, 60000);  //每隔 60秒 
*/

</SCRIPT>
	<a href="" id="avf" target="_blank"></a>
	<br/>
	<logic:present name="divList">
		<logic:iterate id="element" name="divList" >
			<div id="<bean:write name="element"  property="divid"  filter="false"/>" style="float:<bean:write name="element"  property="float"  filter="false"/>;width:49.9%;">
			 
			</div>
		</logic:iterate>
	</logic:present>

</BODY></HTML>
	 