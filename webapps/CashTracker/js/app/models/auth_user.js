CT.AuthUser = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    roles: DS.attr('string')
});
