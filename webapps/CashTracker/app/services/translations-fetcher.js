import Em from 'ember';

const {
    Service, inject, $
} = Em;

const PATH = 'rest/page/app-captions';

export default Service.extend({
    i18n: inject.service(),

    fetch: function() {
        return $.get(PATH).then(this._addTranslations.bind(this));
    },

    _addTranslations: function(json) {
        const i18n = this.get('i18n');

        var tr = {
            'en': {

            }
        };
        var captions = json._Page.captions;

        for (var key in captions) {
            tr.en[key] = captions[key][0];
        }

        i18n.addTranslations('en', tr.en);
    }
});
