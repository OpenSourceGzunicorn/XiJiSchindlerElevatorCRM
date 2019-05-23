<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>

<tiles:insert definition="base" flush="true">
	<tiles:put name="header" value="../Header.jsp"/>
	<tiles:put name="navibar" value="../Navibar.jsp"/>
	<tiles:put name="toolbar" value="../Toolbar.jsp"/>
	<tiles:put name="content" value="../Content.jsp"/>
	<tiles:put name="footer" value="../Footer.jsp"/>
</tiles:insert>
