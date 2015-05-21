<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/constants.xsl" />
	<xsl:import href="../templates/pagination.xsl" />

	<xsl:output method="html" />

	<xsl:template match="/request">
		<div class="container">
			<xsl:call-template name="page-navigator" />
			<ul class="dialog-list">
				<xsl:for-each select="glossaries/roles/entry">
					<li>
						<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
							<input data-type="select" type="radio" name="chbox-roles" value="{name}" data-text="{name}" />
							<span class="input-label">
								<b>
									<xsl:value-of select="name" />
								</b>
								<xsl:text> - </xsl:text>
								<em style="color:#666;">
									<xsl:value-of select="description" />
								</em>
							</span>
						</label>
					</li>
				</xsl:for-each>
			</ul>
		</div>
	</xsl:template>

</xsl:stylesheet>
