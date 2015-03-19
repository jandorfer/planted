var React = require('react');

var Router = require('react-router');
var RouteHandler = Router.RouteHandler;

var Authentication = require('../mixins/authentication');

var Toolbar = require('./toolbar.jsx');
var PlantList = require('./plantList.jsx');

module.exports = React.createClass({
    mixins: [Authentication],

    render: function () {
        return (
            <div className="container">
                <Toolbar />
                <div className="row">
                    <div className="col-md-4">
                        <PlantList />
                    </div>
                    <div className="col-md-8">
                        <RouteHandler />
                    </div>
                </div>
            </div>
        );
    }
});