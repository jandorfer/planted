var React = require('react');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="container">
                <div className="row">
                    <h1>Not Found</h1>
                    <p>The page you requested does not appear to exist. Please try again.</p>
                </div>
            </div>
        );
    }
});