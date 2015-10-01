<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="header">
		<header>
			<xsl:call-template name="navbar" />
			<xsl:call-template name="promo" />
		</header>
	</xsl:template>

	<xsl:template name="navbar">
		<nav class="navbar navbar-fixed-top navbar-default js-navbar-top js-toggle-class">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="?id=welcome">
						<xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				<div id="navbar" class="collapse navbar-collapse" aria-expanded="false">
					<ul class="nav navbar-nav navbar-right ">
						<li class="active">
							<a href="#home">
								<xsl:value-of select="//captions/home/@caption" />
							</a>
						</li>
						<li class="">
							<a href="#about">
								<xsl:value-of select="//captions/about/@caption" />
							</a>
						</li>
						<li class="">
							<a href="#skills">
								<xsl:value-of select="//captions/skills/@caption" />
							</a>
						</li>
						<li class="">
							<a href="#contact">
								<xsl:value-of select="//captions/contact_us/@caption" />
							</a>
						</li>
						<li class="dropdown dropdown-lang">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<xsl:value-of select="//@lang" />
							</a>
							<ul class="dropdown-menu">
								<xsl:apply-templates select="//availablelangs" />
							</ul>
						</li>
					</ul>
				</div>
			</div>
		</nav>
	</xsl:template>

	<xsl:template name="promo">
		<div class="promo promo_agency" id="home">
			<div class="container">
				<div class="row">
					<div class="col-xs-12">
						<h1 class="heading promo__heading animated fadeInUp">
							<xsl:value-of select="//captions/promo_heading/@caption" />
						</h1>
						<p class="heading__sub promo-heading__sub animated fadeInUp delay_1">
							<xsl:value-of select="//captions/promo_heading_sub/@caption" />
						</p>
						<p class="heading__sub promo-heading__experience animated fadeInUp delay_1">
							<xsl:value-of select="//captions/promo_experience/@caption" />
						</p>
						<a href="#contact" class="promo__btn btn btn-lg btn-primary animated fadeInUp delay_2">
							<xsl:value-of select="//captions/contact_us/@caption" />
						</a>
						<a href="#about" class="promo__btn btn btn-lg btn-link animated fadeInUp delay_2">
							<xsl:value-of select="//captions/find_out_more/@caption" />
						</a>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="availablelangs">
		<xsl:if test="count(value[entry = 'ON']) > 1">
			<xsl:apply-templates select="value[entry[1] = 'ON']" mode="lang" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="value" mode="lang">
		<li>
			<a class="lang" href="?id={//request/@id}&amp;lang={entry[2]}">
				<xsl:value-of select="entry[3]" />
			</a>
		</li>
	</xsl:template>

</xsl:stylesheet>
