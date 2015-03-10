var reflux = require("reflux");

var actions = reflux.createActions([
    // Get current state -- triggers loginSuccess or logoutSuccess
    "check",

    // Login
    "login",
    "loginFail",
    "loginSuccess",

    // Logout
    "logout",
    "logoutSuccess",
    "logoutError"
]);
module.exports = actions;