Ext.BLANK_IMAGE_URL = 'resources/s.gif';

//首页调用
function onClickMenuItem(el){
    mainPanel.loadClass(el.href,el.id,null,el.name);
}

Docs = {};

ApiPanel = function() {
    ApiPanel.superclass.constructor.call(this, {
        id:'api-tree',
        region:'west',
        split:true,
        width: 180,//控制树形结构页的宽度
        minSize: 175,
        maxSize: 500,
        collapsible: true,
		collapsed:false,//初始化是否显示树形结构页, false为显示, true为不显示
        margins:'0 0 5 5',
        cmargins:'0 0 0 0',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        animCollapse:false,
        animate: false,
        collapseMode:'mini',
        loader: new Ext.tree.TreeLoader({
			preloadChildren: true,
			clearOnLoad: false
		}),
        root: new Ext.tree.AsyncTreeNode({
            text:'Ext JS',
            id:'root',
            expanded:true,
            children:[Docs.classData]
         }),
        collapseFirst:false
    });
    // no longer needed!
    //new Ext.tree.TreeSorter(this, {folderSort:true,leafAttr:'isClass'});

    this.getSelectionModel().on('beforeselect', function(sm, node){
        return node.isLeaf();
    });
};

Ext.extend(ApiPanel, Ext.tree.TreePanel, {
    selectClass : function(cls){
        if(cls){
            var parts = cls.split('.');
            var last = parts.length-1;
            for(var i = 0; i < last; i++){ // things get nasty - static classes can have .
                var p = parts[i];
                var fc = p.charAt(0);
                var staticCls = fc.toUpperCase() == fc;
                if(p == 'Ext' || !staticCls){
                    parts[i] = 'pkg-'+p;
                }else if(staticCls){
                    --last;
                    parts.splice(i, 1);
                }
            }
            parts[last] = cls;
			//alert("path="+'/root/apidocs/'+parts.join('/'));
            this.selectPath('/root/apidocs/'+parts.join('/'));
        }
    }
});


DocPanel = Ext.extend(Ext.Panel, {
    closable: true,
    autoScroll:true,

    initComponent : function(){
        DocPanel.superclass.initComponent.call(this);
    },

    scrollToMember : function(member){
        var el = Ext.fly(this.cclass + '-' + member);
        if(el){
            var top = (el.getOffsetsTo(this.body)[1]) + this.body.dom.scrollTop;
            this.body.scrollTo('top', top-25, {duration:.75, callback: this.hlMember.createDelegate(this, [member])});
        }
    },

	scrollToSection : function(id){
		var el = Ext.getDom(id);
		if(el){
			var top = (Ext.fly(el).getOffsetsTo(this.body)[1]) + this.body.dom.scrollTop;
			this.body.scrollTo('top', top-25, {duration:.5, callback: function(){
                Ext.fly(el).next('h2').pause(.2).highlight('#8DB2E3', {attr:'color'});
            }});
        }
	},

    hlMember : function(member){
        var el = Ext.fly(this.cclass + '-' + member);
        if(el){
            el.up('tr').highlight('#cadaf9');
        }
    }
});


