<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:decimal-format name="decimal-format" grouping-separator=" " decimal-separator="." />

	<!-- saldo -->
	<!-- Medet. Стилизация описана в saldo.css -->

	<xsl:template match="saldo">
		<xsl:if test="plus &gt; 0 or minus &lt; 0">
			<div class="saldo">
				<xsl:if test="minus &lt; 0">
					<span class="saldo-plus">
						<xsl:value-of select="format-number(plus, '### ###.##', 'decimal-format')" />
					</span>
				</xsl:if>
				<xsl:if test="plus &gt; 0">
					<span class="saldo-minus">
						<xsl:value-of select="format-number(minus, '### ###.##', 'decimal-format')" />
					</span>
				</xsl:if>
				<span class="saldo-sum">
					<xsl:value-of select="format-number(sum, '### ###.##', 'decimal-format')" />
				</span>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template name="saldoPlain">
		<xsl:if test="//saldo/plus &gt; 0 or //saldo/minus &lt; 0">
			<xsl:if test="//saldo/minus &lt; 0">
				<xsl:value-of select="format-number(//saldo/plus, '### ###.##', 'decimal-format')" />
			</xsl:if>
			<xsl:if test="//saldo/plus &gt; 0">
				<xsl:text> </xsl:text>
				<xsl:value-of select="format-number(//saldo/minus, '### ###.##', 'decimal-format')" />
			</xsl:if>
			<xsl:text> [</xsl:text>
			<xsl:value-of select="format-number(//saldo/sum, '### ###.##', 'decimal-format')" />
			<xsl:text>]</xsl:text>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
