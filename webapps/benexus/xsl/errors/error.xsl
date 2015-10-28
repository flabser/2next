<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" />

	<xsl:template match="/">
		<html>
			<head>
				<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
				<title>
					2Next - Error -
					<xsl:value-of select="error/apptype" />
				</title>
				<style>
					header { border-bottom: 1px solid #f2f2f2; }
					footer { border-top: 1px solid #f2f2f2; }
					a, span { margin: 0 5px; }
				</style>
			</head>
			<body>
				<header>
					<h1>
						<xsl:value-of select="error/apptype" />
					</h1>
				</header>
				<section>
					<xsl:choose>
						<xsl:when test="error/type = 'APPLICATION'">
							<h2>Application error</h2>
						</xsl:when>
						<xsl:when test="error/type = 'AUTHFAIL'">
							<h2>Authorization failed</h2>
						</xsl:when>
						<xsl:otherwise>
							<h2>Internal server error</h2>
						</xsl:otherwise>
					</xsl:choose>
					<ul>
						<li>
							code=
							<xsl:value-of select="error/code" />
						</li>
						<li>
							message=
							<xsl:value-of select="error/message" />
						</li>
						<li>
							type=
							<xsl:value-of select="error/type" />
						</li>
						<li>
							location=
							<xsl:value-of select="error/loaction" />
						</li>
						<li>
							name=
							<xsl:value-of select="error/name" />
						</li>
						<li>
							exception=
							<xsl:value-of select="error/exception" />
						</li>
					</ul>
				</section>
				<footer>
					<p>
						<span>
							version
							<xsl:value-of select="error/server" />
						</span>
						<span>F dev team 2015</span>
					</p>
					<a href="http://www.flabs.kz" target="_blank">F dev team</a>
					<a href="http://www.flabser.com" target="_blank">Feedback</a>
				</footer>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>