MainPanel = function(){
	var tools = [{
        id:'gear',
        handler: function(){
            Ext.Msg.alert('Message', 'The Settings tool was clicked.');
        }
    },{
        id:'close',
        handler: function(e, target, panel){
            panel.ownerCt.remove(panel, true);
        }
    }];
	this.searchStore = new Ext.data.Store({
        proxy: new Ext.data.ScriptTagProxy({
            url: 'http://extjs.com/playpen/api.php'
        }),
        reader: new Ext.data.JsonReader({
	            root: 'data'
	        }, 
			['cls', 'member', 'type', 'doc']
		),
		baseParams: {},
        listeners: {
            'beforeload' : function(){
                this.baseParams.qt = Ext.getCmp('search-type').getValue();
            }
        }
    }); 
	//Ext.example.shortBogusMarkup = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.';
	//Ext.example.bogusMarkup = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus tincidunt diam nec urna. Curabitur velit.</p>';

    MainPanel.superclass.constructor.call(this, {
        id:'doc-body',
        region:'center',
        margins:'0 5 5 0',
        resizeTabs: true,
        minTabWidth: 135,
        tabWidth: 175,
        plugins: new Ext.ux.TabCloseMenu(),
        enableTabScroll: true,
        activeTab: 0,
		//xtype:'portal',
       // region:'center',
       // margins:'35 5 5 0',
        items: {
            id:'welcome-panel',
            //title: '首页('+new Date().getYear()+"-"+parseInt(new Date().getMonth()+1)+"-"+new Date().getDate()+")",
            title: '首页',
            autoLoad: {url: 'myWorkspaceAction.do?method=toIndex',scripts: true},
//            autoLoad: {url: '/drp/layout/tree/PortalLayout.jsp', callback: this.initSearch, scope: true},
//            autoLoad: {url: './portalLayoutAction.do?method=toPortalMain', callback: this.initSearch, scope: true},
//            iconCls:'icon-docs', 
            autoScroll: true//,
//			items:[{
//	                columnWidth:.33,
//	                style:'padding:10px 0 10px 10px',
//	                items:[{
//	                    title: 'Grid in a Portlet',
//	                    layout:'fit',
//	                    tools: tools
//	                   // items: new SampleGrid([0, 2, 3])
//	                },{
//	                    title: 'Another Panel 1',
//	                    tools: tools,
//	                    html: '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,'
//	                }]
//	            },{
//	                columnWidth:.33,
//	                style:'padding:10px 0 10px 10px',
//	                items:[{
//	                    title: 'Panel 2',
//	                    tools: tools,
//	                    html: '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,'
//	                },{
//	                    title: 'Another Panel 2',
//	                    tools: tools,
//	                    html: '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,'
//	                }]
//	            },{
//	                columnWidth:.33,
//	                style:'padding:10px',
//	                items:[{
//	                    title: 'Panel 3',
//	                    tools: tools,
//	                    html: '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,'
//	                },{
//	                    title: 'Another Panel 3',
//	                    tools: tools,
//	                    html: '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,'
//	                }]
//	            }]
        }
    });
};

