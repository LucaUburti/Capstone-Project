package uburti.luca.fitnessfordiabetics.FoodInfo;

public class FoodInfoPOJO {
    private String name;
    private String minValue;
    private String maxValue;
    private String average;

    public FoodInfoPOJO(String name, String minValue, String maxValue, String average) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
