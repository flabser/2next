<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(//captions/title/@caption, ' - ', //captions/brand/@caption)" />
		<xsl:param name="include" select="''" />
		<xsl:param name="body_class" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html_head">
				<xsl:with-param name="w_title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="no_transition {$body_class}">
				<div class="layout">
					<header class="layout_header">
						<xsl:call-template name="main-header" />
					</header>
					<section class="content">
						<xsl:call-template name="_content" />
					</section>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="html_head">
		<xsl:param name="include" select="''" />
		<xsl:param name="w_title" select="''" />
		<head>
			<title>
				<xsl:value-of select="$w_title" />
			</title>
			<link rel="shortcut icon" href="favicon.ico" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
			<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
			<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css" />
			<link rel="stylesheet" href="css/app.css" />

			<xsl:call-template name="STYLE_FIX_FIELDSET" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
			<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
			<script type="text/javascript" src="js/app.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<div class="container">
			<nav class="navbar navbar-default">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
						aria-expanded="false">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">
						<xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
								{{username}}
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li>
									<a href="#user-profile">My profile</a>
								</li>
								<li role="separator" class="divider"></li>
								<li>
									<a href="#logout" onclick="nubis.logOut()">Log out</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</nav>
		</div>
	</xsl:template>

	<!-- -->
	<xsl:template name="_content">
		<xsl:call-template name="page_content" />
	</xsl:template>

	<xsl:template name="page_content" />

</xsl:stylesheet>
