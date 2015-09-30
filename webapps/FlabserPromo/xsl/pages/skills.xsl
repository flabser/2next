<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="skills">
		<section class="section skills" id="skills">

			<!-- SKILLS -->
			<div class="container">
				<div class="row">
					<div class="col-xs-12">
						<h3 class="heading">
							<xsl:value-of select="//captions/skills_title/@caption" />
						</h3>
						<p class="heading__sub">
							<xsl:value-of select="//captions/skills_title_sub/@caption" />
						</p>
					</div>
				</div>
				<div class="row">

				</div>
			</div>
		</section>
	</xsl:template>

</xsl:stylesheet>
