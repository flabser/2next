<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<section class="form-signup">
			<div class="page-header">
				<h1 class="text-center">
					<xsl:value-of select="//captions/signup_title/@caption" />
				</h1>
			</div>
			<div class="alert alert-success reg-result-ok" role="alert" style="display:none;">reg-result-ok</div>
			<form method="post" name="signup">
				<div class="form-group">
					<input class="form-control" type="email" name="email" required="required" placeholder="E-mail" autocomplete="off" />
				</div>
				<div class="form-group">
					<input class="form-control" type="password" name="pwd" required="required" placeholder="{//captions/password/@caption}"
						autocomplete="off" />
				</div>
				<div class="text-right">
					<button type="submit" class="btn btn-default">
						<xsl:value-of select="//captions/signup_btn/@caption" />
					</button>
				</div>
			</form>
		</section>
	</xsl:template>

</xsl:stylesheet>
