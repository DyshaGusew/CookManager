package DishModel;

public class StepData {
    private String stepDescription;
    private String stepImagePath;

    public StepData(String stepDescription, String stepImagePath) {
        this.stepDescription = stepDescription;
        this.stepImagePath = stepImagePath;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public void setStepImagePath(String stepImagePath) {
        this.stepImagePath = stepImagePath;
    }

}