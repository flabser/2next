AdminApp.Role = DS.Model.extend({
  name: DS.attr('string'),
  user: DS.belongsTo('role')
})