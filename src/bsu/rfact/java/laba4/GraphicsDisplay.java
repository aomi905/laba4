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
            showMarkers = true,
            showGrid = true,
            showRotate = false;
    private BasicStroke graphicsStroke,
            axisStroke,
            markerStroke,
            gridStroke;
    private Font axisFont,
            divisionsFont;
    public GraphicsDisplay(){
        setBackground(Color.PINK);


        graphicsStroke = new BasicStroke(4.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 10.0f,
                new float[]{20, 5, 5, 5, 10, 5, 5, 5},
                0.0f);

        axisStroke = new BasicStroke(4.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                null, 0.0f);
        markerStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                null, 0.0f);

        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
        divisionsFont = new Font("Serif", Font.PLAIN, 16);
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

    public void setShowGrid(boolean showGrid){
        this.showGrid = showGrid;
        repaint();
    }

    public void setShowRotate(boolean antiClockRotate) {
        this.showRotate = antiClockRotate;
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
        canvas.setColor(Color.RED);
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

        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();
        if (minY <= 0.0 && maxY>= 0.0) {
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
                    (float)(labelPos.getX() + bounds.getHeight() + 10),
                    (float)(labelPos.getY() - bounds.getY()));
        }
        if (minX <= 0.0 && maxX >= 0.0){
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



        for (Double[] point : graphicsData){

            Integer countableX = point[0].intValue();
            Integer countableY = point[1].intValue();
            if (countableX % 2 == 0 && countableY % 2 == 0)
                canvas.setColor(Color.BLUE);
            else
                canvas.setColor(Color.RED);

            Point2D.Double center = xyToPoint(point[0], point[1]);
            canvas.draw(new Line2D.Double(shiftPoint(center, 10, 0),
                    shiftPoint(center, -10, 0)));
            canvas.draw(new Line2D.Double(shiftPoint(center, 0, 10),
                    shiftPoint(center, 0, -10)));

            canvas.draw(new Line2D.Double(shiftPoint(center, 10, 5),
                    shiftPoint(center, 10, -5)));
            canvas.draw(new Line2D.Double(shiftPoint(center, -10, 5),
                    shiftPoint(center, -10, -5)));
            canvas.draw(new Line2D.Double(shiftPoint(center, 5, 10),
                    shiftPoint(center, -5, 10)));
            canvas.draw(new Line2D.Double(shiftPoint(center, 5, -10),
                    shiftPoint(center, -5, -10)));
        }
    }

    protected void paintGrids(Graphics2D canvas){
        canvas.setStroke(gridStroke);

        double from = minX,
                step = (maxX - minX) / 10;
        while (from < maxX){
            canvas.draw(new Line2D.Double(xyToPoint(from, maxY),
                    xyToPoint(from, minY)));
            from += step;
        }

        from = minY;
        step = (maxY - minY) / 10;
        while (from < maxY){
            canvas.draw(new Line2D.Double(xyToPoint(minX, from),
                    xyToPoint(maxX, from)));
            from += step;
        }
    }

    protected void paintDevisions(Graphics2D canvas){

        canvas.setFont(divisionsFont);

        double x,
                y;
        if (minX <= 0.0 && maxX >= 0.0)
            x =0;
        else
            x = minX;
        if (minY <= 0.0 && maxY >= 0.0)
            y = 0;
        else
            y = minY;

        double from = Math.ceil(minX),
                step = Math.ceil((maxX - minX) / 10);
        while (from < Math.ceil(maxX)){
            Point2D.Double point = xyToPoint(from, y);
            String num = String.valueOf(from);
            canvas.drawString(num,
                    (float)(point.getX() + 10),
                    (float) (point.getY() - 10));
            canvas.drawString("|",
                    (float)(point.getX()),
                    (float) (point.getY()));
            from += step;
        }

        from = Math.ceil(minY);
        step = Math.ceil((maxY - minY) / 10);
        while (from < Math.ceil(maxY)){
            Point2D.Double point = xyToPoint(x, from);
            String num = String.valueOf(Math.ceil(from));
            canvas.drawString(num,
                    (float)(point.getX() + 10),
                    (float) (point.getY() - 10));
            canvas.drawString("__",
                    (float)(point.getX()),
                    (float) (point.getY()));

            from += step;
        }
    }

    protected void rotate(Graphics2D canvas){
        AffineTransform transform = AffineTransform.getRotateInstance(
                -Math.PI / 2,
                getSize().getWidth() / 2,
                getSize().getHeight() / 2);
        transform.concatenate(new AffineTransform(
                getSize().getHeight() / getSize().getWidth(),0.0, 0.0,
                getSize().getWidth() / getSize().getHeight(),
                (getSize().getWidth() - getSize().getHeight()) / 2,
                (getSize().getHeight() - getSize().getWidth()) / 2));
        canvas.setTransform(transform);
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
        Font oldFont = canvas.getFont();
        Paint oldPaint = canvas.getPaint();

        if(showRotate)
            rotate(canvas);
        if (showGrid)
            paintGrids(canvas);
        if (showAxis) {
            paintAxis(canvas);
            paintDevisions(canvas);
        }
        paintGraphics(canvas);
        if (showMarkers)
            paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

}

