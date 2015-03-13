var reflux = require("reflux");

var actions = reflux.createActions([
    "getPlants",
    "getPlantsSuccess",
    "getPlantsFailure",

    "fetchPlant",
    "fetchPlantSuccess",
    "fetchPlantFailure",

    "createPlant",
    "createPlantSuccess",
    "createPlantFailure",

    "createReport",
    "createReportSuccess",
    "createReportFailure"
]);
module.exports = actions;