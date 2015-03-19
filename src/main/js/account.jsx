var React = require('react');
var Router = require('react-router');
var Reflux = require('reflux');

var actions = require('./actions/authentication');
var session = require('./data/session');

var ReactBootstrap = require('react-bootstrap')
var Alert = ReactBootstrap.Alert;
var Button = ReactBootstrap.Button;
var Input = ReactBootstrap.Input;
var Panel = ReactBootstrap.Panel;

var LoginLogout = React.createClass({
    mixins: [Router.Navigation,
             Router.State,
             Reflux.listenTo(session, "onSessionUpdated"),
             Reflux.listenTo(actions.login, "onLoginStart"),
             Reflux.listenTo(actions.loginSuccess, "onLoginSuccess"),
             Reflux.listenTo(actions.loginFail, "onLoginFail")],

    getInitialState: function () {
        if (session.isLoggedIn()) {
            this.onLoginSuccess();
        }

        return {
            auth: session.isLoggedIn(),
            loading: false,
            error: false
        };
    },

    renderFrame: function (content) {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm-6 col-md-4 col-md-offset-4">
                        <div className="account-wall">
                            <img className="profile-img" src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120" />
                            {content}
                            {this.renderErrors()}
                        </div>
                    </div>
                </div>
            </div>
        );
    },

    renderErrors: function () {
        if (this.state.error) {
            return (
                <Alert bsStyle="danger" onDismiss={this.handleErrorDismiss}>
                    <p>{this.state.error}</p>
                </Alert>
            );
        }
    },

    renderLoginForm: function () {
        return this.renderFrame(
            <form className="form-signin" onSubmit={this.handleSubmit}>
                <input ref="user" type="text" placeholder="Username" className="form-control" required autoFocus="true" />
                <input ref="pwd" type="password" placeholder="Password" className="form-control" required />
                <Input type="submit" value="Sign In" bsStyle="default" bsSize="large" block />
            </form>
        );
    },

    renderLoginSuccess: function () {
        return this.renderFrame(
            <div className="form-signin">
                <h2>{session.getUser()}</h2>
                <Button onClick={actions.logout} block>Sign Out</Button>
            </div>
        );
    },

    render: function () {
        return this.state.auth ? this.renderLoginSuccess() : this.renderLoginForm();
    },

    handleSubmit: function (event) {
        event.preventDefault();
        var user = this.refs.user.getDOMNode().value;
        var pwd = this.refs.pwd.getDOMNode().value;
        actions.login(user, pwd);
    },

    handleErrorDismiss: function () {
        this.setState({
            error: false
        });
    },

    onSessionUpdated: function(sessionData) {
        this.setState({
            auth: sessionData.auth,
            loading: false,
        });
    },

    onLoginStart: function() {
        this.setState({
            loading: true,
            error: false
        });
    },

    onLoginSuccess: function() {
         if (this.getQuery().nextPath) {
             this.replaceWith(this.getQuery().nextPath);
         }
    },

    onLoginFail: function(message) {
        this.setState({
            error: message
        });
    }
});

module.exports = LoginLogout;