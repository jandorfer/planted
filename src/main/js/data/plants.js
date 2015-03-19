var Reflux = require("reflux");
var Immutable = require('immutable');

var API = require("../actions/plantedApi");

var PlantStore = Reflux.createStore({

    init: function() {
        this.listenTo(API.getPlantsSuccess, this.getPlantsSuccess);
        this.listenTo(API.getPlantsFailure, this.getPlantsFailure);
        this.listenTo(API.createPlantSuccess, this.plantCreated);

        this.data = Immutable.List([]);
        API.getPlants();
    },

    getInitialState: function() {
        return this.data;
    },

    setData: function(data) {
        this.data = Immutable.List.isList(data) ? data : Immutable.List(data);
        this.trigger(this.data);
    },

    // ACTION HANDLING

    getPlantsSuccess: function(plants) {
        this.setData(plants);
    },

    getPlantsFailure: function(msg) {
        this.setData([]);
    },

    plantCreated: function(plant) {
        this.setData(this.data.push(plant));
    },

    // API

    getPlants: function() {
        return this.data;
    },

    getPlant: function(id) {
        return this.data.find(function(value) {return value.rid === id;});
    }
});

module.exports = PlantStore;