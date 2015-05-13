<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--	навигатор по страницам -->
	<xsl:template name="pagenavig">
	
  <xsl:param name="i" select="1"/>
  <xsl:param name="n" select="15"/>
  <xsl:param name="z" select="query/@maxpage -14"/>
  <xsl:param name="f" select="15"/>
  <xsl:param name="c" select="query/@currentpage"/>
  <xsl:param name="d" select="query/@maxpage - 14"/>
  <xsl:param name="currentpage" select="query/@currentpage"/>
  <xsl:param name="maxpage" select="query/@maxpage"/>
  <xsl:param name="nextpage" select="$currentpage + 1"/>
  <xsl:param name="prevpage" select="$currentpage - 1"/>
  <xsl:param name="curview" select="@id"/> 
  <xsl:param name="direction" select="query/@direction"/> 
   
  
		
		<xsl:choose>
		
		<xsl:when test="$maxpage>15">
				<xsl:choose>
				<xsl:when test="$maxpage - $currentpage &lt; 15">
				<xsl:if test="$i != $n+1">
				<xsl:if test="$z &lt; $maxpage + 1"><td>
		<a href="">
   			 <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$z"/>)
   			 </xsl:attribute>
             
   			 <font style="font-size:12px">
    			<xsl:if test="$z=$currentpage">
    			<xsl:attribute name="style">font-weight:bold;font-size:1.3em</xsl:attribute>
    			</xsl:if>
    <xsl:value-of select="$z"/></font>
    
    </a>&#xA0;
		</td></xsl:if>
      		<xsl:call-template name="pagenavig">
	        	<xsl:with-param name="i" select="$i + 1"/>
	        	<xsl:with-param name="n" select="$n"/>
	        	<xsl:with-param name="z" select="$z+1"/>
      		</xsl:call-template>
      	
		</xsl:if>
		<xsl:if test="$currentpage != $maxpage">
		<xsl:if test="$i = $n+1">
		 <td>
      <a href="">
      <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$nextpage"/>)</xsl:attribute><font style="font-size:12px"> > </font> </a>&#xA0;
      
      </td>
       <td><a href="">
       <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$maxpage"/>)</xsl:attribute><font style="font-size:12px"> >> </font></a> &#xA0;
      
       </td>
		</xsl:if>
   </xsl:if>
				
				</xsl:when>
				<xsl:otherwise>
				<xsl:if test="$i != $n+1">
	<xsl:if test="$c &lt; $maxpage + 1"><td>
		<a href="">
   			 <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$c"/>)
   			 </xsl:attribute>
             
   			 <font style="font-size:12px">
    			<xsl:if test="$c=$currentpage">
    			<xsl:attribute name="style">font-weight:bold;font-size:1.3em</xsl:attribute>
    			</xsl:if>
    <xsl:value-of select="$c"/></font>
    
    </a>&#xA0;
		</td></xsl:if>
      		<xsl:call-template name="pagenavig">
	        	<xsl:with-param name="i" select="$i + 1"/>
	        	<xsl:with-param name="n" select="$n"/>
	        	<xsl:with-param name="c" select="$c+1"/>
      		</xsl:call-template>
      	
		</xsl:if>
		<xsl:if test="$currentpage != $maxpage">
		<xsl:if test="$i = $n+1">
		 <td>
      <a href="">
      <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$nextpage"/>)</xsl:attribute><font style="font-size:12px"> > </font> </a>&#xA0;
      
      </td>
       <td><a href="">
       <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$maxpage"/>)</xsl:attribute><font style="font-size:12px"> >> </font></a> &#xA0;
      
       </td>
		</xsl:if>
   </xsl:if>
				</xsl:otherwise>
				</xsl:choose>
		</xsl:when>
		
		
		
		<xsl:otherwise>
	<xsl:if test="$i != $n+1">
	<xsl:if test="$i &lt; $maxpage + 1"><td>
		<a href="">
   			 <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$i"/>)
   			 </xsl:attribute>
             
   			 <font style="font-size:12px">
    			<xsl:if test="$i=$currentpage">
    			<xsl:attribute name="style">font-weight:bold;font-size:1.3em</xsl:attribute>
    			</xsl:if>
    <xsl:value-of select="$i"/></font>
    
    </a>&#xA0;
		</td></xsl:if>
      		<xsl:call-template name="pagenavig">
	        	<xsl:with-param name="i" select="$i + 1"/>
	        	<xsl:with-param name="n" select="$n"/>
	        	<xsl:with-param name="c" select="$c+1"/>
      		</xsl:call-template>
      	
		</xsl:if>
		<xsl:if test="$currentpage != $maxpage">
		<xsl:if test="$i = $n+1">
		 <td>
      <a href="">
      <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$nextpage"/>)</xsl:attribute><font style="font-size:12px"> > </font> </a>&#xA0;
      
      </td>
       <td><a href="">
       <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,&quot;<xsl:value-of select="query/@direction"/>&quot;,<xsl:value-of select="$maxpage"/>)</xsl:attribute><font style="font-size:12px"> >> </font></a> &#xA0;
      
       </td>
		</xsl:if>
   </xsl:if>
   </xsl:otherwise>
   </xsl:choose>
  </xsl:template>
	
	
	<!-- пока не нужно -->
	<xsl:template name="pagenav">
	
  <xsl:param name="i" select="0"/>
  <xsl:param name="n" select="1"/>
  <xsl:param name="currentpage" select="query/@currentpage"/>
  <xsl:param name="maxpage" select="query/@maxpage"/>
  <xsl:param name="nextpage" select="$currentpage + 1"/>
  <xsl:param name="prevpage" select="$currentpage - 1"/>
  <xsl:param name="curview" select="@id"/> 
  
    
   
  <xsl:choose>
  <xsl:when test="1>$maxpage">
  </xsl:when>
  <xsl:when test="$maxpage > 10">
    <xsl:choose>
   		 <xsl:when test="$maxpage > $i">
   		 
    		<td><a href="">
   			 <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$n"/>)
   			 </xsl:attribute>
             
   			 <font>
    			<xsl:if test="$n=$currentpage">
    			<xsl:attribute name="style">font-weight:bold;font-size:1.8em</xsl:attribute>
    			</xsl:if>
    <xsl:value-of select="$n"/></font>
    
    </a>&#xA0;
   </td>
      <xsl:call-template name="pagenav">
        <xsl:with-param name="i" select="$i + 1"/>
        <xsl:with-param name="n" select="$n + 1"/>
        <xsl:with-param name="maxpage" select="query/@maxpage"/>
      </xsl:call-template>
    		</xsl:when>
    <xsl:otherwise>
      <td>
      <a href="">
      <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$nextpage"/>)</xsl:attribute> > </a>
      &#xA0;
      </td>
       <td><a href="">
       <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$maxpage"/>)</xsl:attribute>>></a>
       &#xA0;
       </td>
    </xsl:otherwise>
  </xsl:choose>
  </xsl:when>
  
  <xsl:otherwise>
  <xsl:choose>
    <xsl:when test="$maxpage > $i">
    <td><a href="">
    <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$n"/>)</xsl:attribute>
    <font>
    <xsl:if test="$n=$currentpage">
    <xsl:attribute name="style">font-weight:bold;font-size:1.8em</xsl:attribute>
    </xsl:if>
    
    <xsl:value-of select="$n"/>
    </font>
    
    </a>
    &#xA0;
    </td>
      <xsl:call-template name="pagenav">
        <xsl:with-param name="i" select="$i + 1"/>
        <xsl:with-param name="n" select="$n + 1"/>
        <xsl:with-param name="maxpage" select="query/@maxpage"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <td>
      <a href="">
      <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$nextpage"/>)</xsl:attribute><font> > </font> </a>&#xA0;
      
      </td>
       <td><a href="">
       <xsl:attribute name="href">javascript:updateView(&quot;<xsl:value-of select="@id"/>&quot;,'forward',<xsl:value-of select="$maxpage"/>)</xsl:attribute><font> >> </font></a> &#xA0;
      
       </td>
    
    </xsl:otherwise>
  </xsl:choose>
  </xsl:otherwise>
  </xsl:choose>
</xsl:template>

 <xsl:template name="combobox">
				 <xsl:param name="i" select="1"/>
				 <xsl:param name="k" select="query/@currentpage"/>
 				 <xsl:param name="n" select="query/@maxpage + 1"/>
 				
 				<xsl:choose>
 				<xsl:when test="$n > $i">
 				 <option>
 				 <xsl:attribute name="value"> <xsl:value-of select="$i"/></xsl:attribute>
 				 <xsl:if test="$k=$i">
 				 <xsl:attribute name="selected">true</xsl:attribute>
 				 </xsl:if>
 				 <xsl:value-of select="$i"/>
 				 </option>
 				 
 				 <xsl:call-template name="combobox">
	        	<xsl:with-param name="i" select="$i + 1"/>
	        	<xsl:with-param name="n" select="$n"/>
	        	<xsl:with-param name="k" select="query/@currentpage"/>
	        	</xsl:call-template>
	        	
 				 </xsl:when>
 				 </xsl:choose>
 				 
				 </xsl:template>
				 
</xsl:stylesheet>