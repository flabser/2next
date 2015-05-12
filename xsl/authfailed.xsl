<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">	
	<xsl:output method="html" encoding="utf-8"/>
	<xsl:template match="/request/error">
		<html>
			<head>
				<title>Nextbase - Ошибка</title>						
				<link rel="stylesheet" href="classic/css/main.css"/>	
				<link rel="stylesheet" href="classic/css/actionbar.css"/>			
				<script>
					<![CDATA[
						function CancelForm(){
			   	  			window.history.back();
						}
						function goToLogin(){
					 		window.location = "Logout";					
						}
					]]>
				</script>
			</head>
			<body style="background-image: url(/SharedResources/img/classic/f.gif); font-family:arial">
				<table width="100%" border="0" style="margin-top:140px">
					<tr>
						<td width="20%" align="right" style="font-size:9pt;">
							<font style="font-size:1.9em;">NextBase</font>
							<div style="clear:both; height:10px"/>
							<font style="font-size:1.1em;">
								version <xsl:value-of select="version"/> &#169; Lab of the Future 2012
							</font>
							<br/>
						</td>
						<td width="1%"></td>
						<td style="height:500px" bgcolor="#FFCC00" width="1"></td>
						<td>
							<table style="width:100%; margin-left:1%">
								<tr>
									<td>
										<font style="font-size:2em;">Ошибка авторизации</font>
									</td>
								</tr>
								<tr>
									<td>
										<ul style="font-size:0.9em; margin-top:15px">
											<li type="square">Проверьте правильность написания имени пользователя</li>
											<li type="square" style="margin-top:5px; ">Убедитесь, что пароль вводится на том же языке, что и при регистрации</li>
											<li type="square" style="margin-top:5px">Убедитесь, не нажат ли [Caps Lock]</li>
											<li type="square" style="margin-top:5px"><a href="javascript:goToLogin()"> Повторить попытку авторизации </a></li>
										</ul>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div style="z-index:999; margin-top:-2%; margin-left:22.5%; font-size:0.7em; font-family:arial">&#xA0;<a href="http://www.flabs.kz" target="_blank">Lab of the Future</a>&#xA0; &#8226; &#xA0;<a href="http://www.smartdoc.kz" target="_blank">Feedback</a></div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>