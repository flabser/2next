<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="skills">
		<section class="section skills" id="skills">

			<!-- SKILLS -->
			<div class="container">
				<div class="row">
					<div class="col-xs-12 animated fadeIn">
						<h3 class="heading">
							<xsl:value-of select="//captions/skills_title/@caption" />
							<hr />
						</h3>
					</div>
				</div>
				<xsl:call-template name="skills_feature" />
			</div>
		</section>
	</xsl:template>

	<xsl:template name="skills_feature">
		<div class="row">
			<div class="skills_box skills_box-prog col-xs-6">
				<div class="inner">
					<h3>
						<xsl:value-of select="//captions/skills_prog_lang/@caption" />
					</h3>
					<ul>
						<li>Java (J2EE, Servlets, JPA, Jersey, JasperReports)</li>
						<li>Groovy</li>
						<li>Ember.js</li>
						<li>XSLT</li>
					</ul>
				</div>
			</div>
			<div class="skills_box skills_box-db col-xs-6">
				<div class="inner">
					<h3>
						<xsl:value-of select="//captions/skills_db/@caption" />
					</h3>
					<ul>
						<li>PostgreSQL</li>
						<li>Oracle</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="skills_box skills_box-mobile col-xs-6">
				<div class="inner">
					<h3>
						<xsl:value-of select="//captions/skills_mobile_tech/@caption" />
					</h3>
					<ul>
						<li>Android Applications</li>
						<li>IOS Applications</li>
					</ul>
				</div>
			</div>
			<div class="skills_box skills_box-dev-tools col-xs-6">
				<div class="inner">
					<h3>
						<xsl:value-of select="//captions/skills_dev_tools/@caption" />
					</h3>
					<ul>
						<li>GitHub</li>
						<li>SVN</li>
						<li>Eclipse</li>
						<li>Intellij Idea</li>
						<li>Slack</li>
					</ul>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
