var request = require('superagent');

function getUserInfo(cb) {
    request
        .get('/user')
        .end(function(err, res) {
            if (res.error) {
                cb({authenticated: false, error: res.error.message});
            } else {
                cb({authenticated: true, token: res.text});
            }
        });
}

function doLogin(id, pwd, cb) {
    request
        .post('/login')
        .send('username=' + id)
        .send('password=' + pwd)
        .end(function(err, res) {
            if (res.error) {
                cb({authenticated: false, error: res.error.message});
            } else {
                cb({authenticated: true, token: res.text});
            }
        });
}

function doLogout(cb) {
    request
        .get('/logout')
        .end(function(err, res) {
            if (err) {
                cb({error: err.message});
            } else {
                cb({});
            }
        });
}

module.exports = {
    token: null,

    check: function () {
        getUserInfo(function (res) {
            this.token = res.token;
            this.onChange(res.authenticated);
        }.bind(this));
    },

    login: function (id, pwd, cb) {
        doLogin(id, pwd, function (res) {
            if (res.authenticated) {
                this.token = res.token;
                if (cb) cb(true);
                this.onChange(true);
            } else {
                if (cb) cb(false);
                this.onChange(false);
            }
        }.bind(this));
    },

    getToken: function () {
        return this.token;
    },

    logout: function (cb) {
        doLogout(function(res) {
            delete sessionStorage.token;
            if (cb) cb(res);
            this.onChange(false);
        }.bind(this));
    },

    loggedIn: function () {
        return !!this.token;
    },

    onChange: function () {}
};