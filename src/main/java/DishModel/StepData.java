package DishModel;

import javafx.scene.image.Image;

import java.io.InputStream;

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

    public String getStepDescription() {
        return stepDescription;
    }

    public String getStepImagePath() {
        return stepImagePath;
    }
}