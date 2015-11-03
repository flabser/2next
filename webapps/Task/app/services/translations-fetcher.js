import Em from 'ember';

const {
    Service, inject, $
} = Em;

const PATH = 'rest/page/translations';

export default Service.extend({

    i18n: inject.service(),

    fetch: function() {
        return $.get(PATH).then(this._addTranslations.bind(this));
    },

    _addTranslations: function(json) {
        var tr = {};
        var captions = json._Page.captions;

        for (var key in captions) {
            tr[key] = captions[key];
        }

        this.get('i18n').addTranslations('en', tr);
    }
});
