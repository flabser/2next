import Em from 'ember';

const {
    Service, inject, $
} = Em;

const PATH = 'rest/session/captions';

export default Service.extend({

    i18n: inject.service(),

    fetch: function() {
        return $.get(PATH).then(this._addTranslations.bind(this));
    },

    _addTranslations: function(json) {
        var translations = json.outcome.messages;
        this.get('i18n').addTranslations('en', translations);
    }
});
