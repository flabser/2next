<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="body_class" select="'wlc wlc-child-page'" />
			<xsl:with-param name="include">
				<style>
					.v_process {
						background:#D9EDF7;border:1px solid #BCE8F1;color:#31708F;display:inline-block;padding:.5em;border-radius:6px;margin:.2em;
					}
					.v_error {
						background:#F2DEDE;border:1px solid #EBCCD1;color:#A94442;display:inline-block;padding:.5em;border-radius:6px;margin:.2em;
					}
				</style>
			</xsl:with-param>
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
					<xsl:apply-templates select="//error" mode="verify-result" />
					<xsl:apply-templates select="//process" mode="verify-result" />
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

	<xsl:template match="error[text() = 'invalid-email']" mode="verify-result">
		<p class="v_error">
			<xsl:value-of select="//invalid_email/@caption" />
		</p>
	</xsl:template>

	<xsl:template match="error[text() = 'no-user']" mode="verify-result">
		<p class="v_error">
			<xsl:value-of select="//no_user/@caption" />
		</p>
	</xsl:template>

	<xsl:template match="process[text() = 'user-verified']" mode="verify-result">
		<p class="v_process">
			<xsl:value-of select="//user_already_verified/@caption" />
		</p>
	</xsl:template>

	<xsl:template match="error[text() = 'verify-email']" mode="verify-result">
		<p class="v_error">
			<xsl:value-of select="//error_verify_email/@caption" />
		</p>
	</xsl:template>

	<xsl:template match="process[text() = 'verify-email-send']" mode="verify-result">
		<p class="v_process">
			<xsl:value-of select="//retry_send_verify_email_send_ok/@caption" />
		</p>
	</xsl:template>

</xsl:stylesheet>
