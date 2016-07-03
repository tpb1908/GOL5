import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

/**
 * Created by pearson-brayt15 on 16/03/2016.
 */
public class CellTemplateView extends JFrame {
    private static WindowManager parent;
    private Cell cellTemplate;
    private Cell lastCellTemplate;

    private MigLayout g;
    private JButton OK;
    private JButton Cancel;
    private JSlider[] sliders;
    private JCheckBox[] checkboxes;
    private JScrollPane scrollPane;
    private JPanel scrollPanel;
    private MigLayout scrollLayout;

    private boolean firstRun = true;
    private JCheckBox allRandom;
    private JLabel allRandomLabel;
    private boolean allRandomBool = false;
    private JCheckBox isAlive;
    private JLabel isAliveLabel;
    private int isAliveInt = 1;
    private JLabel drawSizeLabel;
    private JSlider drawSizeSlider;
    private int drawSizeInt;
    private JCheckBox isBlock;
    private JLabel isBlockLabel;
    private boolean isBlockBool = false;

    private int[] lastSliderValues;
    private boolean[] valuesUsed;

    public CellTemplateView(WindowManager parent) {
        this.parent = parent;
        cellTemplate = new Cell();
        lastCellTemplate = cellTemplate;
        sliders = new JSlider[30];
        lastSliderValues = new int[30];
        checkboxes = new JCheckBox[30];
        g = new MigLayout("wrap 3");
        setLayout(g);
        scrollLayout = new MigLayout("wrap 3");
        scrollPanel = new JPanel(scrollLayout);
        scrollPane = new JScrollPane(scrollPanel);
        OK = new JButton("OK");
        Cancel = new JButton("Cancel");
        allRandom = new JCheckBox();
        allRandomLabel = new JLabel("Randomise all traits");
        add(allRandomLabel);
        add(allRandom, "wrap 15");

        drawSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 3);
        Hashtable<Integer, JLabel> map = new Hashtable<Integer, JLabel>();
        map.put(1, new JLabel("1"));
        map.put(10, new JLabel("10"));
        map.put(20, new JLabel("20"));
        map.put(30, new JLabel("30"));
        map.put(40, new JLabel("40"));
        map.put(50, new JLabel("50"));
        drawSizeSlider.setLabelTable(map);
        drawSizeSlider.setLabelTable(map) ;
        drawSizeSlider.setPaintTicks(true);
        drawSizeSlider.setMinorTickSpacing(2);
        drawSizeSlider.setPaintLabels(true);
        drawSizeLabel = new JLabel("Draw size");

