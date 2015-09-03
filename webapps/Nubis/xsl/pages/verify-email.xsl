<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<section class="page-content verify-email">
			<div class="container">
				<div class="page-header">
					<h1>
						<xsl:value-of select="//captions/verify_email_title/@caption" />
					</h1>
				</div>
				<div>
					<xsl:choose>
						<xsl:when test="//process = 'verify-ok'">
							<xsl:value-of select="//captions/verify_email_done/@caption" />
							<p>
								<a href="?id=login">Sign In</a>
							</p>
						</xsl:when>
						<xsl:when test="//process = 'already-registered'">
							<div class="alert alert-info" role="alert">
								<xsl:value-of select="//captions/verify_email_already/@caption" />
							</div>
						</xsl:when>
						<xsl:otherwise>
							<div class="alert alert-danger" role="alert">
								<xsl:value-of select="//captions/verify_email_error/@caption" />
							</div>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</div>
		</section>
	</xsl:template>

</xsl:stylesheet>
