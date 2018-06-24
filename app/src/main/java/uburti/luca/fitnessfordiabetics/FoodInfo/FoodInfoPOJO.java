package uburti.luca.fitnessfordiabetics.FoodInfo;

public class FoodInfoPOJO {
    private String name;
    private String giMinValue;
    private String giMaxValue;
    private String giAverage;
    private String photo;
    private String kcal;
    private String carbs;
    private String protein;
    private String fat;

    public FoodInfoPOJO(String name, String giMinValue, String giMaxValue, String giAverage, String photo, String kcal, String carbs, String protein, String fat) {
        this.name = name;
        this.giMinValue = giMinValue;
        this.giMaxValue = giMaxValue;
        this.giAverage = giAverage;
        this.photo = photo;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiMinValue() {
        return giMinValue;
    }

    public void setGiMinValue(String giMinValue) {
        this.giMinValue = giMinValue;
    }

    public String getGiMaxValue() {
        return giMaxValue;
    }

    public void setGiMaxValue(String giMaxValue) {
        this.giMaxValue = giMaxValue;
    }

    public String getGiAverage() {
        return giAverage;
    }

    public void setGiAverage(String giAverage) {
        this.giAverage = giAverage;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }
}
