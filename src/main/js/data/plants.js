var Reflux = require("reflux");
var Immutable = require('immutable');

var API = require("../actions/plantedApi");

var PlantStore = Reflux.createStore({

    init: function() {
        this.listenTo(API.getPlantsSuccess, this.getPlantsSuccess);
        this.listenTo(API.getPlantsFailure, this.getPlantsFailure);

        this.data = Immutable.List([]);
        API.getPlants();
    },

    getInitialState: function() {
        return this.data;
    },

    setData: function(data) {
        this.data = Immutable.List(data);
        this.trigger(this.data.toArray());
    },

    // ACTION HANDLING

    getPlantsSuccess: function(plants) {
        this.setData(plants);
    },

    getPlantsFailure: function(msg) {
        this.setData([]);
    },

    // API

    getPlants: function() {
        return this.data.toArray();
    }
});

module.exports = PlantStore;