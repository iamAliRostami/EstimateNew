package com.leon.estimate_new.infrastructure;

import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.leon.estimate_new.enums.MapType;

public interface IMapLayer {
    String getMapUrl(TileKey tileKey);

    WebTiledLayer createLayer(MapType layerType);

    WebTiledLayer createLayer();
}
