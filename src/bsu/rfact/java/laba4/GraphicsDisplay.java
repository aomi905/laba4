package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;

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

    protected void paintGraphics(Graphics2D canvas){
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.ORANGE);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++){
            Point2D point = xyToPoint(graphicsData[i][0],
                    graphicsData[i][1]);
            if (i > 0)
                graphics.lineTo(point.getX(), point.getY());
            else
                graphics.moveTo(point.getX(), point.getY());
        }
        canvas.draw(graphics);
    }


}

