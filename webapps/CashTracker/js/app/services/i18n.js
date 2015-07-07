CT.I18nService = Ember.Service.extend({

    translations: [],

    init: function() {
        Ember.HTMLBars._registerHelper('t', this.t);
        this.fetchTranslations().then(function(translations) {
            CT.I18nService.translations = translations;
        });
    },

    fetchTranslations: function() {
        return $.getJSON('rest/page/app-captions').then(function(data) {
            return data._Page.captions;
        });
    },

    t: function(key) {
        if (CT.I18nService.translations.hasOwnProperty(key)) {
            return CT.I18nService.translations[key][0];
        } else {
            return key;
        }
    }
});
