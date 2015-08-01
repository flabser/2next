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
					<header class="header">
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
			<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
			<link rel="stylesheet" href="/SharedResources/css/normalize.css" />
			<link rel="stylesheet" href="css/common.css" />
			<link rel="stylesheet" href="css/layout.css" />
			<link rel="stylesheet" href="css/header.css" />
			<link rel="stylesheet" href="css/button.css" />
			<link rel="stylesheet" href="css/form.css" />
			<link rel="stylesheet" href="css/app.css" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
			<script type="text/javascript" src="js/app.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<div class="container">
			<nav class="navbar">
				<div class="navbar-header">
					<a class="navbar-brand" href="#">
						<xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				<ul class="nav navbar-nav navbar-right">
					<xsl:if test="//@userid = 'anonymous'">
						<xsl:if test="//@id = 'signup'">
							<li>
								<a href="?id=login">Sign in</a>
							</li>
						</xsl:if>
						<xsl:if test="//@id = 'login'">
							<li>
								<a href="?id=signup">Sign up</a>
							</li>
						</xsl:if>
					</xsl:if>
					<xsl:if test="//@userid != 'anonymous'">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								{{username}}
								<i class="fa fa-caret-down"></i>
							</a>
							<ul class="dropdown-menu">
								<li>
									<a href="#user-profile">My profile</a>
								</li>
								<li class="divider"></li>
								<li>
									<button class="logout" onclick="nubis.logOut()">
										<span>
											<xsl:value-of select="//captions/logout/@caption" />
										</span>
									</button>
								</li>
							</ul>
						</li>
					</xsl:if>
				</ul>
			</nav>
		</div>
	</xsl:template>

	<xsl:template name="_content" />

</xsl:stylesheet>
