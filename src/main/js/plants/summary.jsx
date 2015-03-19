var React = require('react');
var Reflux = require('reflux');

var Plants = require('../data/plants');

module.exports = React.createClass({
    mixins: [Reflux.connect(Plants)],

    render: function () {
        var plantCount = this.state.size;
        var widthStyle = {
            maxWidth: '50%'
        };
        return (
            <div className="vcenter-aligner">
                <div style={widthStyle}>
                    <h2>Your Garden</h2>
                    <p>
                        You have {plantCount} plants tracked in your garden.
                        Update them or add more now!
                    </p>
                </div>
            </div>
        );
    }
});