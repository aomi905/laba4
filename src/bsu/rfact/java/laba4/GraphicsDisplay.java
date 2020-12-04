package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

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

    public GraphicsDisplay(){
        setBackground(Color.PINK);

        axisFont = new Font("Serif", Font.BOLD, 36);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.CAP_ROUND, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

    }

    public void showGraphics(Double[][] graphicsData){
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis){
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers){
        this.showMarkers = showMarkers;
        repaint();
    }

    protected Point2D.Double xyToPoint(double x, double y){
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src,
                                        double deltaX, double deltaY){
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

}

