var Reflux = require("reflux");
var Immutable = require('immutable');

var AuthActions = require("../actions/authentication");

var defaultData = {
    user: null,
    auth: false,
    roles: []
};

var SessionStore = Reflux.createStore({

    init: function() {
        this.data = Immutable.Map(defaultData);
        AuthActions.check();
    },

    getInitialState: function() {
        return this.data;
    },

    setData: function(data) {
        this.data = this.data.merge(data);
        this.trigger(this.data.toObject());
    },

    // ACTION HANDLING

    listenables: AuthActions,

    onLoginSuccess: function(userid) {
        this.setData({
            user: userid,
            auth: true,
            roles: ["user"]
        });
    },

    onLoginFail: function(msg) {
        this.setData(defaultData);
    },

    onLogoutSuccess: function() {
        this.setData(defaultData);
    },

    // API

    isLoggedIn: function() {
        return this.data.get("auth");
    },

    getUser: function() {
        return this.data.get("user");
    }
});

module.exports = SessionStore;