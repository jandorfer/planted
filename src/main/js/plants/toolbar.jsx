var React = require('react');

var Router = require('react-router');
var RouteHandler = Router.RouteHandler;

var ButtonLink = require('react-router-bootstrap').ButtonLink;

module.exports = React.createClass({
    render: function () {
        return (
            <div className="row">
                <ButtonLink to="new" className="pull-right">Enter New Plant</ButtonLink>
            </div>
        );
    }
});