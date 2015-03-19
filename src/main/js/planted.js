var Bootstrap = require('react-bootstrap');
var React = require('react');

var NavBar = require('./navbar.jsx');
var Landing = require('./landing.jsx');
var About = require('./about.jsx');
var Account = require('./account.jsx');
var NotFound = require('./notfound.jsx');

var PlantsLayout = require('./plants/layout.jsx');
var NewPlant = require('./plants/createPlant.jsx');
var ViewPlant = require('./plants/details.jsx');
var PlantSummary = require('./plants/summary.jsx');

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
        <Route name="plants" path="/plants" handler={PlantsLayout}>
            <Route name="new" path="new" handler={NewPlant}/>
            <Route name="plant" path=":plantId" handler={ViewPlant}/>
            <DefaultRoute handler={PlantSummary} />
        </Route>
        <Route name="about" path="/about" handler={About}/>
        <Route name="account" path="/account" handler={Account}/>
        <NotFoundRoute handler={NotFound}/>
    </Route>
);

Router.run(routes, Router.HistoryLocation, function (Handler) {
    React.render(<Handler/>, document.body);
});