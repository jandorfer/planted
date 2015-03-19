var React = require('react');
var Router = require('react-router');
var Reflux = require('reflux');

var ReactBootstrap = require('react-bootstrap');
var Alert = ReactBootstrap.Alert;
var Button = ReactBootstrap.Button;
var Input = ReactBootstrap.Input;

var API = require('../actions/plantedApi');

var DateInput = require('../partials/dateInput.jsx');

module.exports = React.createClass({
    mixins: [Router.Navigation,
             Reflux.listenTo(API.createPlantSuccess, "createSuccess"),
             Reflux.listenTo(API.createPlantFailure, "createFailure")],

    getInitialState: function () {
        return {
            error: null
        };
    },

    render: function () {
        return (
            <form onSubmit={this.handleSubmit}>
                {this.renderErrors()}
                <Input type="text" name="title" label="Title" />
                <Input type="textarea" name="description" label="Description" />
                <DateInput name="planted" label="Date Planted" />
                <Input type="submit" value="Save New Plant" bsStyle="primary" className="pull-right" />
            </form>
        );
    },

    renderErrors: function () {
        if (this.state.error) {
            return (
                <Alert bsStyle="danger" onDismiss={this.handleErrorDismiss}>
                    <p>{this.state.error}</p>
                </Alert>
            );
        }
    },

    handleErrorDismiss: function () {
        this.setState({
            error: false
        });
    },

    handleSubmit: function (event) {
        event.preventDefault();
        var newPlantObj = {};
        for (var i=0; i<event.target.length; i++) {
            var value = event.target[i].value;
            var name = event.target[i].name;
            if (name && value) {
                newPlantObj[name] = value;
            }
        }
        console.log(newPlantObj);
        API.createPlant(newPlantObj);
    },

    createSuccess: function(data) {
        this.setState({
            error: null
        });
        this.transitionTo('plant', {plantId: encodeURIComponent(data.rid)});
    },

    createFailure: function(message) {
        this.setState({
            error: message
        });
    }
});