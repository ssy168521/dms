define(["dojo/_base/declare","esri/layers/tiled"], function(declare){
return declare(esri.layers.TiledMapServiceLayer, {
        constructor: function(option) {
          this.spatialReference = new esri.SpatialReference({ wkid:4326 });
          this.initialExtent = (this.fullExtent = new esri.geometry.Extent(-180.0, -90.0, 180.0, 90.0, this.spatialReference));
          
          this.url=option.url;
          
          this.tileInfo = new esri.layers.TileInfo({
            "rows" : 512,
            "cols" : 512,
            "compressionQuality" :0,
            "origin" : {
              "x" : -180,
              "y" : 180
            },
            "spatialReference" : {
              "wkid" : 4326
            },
            "lods" : [
              {"level" : 0, "resolution" : 0.703125, "scale" : 2.95497598570834E8},
              {"level" : 1, "resolution" : 0.3515625 , "scale" : 1.47748799285417E8},
              {"level" : 2, "resolution" : 0.17578125, "scale" : 7.38743996427087E7},
              {"level" : 3, "resolution" : 0.087890625, "scale" : 3.69371998213544E7},
              {"level" : 4, "resolution" : 0.0439453125, "scale" : 1.84685999106772E7},
              {"level" : 5, "resolution" : 0.02197265625, "scale" :  9234299.95533859},
              {"level" : 6, "resolution" : 0.010986328125, "scale" : 4617149.97766929},
              {"level" : 7, "resolution" : 0.0054931640625, "scale" : 2308574.98883465},
              {"level" : 8, "resolution" : 0.00274658203125, "scale" : 1154287.49441732},
              {"level" : 9, "resolution" : 0.001373291015625, "scale" : 577143.747208662},
              {"level" : 10, "resolution" : 0.000686645507812, "scale" : 288571.873604331},
              {"level" : 11, "resolution" : 0.000343322753906, "scale" : 144285.936802165},
              {"level" : 12, "resolution" : 0.000171661376953, "scale" : 72142.9684010825}
            ]
          });

          this.loaded = true;
          this.onLoad(this);
        },

        getTileUrl: function(level, row, col) {
        	// return "http://59.255.40.13:8399/arcgis/rest/services/2001931/MapServer/tile/"+level+"/"+row+"/"+col;
          //return "http://www.geodata.gov.cn/mapserver/tms/services/100wshiliang/MapServer/tile/"+level+"/"+row+"/"+col;
          //return "http://t" + col%8 + ".tianditu.cn/vec_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=c&TILEMATRIX="+level+"&TILEROW="+row+"&TILECOL="+col+"&FORMAT=tiles";
        	//return "http://192.168.1.234/mapserver/tms/services/globaldem/MapServer/tile/"+level+"/"+row+"/"+col;
        	return this.url+level+"/"+row+"/"+col;
        }
      });
});