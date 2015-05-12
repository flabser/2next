<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/util-constants.xsl" />
	<xsl:import href="templates/nav-ws.xsl" />
	<xsl:import href="templates/view.xsl" />
	<xsl:import href="templates/sharedactions.xsl" />
	<xsl:import href="templates/saldo.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:decimal-format name="df" grouping-separator=" " />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(page/captions/viewnamecaption/@caption, ' - ', $APP_NAME)" />
		<xsl:param name="include" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html-head">
				<xsl:with-param name="title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="no_transition">
				<div class="layout">
					<div class="layout_canvas">
						<header class="layout_header">
							<xsl:call-template name="header-page" />
						</header>
						<section class="layout_content">
							<xsl:call-template name="_content" />
						</section>
						<xsl:apply-templates select="//availableapps" mode="ws" />
					</div>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="_content" />

	<xsl:template name="html-head">
		<xsl:param name="title" select="page/captions/viewnamecaption/@caption" />
		<xsl:param name="include" select="''" />

		<head>
			<title>
				<xsl:value-of select="concat($title, ' - ', $APP_NAME)" />
			</title>
			<link rel="shortcut icon" href="favicon.ico" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
			<meta name="format-detection" content="telephone=no" />
			<meta name="format-detection" content="address=no" />

			<link href="/SharedResources/jquery/css/jquery-ui-1.10.4.custom/css/smoothness/jquery-ui-1.10.4.custom.min.css"
				rel="stylesheet" />
			<link href="ipadandtab/css/all.min.css" rel="stylesheet" />

			<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.11.0.min.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/jquery-ui-1.10.4.custom.min.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/cookie/jquery.cookie.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/jquery.xml2json.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/ui/minified/i18n/jquery.ui.datepicker-ru.min.js"></script>

			<script type="text/javascript" src="ipadandtab/js/app.min.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="header-page">
		<div class="main-header">
			<div class="nav" role="navigation">
				<div class="nav-item">
					<a class="nav-brand" href="Provider?type=page&amp;id=alloperations&amp;page=0&amp;entryid=&amp;title=">
						<img alt="logo" src="{$APP_LOGO_IMG_SRC}" class="brand-logo" />
						<span class="app-title desktop">
							<xsl:value-of select="$APP_NAME" />
						</span>
					</a>
				</div>
				<div class="pull-right">
					<xsl:if test="//action[@id='new_document'][@mode='ON']">
						<div class="nav-item">
							<a class="nav-item add-operation splitter" href="{//action[@id='new_document']/@url}">
								<span class="add-operation-icon icon-plus"></span>
								<span class="desktop"> Добавить</span>
							</a>
						</div>
					</xsl:if>
					<div class="nav-item nav-search splitter">
						<div class="sub-menu">
							<a href="javascript:void(0)" class="menu-toggle" data-role="menu-toggle" data-class="active">
								<div class="icon-search" />
							</a>
							<div class="dropdown-menu search">
								<div class="menu-nav">
									<form action="Provider" method="get" name="search" class="search">
										<input type="hidden" name="type" value="page" />
										<input type="hidden" name="id" value="search" />
										<input type="search" name="keyword" class="keyword" value="{//current_outline_entry/response/content/search}"
											required="required" placeholder="Поиск" />
									</form>
								</div>
							</div>
						</div>
					</div>
					<div class="nav-item mobile splitter">
						<div class="sub-menu">
							<a href="javascript:void(0)" class="menu-toggle menu-toggle-bar" data-role="menu-toggle" data-class="active">
								<div class="menu-icon-bar" />
								<div class="menu-icon-bar" />
								<div class="menu-icon-bar" />
							</a>
							<div class="dropdown-menu profile">
								<ul class="menu-nav">
									<li class="menu-item">
										<a class="profile" href="Provider?type=edit&amp;element=userprofile&amp;id=userprofile">
											<span class="icon-user"></span>
											<xsl:value-of select="concat(' ', @username)" />
										</a>
									</li>
									<li class="menu-item">
										<a class="logout" href="Logout">
											<span class="icon-off"></span>
											Выйти
										</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="nav-item desktop splitter">
						<a class="profile" href="Provider?type=edit&amp;element=userprofile&amp;id=userprofile">
							<span class="icon-user"></span>
							<xsl:value-of select="concat(' ', @username)" />
						</a>
					</div>
					<div class="nav-item">
						<div class="nav-ws-toggle js-toggle-nav-ws"></div>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
