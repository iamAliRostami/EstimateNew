package com.leon.estimate_new.utils.gis;

import com.esri.arcgisruntime.geometry.Point;

public class Storage {

    private static Storage mStorage;
    private Point currentPoint;
    private double scale;
    private double scaleSecondLayer;
    private OsmMapLayer currentLayer;
    private Storage() {
    }

    public static final Storage getStorage() {
        if (mStorage == null) {
            mStorage = new Storage();
        }
        return mStorage;
    }
//    private ArcGISTiledMapServiceLayer mTopoLayer;
//    private ArcGISTiledMapServiceLayer mSatelliteLayer;

    public OsmMapLayer getCurrentLayer() {
        return currentLayer;
    }

    public void setCurrentLayer(OsmMapLayer mCurrentLayer) {
        this.currentLayer = mCurrentLayer;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double mScale) {
        this.scale = mScale;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    public double getScaleSecondLayer() {
        return scaleSecondLayer;
    }

    public void setScaleSecondLayer(double mScaleSecondLayer) {
        this.scaleSecondLayer = mScaleSecondLayer;
    }
}
