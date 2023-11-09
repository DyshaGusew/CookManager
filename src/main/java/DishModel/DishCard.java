package DishModel;

import javafx.scene.control.Button;

public class DishCard {
    private String name;
    private String time;
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

    public String getTime() {
        return time;
    }

    public void setTime(int time) {
        if(time < 60){
            this.time = String.valueOf(time) + " мин";
        }
        else {
            float n = time/60.0f*10;
            int result = (int)Math.round(n);

            String form;
            if (time % 60 == 0) {
                switch (time){
                    case 60:
                        form = " час";
                        break;
                    case 120:
                        form = " часа";
                        break;
                    case 180:
                        form = " часа";
                        break;
                    case 240:
                        form = " часа";
                        break;
                    default:
                        form = " часов";
                }
            }
            else {
                form = " часа";
            }
            this.time = String.valueOf((float) result/10) + form;
        }

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
