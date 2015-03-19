var React = require('react');
var Reflux = require('reflux');

var Plants = require('../data/plants');

var PlantTile = require('./plantTile.jsx');

module.exports = React.createClass({
    mixins: [Reflux.connect(Plants, 'plants')],

    render: function () {
        var plantNodes = !this.state.plants.map ? null : this.state.plants.map(function(plant) {
            return (
                <PlantTile plant={plant} key={plant.rid} />
            );
        }).toJS();

        return (
            <div className="tile-container">
                {plantNodes}
            </div>
        );
    }
});