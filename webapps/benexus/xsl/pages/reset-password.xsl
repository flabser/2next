<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="page-content reset-password">
			<div class="container">			
					<xsl:call-template name="page_content_send" />					
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
			<script>
				function reset_password(){
				data=$('.form-inline').serialize();
				$.ajax({
				type: "POST",
				url: "rest/session/resetpassword",
				cache:false,
				data:data,
				success: function (msg){
				//window.location.reload()
				},
				error: function(data,status,xhr) {
				}
				})
				}
			</script>
			<form class="form-inline" method="POST">

				<div class="form-group">
					<input type="email" class="form-control" name="email" value="{//email}" placeholder="email" required="required" />
				</div>
				<button type="button" class="btn btn-success" name="action" onclick="javascript:reset_password()">
					<xsl:value-of select="//captions/pwd_reset_send_btn/@caption" />
				</button>
<!--				<button type="submit" class="btn btn-success" name="action" value="send">
					<xsl:value-of select="//captions/pwd_reset_send_btn/@caption" />
				</button>-->
				<!--<input type="hidden" name="id" value="reset_password" />-->
			</form>
		</section>
	</xsl:template>



</xsl:stylesheet>