Ext.extend(MainPanel, Ext.TabPanel, {

    initEvents : function(){
        MainPanel.superclass.initEvents.call(this);
        this.body.on('click', this.onClick, this);
        
        //add by zl start
        this.on('add', this.onAdd, this, {target: this});   
        this.on('remove', this.onRemove, this, {target: this});   
        this.mon(this.strip, 'mousedown', this.onStripMouseDown, this);   
        this.mon(this.strip, 'contextmenu', this.onStripContextMenu, this);   
        if(this.enableTabScroll){   
            this.mon(this.strip, 'mousewheel', this.onWheel, this);   
        }   
        // ADD:monitor title dbclick   
        this.mon(this.strip,'dblclick',this.onTitleDbClick,this);
        //add by zl end
    },
    
    //add by zl start
    onTitleDbClick : function(e,target,o){   
        var t = this.findTargets(e); 
        if (t.item != null && t.item.closable && t.item.fireEvent('beforeclose', t.item) !== false) {   
            t.item.fireEvent('close', t.item);   
            this.remove(t.item);                   
        }   
    },
 	isObject : function(v){
        return v && typeof v == "object";
    },
 	mon : function(item, ename, fn, scope, opt){
        if(!this.mons){
            this.mons = [];
            this.on('beforedestroy', this.clearMons, this, {single: true});
        }
        if(this.isObject(ename)){
        	var propRe = /^(?:scope|delay|buffer|single|stopEvent|preventDefault|stopPropagation|normalized|args|delegate)$/;

            var o = ename;
            for(var e in o){
                if(propRe.test(e)){
                    continue;
                }
                if(Ext.isFunction(o[e])){
                    // shared options
			        this.mons.push({
			            item: item, ename: e, fn: o[e], scope: o.scope
			        });
			        item.on(e, o[e], o.scope, o);
                }else{
                    // individual options
			        this.mons.push({
			            item: item, ename: e, fn: o[e], scope: o.scope
			        });
			        item.on(e, o[e]);
                }
            }
            return;
        }


        this.mons.push({
            item: item, ename: ename, fn: fn, scope: scope
        });
        item.on(ename, fn, scope, opt);
	},  
    //add by zl end

    onClick: function(e, target){
        if(target = e.getTarget('a:not(.exi)', 3)){
            var cls = Ext.fly(target).getAttributeNS('ext', 'cls');
            e.stopEvent();
            if(cls){
                var member = Ext.fly(target).getAttributeNS('ext', 'member');
                this.loadClass(target.href, cls, member,null);
            }else if(target.className == 'inner-link'){
                this.getActiveTab().scrollToSection(target.href.split('#')[1]);
            }else{
                window.open(target.href);
            }
        }else if(target = e.getTarget('.micon', 2)){
            e.stopEvent();
            var tr = Ext.fly(target.parentNode);
            if(tr.hasClass('expandable')){
                tr.toggleClass('expanded');
            }
        }
    },

    loadClass : function(href, cls, member,tagName){
        var id = 'docs-' + cls;
        var tab = this.getComponent(id);
		//alert("id="+id);
        if(tab){
            this.setActiveTab(tab);
            if(member){
                tab.scrollToMember(member);
            }
        }else{
            var autoLoad = {url: href};
            if(member){
                autoLoad.callback = function(){
                    Ext.getCmp(id).scrollToMember(member);
                }
            }
			//alert("add docpanel "+id+"	"+cls+"	"+member+"	"+tagName);
			//cls=cls+"."+tagName;
			//alert("cls="+cls);
            var p = this.add(new DocPanel({
                id: id,
                cclass : cls,
                autoLoad: autoLoad,
                iconCls: Docs.icons[cls],
				title:tagName
            }));
            this.setActiveTab(p);
        }
    },
	
	initSearch : function(){
		// Custom rendering Template for the View
	    var resultTpl = new Ext.XTemplate(
	        '<tpl for=".">',
	        '<div class="search-item">',
	            '<a class="member" ext:cls="{cls}" ext:member="{member}" href="output/{cls}.html">',
				'<img src="../resources/images/default/s.gif" class="item-icon icon-{type}"/>{member}',
				'</a> ',
				'<a class="cls" ext:cls="{cls}" href="output/{cls}.html">{cls}</a>',
	            '<p>{doc}</p>',
	        '</div></tpl>'
	    );
		
		var p = new Ext.DataView({
            applyTo: 'search',
			tpl: resultTpl,
			loadingText:'Searching...',
            store: this.searchStore,
            itemSelector: 'div.search-item',
			emptyText: '<h3>Use the search field above to search the Ext API for classes, properties, config options, methods and events.</h3>'
        });
	},
	
	doSearch : function(e){
		var k = e.getKey();
		if(!e.isSpecialKey()){
			var text = e.target.value;
			if(!text){
				this.searchStore.baseParams.q = '';
				this.searchStore.removeAll();
			}else{
				this.searchStore.baseParams.q = text;
				this.searchStore.reload();
			}
		}
	}
});

var mainPanel = null;
Ext.onReady(function(){

    Ext.QuickTips.init();

    var api = new ApiPanel();
    mainPanel = new MainPanel();
	
    api.on('click', function(node, e){
         if(node.isLeaf()){
            e.stopEvent();
			//alert("node.text="+node+"	"+node.text);
            mainPanel.loadClass(node.attributes.href, node.id,null,node.text);
         }
    });
	
    mainPanel.on('tabchange', function(tp, tab){
        api.selectClass(tab.cclass); 
    });

    var hd = new Ext.Panel({
        border: false,
        layout:'anchor',
        region:'north',
        cls: 'docs-header',
        height:62,
        items: [{
            xtype:'box',
            el:'header',
            border:false,
            anchor: 'none -25'
        },
        new Ext.Toolbar({
            cls:'top-toolbar',
            items:[ ' ',
			new Ext.form.TextField({
				width: 184,//控制搜索框的宽度
				emptyText:'请输入关键字',
				listeners:{
					render: function(f){
						f.el.on('keydown', filterTree, f, {buffer: 350});
					}
				}
			}), ' ', ' ',
			{
                iconCls: 'icon-expand-all',
				tooltip: '展开',
                handler: function(){ api.root.expand(true); }
            }, '-', {
                iconCls: 'icon-collapse-all',
                tooltip: '收起',
                handler: function(){ api.root.collapse(true); }
            },new Ext.Toolbar.TextItem(document.getElementById("WelcomePeople").innerHTML)]
        })]
    })

    var viewport = new Ext.Viewport({
        layout:'border',
        items:[ hd, api, mainPanel ]
    });

    api.expandPath('/root/apidocs');
	api.root.expandChildNodes(1);
    // allow for link in
    var page = window.location.href.split('?')[1];
	//alert("page="+page);
    /* --init page
	if(page){
        var ps = Ext.urlDecode(page);
        var cls = ps['class'];
        mainPanel.loadClass('output/' + cls + '.html', cls, ps.member);
    }
    */
    viewport.doLayout();
	
	setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);


//---start----------------------------------------------------------------
/*	var i=0;
	setInterval(
			function(){
			var node=api.root.findChild("id","N0");
			
			alert("api.id="+node.id+node.text);
			
			//alert("api.id="+node.innerHTML);
			//node.text=node.text+"("+i+")";
			//node.show();
			node.setText(node.text+"<font color='red'>("+i+")</font>");
			i++;
			},2000);
*/
//---end----------------------------------------------------------------

	
	var filter = new Ext.tree.TreeFilter(api, {
		clearBlank: true,
		autoClear: true
	});
	var hiddenPkgs = [];
	function filterTree(e){
		var text = e.target.value;
		Ext.each(hiddenPkgs, function(n){
			n.ui.show();
		});
		if(!text){
			filter.clear();
			return;
		}
		api.expandAll();
		
		var re = new RegExp( Ext.escapeRe(text), 'i');///查询数据
		filter.filterBy(function(n){
			return !n.attributes.isClass || re.test(n.text);
		});
		
		// hide empty packages that weren't filtered
		hiddenPkgs = [];
		api.root.cascade(function(n){
			if(!n.attributes.isClass && n.ui.ctNode.offsetHeight < 3){
				n.ui.hide();
				hiddenPkgs.push(n);
			}
		});
	}
	
});


