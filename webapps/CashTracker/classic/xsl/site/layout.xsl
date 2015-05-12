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
					<xsl:attribute name="class" select="concat('wlc no_transition touch mobile ',  $body_class)" />
				</xsl:if>
				<div class="content-overlay js-content-overlay"></div>
				<xsl:if test="@userid = 'anonymous'">
					<div class="login js-login-form">
						<div class="login-dlg-close" onclick="nbApp.wlc.loginFormClose()">x</div>
						<h4>
							<xsl:value-of select="//captions/login_title/@caption" />
						</h4>
						<form action="Login" method="post" name="login-form" class="login-form">
							<input type="hidden" name="type" value="login" />
							<input class="input" type="text" name="login" value="" required="required" placeholder="{//captions/login_login/@caption}" />
							<input class="input" type="password" name="pwd" value="" required="required" placeholder="{//captions/login_pwd/@caption}" />
							<div class="login-form-bottom">
								<label class="noauth">
									<input type="checkbox" name="noauth" value="1" />
									<xsl:value-of select="//captions/login_alien_device/@caption" />
								</label>
								<button type="submit" class="btn">
									<xsl:value-of select="//captions/login_button/@caption" />
								</button>
							</div>
						</form>
						<div class="social">
							<a href="#vk" rel="nofollow">
								<i class="social-icon-vk" />
							</a>
							<a href="#fb" rel="nofollow">
								<i class="social-icon-fb" />
							</a>
							<a href="#twitter" rel="nofollow">
								<i class="social-icon-twitter" />
							</a>
						</div>
						<footer>
							<a href="?type=page&amp;id=password-recovery" rel="nofollow">
								<xsl:value-of select="//captions/lost_password/@caption" />
							</a>
							<br />
						</footer>
					</div>
				</xsl:if>
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
			<link type="text/css" rel="stylesheet" href="classic/css/all.min.css" />

			<xsl:call-template name="STYLE_FIX_FIELDSET" />

			<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.11.0.min.js"></script>
			<script type="text/javascript" src="/SharedResources/jquery/js/cookie/jquery.cookie.js"></script>
			<script type="text/javascript" src="/SharedResources/js/mobile-detect.min.js"></script>
			<script type="text/javascript" src="classic/js/app.min.js"></script>
			<script type="text/javascript" src="/SharedResources/js/jso/build/jso.js"></script>
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
					<a class="nav-item" href="?type=page&amp;id=about">
						<xsl:value-of select="//captions/about/@caption" />
					</a>
					<xsl:if test="@userid = 'anonymous'">
						<a href="#" class="nav-item btn-login js-ShowLoginForm">
							<span>
								<xsl:value-of select="//captions/login_button/@caption" />
							</span>
						</a>
					</xsl:if>
					<xsl:if test="@userid != 'anonymous'">
						<div class="nav-dropdown">
							<a class="nav-title username" href="#" onclick="return false;">
								<xsl:value-of select="@username" />
							</a>
							<div class="nav-dropdown-menu">
								<a class="nav-item" href="?type=page&amp;id=operations">
									<xsl:value-of select="//captions/operations/@caption" />
								</a>
								<a class="nav-item" href="?type=edit&amp;element=userprofile&amp;id=userprofile">
									<xsl:value-of select="//captions/user_profile/@caption" />
								</a>
								<a class="nav-item btn-logout" href="Logout">
									<span>
										<xsl:value-of select="//captions/sign_out/@caption" />
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
						<copy>@copyright</copy>
					</li>
				</ul>
				<div class="clearfix" />
			</div>
		</footer>
	</xsl:template>

	<xsl:template match="availablelangs">
		<xsl:if test="count(query/entry[viewcontent/viewtext = 'ON']) > 1">
			<li class="select-lang">
				<a href="#" onclick="return false;" class="lang-title">
					<i>
						<xsl:attribute name="class">
							<xsl:value-of select="concat('lang-icon-', lower-case(//@lang))" />
						</xsl:attribute>
					</i>
				</a>
				<div class="langs" onclick="return void(0)">
					<xsl:apply-templates select="query/entry[viewcontent/viewtext = 'ON']" mode="lang" />
				</div>
			</li>
		</xsl:if>
	</xsl:template>

	<xsl:template match="entry" mode="lang">
		<a class="lang" href="#" onclick="nbApp.wlc.setLang('{viewcontent/viewtext1}')">
			<xsl:value-of select="viewcontent/viewtext2" />
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
						Сервис для учета и планирования
						<div>корпоративных и семейных финансов</div>
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
						<xsl:value-of select="//captions/reg_title/@caption" />
					</h1>
					<form action="Provider" method="post" name="form-reg" class="reg_form">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="reg-user" />
						<section>
							<div class="reg_form-inp">
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
							<xsl:value-of select="//captions/or/@caption" />
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
