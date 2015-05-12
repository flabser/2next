<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />
	<xsl:import href="templates/outline.xsl" />
	<xsl:import href="templates/nav-ws.xsl" />
	<xsl:import href="templates/view.xsl" />
	<xsl:import href="templates/actions.xsl" />
	<xsl:import href="templates/saldo.xsl" />
	<xsl:import href="templates/utils.xsl" />

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
			<body class="no_transition {$body_class}">
				<div class="content-overlay js-content-overlay"></div>
				<div class="layout {$aside_collapse}">
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
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

			<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" />
			<link type="text/css" rel="stylesheet"
				href="/SharedResources/jquery/css/jquery-ui-1.10.4.custom/css/smoothness/jquery-ui-1.10.4.custom.min.css" />
			<link type="text/css" rel="stylesheet" href="classic/css/all.min.css" />

			<xsl:call-template name="STYLE_FIX_FIELDSET" />

			<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.11.0.min.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/jquery-ui-1.10.4.custom.min.js"></script>

			<script type="text/javascript" src="/SharedResources/jquery/js/ui/minified/i18n/jquery.ui.datepicker-ru.min.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/cookie/jquery.cookie.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/scrollTo/scrollTo.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/jquery.number.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/jquery.xml2json.js"></script>
			<script type="text/javascript" src="/SharedResources/js/mobile-detect.min.js"></script>

			<script type="text/javascript" src="classic/js/app.build.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<div class="main-header">
			<div class="nav-app-toggle js-toggle-nav-app"></div>
			<div class="brand">
				<img alt="logo" src="{$APP_LOGO_IMG_SRC}" class="brand-logo" />
				<span class="brand-title">
					<xsl:value-of select="$APP_NAME" />
				</span>
			</div>
			<div class="nav">
				<xsl:if test="not(document)">
					<div class="nav-item nav-add">
						<button class="add-new" onclick="alert('была бы такая кнопка, нажимаешь а она все делает')">
							<i class="fa fa-pencil-square-o" />
						</button>
					</div>
					<div class="nav-item nav-search">
						<div class="search-toggle" title="Поиск" onclick="nbApp.toggleSearchForm()">
							<i class="fa fa-search" />
						</div>
						<div class="search" id="search-form-block">
							<div class="search-toggle-back" onclick="nbApp.toggleSearchForm()">
								<i class="fa fa-chevron-left" />
							</div>
							<form action="Provider" method="get" name="search">
								<input type="hidden" name="type" value="page" />
								<input type="hidden" name="id" value="search" />
								<input type="search" name="keyword" value="{//search_keyword}" class="search-keyword" required="required"
									placeholder="Поиск" />
								<button type="submit" class="search-button" title="Поиск" value="">
									<i class="fa fa-search" />
								</button>
							</form>
						</div>
					</div>
				</xsl:if>
				<div class="nav-item nav-toggle">
					<div class="nav-ws-toggle js-toggle-nav-ws"></div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="main-footer" />

</xsl:stylesheet>
