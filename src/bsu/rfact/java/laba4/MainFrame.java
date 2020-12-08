package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainFrame extends JFrame {

    private static final int WIDTH = 1200,
            HEIGHT = 1000;
    private JFileChooser fileChooser = null;
    private boolean fileLoaded = false;
    private GraphicsDisplay display = new GraphicsDisplay();
    private JCheckBoxMenuItem showAxisMenuItem,
            showMarkersMenuItem,
            showGridsMenuItem,
            showRotateMenuItem;
    public MainFrame(){
        super("Plotting function graphs based on prepared files");

        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Open file") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("F"));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);

        JMenu graphicsMenu = new JMenu("Graph");
        menuBar.add(graphicsMenu);
        Action showAxisAction = new AbstractAction("Show coordinate axes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Show point markers") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        Action showGridsAction = new AbstractAction("Show grid") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowGrid(showGridsMenuItem.isSelected());
            }
        };
        showGridsMenuItem = new JCheckBoxMenuItem(showGridsAction);
        graphicsMenu.add(showGridsMenuItem);
        showGridsMenuItem.setSelected(true);
        Action showRotateAction = new AbstractAction("Rotate 90 degree to the left") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowRotate(showRotateMenuItem.isSelected());
            }
        };
        showRotateMenuItem = new JCheckBoxMenuItem(showRotateAction);
        graphicsMenu.add(showRotateMenuItem);
        showRotateMenuItem.setSelected(false);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    private void openGraphics(File selectedFile) {

        try {
            DataInputStream in = new DataInputStream(
                    new FileInputStream(selectedFile));

            Double[][] graphicsData = new Double[in.available() / (Double.SIZE / 8) / 2][];

            int i = 0;
            while (in.available() > 0){
                Double x = in.readDouble(),
                        y = in.readDouble();
                graphicsData[i++] = new Double[]{x, y};
            }

            if (graphicsData != null && graphicsData.length > 0){
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }

            in.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "The specified file wasn't found", "Data loading error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Error in reading point coordinates from file", "Data loading error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    public static void main(String[] args) {

        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private class GraphicsMenuListener implements MenuListener{
        @Override
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridsMenuItem.setEnabled(fileLoaded);
            showRotateMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent e) {}

        @Override
        public void menuCanceled(MenuEvent e) {}
    }

}
