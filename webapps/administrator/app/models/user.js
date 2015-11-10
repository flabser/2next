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
    isSupervisor: DS.attr('boolean'),
    dbLogin: DS.attr('string'),
    defaultDbPwd:  DS.attr('string'),

    roles: DS.hasMany('role', {
        async: true
      })
});

