<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/jsp/template.jsp" flush="true">

  <tiles:put name="title" value="HTMLTable - Table framework for Struts (paging/sorting/and more)"/>

  <tiles:put name="body" direct="true">
  
    <p>
    HTMLTable is a table centric framework designed to be used with 
    <a href="http://jakarta.apache.org/struts">Struts</a>. It facilitates
    operations for table paging / navigation, sorting and filtering; incorporates look
    and feel of tables in one centric XML file with abilities of properties 
    inheritance and changing them in run-time. The framework also provides
    convenient mechanisms to work with editable items in tables and more.
    </p>
    
    <h3><a name="conf">Configuration</a></h3>
    <p>
    This site describes HTMLTable version 0.4-rc1. This version is to be used with 
    Struts 1.1.
    </p>
    <p>
      <ol>
        <li>
          Copy <b>htmltable.jar</b> to WEB-INF/lib and <b>html-table.xml</b> 
          and <b>html-table.tld</b> 
          to WEB-INF directory of your web application or other directories 
          based on deployment.
        </li>
        <li>
          Add declaration of <b>html-table.tld</b> into <b>web.xml</b> file of your 
          web application. For example:
          <PRE>
          <SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>
          <SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">-</SPAN><SPAN CLASS="syntax10">uri</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>/WEB-INF/tld/html-table.tld<SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">/</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">-</SPAN><SPAN CLASS="syntax10">uri</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>
          <SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">-</SPAN><SPAN CLASS="syntax10">location</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>/WEB-INF/tld/html-table.tld<SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">/</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">-</SPAN><SPAN CLASS="syntax10">location</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>
          <SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">/</SPAN><SPAN CLASS="syntax10">taglib</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>
          </PRE>
        </li>
        <li>
          Copy provided images to the directory of your choice for your web application.
        </li>
        <li>
          Edit <b>struts-config.xml</b> file to set up HTMLTable start-up plug-in:
          <PRE>
          <SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">&lt;</SPAN><SPAN CLASS="syntax10">plug</SPAN><SPAN CLASS="syntax10">-</SPAN><SPAN CLASS="syntax10">in</SPAN><SPAN CLASS="syntax125"> </SPAN><SPAN CLASS="syntax10">className</SPAN><SPAN CLASS="syntax10">=</SPAN><SPAN CLASS="syntax3">&quot;</SPAN><SPAN CLASS="syntax3">com.zubarev.htmltable.HTMLTablePlugIn</SPAN><SPAN CLASS="syntax3">&quot;</SPAN><SPAN CLASS="syntax10">/</SPAN><SPAN CLASS="syntax10">&gt;</SPAN>
          </PRE>
          The plug-in takes the following optional parameters:
          <ul>
            <li>
              <i>config</i> - location of html-table.xml configuration file. 
              Default value is "/WEB-INF/html-table.xml".
            </li>
            <li>
              <i>images</i> - location of the directory with images HTMLTable
              is using to render certain tags.
              Default value is "/images/htmltable".
            </li>
            <li>
              <i>actionPath</i> - for "live" tables the framework requires one
              action of type 
              <SPAN CLASS="syntax3">com.zubarev.htmltable.action.ServeTableAction</SPAN>
              to be declared in <b>struts-config.xml</b> file. This parameter indicates 
              a path for that action. Default value is "/ServeTable".
            </li>
            <li>
              <i>actionPathMapping</i> - servlet mapping for the previous 
              parameter. Default value is ".do".
            </li>
          </ul>
        </li>
        <li>
          Optionally you can have key/value pairs in your resource bundle to customize
          messages some HTMLTable tags render.
          <html:link page="/jsp/resource-properties.html">Example</html:link>
        </li>
      </ol>
    </p>
  
    <h3><a name="examples">Examples</a></h3>
    <p>
    Every example has code snippets from html-table.xml configuration file,
    corresponding JSP page and Struts Action. Contents of every table is
    retrieved from a sample database using <a href="http://db.apache.org/ojb">OJB</a>.
    </p>
    
      <ul>
        <li>
          <html:link page="/customerListSimple.do"><b>Simplest</b></html:link>
          Illustrates all pieces that should be put together to have HTMLTable 
          display data. One page contains all items therefore there is no navigation. 
          No stylesheet is applied.
        </li>
        <li>
          <html:link page="/customerListSimpleFunctional1.do"><b>Live table 1</b></html:link> 
          A table with limited number of items on one page with ability to navigate
          through pages. Information can be sorted dynamically against some columns.
          A stylesheet is applied to the table and to tell between header, 
          even and odd rows. "Backend" only returns data needs to be displayed
          on one page. This is the first paging example in Struts with HTMLTable.
        </li>
        <li>
          <html:link page="/customerListSimpleFunctional2.do"><b>Live table 2</b></html:link> 
          Similar to the previous example with the difference that "backend" always 
          returns full set of data even though we have to display only a subset
          of it in one page. Useful in situations where underlying collection
          of elements is not so big.
        </li>
        <li>
          <html:link page="/customerListSimpleFunctional3.do"><b>Live table 3</b></html:link> 
          Similar to the previous example. The difference is that HTMLTable is decoupled
          from data to be displayed - information on HTMLTable goes into session scope,
          data - into request scope. Useful when requirements do not allow to have
          big collections (data) into the session scope.
        </li>
        <li>
          <html:link page="/customerListForm1.do"><b>Table in a form 1</b></html:link> 
          HTMLTable is within a form where search criteria can be specified
          and submitted. Every row has a chebox to perform group operations.
          Look and feel of the table has been inherited from another table in
          html-table.xml file. This is also the first example where <b>ServeTableForm</b>
          is shown in action.
        </li>
        <li>
          <html:link page="/customerListForm2.do"><b>Table in a form 2</b></html:link> 
          Similar to the previous example with the difference in navigator tag that
          issues POST requests instead of GET ones. Tha table also has a different
          info message.
        </li>
        <li>
          <html:link page="/customerListEditable2.do"><b>Editable (indexed properties)</b></html:link>
          Instead if showing just text, in this example, a table shows HTML form widgets
          contents of which can be changed. <b>IndexedPropertiesManager</b> class is employed
          to facilitate operations with indexed properties coming from request. 
          This example also shows how to retain checkbox selections while paging.
          Separate stylesheet is applied to checkbox column.
        </li>
        <li>
          <html:link page="/customerListMergedCells.do"><b>Table with merged cells</b></html:link>
          In a situation when a table needs customization in a way of cell appearance,
          you have to take "dirty" job on writing TD tags. This example also features
          different navigator tag.
        </li>
        <li>
          <html:link page="/customerListJSColumnCB.do"><b>Automated checkbox columns</b></html:link>
          This table has multiple columns with checkboxes and some columns have "main"
          checkbox located in header of the table that switches all dependent elements
          on and off upon clicking. This example features yet another navigator tag.
          Different CSS is applied to the table to alter its look and feel.
        </li>
        <li>
          <html:link page="/customerListAdjustable.do"><b>User-controlled columns</b></html:link>
          In this example a user can choose any set of columns to hide/display
          and rearrange their order.
        </li>
        <li>
          <html:link page="/customerListHighlighting.do"><b>Rows highlighting</b></html:link>
          Rows with value for 'country' column being 'Canada' are highlighted with one color;
          rows with no fax number - with another one. Yet another color is applied to rows
          with 'country' value being 'Brazil' and no fax number (in future version 
          the value to be compared against will be dynamic as well).
        </li>
        <li>
          <html:link page="/customerListRunTimeUI.do"><b>Dynamic look & feel</b></html:link>
          <html:img page="/images/new.gif"/>
          Every setting of html-table.xml can be modified in run-time. This feature will be available
          in version 0.4-rc2
        </li>
        <li>
          <html:link page="/invalidateSession.do"><i>Invalidate Session</i></html:link>
        </li>
      </ul>
      
      <hr/>
      
      <div align="center">
        <a href="mailto:yuriy_zubarev@yahoo.ca?subject=HTMLTable inquiry">Email me for inquiries</a>
      </div>

  </tiles:put>
  
</tiles:insert>
