// CT.register('service:i18n', Ember.Object);

// CT.inject('model', 'i18n', 'service:i18n');

CT.i18n = {
    translations: [],

    getTranslations: function() {
        return $.getJSON('rest/page/app-captions').then(function(data) {
            CT.i18n.translations = data._Page.captions;
            return data._Page.captions;
        });
    },

    translate: function(key) {
        if (CT.i18n.translations.hasOwnProperty(key)) {
            return CT.i18n.translations[key][0];
        } else {
            return key;
        }
    }
};

CT.register('service:i18n', CT.i18n);

Ember.HTMLBars._registerHelper('t', CT.i18n.translate);
