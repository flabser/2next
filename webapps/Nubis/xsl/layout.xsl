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
					<xsl:call-template name="main-header" />
					<section class="content">
						<xsl:call-template name="_content" />
					</section>
					<xsl:call-template name="main-footer" />
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
			<link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css" />
			<link rel="stylesheet" href="css/layout.css" />
			<link rel="stylesheet" href="css/header.css" />
			<link rel="stylesheet" href="css/content.css" />
			<link rel="stylesheet" href="css/ws.css" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/bootstrap/js/bootstrap.min.js"></script>
			<script type="text/javascript" src="js/app.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<header class="header navbar navbar-static-top">
			<div class="container">
				<div class="navbar-header">
					<button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#nb-navbar"
						aria-controls="nb-navbar" aria-expanded="false">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="?id=ws">
						<xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				<nav id="nb-navbar" class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right">
						<xsl:if test="//@userid = 'anonymous'">
							<xsl:if test="//@id = 'signup' or //@id = 'reset_password' or //@id = 'verify_email'">
								<li class="login">
									<a href="?id=login">
										<xsl:value-of select="//captions/login/@caption" />
									</a>
								</li>
							</xsl:if>
							<xsl:if test="//@id = 'login' or //@id = 'reset_password' or //@id = 'verify_email'">
								<li class="signup">
									<a href="?id=signup">
										<xsl:value-of select="//captions/signup/@caption" />
									</a>
								</li>
							</xsl:if>
						</xsl:if>
						<xsl:if test="//@userid != 'anonymous'">
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
									<xsl:value-of select="//@userid" />
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<li>
										<a href="#user-profile">
											<xsl:value-of select="//captions/your_profile/@caption" />
										</a>
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
					<xsl:if test="//@userid != 'anonymous'">
						<form class="navbar-form navbar-right" role="search" onsubmit="alert('кручу-верчу найти хочу'); return false;">
							<input type="text" class="form-control" name="keyword" placeholder="Search" />
						</form>
					</xsl:if>
				</nav>
			</div>
		</header>
	</xsl:template>

	<xsl:template name="main-footer">
		<div class="footer-spacer"></div>
		<footer class="footer">
			<div class="container">
				@
			</div>
		</footer>
	</xsl:template>

	<xsl:template name="_content" />

</xsl:stylesheet>
