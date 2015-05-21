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
					<xsl:value-of select="//captions/help/@caption" />
				</h1>
				<section>
					<ul class="help-list">
						<li>
							<a href="?type=page&amp;id=retry-send-verify-email" rel="nofollow">
								<xsl:value-of select="//captions/no_verify_mail/@caption" />
							</a>
						</li>
						<li>
							<a href="?type=page&amp;id=password-recovery" rel="nofollow">
								<xsl:value-of select="//captions/lost_password/@caption" />
							</a>
						</li>
					</ul>
				</section>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
