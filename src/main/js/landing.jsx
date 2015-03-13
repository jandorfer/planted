var React = require('react');
var Reflux = require('reflux');

var Authentication = require('./mixins/authentication');
var Plants = require('./data/plants');

module.exports = React.createClass({
    mixins: [Authentication,
             Reflux.connect(Plants)],

    getInitialState: function () {
        return Plants.getPlants();
    },

    render: function () {
        console.log(this.state);
        var plantNodes = this.state.map(function(plant, index) {
            // Temporary, will break out to separate jsx
            return (
                <li key={plant.rid}>
                {plant.title}
                </li>
            );
        });
        return (
            <div className="container">
                <div className="row">
                    <h1>Your Garden</h1>
                    <ul>
                        {plantNodes}
                    </ul>
                </div>
            </div>
        );
    }
});