<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="page_content">
		<div class="container">
			<div class="signup">
				<h1 class="text-center">
					<xsl:value-of select="//captions/signup_title/@caption" />
				</h1>
				<form method="post">
					<input class="form-control" type="text" name="login" value="" required="required" placeholder="{//captions/username/@caption}" />
					<input class="form-control" type="password" name="pwd" value="" required="required" placeholder="{//captions/password/@caption}" />
					<div class="form-group">
						<button type="submit" class="btn btn-default">
							<xsl:value-of select="//captions/signup_btn/@caption" />
						</button>
					</div>
				</form>
			</div>
			<footer>
				<a href="?id=signin">Sign In</a>
			</footer>
		</div>
	</xsl:template>

</xsl:stylesheet>
