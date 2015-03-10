var React = require('react');
var Reflux = require('reflux');

var ReactBootstrap = require('react-bootstrap')
var Input = ReactBootstrap.Input;
var Glyphicon = ReactBootstrap.Glyphicon;
var DropdownButton = ReactBootstrap.DropdownButton;

var ReactRouterBootstrap = require('react-router-bootstrap')
var NavItemLink = ReactRouterBootstrap.NavItemLink;

var actions = require('./actions/authentication');
var session = require('./data/session');

var loginFormStyle = {
    padding: '15px',
    minWidth: '250px'
};

var Inline = React.createClass({
    mixins: [Reflux.connect(session)],
    render: function () {
        if (this.state.auth) {
            var message = this.state.auth ?
                (<span><Glyphicon glyph="user" /> {this.state.user}</span>)
                : "Sign In";
            return (
                <NavItemLink className="hidden-xs" eventKey={3} to="account">{message}</NavItemLink>
            );
        }
        return (
            <DropdownButton className="hidden-xs" eventKey={3} title="Sign In" navItem>
                <form method="post" action="login" style={loginFormStyle} onSubmit={this.handleSubmit}>
                    <Input ref="user" type="text" label="User Name" required addonBefore={<Glyphicon glyph="user" />} />
                    <Input ref="pwd" type="password" label="Password" required addonBefore={<Glyphicon glyph="lock" />} />
                    <Input type="submit" value="Sign In" bsStyle="default" block />
                </form>
            </DropdownButton>
        );
    },
    handleSubmit: function (event) {
        event.preventDefault();
        var user = this.refs.user.getValue();
        var pwd = this.refs.pwd.getValue();
        actions.login(user, pwd);
    }
});

var LoginLink = React.createClass({
    mixins: [Reflux.connect(session)],
    render: function () {
        var message = this.state.auth ? this.state.user : "Sign In";
        return (
            <NavItemLink className="visible-xs-block" eventKey={3} to="account">{message}</NavItemLink>
        );
    }
});

module.exports = {
    Inline: Inline,
    LoginLink: LoginLink
};