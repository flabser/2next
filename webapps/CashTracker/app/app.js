"use strict";

import Em from 'ember';
import Resolver from 'ember/resolver';
import loadInitializers from 'ember/load-initializers';
import config from './config/environment';

var App;

Em.MODEL_FACTORY_INJECTIONS = true;

App = Em.Application.extend({
    modulePrefix: config.modulePrefix,
    podModulePrefix: config.podModulePrefix,
    Resolver: Resolver,

    ready: function() {
        Em.$(document).on('drop dragover', function(e) {
            e.preventDefault();
            // Em.$('body').addClass('has-drag');
        }).on('dragleave', function(e) {
            // e.preventDefault();
            // Em.$('body').removeClass('has-drag');
        });
    }
});

loadInitializers(App, config.modulePrefix);

export default App;
