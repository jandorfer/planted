var request = require('superagent');

var AuthActions = require("../actions/authentication");

AuthActions.check.listen(function() {
    request
        .get('/user')
        .end(function(err, res) {
            if (res.error) {
                AuthActions.logoutSuccess();
            } else {
                AuthActions.loginSuccess(res.text);
            }
        });
});

AuthActions.login.listen(function(id, pwd, cb) {
    request
        .post('/login')
        .send('username=' + id)
        .send('password=' + pwd)
        .end(function(err, res) {
            if (res.error) {
                AuthActions.loginFail(res.text);
            } else {
                // On success username is returned (that's the res.text value)
                AuthActions.loginSuccess(res.text);
                if (cb) { cb(res.text); }
            }
        });
});

AuthActions.logout.listen(function() {
    request
        .get('/logout')
        .end(function(err, res) {
            if (err) {
                AuthActions.logoutError(err.message);
            } else {
                AuthActions.logoutSuccess();
            }
        });
});