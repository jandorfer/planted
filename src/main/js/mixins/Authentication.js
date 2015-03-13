var auth = require('../data/session');

var Authentication = {
    statics: {
        willTransitionTo: function (transition) {
            if (!auth.isLoggedIn()) {
                transition.redirect(
                    '/account', {}, {'nextPath': transition.path});
            }
        }
    }
};

module.exports = Authentication;