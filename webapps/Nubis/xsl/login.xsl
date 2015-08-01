<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="container">
			<section class="login">
				<h1 class="text-center">
					<xsl:value-of select="//captions/login_title/@caption" />
				</h1>
				<div class="alert alert-danger" id="login-error" role="alert" style="display:none;">Login or password incorrect</div>
				<form method="post" name="signin">
					<div class="form-group">
						<input class="form-control" type="email" name="login" required="required" placeholder="E-mail" />
					</div>
					<div class="form-group">
						<input class="form-control" type="password" name="pwd" required="required" placeholder="{//captions/password/@caption}" />
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-default">
							<xsl:value-of select="//captions/login_btn/@caption" />
						</button>
					</div>
				</form>
			</section>
		</div>
	</xsl:template>

</xsl:stylesheet>
