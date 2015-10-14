<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="about">
		<section class="section about" id="about">

			<!-- about -->
			<div class="container animated fadeIn">
				<div class="row">
					<xsl:call-template name="about_feature" />
				</div>
			</div>
		</section>
	</xsl:template>

	<xsl:template name="about_feature">
		<div class="feature_box col-md-12">
			<div class="inner">
				<p>
					<xsl:value-of select="//captions/about_text1/@caption" />
				</p>
			</div>
		</div>
		<div class="feature_box col-md-12">
			<div class="inner">
				<p>
					<xsl:value-of select="//captions/about_text2/@caption" />
				</p>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
