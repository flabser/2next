<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="about">
		<section class="section about" id="about">

			<!-- ABOUT -->
			<div class="container">
				<div class="row">
					<div class="col-xs-12 animated fadeIn">
						<h3 class="heading">
							<xsl:value-of select="//captions/about_company/@caption" />
							<hr />
						</h3>
					</div>
				</div>
				<div class="row">
					<xsl:call-template name="about_feature" />
				</div>
			</div>
		</section>
	</xsl:template>

	<xsl:template name="about_feature">
		<div class="feature_box col-md-6">
			<div class="inner animated fadeIn">
				<i class="fa fa-university"></i>
				<h3>
					<xsl:value-of select="//captions/about_us/@caption" />
				</h3>
				<p>
					<xsl:value-of select="//captions/about_us_text/@caption" />
				</p>
			</div>
		</div>
		<div class="feature_box col-md-6">
			<div class="inner animated fadeIn">
				<i class="fa fa-hourglass"></i>
				<h3>
					<xsl:value-of select="//captions/promo_experience/@caption" />
				</h3>
				<p>
					<xsl:value-of select="//captions/about_experience_text/@caption" />
				</p>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
