<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>                                          
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='ExtCSS'/>" />
<script type="text/javascript" src="<html:rewrite forward='ExtBase'/>"></script>
<script type="text/javascript" src="<html:rewrite forward='ExtAll'/>"></script>

</head>
<body>
 
<div id="mytaskshow" ></div>

<script type="text/javascript">

			Ext.onReady(function(){
			
		    Ext.QuickTips.init();
		    
		    var xg = Ext.grid;
		
		    var reader=new Ext.data.XmlReader({
					record:"row",
					id:'createdate'},
					Ext.data.Record.create([
			           {name: 'taskname'},
			           {name: 'createdate'},
			           {name: 'des'},
			           {name: 'tasktype'},
			           {name: 'taskurl'},
			           {name: 'flowname'},
			           {name: 'taskprocs'},
			           {name: 'taskprocurl'},
			           {name: 'hiddentasktarget'},
			           {name: 'hiddentasktype'},
			           {name: 'desc'}
					])
			);
			var myTaskStore = new Ext.data.GroupingStore({
				url:"../myTaskOaSearchAction.do",
				reader:reader,
				remoteSort:true,
				sortInfo:{field:"hiddentasktype",direction:'ASC'},
				groupField:'hiddentasktype' //分组列
			});
        	
        	
			var mycm = new Ext.grid.ColumnModel([
				{header:"任务名称",width:100,dataIndex:"taskname",sortable:true},
				{header:"任务创建时间",width:130,dataIndex:"createdate",sortable:true},
				{header:"任务描述",width:80,dataIndex:"des",sortable:true},
				{header:"任务类型",width:160,dataIndex:"tasktype",renderer:linker,sortable:true},
				{header:"隐藏任务类型URL",width:180,hidden:true,dataIndex:"taskurl",sortable:true},
				{header:"流程名称",width:100,dataIndex:"flowname",sortable:true},
				{header:"查看流程进度图",width:100,dataIndex:"taskprocs",renderer:taskProclinker,sortable:true},
				{header:"隐藏查看流程进度图URL",width:100,hidden:true,dataIndex:"taskprocurl",sortable:true},
				{header:"hiddentasktarget",width:100,hidden:true,dataIndex:"hiddentasktarget",sortable:true},
				{header:"任务类型",width:100,hidden:true,dataIndex:"hiddentasktype",sortable:true}
			]);
			var grid = new xg.GridPanel({
			        cm:mycm,
					store: myTaskStore,
			        view: new Ext.grid.GroupingView(),
			        frame:true,
			        width: Ext.get("mytaskshow").getWidth(),
			        height: 450,
			        collapsible: true,
			        animCollapse: false,
			        title: '我的待办工作',
			        iconCls: 'icon-grid',
			        renderTo: 'mytaskshow'
			});
			
		       function linker(value, p, record){
			        return String.format(
			                '<b><a href="{1}" target="{2}">{0}</a>',
			                value, record.data.taskurl,record.data.hiddentasktarget);
			   }
			   function taskProclinker(value, p, record){
			        return String.format(
			                '<b><a href="{1}" target="Approve">{0}</a>',
			                value, record.data.taskprocurl);
			   }
			   
			   myTaskStore.baseParams={method:'toSearchMyTaskRecord'};
			   myTaskStore.load();
//			   grid.reconfigure(myTaskStore,mycm);
	
		});

		
</script>
</body>
</html>