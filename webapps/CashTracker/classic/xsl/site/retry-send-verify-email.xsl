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
					<xsl:value-of select="//captions/title/@caption" />
				</h1>
				<section>
					<div class="error">
						<xsl:if test="//retry-verify-email//error">
							<xsl:value-of select="//retry-verify-email//error" />
						</xsl:if>
						<xsl:if test="//retry-verify-email//process">
							<xsl:value-of select="//retry-verify-email//process" />
						</xsl:if>
					</div>
					<form class="reg_form" action="" method="POST">
						<input type="hidden" name="type" value="page" />
						<input type="hidden" name="id" value="retry-send-verify-email" />
						<div class="reg_form-inp">
							<input class="input span4" type="email" name="email" value="{//email}" placeholder="email" required="required" />
						</div>
						<div class="reg_form-inp-btn">
							<button class="btn-submit" type="submit" name="sendVerify" value="send">
								<xsl:value-of select="//captions/btn_verify_send/@caption" />
							</button>
						</div>
					</form>
				</section>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
