import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by pearson-brayt15 on 16/03/2016.
 */
public class WindowManager extends JFrame {
    private AtomicBoolean paused;
    public JTextArea output;    //The text area for console output
    private JButton pauseButton;  //Pause button
    private JButton stepButton;
    private JButton drawGridButton;
    private JButton startButton;  //Start button
    private JButton saveButton;   //Save button
    private JButton loadButton;   //Load button
    private JButton editCellButton;
    private JButton saveImageButton;
    private JButton cellTemplateButton;
    private JButton environmentVarButton;
    private GridBagConstraints constraints;
    
    private CellTemplateView cellTemplateView;
    private EnvironmentTemplateView environmentTemplateView;
    private GridManager gridManager;
    private Visualiser visualiser;
    
    private Thread gridThread;

    private Cell cellTemplate;
    private Object[] environmentTemplate;

    private int textLine = 0;     //An integer which is incremented as the text area fills
    private float percent;
    private int gridSize;

    public WindowManager() {
        setLayout(new MigLayout("wrap 3"));
        gridManager = new GridManager(this);
        paused = new AtomicBoolean(false);
        output = new JTextArea(50, 50);
        pauseButton = new JButton();    //Initialising the buttons
        stepButton = new JButton();
        startButton = new JButton();
        drawGridButton = new JButton();
        saveButton = new JButton();
        loadButton = new JButton();
        editCellButton = new JButton();
        saveImageButton = new JButton();
        cellTemplateButton = new JButton();
        environmentVarButton = new JButton();
        setMinimumSize(this.getPreferredSize());
        
        initComponents();
    }
    
    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        
        output.setBackground(Color.black);
        output.setForeground(Color.white);
        JScrollPane sp = new JScrollPane(output);
        output.setMinimumSize(new Dimension(500, 900));
        sp.setMinimumSize(new Dimension(500, 900));
        add(sp);
        menuBar.setSize(new Dimension(400, 50));
        menuBar.setMinimumSize(new Dimension(200, 50));

        startButton.setText("Begin");
        pauseButton.setText("Pause");
        stepButton.setText("Step");
        drawGridButton.setText("Draw divisions");
        saveButton.setText("Save");
        loadButton.setText("Load");
        editCellButton.setText("Edit");
        saveImageButton.setText("Save image");
        cellTemplateButton.setText("Cell template");
        environmentVarButton.setText("Environment");

        setButtonLook(startButton);
        setButtonLook(pauseButton);
        setButtonLook(stepButton);
        setButtonLook(saveButton);
        setButtonLook(drawGridButton);
        setButtonLook(loadButton);
        setButtonLook(editCellButton);
        setButtonLook(saveImageButton);
        setButtonLook(cellTemplateButton);
        setButtonLook(environmentVarButton);
        menuBar.add(startButton);
        menuBar.add(pauseButton);
        menuBar.add(stepButton);
        menuBar.add(drawGridButton);
        menuBar.add(saveButton);
        menuBar.add(loadButton);
        menuBar.add(editCellButton);
        menuBar.add(saveImageButton);
        menuBar.add(cellTemplateButton);
        menuBar.add(environmentVarButton);

        menuBar.setForeground(Color.BLACK);
        menuBar.setBackground(Color.WHITE);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(1, 1, 1, 1);
        Border compound = new CompoundBorder(line, margin);
        menuBar.setBorder(compound);
        setJMenuBar(menuBar);

