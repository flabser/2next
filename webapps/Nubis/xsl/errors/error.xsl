<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<html>
			<head>
				<title>2Next - Error  - <xsl:value-of select="error/apptype" /></title>			

			</head>
			<body>

				<div style="clear:both; height:10px" />
				<font style="font-size:1.1em;">
					version
					<xsl:value-of select="error/server" />
					&#169; F dev team 2015
				</font>
				<br />
				<xsl:value-of select="error/apptype" /><br/>

<h1>
				<xsl:choose>
					<xsl:when test="error/type = 'APPLICATION'">
						<font style="font-size:2em;">Application error</font><br/>
						code=<xsl:value-of select="error/code" /><br/>
						message=<xsl:value-of select="error/message" /><br/>
						type=<xsl:value-of select="error/type" /><br/>
						location=<xsl:value-of select="error/loaction" /><br/>						
						name=<xsl:value-of select="error/name" /><br/>
						exception=<xsl:value-of select="error/exception" /><br/>

					</xsl:when>
					<xsl:when test="error/type = 'AUTHFAIL'">
						<font style="font-size:2em;">Authorization failed</font><br/>
						code=<xsl:value-of select="error/code" /><br/>
						message=<xsl:value-of select="error/message" /><br/>
						type=<xsl:value-of select="error/type" /><br/>
						location=<xsl:value-of select="error/loaction" /><br/>						
						name=<xsl:value-of select="error/name" /><br/>
						exception=<xsl:value-of select="error/exception" /><br/>
					</xsl:when>
					<xsl:otherwise>
						<font style="font-size:2em;">Internal server error</font><br/>
						code=<xsl:value-of select="error/code" /><br/>
						message=<xsl:value-of select="error/message" /><br/>
						type=<xsl:value-of select="error/type" /><br/>
						location=<xsl:value-of select="error/loaction" /><br/>						
						name=<xsl:value-of select="error/name" /><br/>
						exception=<xsl:value-of select="error/exception" /><br/>
					</xsl:otherwise>
				</xsl:choose>

<br/>
				<div
					style="z-index:999; margin-top:-2%; margin-left:22.5%; font-family:arial; font-size:0.71em">
					&#xA0;
					<a href="http://www.flabs.kz" target="_blank">F dev team</a>
					&#xA0; &#8226; &#xA0;
					<a href="http://www.flabser.com" target="_blank">Feedback</a>
				</div>
</h1>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>