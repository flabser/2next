<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="body_class" select="'wlc wlc-child-page'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<xsl:call-template name="section_promo" />

		<xsl:choose>
			<xsl:when test="//recovery-mode = 'password'">
				<xsl:call-template name="page_content_password" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="page_content_send" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="page_content_send">
		<div class="wlc-page-content">
			<div class="container">
				<h1>
					<xsl:value-of select="//captions/title/@caption" />
					<p>
						<xsl:value-of select="//captions/title_send_description/@caption" />
					</p>
				</h1>
				<section>
					<div class="error">
						<xsl:if test="//password-recovery//error">
							<xsl:value-of select="//password-recovery//error" />
						</xsl:if>
						<xsl:if test="//password-recovery//process">
							<xsl:value-of select="//password-recovery//process" />
						</xsl:if>
					</div>
					<form class="reg_form" action="" method="POST">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="password-recovery" />
						<div class="reg_form-inp">
							<input class="input span4" type="email" name="email" value="{//email}" placeholder="email" required="required" />
						</div>
						<div class="reg_form-inp-btn">
							<button class="btn-submit" type="submit" name="action" value="recovery">
								<xsl:value-of select="//captions/btn_recovery_send/@caption" />
							</button>
						</div>
					</form>
				</section>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="page_content_password">
		<div class="wlc-page-content">
			<div class="container">
				<h1>
					<xsl:value-of select="//captions/title/@caption" />
					<p>
						<xsl:value-of select="//captions/title_pwd_description/@caption" />
					</p>
				</h1>
				<section>
					<div class="error">
						<xsl:if test="//password-recovery//error">
							<xsl:value-of select="//password-recovery//error" />
						</xsl:if>
						<xsl:if test="//password-recovery//process">
							<xsl:value-of select="//password-recovery//process" />
						</xsl:if>
					</div>
					<form class="reg_form" action="" method="POST">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="password-recovery" />
						<input type="hidden" name="code" value="{//recovery-code}" />
						<div class="reg_form-inp">
							<input class="input span4" type="password" name="pwd" placeholder="new password" required="required" />
						</div>
						<div class="reg_form-inp-btn">
							<button class="btn-submit" type="submit" name="action" value="recovery">
								<xsl:value-of select="//captions/btn_recovery_pwd/@caption" />
							</button>
						</div>
					</form>
				</section>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
