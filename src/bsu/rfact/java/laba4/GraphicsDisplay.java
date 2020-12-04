package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;

public class GraphicsDisplay extends JPanel {

    private Double[][] graphicsData;
    private double minX,
            maxX,
            minY,
            maxY,
            scale;
    private boolean showAxis = true,
            showMarkers = true;
    private Font axisFont;
    private BasicStroke axisStroke,
            graphicsStroke,
            markerStroke;

}

