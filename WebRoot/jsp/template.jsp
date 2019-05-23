<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>

<tiles:importAttribute/>

<html:html>

  <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

  <head>
    <title><tiles:getAsString name="title" ignore="true"/></title>
    <table:stylesheet page="/jsp/styles.css"/>
    <table:stylesheet page="/jsp/jedit.css"/>
    <meta name="keywords" content="struts,table,htmltable,html table,paging,pagination,table sort,table sorting,navigation,table navigation,navigator,table navigator,ascending,descending">
    <meta name="description" content="HTMLTable is a table centric framework designed to be used with Struts. It facilitates operations for table paging, sorting and filtering; incorporates look and feel of tables in one centric XML file with abilities of properties inheritance and changing them in run-time. The framework also provides mechanisms to work with editable items in tables and more.">
  </head>

  <body>
    
   
    
    <h2><tiles:getAsString name="title" ignore="true"/></h2>
    
    <tiles:insert attribute="body" ignore="true"/>
    
    <p>
      <hr/>
    </p>
    
    <logic:present name="xmlConfig">
      <h4>
        Source of html-table.xml
        <html:link page="/jsp/html-table.html" anchor="<%=pageContext.getAttribute("xmlConfig").toString()%>">html-table</html:link>
      </h4>
    </logic:present>
    
    <logic:present name="jspConfig">
      <h4>Source of this JSP page in regards to HTMLTable</h4>
      <p>
        <tiles:insert attribute="jspConfig" ignore="true"/>
      </p>
    </logic:present>
    
    <logic:present name="javaConfig">
      <h4>Source of the corresponding Action in regards to HTMLTable</h4>
      <p>
        <tiles:insert attribute="javaConfig" ignore="true"/>
      </p>
    </logic:present>
    
    <logic:present name="strutsConfig">
      <h4>
        Source of struts-config.xml
        <html:link page="/jsp/struts-config.html">struts-config</html:link>
      </h4>
    </logic:present>
    
  </body>
  
</html:html>
