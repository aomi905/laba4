package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;

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

    protected void paintAxis(Graphics2D canvas){
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();
        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                    xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5,
                    arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10,
                    arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("Y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("Y",
                    (float)labelPos.getX() + 10,
                    (float)(labelPos.getY() - bounds.getY()));
        }
        if (minY <= 0.0 && maxY >= 0.0){
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                    xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20,
                    arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("X", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("X",
                    (float)(labelPos.getX() - bounds.getWidth() - 10),
                    (float)(labelPos.getY() + bounds.getY()));
        }
    }

    protected void paintMarkers(Graphics2D canvas){
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.ORANGE);
        canvas.setPaint(Color.ORANGE);

        for (Double[] point : graphicsData){
            Ellipse2D.Double marker = new Ellipse2D.Double();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            Point2D.Double corner = shiftPoint(center, 3, 3);
            marker.setFrameFromCenter(center, corner);
            canvas.draw(marker);
            canvas.fill(marker);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (graphicsData == null || graphicsData.length == 0)
            return;

        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length-1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        for (int i = 1; i < graphicsData.length; i++){
            if (graphicsData[i][1] < minY)
                minY = graphicsData[i][1];
            if (graphicsData[i][1] > maxY)
                maxY = graphicsData[i][1];
        }

        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);

        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX){
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY){
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D)g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showAxis)
            paintAxis(canvas);
        if (showMarkers)
            paintMarkers(canvas);

        canvas.setStroke(oldStroke);
        canvas.setColor(oldColor);
        canvas.setPaint(oldPaint);
        canvas.setFont(oldFont);
    }

}

