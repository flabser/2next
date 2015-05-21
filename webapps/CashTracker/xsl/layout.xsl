<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />
	<xsl:import href="templates/outline.xsl" />
	<xsl:import href="templates/nav-ws.xsl" />
	<xsl:import href="templates/view.xsl" />
	<xsl:import href="templates/actions.xsl" />
	<xsl:import href="templates/saldo.xsl" />
	<xsl:import href="templates/utils.xsl" />

	<xsl:variable name="isAjaxRequest" select="//ajax = '1'" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:decimal-format name="df" grouping-separator=" " />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(//captions/viewnamecaption/@caption, ' - ', $APP_NAME)" />
		<xsl:param name="active_aside_id" select="//app_menu/response/content/current/entry/@id" />
		<xsl:param name="aside_collapse" select="''" />
		<xsl:param name="include" select="''" />
		<xsl:param name="body_class" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html_head">
				<xsl:with-param name="w_title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="no_transition {$body_class}" onresize="nbApp.uiWindowResize()">
				<div class="main-load" id="main-load"></div>
				<div class="layout layout_header-fixed {$aside_collapse}">
					<div class="content-overlay" id="content-overlay"></div>
					<header class="layout_header">
						<xsl:call-template name="main-header" />
					</header>
					<xsl:apply-templates select="//app_menu" mode="outline">
						<xsl:with-param name="active-entry-id" select="$active_aside_id" />
					</xsl:apply-templates>
					<section class="layout_content">
						<xsl:call-template name="_content" />
					</section>
					<xsl:apply-templates select="//availableapps" mode="ws" />
					<xsl:call-template name="main-footer" />
				</div>
				<xsl:call-template name="util-js-mark-as-read" />
			</body>
		</html>
	</xsl:template>

	<xsl:template name="_content" />

	<xsl:template name="html_head">
		<xsl:param name="include" select="''" />
		<xsl:param name="w_title" select="''" />
		<head>
			<title>
				<xsl:value-of select="$w_title" />
			</title>
			<link rel="shortcut icon" href="favicon.ico" />
			<meta name="format-detection" content="telephone=no" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

			<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
			<link type="text/css" rel="stylesheet" href="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.css" />
			<link type="text/css" rel="stylesheet" href="css/all.min.css" />

			<xsl:call-template name="STYLE_FIX_FIELDSET" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-1.11.3.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery.cookie.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery.scrollTo.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery.mobile.touch.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery.number.min.js"></script>
			<script type="text/javascript" src="/SharedResources/js/mobile-detect.min.js"></script>

			<script type="text/javascript" src="js/app.build.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<div class="main-header">
			<div class="head-item head-nav-app-toggle" id="toggle-nav-app">
				<div class="nav-app-toggle"></div>
			</div>
			<div class="head-item brand">
				<img alt="logo" src="{$APP_LOGO_IMG_SRC}" class="brand-logo" />
				<span class="brand-title">
					<xsl:value-of select="$APP_NAME" />
				</span>
			</div>
			<div class="head-item head-nav-ws-toggle">
				<div class="nav-ws-toggle" id="toggle-nav-ws"></div>
			</div>
			<xsl:if test="not(document)">
				<div class="head-item nav-search-toggle" title="Поиск" id="toggle-head-search">
					<i class="fa fa-search" />
				</div>
				<div class="head-item nav-search" id="search-block">
					<div class="search-toggle-back" id="search-close">
						<i class="fa fa-chevron-left" />
					</div>
					<form action="Provider" method="GET" name="search">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="search" />
						<input type="search" name="keyword" value="{//search_keyword}" class="search-keyword" required="required"
							placeholder="Поиск" />
						<button type="submit" class="search-btn" title="Поиск">
							<i class="fa fa-search" />
						</button>
					</form>
				</div>
				<xsl:apply-templates select="//actionbar/action[@id = 'add_new']" mode="header" />
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template name="main-footer" />

	<xsl:template match="action" mode="header">
		<a class="no-desktop head-item nav-action" title="{@hint}" href="#" data-action="{@id}">
			<xsl:if test="js">
				<xsl:attribute name="href" select="concat('javascript:', js)" />
			</xsl:if>
			<xsl:if test="@url != ''">
				<xsl:attribute name="href" select="@url" />
			</xsl:if>
			<xsl:choose>
				<xsl:when test="@id = 'add_new'">
					<i class="fa fa-plus" />
				</xsl:when>
				<xsl:when test="@id = 'delete_document'">
					<i class="fa fa-remove" />
				</xsl:when>
			</xsl:choose>
			<span class="action-label">
				<xsl:value-of select="@caption" />
			</span>
		</a>
	</xsl:template>

</xsl:stylesheet>
