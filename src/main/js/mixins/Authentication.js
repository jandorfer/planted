var auth = require('../services/authentication');

var Authentication = {
    statics: {
        willTransitionTo: function (transition) {
            var nextPath = transition.path;
            if (!auth.loggedIn()) {
                transition.redirect(
                    '/login',
                    {},
                    {'nextPath' : nextPath});
            }
        }
    }
};