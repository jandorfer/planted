var React = require('react');

var Landing = require('./landing.jsx');
var About = require('./about.jsx');

var Router = require('react-router');
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var App = React.createClass({
    render: function () {
        return (
            <div>
                <header>
                    <ul>
                        <li><Link to="app">Home</Link></li>
                        <li><Link to="about">About</Link></li>
                    </ul>
                    Logged in as Nobody
                </header>
                <RouteHandler/>
            </div>
        );
    }
});

var routes = (
    <Route name="app" path="/" handler={App}>
        <Route name="about" handler={About}/>
        <DefaultRoute handler={Landing}/>
    </Route>
);

Router.run(routes, Router.HistoryLocation, function (Handler) {
    React.render(<Handler/>, document.body);
});