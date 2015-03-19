var auth = require('../data/session');

var Authentication = {
    statics: {
        willTransitionTo: function (transition) {
            if (!auth.isLoggedIn()) {
                transition.redirect(
                    '/account', {}, {'nextPath': encodeURIComponent(transition.path)});
            }
        }
    }
};

module.exports = Authentication;