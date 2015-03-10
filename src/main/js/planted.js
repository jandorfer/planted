var React = require('react');

var Landing = require('./landing.jsx');
var About = require('./about.jsx');
var AuthPages = require('./login.jsx');
var Login = AuthPages.Login;
var Logout = AuthPages.Logout;
var NotFound = require('./notfound.jsx');

var Router = require('react-router');
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;
var NotFoundRoute = Router.NotFoundRoute;

var auth = require('./services/authentication');

var App = React.createClass({
    getInitialState: function () {
        return {
            loggedIn: auth.loggedIn()
        };
    },

    setStateOnAuth: function (loggedIn) {
        this.setState({
            loggedIn: loggedIn
        });
    },

    componentWillMount: function () {
        auth.onChange = this.setStateOnAuth;
        auth.check();
    },

    render: function () {
        var loginOrOut = this.state.loggedIn ?
            <Link to="logout">Log out</Link> :
            <Link to="login">Sign in</Link>;
        var userBlurb = this.state.loggedIn ?
            <p>Logged in as {auth.getToken()}</p> :
            <p>Not logged in</p>;
        return (
            <div>
                <header>
                    <ul>
                        <li>{loginOrOut}</li>
                        <li><Link to="app">Home</Link></li>
                        <li><Link to="about">About</Link></li>
                    </ul>
                    {userBlurb}
                </header>
                <RouteHandler/>
            </div>
        );
    }
});

var routes = (
    <Route name="app" path="/" handler={App}>
        <DefaultRoute handler={Landing}/>
        <Route name="login" handler={Login}/>
        <Route name="logout" handler={Logout}/>
        <Route name="about" handler={About}/>
        <NotFoundRoute handler={NotFound}/>
    </Route>
);

Router.run(routes, Router.HistoryLocation, function (Handler) {
    React.render(<Handler/>, document.body);
});