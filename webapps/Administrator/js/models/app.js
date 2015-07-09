AdminApp.App = DS.Model.extend({
    appName: DS.attr('string'),
    owner: DS.attr('string'),
    dbHost: DS.attr('string'),
    dbLogin: DS.attr('string'),
    dbPwd: DS.attr('date'),
    dbName: DS.attr('string'),
  
});
