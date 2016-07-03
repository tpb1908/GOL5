import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by pearson-brayt15 on 16/03/2016.
 */
public class GridManager {
    private static WindowManager parent;
    private Cell[][] cells;
    private int generationCounter;
    private transient WindowManager windowManager;
    private Random rnd = new Random();
    private long startTime;
    private ArrayBlockingQueue<Cell[][]> visualiserQueue;
    private int gridSize;
    private Object[] environmentTemplate;
    private Cell cellTemplate;
    private ExecutorService exec;
    private boolean[] valuesUsed;
    
    public GridManager(WindowManager parent) {
        this.parent = parent;
        generationCounter = 0;
        visualiserQueue = new ArrayBlockingQueue<Cell[][]>(500);
        startTime = System.nanoTime();
    }

    public void initGrid( int cellWidth, int cellHeight) {
        cells = new Cell[cellWidth][cellHeight];
    }
    
    public void randomiseCells(float percent) {
        for (Cell[] row : cells) {
            for (int j = 0; j < row.length; j++) {
                row[j] = new Cell();
                if (rnd.nextDouble() < percent) {
                    row[j] = cellTemplate;
                    //If the random meets the requirement, the cell is brought to life
                }
            }
        }
        startTime = System.nanoTime();
    }

    public void iterate() {
        try {
            //TODO- Fix this so that it doesn't do so many runnables
            for(int i = 0; i < cells.length; i++) {
                final Cell[] row = cells[i];
                final int finalI = i;
                exec.submit((Runnable) () -> { //Lambda replaces new Runnable()
                    for (int j = 0; j < cells.length; j++) {
                        doUpdate(finalI, j, row);
                    }
                });
            }
            Thread.sleep(25);
        } catch (Exception e) {} finally {
            generationCounter++;
        }
        if (generationCounter % 100 == 0) { //TODO- Allow this to be changed?
            float endTime = System.nanoTime(); //Collecting the end time
            windowManager.setOutput("Generation count " + generationCounter);
            try {
                float seconds = ((endTime - startTime) / 1000000000);
                System.out.println("Seconds = " + seconds);
                System.out.println("Generation counter = " + generationCounter);
                windowManager.setOutput("Iterating at a speed of " + (100 / seconds) + " iterations per second");
            } catch (ArithmeticException e) {
                e.printStackTrace();
                /*This shouldn't actually happen anymore
                * It used to happen because of a casting problem between int and float, whereby the division
                * of endTime-startTime by 1000000000 resulted in 0, as it was cast from a float to an int
                * This resulted in an ArithmeticException when 100 was divided by 0
                 */
            }
            startTime = System.nanoTime(); //Resetting the start time
        }
        if(visualiserQueue.remainingCapacity() != 0) {
            visualiserQueue.add(cells); //Adding the cell array to the blocking queue, allowing the visualiser to access it
        } else {
            visualiserQueue.clear();
        }
    }

    private void doUpdate(int i, int j, Cell[] row) {
        List<Cell> surroundingCells = new ArrayList<Cell>() {}; //The list for surrounding cells
        try {
            //Collecting the surrounding cells
            surroundingCells.add(cells[i + 1][j]);
            surroundingCells.add(cells[i][j + 1]);
            surroundingCells.add(cells[i][j - 1]);
            surroundingCells.add(cells[i - 1][j + 1]);
            surroundingCells.add(cells[i + 1][j + 1]);
            surroundingCells.add(cells[i - 1][j - 1]);
            surroundingCells.add(cells[i + 1][j - 1]);
            surroundingCells.add(cells[i - 1][j]);
        } catch (IndexOutOfBoundsException outOfBounds) {
                    /*This exception will happen if the cell is at the edge of the array
                    *I need to find out if it is more efficient to catch the exception or to add extra code
                    * to stop the exception from happening.
                    * As there are so many cells, and the exception will happen at least 4000 times per iteration,
                    * it seems like leaving the catch in will be the most efficient
                     */
        } catch (Exception e) {
            //Any other exception, hasn't happened... yet
            System.out.println("An unknown exception has occured while gathering surrounding cells");
            System.out.println("Cell row of " + i + " and cell column of " + j);
            System.out.println(e.getStackTrace());
        }
       // row[j].update(surroundingCells);
        if(row[j].getIsAlive() > 0 && !row[j].isBlock) {
            row[j].changeFood(-0.5f);
            if(row[j].getFoodLevel() 0) {
                kill();
            }
            int sum = 0;
            boolean killed = false;
            for (Cell cell : surroundingCells) {
                //Iterating through the surrounding cells
                sum += cell.getIsAlive();
            }
            if((int) environmentTemplate[4] > (int) row[j].getRadiationResistance()) {
                //System.out.println("Killed due to radiation");
                kill();
            } else if((int)environmentTemplate[1] > (int) row[j].getHumidityResistance()) {
                //System.out.println("Killed due to humidity");
                kill();
            } else if(environmentOxygenLevel < minOxygenLevel && minOxygenLevelUsed) {
                // System.out.println("Killed due to oxygen level");
                kill();
            } else if(environmentPH > acidResistance && acidResistanceUsed) {
                // System.out.println("Killed due to acid: Acid resistance of " + acidResistance + " and PH of " + environmentPH);
                kill();
            }  else if(environmentPH < alkaliResistance && alkaliResistanceUsed) {
                // System.out.println("Killed due to alkali");
                kill();
            } else if(environmentPressure > atmosphericPressureResistance && atmosphericPressureResistanceUsed) {
                // System.out.println("Killed due to pressure");
                kill();
            } else if(environmentToxicity > toxicityResistance && toxicityResistanceUsed) {
                // System.out.println("Killed due to toxicity");
                kill();
            }

            else if (sum < lowerSurvivalBound || sum > upperSurvivalBound /*  Placeholder- If sum is between survival upper and lower bounds */) {
                //System.out.println("Killing myself with sum of " + sum);
                kill();
            }

        }
    }

    public void setOutput(String t) {
        parent.setOutput(t);
    }
}