Ext.app.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        if(!this.store.baseParams){
			this.store.baseParams = {};
		}
		Ext.app.SearchField.superclass.initComponent.call(this);
		this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch : false,
    paramName : 'query',

    onTrigger1Click : function(){
		//alert("onTrigger1Click");

        if(this.hasSearch){
			//alert("this.el.dom="+this.el.dom);

            this.store.baseParams[this.paramName] = '';
			this.store.removeAll();
			this.el.dom.value = '';
            this.triggers[0].hide();
            this.hasSearch = false;
			this.focus();
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
		if(v.length < 2){
			Ext.Msg.alert('Invalid Search', 'You must enter a minimum of 2 characters to search the API');
			return;
		}
		this.store.baseParams[this.paramName] = v;
        var o = {start: 0};
        this.store.reload({params:o});
        this.hasSearch = true;
        this.triggers[0].show();
		this.focus();
    }
});


/**
 * Makes a ComboBox more closely mimic an HTML SELECT.  Supports clicking and dragging
 * through the list, with item selection occurring when the mouse button is released.
 * When used will automatically set {@link #editable} to false and call {@link Ext.Element#unselectable}
 * on inner elements.  Re-enabling editable after calling this will NOT work.
 *
 * @author Corey Gilmore
 * http://extjs.com/forum/showthread.php?t=6392
 *
 * @history 2007-07-08 jvs
 * Slight mods for Ext 2.0
 */
Ext.ux.SelectBox = function(config){
	this.searchResetDelay = 1000;
	config = config || {};
	config = Ext.apply(config || {}, {
		editable: false,
		forceSelection: true,
		rowHeight: false,
		lastSearchTerm: false,
        triggerAction: 'all',
        mode: 'local'
    });

	Ext.ux.SelectBox.superclass.constructor.apply(this, arguments);

	this.lastSelectedIndex = this.selectedIndex || 0;
};

