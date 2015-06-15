<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" />

<head>
  <meta charset="utf-8">
  <title>Ember Starter Kit</title>
  <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/normalize/3.0.1/normalize.css">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://builds.emberjs.com/tags/v1.12.0/ember-template-compiler.js"></script>
  <script src="http://builds.emberjs.com/tags/v1.12.0/ember.debug.js"></script>
</head>
<body>

  <script type="text/x-handlebars">
    <h2>Welcome to Ember.js</h2>

    <a href="#/">index</a>
    <a href="#/post">post</a>

    {{outlet}}
  </script>

  <script type="text/x-handlebars" data-template-name="index">
    <ul>
    {{#each model as |item|}}
      <li>{{item}}</li>
    {{/each}}
    </ul>
  </script>
  
  <script type="text/x-handlebars" data-template-name="post">
    <ul>
    {{#each model as |item|}}
      <li {{action "click"}}>{{item}}</li>
    {{/each}}
    </ul>
  </script>

</body>

	
</xsl:stylesheet>