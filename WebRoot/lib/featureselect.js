/**
 * 
 */

  define([
    "esri/map",
    "esri/InfoTemplate",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/symbols/SimpleFillSymbol",
    "esri/symbols/SimpleLineSymbol",
    "esri/tasks/IdentifyTask",
    "esri/tasks/IdentifyParameters",
    "esri/dijit/Popup",
    "dojo/_base/array",
    "esri/Color",
    "dojo/dom-construct",
    "dojo/domReady!"
  ], function (
    Map, InfoTemplate, ArcGISDynamicMapServiceLayer, SimpleFillSymbol,
    SimpleLineSymbol, IdentifyTask, IdentifyParameters, Popup,
    arrayUtils, Color, domConstruct
  ) {

    var identifyTask, identifyParams;

    var popup = new Popup({
      fillSymbol: new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
        new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
          new Color([255, 0, 0]), 2), new Color([255, 255, 0, 0.25]))
    }, domConstruct.create("div"));

    map = new Map("map", {
      //basemap: "satellite",
      center: [-83.275, 42.573],
      zoom: 18,
      infoWindow: popup
    });

    map.on("load", mapReady);

    var parcelsURL = "http://172.17.3.145:6080/arcgis/rest/services/BaseMap/MapServer";
    map.addLayer(new ArcGISDynamicMapServiceLayer(parcelsURL,
      { opacity: 0.55 }));

    function mapReady () {
      map.on("click", executeIdentifyTask);
      //create identify tasks and setup parameters
      identifyTask = new IdentifyTask(parcelsURL);

      identifyParams = new IdentifyParameters();
      identifyParams.tolerance = 3;
      identifyParams.returnGeometry = true;
      identifyParams.layerIds = [0, 2];
      identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_ALL;
      identifyParams.width = map.width;
      identifyParams.height = map.height;
    }

    function executeIdentifyTask (event) {
      identifyParams.geometry = event.mapPoint;
      identifyParams.mapExtent = map.extent;

      var deferred = identifyTask
        .execute(identifyParams)
        .addCallback(function (response) {
          // response is an array of identify result objects
          // Let's return an array of features.
          return arrayUtils.map(response, function (result) {
            var feature = result.feature;
            var layerName = result.layerName;

/*            feature.attributes.layerName = layerName;
            if (layerName === 'Tax Parcels') {
              var taxParcelTemplate = new InfoTemplate("",
                "${Postal Address} <br/> Owner of record: ${First Owner Name}");
              feature.setInfoTemplate(taxParcelTemplate);
            }
            else if (layerName === 'Building Footprints') {
              console.log(feature.attributes.PARCELID);
              var buildingFootprintTemplate = new InfoTemplate("",
                "Parcel ID: ${PARCELID}");
              feature.setInfoTemplate(buildingFootprintTemplate);
            }*/
            return feature;
          });
        });

      // InfoWindow expects an array of features from each deferred
      // object that you pass. If the response from the task execution
      // above is not an array of features, then you need to add a callback
      // like the one above to post-process the response and return an
      // array of features.
      map.infoWindow.setFeatures([deferred]);
      map.infoWindow.show(event.mapPoint);
    }
  });