Ext.extend(Ext.ux.SelectBox, Ext.form.ComboBox, {
    lazyInit: false,
	initEvents : function(){
		Ext.ux.SelectBox.superclass.initEvents.apply(this, arguments);
		// you need to use keypress to capture upper/lower case and shift+key, but it doesn't work in IE
		this.el.on('keydown', this.keySearch, this, true);
		this.cshTask = new Ext.util.DelayedTask(this.clearSearchHistory, this);
	},

	keySearch : function(e, target, options) {
		var raw = e.getKey();
		var key = String.fromCharCode(raw);
		var startIndex = 0;

		if( !this.store.getCount() ) {
			return;
		}

		switch(raw) {
			case Ext.EventObject.HOME:
				e.stopEvent();
				this.selectFirst();
				return;

			case Ext.EventObject.END:
				e.stopEvent();
				this.selectLast();
				return;

			case Ext.EventObject.PAGEDOWN:
				this.selectNextPage();
				e.stopEvent();
				return;

			case Ext.EventObject.PAGEUP:
				this.selectPrevPage();
				e.stopEvent();
				return;
		}

		// skip special keys other than the shift key
		if( (e.hasModifier() && !e.shiftKey) || e.isNavKeyPress() || e.isSpecialKey() ) {
			return;
		}
		if( this.lastSearchTerm == key ) {
			startIndex = this.lastSelectedIndex;
		}
		this.search(this.displayField, key, startIndex);
		this.cshTask.delay(this.searchResetDelay);
	},

	onRender : function(ct, position) {
		this.store.on('load', this.calcRowsPerPage, this);
		Ext.ux.SelectBox.superclass.onRender.apply(this, arguments);
		if( this.mode == 'local' ) {
			this.calcRowsPerPage();
		}
	},

	onSelect : function(record, index, skipCollapse){
		if(this.fireEvent('beforeselect', this, record, index) !== false){
			this.setValue(record.data[this.valueField || this.displayField]);
			if( !skipCollapse ) {
				this.collapse();
			}
			this.lastSelectedIndex = index + 1;
			this.fireEvent('select', this, record, index);
		}
	},

	render : function(ct) {
		Ext.ux.SelectBox.superclass.render.apply(this, arguments);
		if( Ext.isSafari ) {
			this.el.swallowEvent('mousedown', true);
		}
		this.el.unselectable();
		this.innerList.unselectable();
		this.trigger.unselectable();
		this.innerList.on('mouseup', function(e, target, options) {
			if( target.id && target.id == this.innerList.id ) {
				return;
			}
			this.onViewClick();
		}, this);

		this.innerList.on('mouseover', function(e, target, options) {
			if( target.id && target.id == this.innerList.id ) {
				return;
			}
			this.lastSelectedIndex = this.view.getSelectedIndexes()[0] + 1;
			this.cshTask.delay(this.searchResetDelay);
		}, this);

		this.trigger.un('click', this.onTriggerClick, this);
		this.trigger.on('mousedown', function(e, target, options) {
			e.preventDefault();
			this.onTriggerClick();
		}, this);

		this.on('collapse', function(e, target, options) {
			Ext.getDoc().un('mouseup', this.collapseIf, this);
		}, this, true);

		this.on('expand', function(e, target, options) {
			Ext.getDoc().on('mouseup', this.collapseIf, this);
		}, this, true);
	},

	clearSearchHistory : function() {
		this.lastSelectedIndex = 0;
		this.lastSearchTerm = false;
	},

	selectFirst : function() {
		this.focusAndSelect(this.store.data.first());
	},

	selectLast : function() {
		this.focusAndSelect(this.store.data.last());
	},

	selectPrevPage : function() {
		if( !this.rowHeight ) {
			return;
		}
		var index = Math.max(this.selectedIndex-this.rowsPerPage, 0);
		this.focusAndSelect(this.store.getAt(index));
	},

	selectNextPage : function() {
		if( !this.rowHeight ) {
			return;
		}
		var index = Math.min(this.selectedIndex+this.rowsPerPage, this.store.getCount() - 1);
		this.focusAndSelect(this.store.getAt(index));
	},

	search : function(field, value, startIndex) {
		field = field || this.displayField;
		this.lastSearchTerm = value;
		var index = this.store.find.apply(this.store, arguments);
		if( index !== -1 ) {
			this.focusAndSelect(index);
		}
	},

	focusAndSelect : function(record) {
		var index = typeof record === 'number' ? record : this.store.indexOf(record);
		this.select(index, this.isExpanded());
		this.onSelect(this.store.getAt(record), index, this.isExpanded());
	},

	calcRowsPerPage : function() {
		if( this.store.getCount() ) {
			this.rowHeight = Ext.fly(this.view.getNode(0)).getHeight();
			this.rowsPerPage = this.maxHeight / this.rowHeight;
		} else {
			this.rowHeight = false;
		}
	}

});

Ext.Ajax.on('requestcomplete', function(ajax, xhr, o){
    if(typeof urchinTracker == 'function' && o && o.url){
        urchinTracker(o.url);
    }
});