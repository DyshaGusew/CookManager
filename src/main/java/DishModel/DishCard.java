package DishModel;

import javafx.scene.control.Button;

public class DishCard {
    private String name;
    private String ratingUrl;
    private String imageUrl;
    private static DishCard selectedDish;
    private Button nextButton;

    public void setSelected() {
        selectedDish = this;
    }

    public static DishCard getSelectedDish() {
        return selectedDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingUrl() {
        return ratingUrl;
    }

    public void setRatingUrl(float rating)
    {
        if(rating <= 1.4){
            this.ratingUrl = "/img/Other/1star.png";
        }
        if(rating <= 2.4 && rating >= 1.5){
            this.ratingUrl = "/img/Other/2stars.png";
        }
        if(rating <= 3.4 && rating >= 2.5){
            this.ratingUrl = "/img/Other/3stars.png";
        }
        if(rating <= 4.4 && rating >= 3.5){
            this.ratingUrl = "/img/Other/4stars.png";
        }
        if(rating >= 4.5){
            this.ratingUrl = "/img/Other/5stars.png";
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String nameRec) {
        this.imageUrl = "/img/MainImage/" + nameRec;
    }
}
