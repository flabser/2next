<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="how-it-works">
		<section class="section how-it-works" id="how-it-works">

			<!-- how-it-works -->
			<div class="container animated fadeIn">
				<div class="row">
					<div class="col-xs-12">
						<h3 class="how-it-works__heading">
							<xsl:value-of select="//captions/how_it_works/@caption" />
						</h3>
					</div>
				</div>
				<div class="row">
					<xsl:call-template name="how_feature" />
				</div>
			</div>
		</section>
	</xsl:template>

	<xsl:template name="how_feature">
		<div class="feature_box col-md-4">
			<div class="inner">
				step 1 >
			</div>
		</div>
		<div class="feature_box col-md-4">
			<div class="inner">
				step 2 >
			</div>
		</div>
		<div class="feature_box col-md-4">
			<div class="inner">
				step 3
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
