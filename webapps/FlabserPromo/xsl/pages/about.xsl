<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="about">
		<section class="section about" id="about">

			<!-- ABOUT -->
			<div class="container">
				<div class="row">
					<div class="col-xs-12">
						<h3 class="heading">
							<xsl:value-of select="//captions/about_title/@caption" />
						</h3>
						<p class="heading__sub">
							<xsl:value-of select="//captions/about_title_sub/@caption" />
						</p>
					</div>
				</div>
			</div>

			<!-- FEATURES -->
			<div class="container">
				<div class="row">
					<div class="col-sm-4">

					</div>
					<div class="col-sm-4">

					</div>
					<div class="col-sm-4">

					</div>
					<div class="col-sm-4">

					</div>
					<div class="col-sm-4">

					</div>
					<div class="col-sm-4">

					</div>
				</div>
			</div>
		</section>
	</xsl:template>

</xsl:stylesheet>
