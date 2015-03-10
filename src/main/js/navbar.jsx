var React = require('react');

var NavLogin = require('./navlogin.jsx');
var LoginInline = NavLogin.Inline;
var LoginLink = NavLogin.LoginLink;

var ReactBootstrap = require('react-bootstrap');
var Nav = ReactBootstrap.Nav;
var NavBar = ReactBootstrap.Navbar;

var ReactRouterBootstrap = require('react-router-bootstrap')
var NavItemLink = ReactRouterBootstrap.NavItemLink;

module.exports = React.createClass({
    render: function () {
        return (
            <NavBar brand="Planted." fixedTop toggleNavKey={0}>
                <Nav right eventKey={0}>
                    <NavItemLink eventKey={1} to="dash">Get Started</NavItemLink>
                    <NavItemLink eventKey={2} to="about">About</NavItemLink>
                    <LoginLink />
                    <LoginInline />
                </Nav>
            </NavBar>
        );
    }
});