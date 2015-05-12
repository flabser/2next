<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/view.xsl" />
	<xsl:variable name="viewtype" select="'Статистика/Расходы'" />

	<xsl:output
		method="html"
		encoding="utf-8"
		indent="yes" />

	<xsl:template match="/request">

		<div class="org-struct">
			<div class="title">
				<p style="font-family:Calibri;font-size:2.2em;margin:10px 0 10px 0;">Расходы</p>
			</div>
			<div id="stat" style="padding:5px;">
				<div id="statbody" style="display:none;margin-top:10px;width:100%;position:relative;"></div>
				<div id="statload" style="padding:5px;margin-top:10px;">
					<span><img src="/SharedResources/img/classic/loader-mini.gif" alt="" /></span>
					<span style="margin-left:5px;padding:0 3px;background-color:#AF2727;color:#FFF;">загрузка...</span>
				</div>
			</div>
		</div>

		<script type="text/javascript" src="/SharedResources/scripts/json/json2.js"></script>
		<script type="text/javascript"><![CDATA[
			var statloading = false;
			var statLoadCounter = 0;
			var gdata = {};

			function gDraw(){

				try {
					var stat = ""
					var maxAll = parseInt(gdata["maxOut"]);
					var maxInDay = 0;
					var totalInDay = 0;
					var beststyle = "";

					stat += "<br /><div class='stats' id='bydate'><div class='statsstats st'><span class='title'>Расходы</span>"
					stat += "<span class='pick expand'></span></div><div class='section'>"

					stat += '<table width="100%"><tr style="background-color:#EFEFEF;border-bottom:2px solid #CFCFCF;">';
					stat += '<td class="date" style="padding:2px;text-align:center;width:85px;">Год</td>';
					stat += '<td style="padding:2px;"><span style="float:right;">Тип операции</span>Сумма</td></tr>';

	                for( var key in gdata["out"] ){
	                	maxInDay = 0;
	                	totalInDay = 0;

						for( var k in gdata["out"][key] ){
							if(maxInDay < gdata["out"][key][k]){
								maxInDay = parseInt(gdata["out"][key][k]);
							}

							totalInDay += parseInt(gdata["out"][key][k]);
						}

						stat += '<tr style="background-color:#FFF;">';
						stat += '<td class="date" style="border-bottom:1px solid #CFCFCF;padding:3px;text-align:center;vertical-align:top;">';
						stat += key+' <span style="display:block">(<span style="font-size:85%;font-color:#999;font-weight:bold;">'+totalInDay+'</span>)</span></td>';
						stat += '<td style="border-bottom:1px solid #CFCFCF;padding:1px;">';

	                	for( var k in gdata["out"][key] ){
	                		all = parseInt(gdata["out"][key][k]);
							width = Math.abs(Math.round((all/maxAll)*100));

							beststyle = (maxInDay == all) ? "font-weight:bold;" : "";

							stat += '<div style="position:relative;display:block;"><ul style="clear:both;width:'+width+'%;background-color:#D8E5F2;color:#000;border:1px solid #ADC4DB;margin:1px 0;">';
					    	stat += '<li style="float:right;position:absolute;right:0;z-index:2;text-align:right;padding:1px;width:99%;border-bottom:1px dotted #CCC;'+beststyle+'">'+k+'</li>';
							stat += '<li style="text-align:left;padding:1px;z-index:3;">'+gdata["out"][key][k]+'</li>';
							stat += '</ul></div>';
						}

						stat += '</td></tr>';
					}

					stat += '</table></div></div>';

					$("#statbody").html(stat);
					//$("#statbody").html();
                }catch(e){ alert(e); }
			}


			$(document).ready(function(){
				(function(){
					if( ! $("#statbody").length) return;
					var $statbody = $("#statbody");
					$("#getStat").click(function(){ getStat(); });

					getStat();

					function getStat(){
						if(!statloading){
							blockWaiting.block();
							$("#getStat").attr("disabled", true);
							statloading = true;

							$statbody.css("display", "block");
							$statbody.addClass("hidden");
							$("#statload").removeClass("hidden");

							$.ajax({
								type: "get",
								dataType: "text",
								url: 'Provider?type=handler&id=stat-out',
								success: function(xml){
									var rt = xml.split("<json>")[1];
									rt = rt.split("</json>")[0];

									try {
										//gdata = eval(rt);
										gdata = JSON.parse(rt);
										//gdata = jQuery.parseJSON( rt );
									} catch(e) {
										alert(e);
									}

									$("#statload").addClass("hidden");
									$statbody.removeClass("hidden");

									gDraw();

									$('.statsstats').expander();
									$("#getStat").attr("disabled", false);
									$("#statbody").removeClass("hidden");
									blockWaiting.destroy();

									statloading = false;
									statLoadCounter++;
								}
							});
						}
					}
				})();
			});]]>
		</script>

	</xsl:template>
</xsl:stylesheet>