        positionUI();
        addListeners();
        this.setMinimumSize(new Dimension(530, 700));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    }

    public void positionUI() {
        add(drawSizeLabel, "wrap 15");
        add(drawSizeSlider, "wrap 15");

        scrollPanel.setMinimumSize(new Dimension(500, 450));
        scrollPane.setMinimumSize(new Dimension(500, 450));
        scrollPanel.setMaximumSize(new Dimension(500, 450));
        scrollPane.setMaximumSize(new Dimension(500, 450));
        scrollPanel.setLayout(scrollLayout);
        scrollPanel.setVisible(true);
        scrollPane.setVisible(true);
        scrollPane.add(scrollPanel);
        scrollPane.setViewportView(scrollPanel);
        add(scrollPane, "span, wrap 15");
        sliders[0] = drawSizeSlider;

        addSlider("Cold resistance", 10, -50, 0, 0, 8, 9);
        addSlider("Heat resistance", 10, 0, 50, 0, 10, 11); //Heat resistance, 0 to 50C
        addSlider("Humidity resistance", 10, 0, 100, 10, 12, 13); //Humidity of 0-100%
        addSlider("Acid resistance", 1, 7, 14, 7, 14, 15); //PH of 7-14
        addSlider("Alkali resistance", 1, 0, 7, 7, 16, 17); //PH of 7-0
        addSlider("Atmospheric pressure", 100, 0, 1000, 0, 18, 19); //bars
        addSlider("Radiation resistance", 5,0, 30, 3, 20, 21); //Gy doesn't just kill, increases chance of mutation
        //addSlider("Decay", 1, 1, 8, 3, 24, 25); //Increases the chance of disease
        addSlider("Oxygen level", 10, 0, 100, 22, 22, 23); //Parts per million, can cause to split faster
        //Possibly add photosynthesis and food supplies per cell
        //addSlider("Static electricity", 1, 1, 8,3, 28, 29); //define later
        addSlider("Toxicity", 1, 0, 8, 0, 24, 25); //https://en.wikipedia.org/wiki/Cytotoxicity
        //Ground suitability for cell division


        add(OK);
        add(Cancel);

        this.setVisible(true);
    }

    public void addSlider(String title, int majorTickSpacing, int low, int high, int defaultValue, final int valueToChange, final int randomBooleanToChange) {
        JLabel label = new JLabel(title);

        JCheckBox UsedCheckBox = new JCheckBox();

        final JSlider  slider = new JSlider(low, high, defaultValue);
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        sliders[valueToChange] = slider;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (valueToChange) {

                    case 8:
                        cellTemplate.setLowTempResistance(slider.getValue());
                        break;
                    case 10:
                        cellTemplate.setHighTempResistance(slider.getValue());
                        break;
                    case 12:
                        cellTemplate.setHumidityResistance(slider.getValue());
                        break;
                    case 14:
                        cellTemplate.setAcidResistance(slider.getValue());
                        break;
                    case 16:
                        cellTemplate.setAlkaliResistance(slider.getValue());
                        break;
                    case 18:
                        cellTemplate.setAtmosphericPressureResistance(slider.getValue());
                        break;
                    case 20:
                        cellTemplate.setRadiationResistance(slider.getValue());
                        break;
                    case 22:
                        cellTemplate.setMinOxygenLevel(slider.getValue());
                        break;
                    case 24:
                        cellTemplate.setToxicityResistance(slider.getValue());
                        break;
                }

            }
        });


        UsedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean checkBox = true;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkBox = false;
                }
                if(randomBooleanToChange%2 != 0) {
                    valuesUsed[randomBooleanToChange] = checkBox;
                }

            }
        });
        scrollPanel.add(label, "wrap 15");
        scrollPanel.add(UsedCheckBox);
        scrollPanel.add(slider, "span 2, wrap 15");
    }

    private void addListeners(){
        drawSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawSizeInt = drawSizeSlider.getValue();
            }
        });
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstRun) {
                    firstRun = false;
                    parent.setCellTemplate(cellTemplate);
                    parent.setCellViewSetUpDone();
                    setVisible(false);
                } else {
                    //generateCellTemplate();
                    parent.setCellTemplate(cellTemplate);
                    parent.setOutput("Changed cell template values");
                    setVisible(false);
                }
            }
        });
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreLastTemplate();
                parent.setOutput("Cancelled changing cell template values");
                setVisible(false);
            }
        });

        allRandom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    scrollPane.setVisible(false);
                    setMinimumSize(new Dimension(500, 300));
                    setSize(500, 300);
                    scrollPane.setMinimumSize(new Dimension(1, 1));
                    scrollPane.setSize(1,1);
                } else {
                    scrollPane.setVisible(true);
                    setMinimumSize(new Dimension(530, 1000));
                    scrollPane.setMinimumSize(new Dimension(500, 700));
                    scrollPane.setSize(500, 700);
                }
            }
        });
    }

    public void setLastCellTemplate() {
        for(int i = 0; i < sliders.length; i++) {
            lastSliderValues[i] = sliders[i].getValue();
        }
        lastCellTemplate = cellTemplate;
    }

    public void restoreLastTemplate() {
        cellTemplate = lastCellTemplate;
        for (int i = 0; i < sliders.length; i += 2) {
            if(sliders[i] != null && i != 2) {
                sliders[i].setValue(lastSliderValues[i]);
            } else {
                System.out.println("Null slider");
            }
        }
    }

    public void setCellTemplate() {
        parent.setCellTemplate(cellTemplate);
    }
}
