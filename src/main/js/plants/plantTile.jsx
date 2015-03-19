var React = require('react');
var Link = require('react-router').Link;

var Thumbs = require('../partials/imageThumbset.jsx');

module.exports = React.createClass({
    render: function () {
        var plant = this.props.plant;
        return (
            <div className="tile" key={plant.rid}>
                <Thumbs images={plant.images} />
                <h2>
                    <Link to="plant" params={{plantId: encodeURIComponent(plant.rid)}}>
                        {plant.title}
                    </Link>
                </h2>
                <p>{plant.description}</p>
            </div>
        );
    }
});