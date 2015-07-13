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
		<xsl:call-template name="page_content" />
	</xsl:template>

	<xsl:template name="page_content">
		<div class="wlc-page-content">
			<div class="container">
				<h1>
					<xsl:value-of select="//captions/retry_send_verify_email_title/@caption" />
				</h1>
				<section>
					<xsl:if test="//error or //process">
						<h3>
							<xsl:if test="//process">
								<xsl:attribute name="style">
									background:#D9EDF7;border:1px solid #BCE8F1;color:#31708F;display:inline-block;padding:.5em;border-radius:6px;margin:.2em;
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="//error">
								<xsl:attribute name="style">
									background:#F2DEDE;border:1px solid #EBCCD1;color:#A94442;display:inline-block;padding:.5em;border-radius:6px;margin:.2em;
								</xsl:attribute>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="//error = 'invalid-email'">
									<xsl:value-of select="//invalid_email/@caption" />
								</xsl:when>
								<xsl:when test="//error = 'no-user'">
									<xsl:value-of select="//no_user/@caption" />
								</xsl:when>
								<xsl:when test="//process = 'user-verified'">
									<xsl:value-of select="//user_already_verified/@caption" />
								</xsl:when>
								<xsl:when test="//error = 'verify-email'">
									<xsl:value-of select="//error_verify_email/@caption" />
								</xsl:when>
							</xsl:choose>
							<xsl:if test="//process = 'verify-email-send'">
								<xsl:value-of select="//retry_send_verify_email_send_ok/@caption" />
							</xsl:if>
						</h3>
					</xsl:if>
					<div>
						<form class="reg_form" method="POST">
							<input type="hidden" name="type" value="page" />
							<input type="hidden" name="id" value="retry-send-verify-email" />
							<div class="reg_form-inp span5">
								<input class="input span5" type="email" name="email" value="{//email}" placeholder="email" required="required" />
							</div>
							<div class="reg_form-inp-btn">
								<button class="btn-submit" type="submit" name="sendVerify" value="send">
									<xsl:value-of select="//captions/retry_send_verify_email_btn_send/@caption" />
								</button>
							</div>
						</form>
					</div>
				</section>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
