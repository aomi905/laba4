package bsu.rfact.java.laba4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainFrame extends JFrame {

    private static final int WIDTH = 1000,
            HEIGHT = 500;
    private boolean fileLoaded = false;
    private GraphicsDisplay display = new GraphicsDisplay();
    private JCheckBoxMenuItem showAxisMenuItem,
            showMarkersMenuItem;
    private JFileChooser fileChooser = null;
    public MainFrame(){
        super("Plotting function graphs based on prepared files");

        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2;
        setExtendedState(MAXIMIZED_BOTH);

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
        showMarkersMenuItem.setSelected(false);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    private void openGraphics(File selectedFile) {
    }

    public static void main(String[] args) {

    }

    private class GraphicsMenuListener implements MenuListener{
        @Override
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent e) {}

        @Override
        public void menuCanceled(MenuEvent e) {}
    }

}
