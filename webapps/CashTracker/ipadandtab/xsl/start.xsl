<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/util-constants.xsl" />
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template match="/request/content">
		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<head>
				<title>
					<xsl:value-of select="concat($APP_NAME, ' - ', //org)" />
				</title>
				<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
				<link type="text/css" href="/SharedResources/css/normalize.css" rel="stylesheet" />
				<link type="text/css" href="ipadandtab/css/form.css?v=4" rel="stylesheet" />
				<link type="text/css" href="ipadandtab/css/button.css?v=4" rel="stylesheet" />
				<link type="text/css" href="ipadandtab/css/start.css?v=4" rel="stylesheet" />
				<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.8.3.min.js"></script>
			</head>
			<body class="sign-in">
				<div class="sign-in-form-container">
					<h2>
						<xsl:value-of select="$APP_NAME" />
					</h2>
					<form action="Login" method="post" name="sign-in">
						<fieldset class="fieldset">
							<div class="control-group">
								<div class="control-label">Пользователь</div>
								<div class="controls">
									<input type="text" autofocus="autofocus" name="login" value="" required="required" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">Пароль</div>
								<div class="controls">
									<input type="password" name="pwd" value="" required="required" />
								</div>
							</div>
							<br />
							<div>
								<label style="font-size:.8em;margin-top:4px;">
									<input type="checkbox" id="noauth" name="noauth" value="1" />
									Чужой компьютер
								</label>
								<button type="submit" class="button button-primary" title="Хорошего рабочего дня!">
									<span class="icon-arrow-right" />
									Войти
								</button>
							</div>
						</fieldset>
					</form>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
