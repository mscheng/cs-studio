/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.graphene;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.epics.graphene.*;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.data.ValueUtil;

/**
 *
 * @author carcassi
 */
class Histogram1DFunction extends Function<VImage> {
    
    private Function<List<VDouble>> argument;
    private Dataset1D dataset = new Dataset1DArray(1000000);
    private Histogram1D histogram = Histograms.createHistogram(dataset);
    private Histogram1DRenderer renderer = new Histogram1DRenderer(300, 200);
    private VImage previousImage;
    private List<Histogram1DUpdate> histogramUpdates = Collections.synchronizedList(new ArrayList<Histogram1DUpdate>());
    private List<Histogram1DRendererUpdate> rendererUpdates = Collections.synchronizedList(new ArrayList<Histogram1DRendererUpdate>());

    public Histogram1DFunction(Function<List<VDouble>> argument) {
        this.argument = argument;
    }
    
    public void update(Histogram1DUpdate update) {
        // Already synchronized
        histogramUpdates.add(update);
    }
    
    public void update(Histogram1DRendererUpdate update) {
        // Already synchronized
        rendererUpdates.add(update);
    }

    @Override
    public VImage getValue() {
        List<VDouble> newData = argument.getValue();
        if (newData.isEmpty() && previousImage != null && histogramUpdates.isEmpty() && rendererUpdates.isEmpty())
            return previousImage;
        
        // Update the dataset
        Dataset1DUpdate update = new Dataset1DUpdate();
        for (VDouble vDouble : newData) {
            update.addData(vDouble.getValue());
        }
        dataset.update(update);
        
        // Process all updates
        synchronized(histogramUpdates) {
            for (Histogram1DUpdate histogramUpdate : histogramUpdates) {
                histogram.update(histogramUpdate);
            }
            histogramUpdates.clear();
        }
        histogram.update(new Histogram1DUpdate().recalculateFrom(dataset));

        // Process all renderer updates
        synchronized(rendererUpdates) {
            for (Histogram1DRendererUpdate rendererUpdate : rendererUpdates) {
                renderer.update(rendererUpdate);
            }
            rendererUpdates.clear();
        }
        
        // If no size is set, don't calculate anything
        if (renderer.getImageHeight() == 0 && renderer.getImageWidth() == 0)
            return null;
        
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), histogram);
        
        previousImage = ValueUtil.toVImage(image);
        return previousImage;
    }
    
}