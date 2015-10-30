import Em from 'ember';

const PATH = 'rest/dashboard';

export default Em.Component.extend({
    data: {},

    init: function() {
        this._super();
        this.getData().then((resp) => {
            this.set('data', resp);
        });
    },

    getData: function() {
        return $.ajax({
            url: PATH,
            method: 'GET',
            cache: false,
            dataType: 'json',
            contentType: 'application/json',
            success: function(result) {
                return result;
            },
            error: function(err) {
                console.log(err);
            }
        });
    }
});
