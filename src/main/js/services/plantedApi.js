var request = require('superagent');

var API = require("../actions/plantedApi");

API.getPlants.listen(function() {
    request
        .get('/data/plant')
        .accept('json')
        .end(function(err, res) {
            if (res.error) {
                API.getPlantsFailure(res.text);
            } else {
                API.getPlantsSuccess(res.body);
            }
        });
});

API.fetchPlant.listen(function(id) {
    request
        .get('/data/plant/' + id)
        .accept('json')
        .end(function(err, res) {
            if (res.error) {
                API.fetchPlantFailure(res.text);
            } else {
                API.fetchPlantSuccess(res.body);
            }
        });
});

API.createPlant.listen(function(plant) {
    request
        .post('/data/plant')
        .type('json')
        .send(plant)
        .end(function(err, res) {
            if (res.error) {
                API.createPlantFailure(res.text);
            } else {
                API.createPlantSuccess(res.body);
            }
        });
});

API.createReport.listen(function(report) {
    request
        .post('/data/report')
        .type('json')
        .send(report)
        .end(function(err, res) {
            if (res.error) {
                API.createReportFailure(res.text);
            } else {
                API.createReportSuccess(res.body);
            }
        });
});