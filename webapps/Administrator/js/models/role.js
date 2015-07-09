AdminApp.Role = DS.Model.extend({
  name: DS.attr('string'),
  description: DS.attr('string'),
  user: DS.belongsTo('role')
})