var React = require('react');
var Router = require('react-router');
var Reflux = require('reflux');

var Plants = require('../data/plants');

var Thumbs = require('../partials/imageThumbset.jsx');

module.exports = React.createClass({
    mixins: [Router.State,
             Reflux.ListenerMixin],

    getInitialState: function () {
        return {plant: this.getPlant()};
    },

    componentDidMount: function() {
        this.listenTo(Plants, this.onPlantsChanged);
    },

    componentWillReceiveProps: function() {
        this.setState({plant: this.getPlant()});
    },

    render: function () {
        var plant = this.state.plant;
        if (!plant) {
            return (
                <h1>Plant not found.</h1>
            );
        }

        return (
            <div>
                <Thumbs images={plant.images} />
                <h2>{plant.title}</h2>
                <p>{plant.description}</p>
            </div>
        );
    },

    onPlantsChanged: function(plants) {
        this.setState({plant: this.getPlant()});
    },

    getPlant: function() {
        return Plants.getPlant(this.getId());
    },

    getId: function() {
        return decodeURIComponent(this.getParams().plantId);
    }
});