        addButtonListeners();
        
    }

    private void setButtonLook(JButton button) {
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
    }

    public void addButtonListeners(){
        final WindowManager thisWindow = this;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pcnt = JOptionPane.showInputDialog("Enter the percentage of cells in the array to be populated \n" +
                        "from 1 = 100% to 0 = 0%");
                try {
                    percent = Float.parseFloat(pcnt);
                    if (percent > 1 || percent < 0) {
                        setOutput("\"" + pcnt + "\" is not a valid value. Please try again");
                    } else {
                        String size = JOptionPane.showInputDialog("Enter a grid size between 100 and 1000");
                        try {
                            gridSize = Integer.parseInt(size);
                            if (gridSize >= 100 && gridSize <= 5000) {
                                setOutput("Initialising a new grid with dimensions of " + gridSize + "^2");
                                environmentTemplateView = new EnvironmentTemplateView(thisWindow);
                                environmentTemplateView.setLocation(getX(), getY());
                                environmentTemplateView.setVisible(true);
                                CellTemplateView cellView = new CellTemplateView(thisWindow);
                                cellView.setLocation(environmentTemplateView.getX() + environmentTemplateView.getWidth(), environmentTemplateView.getY());
                                cellView.setVisible(true);
                            }
                        } catch (NumberFormatException e1) {
                            setOutput("\"" + pcnt + "\" is not a valid value");
                        }
                    }
                } catch (NumberFormatException n) {
                    setOutput("\"" + pcnt + "\" is not a valid value");
                } catch (NullPointerException n) {
                    //This will be thrown if the operation is cancelled
                } catch (Exception unknown) {
                    setOutput("An unknown error has occurred");
                    unknown.printStackTrace();
                }
            }
        });

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gridThread.isAlive()) {
                    gridManager.iterate();
                    setOutput("Performed single iteration");
                    visualiser.calc();
                } else {
                    setOutput("No simulation is running");
                }
            }
        });
        //The listeners below are fairly self explanatory
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
                startButton.setVisible(false);
            }
        });
        editCellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pauseButton.doClick();
                //TODO Finish the cell edit view so that this button has something to do
            }
        });
        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gridThread.isAlive()) {
                    paused.set(true);
                    visualiser.saveImage();
                    paused.set(false);
                } else {
                    setOutput("No simulation is running. Therefore there is no image to save");
                }
            }
        });
        cellTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cellTemplateView == null && gridThread.isAlive()) {
                    cellTemplateView = new CellTemplateView(thisWindow);
                } else if(gridThread.isAlive()){
                    cellTemplateView.setVisible(true);
                    cellTemplateView.setLastCellTemplate();
                } else {
                    setOutput("No simulation is running");
                }
            }
        });
        drawGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gridThread.isAlive()) {
                    String cellSize = JOptionPane.showInputDialog(
                            thisWindow,
                            "Enter an integer size for each cell. \n" +
                                    "it must be less than the size of the grid, but greater than 10 ");
                    try{
                        int size = Integer.parseInt(cellSize);
                        if(size >= 10 && size < visualiser.getGridSize() ) {
                            visualiser.drawGrid(size);
                        } else {
                            setOutput("\"" + cellSize + "\"" + " is not a valid");
                        }
                    } catch (NumberFormatException e1) {
                        setOutput("\"" + cellSize + "\"" + " is not a valid");
                    }
                } else {
                    setOutput("No simulation is running");
                }
            }
        });


        environmentVarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (environmentTemplateView != null) {
                    environmentTemplateView.setVisible(true);
                } else {
                    environmentTemplateView = new EnvironmentTemplateView(thisWindow);
                }

            }
        });
    }

    public void setOutput(String text) {
        String current = output.getText();
        output.setText(current + "\n" + textLine++ + " " + text);
    }

    public void setCellTemplate(Cell template) {
        this.cellTemplate = template;
    }



    //The getters and setters below are used when initialising the grid
    public void setCellViewSetUpDone() {
        //cellViewSetUpDone = true;
    }

    public void setEnvironmentVarViewSetUpDone() {
        //environmentVarViewSetUpDone = true;
    }

    public boolean getEnvironmentVarViewSetUpDone() {
        return  true;//environmentVarViewSetUpDone;
    }

    public boolean getCellViewSetUpDone() {
        return  true;//cellViewSetUpDone;
    }

    private void save() {}

    private void load() {
    }


    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (gridThread.isAlive()) {
                if (!paused.get()) {
                    pauseButton.setText("Start");
                    setOutput("Paused");
                    paused.set(true);
                    visualiser.setParentPauseState(paused);
                } else {
                    pauseButton.setText("Pause");
                    setOutput("Unpaused");
                    paused.set(false);
                    visualiser.setParentPauseState(paused);
                    System.out.println("Setting visualiserPaused to " + paused);

                    // Resume
                    synchronized (gridThread) {
                        gridThread.notify();
                    }
                }
            }
        }
    }



}
