var Bootstrap = require('react-bootstrap');
var React = require('react');

var NavBar = require('./navbar.jsx');
var Landing = require('./landing.jsx');
var About = require('./about.jsx');
var Account = require('./account.jsx');
var NotFound = require('./notfound.jsx');

var Router = require('react-router');
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;
var NotFoundRoute = Router.NotFoundRoute;

// Load services
require('./services/authentication');
require('./services/plantedApi');

var App = React.createClass({
    render: function () {
        return (
            <div>
                <NavBar/>
                <RouteHandler/>
            </div>
        );
    }
});

var routes = (
    <Route name="app" path="/" handler={App}>
        <DefaultRoute handler={Landing}/>
        <Route name="dash" path="/" handler={Landing}/>
        <Route name="about" path="/about" handler={About}/>
        <Route name="account" path="/account" handler={Account}/>
        <NotFoundRoute handler={NotFound}/>
    </Route>
);

Router.run(routes, Router.HistoryLocation, function (Handler) {
    React.render(<Handler/>, document.body);
});