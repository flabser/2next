AdminApp.User = DS.Model.extend({
    userName: DS.attr('string'),
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    verifyCode: DS.attr('string'),
    primaryRegDate: DS.attr('date'),
    regDate: DS.attr('string'),
    status: DS.attr('string'),
    email: DS.attr('string'),
    isSupervisor: DS.attr('number'),

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
