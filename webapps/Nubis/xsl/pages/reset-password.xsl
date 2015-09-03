<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="body_class" select="'wlc wlc-child-page'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<xsl:choose>
			<xsl:when test="//recovery-mode = 'password'">
				<xsl:call-template name="page_content_password" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="page_content_send" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="page_content_send">
		<div class="reset-password">
			<div class="page-header">
				<h1>
					<xsl:value-of select="//captions/pwd_recovery_title/@caption" />
					<br />
					<small>
						<xsl:value-of select="//captions/pwd_recovery_description/@caption" />
					</small>
				</h1>
			</div>
			<section>
				<div class="error">
					<xsl:if test="//password-recovery//error">
						<xsl:value-of select="//password-recovery//error" />
					</xsl:if>
					<xsl:if test="//password-recovery//process">
						<xsl:value-of select="//password-recovery//process" />
					</xsl:if>
				</div>
				<form class="form-inline" method="POST">
					<input type="hidden" name="id" value="reset_password" />
					<div class="form-group">
						<input type="email" class="form-control" name="email" value="{//email}" placeholder="email" required="required" />
					</div>
					<button type="submit" class="btn btn-success" name="action" value="send">
						<xsl:value-of select="//captions/pwd_recovery_btn_send/@caption" />
					</button>
				</form>
			</section>
		</div>
	</xsl:template>

	<xsl:template name="page_content_password">
		<div class="reset-password">
			<div class="page-header">
				<h1>
					<xsl:value-of select="//captions/pwd_recovery_title/@caption" />
					<br />
					<small>
						<xsl:value-of select="//captions/pwd_recovery_new_pwd/@caption" />
					</small>
				</h1>
			</div>
			<section>
				<div class="error">
					<xsl:if test="//password-recovery//error">
						<xsl:value-of select="//password-recovery//error" />
					</xsl:if>
					<xsl:if test="//password-recovery//process">
						<xsl:value-of select="//password-recovery//process" />
					</xsl:if>
				</div>
				<form class="form-inline" method="POST">
					<input type="hidden" name="id" value="reset_password" />
					<input type="hidden" name="code" value="{//recovery-code}" />
					<div class="form-group">
						<input type="password" class="form-control" name="pwd" required="required" />
					</div>
					<button type="submit" class="btn btn-success" name="action" value="new">
						<xsl:value-of select="//captions/pwd_recovery_btn_ok/@caption" />
					</button>
				</form>
			</section>
		</div>
	</xsl:template>

</xsl:stylesheet>
