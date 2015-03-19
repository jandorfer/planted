var React = require('react');

module.exports = React.createClass({
    render: function () {
        var label = !this.props.label ? null : (
            <label className="control-label">{this.props.label}</label>
        );
        return (
            <div className="form-group">
                {label}
                <input {...this.props} className="form-control" type='date' />
            </div>
        );
    }
});