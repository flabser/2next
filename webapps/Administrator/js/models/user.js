App.User = DS.Model.extend({
	login:DS.attr('string'),
	pwd:DS.attr('string')
});


App.User.FIXTURES = [
  {id:"1",login:"admin", pwd:"ssss"},
  {id:"2",login:"user", pwd:"s1"}
];


