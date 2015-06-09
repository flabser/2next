<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/constants.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(//captions/title/@caption, ' - ', $APP_NAME)" />
		<xsl:param name="include" select="''" />
		<xsl:param name="body_class" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html_head">
				<xsl:with-param name="w_title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="wlc no_transition {$body_class}">
				<xsl:if test="$UI_CLIENT = 'mobile'">
					<xsl:attribute name="class" select="concat('wlc no_transition phone ',  $body_class)" />
				</xsl:if>
				<div class="content-overlay" id="content-overlay"></div>
				<div class="main-load" id="main-load"></div>
				<div class="layout">
					<header class="layout_header">
						<xsl:call-template name="main-header" />
					</header>
					<section class="layout_content">
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
			<link type="text/css" rel="stylesheet" href="css/all.min.css" />

			<xsl:call-template name="STYLE_FIX_FIELDSET" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-1.11.3.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery.cookie.min.js"></script>
			<script type="text/javascript" src="/SharedResources/js/mobile-detect.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/jso/build/jso.js"></script>
			<script type="text/javascript" src="js/app.min.js"></script>
			<script type="text/javascript">
				$(document).ready(nbApp.wlc.init);
			</script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<div class="main-header">
			<div class="container">
				<nav class="header-nav pull-left">
					<a class="nav-item brand" href="?type=page&amp;id=welcome">
						<img alt="logo" src="{$APP_LOGO_IMG_SRC}" class="brand-logo" />
						<span class="brand-title">
							<xsl:value-of select="$APP_NAME" />
						</span>
					</a>
				</nav>
				<nav class="header-nav pull-right">
					<xsl:if test="@id != 'login'">
						<a class="nav-item" href="?type=page&amp;id=about">
							<xsl:value-of select="//captions/about/@caption" />
						</a>
					</xsl:if>
					<xsl:if test="@id = 'login' and (@userid = 'anonymous' or @userid = '')">
						<a href="?type=page&amp;id=welcome" class="nav-item btn-login">
							<span>
								<xsl:value-of select="//captions/reg/@caption" />
							</span>
						</a>
					</xsl:if>
					<xsl:if test="@id != 'login' and (@userid = 'anonymous' or @userid = '')">
						<a href="?type=page&amp;id=login" class="nav-item btn-login">
							<span>
								<xsl:value-of select="//captions/login/@caption" />
							</span>
						</a>
					</xsl:if>
					<xsl:if test="@userid != 'anonymous' and @userid != ''">
						<div class="nav-dropdown user-menu">
							<a class="nav-title username" href="#" onclick="return false;">
								<i class="fa fa-user user-icon"></i>
								<span>
									<xsl:value-of select="@userid" />
								</span>
								<i class="fa fa-caret-down"></i>
							</a>
							<div class="nav-dropdown-menu">
								<a class="nav-item" href="?type=page&amp;id=transactions">
									<xsl:value-of select="//captions/operations/@caption" />
								</a>
								<a class="nav-item" href="?type=edit&amp;element=userprofile&amp;id=userprofile">
									<xsl:value-of select="//captions/user_profile/@caption" />
								</a>
								<a class="nav-item btn-logout" href="Logout">
									<span>
										<xsl:value-of select="//captions/logout/@caption" />
									</span>
								</a>
							</div>
						</div>
					</xsl:if>
				</nav>
				<div class="clearfix" />
			</div>
		</div>
	</xsl:template>

	<xsl:template name="main-footer">
		<div class="footer-spacer"></div>
		<footer class="layout_footer">
			<div class="container">
				<ul class="footer-nav container">
					<li>
						<a href="?type=page&amp;id=privacy">
							<xsl:value-of select="//captions/privacy/@caption" />
						</a>
					</li>
					<li>
						<a href="?type=page&amp;id=terms">
							<xsl:value-of select="//captions/terms/@caption" />
						</a>
					</li>
					<li>
						<a href="?type=page&amp;id=help">
							<xsl:value-of select="//captions/help/@caption" />
						</a>
					</li>
					<li>
						<a href="?type=page&amp;id=about">
							<xsl:value-of select="//captions/about/@caption" />
						</a>
					</li>
					<xsl:apply-templates select="//availablelangs" />
					<li class="copy">
						<copy>
							<xsl:value-of select="//captions/copyright/@caption" />
						</copy>
					</li>
				</ul>
				<div class="clearfix" />
			</div>
		</footer>
	</xsl:template>

	<xsl:template match="availablelangs">
		<xsl:if test="count(value[entry = 'ON']) > 1">
			<li class="select-lang-li">
				<div class="select-lang">
					<a href="#" onclick="return false;" class="lang-title">
						<i>
							<xsl:attribute name="class">
								<xsl:value-of select="concat('lang-icon-', lower-case(//@lang))" />
							</xsl:attribute>
						</i>
					</a>
					<div class="langs" onclick="return void(0)">
						<xsl:apply-templates select="value[entry[1] = 'ON']" mode="lang" />
					</div>
				</div>
			</li>
		</xsl:if>
	</xsl:template>

	<xsl:template match="value" mode="lang">
		<a class="lang" href="#" onclick="nbApp.wlc.setLang('{entry[2]}')">
			<xsl:value-of select="entry[3]" />
		</a>
	</xsl:template>

	<!-- -->
	<xsl:template name="_content">
		<xsl:call-template name="section_promo" />
		<xsl:call-template name="page_content" />
		<xsl:call-template name="section_reg" />
	</xsl:template>

	<xsl:template name="page_content" />

	<xsl:template name="section_promo">
		<div class="wlc_promo">
			<div class="wlc_promo-inner">
				<div class="wlc_promo-content container">
					<h1>
						<xsl:value-of select="//captions/promo_line1/@caption" />
						<div>
							<xsl:value-of select="//captions/promo_line2/@caption" />
						</div>
					</h1>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="section_reg">
		<div class="wlc_reg">
			<div class="wlc_reg-inner">
				<div class="container">
					<h1 class="wlc_reg-header">
						<xsl:value-of select="//captions/reg/@caption" />
					</h1>
					<h2 class="reg-result-ok"></h2>
					<form action="Provider" method="post" name="form-reg" class="reg_form">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="reg-user" />
						<section>
							<div class="reg_form-inp" style="display:none;">
								<input class="input" type="text" name="budget_name" value="" autocomplete="off"
									placeholder="{//captions/reg_budget_name/@caption}" title="{//captions/reg_budget_name/@caption}" />
								<div class="reg_form-inp-inf">
									<xsl:value-of select="//captions/reg_budget_name/@caption" />
								</div>
							</div>
							<div class="reg_form-inp">
								<input class="input" type="email" name="email" value="" required="required" autocomplete="off"
									placeholder="{//captions/reg_email/@caption}" title="{//captions/reg_email/@caption}" />
								<div class="reg_form-inp-inf">
									<xsl:value-of select="//captions/reg_email_inf/@caption" />
								</div>
								<div class="reg_form-invalid reg-email-invalid">
									<xsl:value-of select="//captions/reg_email_invalid/@caption" />
								</div>
								<div class="reg_form-invalid reg-email-exists">
									<xsl:value-of select="//captions/reg_email_exists/@caption" />
								</div>
							</div>
							<div class="reg_form-inp">
								<input class="input" type="password" name="pwd" value="" required="required" placeholder="{//captions/reg_pwd/@caption}"
									title="{//captions/reg_pwd/@caption}" />
								<div class="reg_form-inp-inf">
									<xsl:value-of select="//captions/reg_pwd_inf/@caption" />
								</div>
								<div class="reg_form-invalid reg-pwd-weak">
									<xsl:value-of select="//captions/reg_pwd_weak/@caption" />
								</div>
							</div>
							<div class="reg_form-inp-btn">
								<button type="submit" class="btn-submit">
									<xsl:value-of select="//captions/reg_button/@caption" />
								</button>
							</div>
						</section>
					</form>
					<div class="terms">
						<a href="?type=page&amp;id=terms">
							<xsl:value-of select="//captions/reg_terms/@caption" />
						</a>
					</div>
					<div class="social">
						<div class="or">
							<xsl:value-of select="//captions/login_or/@caption" />
						</div>
						<div>
							<xsl:value-of select="//captions/use_oauth/@caption" />
						</div>
						<section>
							<a href="#vk" rel="nofollow">
								<i class="social-icon-vk" />
							</a>
							<a href="#fb" rel="nofollow">
								<i class="social-icon-fb" />
							</a>
							<a href="#twitter" rel="nofollow">
								<i class="social-icon-twitter" />
							</a>
						</section>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
