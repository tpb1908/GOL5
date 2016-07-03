import java.io.Serializable;
import java.util.Random;

/**
 * Created by pearson-brayt15 on 16/03/2016.
 */
public class Cell implements Serializable {
    public float isAlive;
    private boolean killed = false;
    public boolean isBlock;
    private float generationsSurvived;
    private float lowerSurvivalBound;
    private float upperSurvivalBound;
    private float birthLowerBound;
    private float birthUpperBound;
    private float colourCode;
    private float foodLevel;
    private Random random;
    private float generationsBetweenSplit;

    private int lowTempResistance;
    private int highTempResistance;
    private int humidityResistance;
    private int acidResistance;
    private int alkaliResistance;
    private int atmosphericPressureResistance;
    private int radiationResistance;
    private double minOxygenLevel;
    private int toxicityResistance;

    public Cell() {}

    public void kill() {
        killed = true;
        isAlive = 0;
        generationsSurvived = 0;
        //NO need to sort the rest, they are reset on split()
    }

    public void split() {
        isAlive = 1;
        generationsSurvived = 0;

    }


    public void changeFood(float increase) {
        foodLevel += increase;
    }

    //Get cell template is replaced by getting the cell itself

    public int generateColourCode() {
        if(isBlock) {
            return 689127;
        } else if(isAlive > 0) {
            return 16711680;
        } else {
            return 16777215;
        }
    }

    public float getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(float isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public float getGenerationsSurvived() {
        return generationsSurvived;
    }

    public void setGenerationsSurvived(float generationsSurvived) {
        this.generationsSurvived = generationsSurvived;
    }

    public float getLowerSurvivalBound() {
        return lowerSurvivalBound;
    }

    public void setLowerSurvivalBound(float lowerSurvivalBound) {
        this.lowerSurvivalBound = lowerSurvivalBound;
    }

    public float getUpperSurvivalBound() {
        return upperSurvivalBound;
    }

    public void setUpperSurvivalBound(float upperSurvivalBound) {
        this.upperSurvivalBound = upperSurvivalBound;
    }

    public float getBirthLowerBound() {
        return birthLowerBound;
    }

    public void setBirthLowerBound(float birthLowerBound) {
        this.birthLowerBound = birthLowerBound;
    }

    public float getBirthUpperBound() {
        return birthUpperBound;
    }

    public void setBirthUpperBound(float birthUpperBound) {
        this.birthUpperBound = birthUpperBound;
    }

    public float getColourCode() {
        return colourCode;
    }

    public void setColourCode(float colourCode) {
        this.colourCode = colourCode;
    }

    public float getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(float foodLevel) {
        this.foodLevel = foodLevel;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public float getGenerationsBetweenSplit() {
        return generationsBetweenSplit;
    }

    public void setGenerationsBetweenSplit(float generationsBetweenSplit) {
        this.generationsBetweenSplit = generationsBetweenSplit;
    }

    public int getLowTempResistance() {
        return lowTempResistance;
    }

    public void setLowTempResistance(int lowTempResistance) {
        this.lowTempResistance = lowTempResistance;
    }

    public int getHighTempResistance() {
        return highTempResistance;
    }

    public void setHighTempResistance(int highTempResistance) {
        this.highTempResistance = highTempResistance;
    }

    public int getHumidityResistance() {
        return humidityResistance;
    }

    public void setHumidityResistance(int humidityResistance) {
        this.humidityResistance = humidityResistance;
    }

    public int getAcidResistance() {
        return acidResistance;
    }

    public void setAcidResistance(int acidResistance) {
        this.acidResistance = acidResistance;
    }

    public int getAlkaliResistance() {
        return alkaliResistance;
    }

    public void setAlkaliResistance(int alkaliResistance) {
        this.alkaliResistance = alkaliResistance;
    }

    public int getAtmosphericPressureResistance() {
        return atmosphericPressureResistance;
    }

    public void setAtmosphericPressureResistance(int atmosphericPressureResistance) {
        this.atmosphericPressureResistance = atmosphericPressureResistance;
    }

    public int getRadiationResistance() {
        return radiationResistance;
    }

    public void setRadiationResistance(int radiationResistance) {
        this.radiationResistance = radiationResistance;
    }

    public double getMinOxygenLevel() {
        return minOxygenLevel;
    }

    public void setMinOxygenLevel(double minOxygenLevel) {
        this.minOxygenLevel = minOxygenLevel;
    }

    public int getToxicityResistance() {
        return toxicityResistance;
    }

    public void setToxicityResistance(int toxicityResistance) {
        this.toxicityResistance = toxicityResistance;
    }
}
