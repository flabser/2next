CT.User = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string')
});
