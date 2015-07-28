<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="page_content">
		<div class="container">
			<div class="signin">
				<h1 class="text-center">
					<xsl:value-of select="//captions/signin_title/@caption" />
				</h1>
				<form method="post" name="signin">
					<input class="form-control" type="email" name="login" required="required" placeholder="E-mail" />
					<input class="form-control" type="password" name="pwd" required="required" placeholder="{//captions/password/@caption}" />
					<div class="form-group">
						<button type="submit" class="btn btn-default">
							<xsl:value-of select="//captions/signin_btn/@caption" />
						</button>
					</div>
				</form>
			</div>
			<footer>
				<a href="?id=signup">Sign Up</a>
			</footer>
		</div>
	</xsl:template>

</xsl:stylesheet>
