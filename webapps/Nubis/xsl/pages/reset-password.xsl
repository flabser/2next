<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="page-content reset-password">
			<div class="container">
				<xsl:choose>
					<xsl:when test="//reset-mode = 'password'">
						<xsl:call-template name="page_content_password" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="page_content_send" />
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="page_content_send">
		<div class="page-header">
			<h1>
				<xsl:value-of select="//captions/pwd_reset_title/@caption" />
				<br />
				<small>
					<xsl:value-of select="//captions/pwd_reset_description/@caption" />
				</small>
			</h1>
		</div>
		<section>
			<div class="error">
				<xsl:if test="//password-reset//error">
					<xsl:value-of select="//password-reset//error" />
				</xsl:if>
				<xsl:if test="//password-reset//process">
					<xsl:value-of select="//password-reset//process" />
				</xsl:if>
			</div>
			<form class="form-inline" method="POST">
				<input type="hidden" name="id" value="reset_password" />
				<div class="form-group">
					<input type="email" class="form-control" name="email" value="{//email}" placeholder="email" required="required" />
				</div>
				<button type="submit" class="btn btn-success" name="action" value="send">
					<xsl:value-of select="//captions/pwd_reset_send_btn/@caption" />
				</button>
			</form>
		</section>
	</xsl:template>

	<xsl:template name="page_content_password">
		<div class="page-header">
			<h1>
				<xsl:value-of select="//captions/pwd_reset_title/@caption" />
				<br />
				<small>
					<xsl:value-of select="//captions/pwd_reset_new_pwd/@caption" />
				</small>
			</h1>
		</div>
		<section>
			<div class="error">
				<xsl:if test="//password-reset//error">
					<xsl:value-of select="//password-reset//error" />
				</xsl:if>
				<xsl:if test="//password-reset//process">
					<xsl:value-of select="//password-reset//process" />
				</xsl:if>
			</div>
			<form class="form-inline" method="POST">
				<input type="hidden" name="id" value="reset_password" />
				<input type="hidden" name="code" value="{//reset-code}" />
				<div class="form-group">
					<input type="password" class="form-control" name="pwd" required="required" />
				</div>
				<button type="submit" class="btn btn-success" name="action" value="new">
					<xsl:value-of select="//captions/pwd_reset_ok_btn/@caption" />
				</button>
			</form>
		</section>
	</xsl:template>

</xsl:stylesheet>
