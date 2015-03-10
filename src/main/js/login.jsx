var React = require('react');
var Router = require('react-router');

var auth = require('./services/authentication');

var Login = React.createClass({
    mixins: [ Router.Navigation, Router.State],

    getInitialState: function () {
        return {
            error: false
        };
    },

    handleSubmit: function (event) {
        event.preventDefault();
        var nextPath = this.getQuery().nextPath;
        var user = this.refs.user.getDOMNode().value;
        var pwd = this.refs.pwd.getDOMNode().value;
        auth.login(user, pwd, function (loggedIn) {
            if (!loggedIn) return this.setState({ error: true });
            if (nextPath) {
                this.transitionTo(nextPath);
            } else {
                this.replaceWith('/');
            }
        }.bind(this));
    },

    render: function () {
        var errors = this.state.error ? <p>Bad login information</p> : '';
        return (
            <form onSubmit={this.handleSubmit}>
                {errors}
                <label><input ref="user" placeholder="user id"/></label>
                <label><input ref="pwd" placeholder="password"/></label>
                <button type="submit">login</button>
            </form>
        );
    }
});

var Logout = React.createClass({
    getInitialState: function () {
        return {
            error: null
        };
    },

    componentDidMount: function () {
        auth.logout(function(res) {
            if (res.error) {
                this.setState({ error: res.error });
            }
        }.bind(this));
    },

    render: function () {
        if (this.state.error) {
            return <p>There was a problem logging out: {this.state.error}</p>;
        } else {
            return <p>You are now logged out</p>;
        }
    }
});

module.exports = {Login: Login, Logout: Logout};