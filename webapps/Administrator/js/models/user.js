AdminApp.User = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    roles: DS.hasMany('role', {
        async: true
      })
});

AdminApp.User.FIXTURES = [{
    id: "3",
    login: " bug",
    pwd: "123",
    email: "dddd",
    role: "role"
}, {
    id: "4",
    login: " player",
    pwd: "123",
    email: "dddd",
    role: "role"
}, {
    id: "5",
    login: "Fix",
    pwd: "123",
    email: "dddd",
    role: "role22"
}];
