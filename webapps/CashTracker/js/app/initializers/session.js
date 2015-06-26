CT.register('service:session', Ember.Object);

CT.inject('route', 'session', 'service:session');
CT.inject('controller', 'session', 'service:session');
