MyApp.AuthUser = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    roles: DS.attr('string'),
    status:DS.attr('string'),
    error:DS.attr('string')
});
