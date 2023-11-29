package DishModel;

public class DishCard {
    private String name;

    private String time;

    private String ratingUrl;

    private String imageUrl;

    private String calories;

    private String timeImageUrl;

    private static DishCard selectedDish;

    public String getTime() {
        return time;
    }

    public void setTime(int time) {
        if(time < 60){
            this.time = time + "мин";
        }
        else {
            int hour = time/60;
            int minutes = time % 60;

            this.time = hour + "ч " + minutes + "мин";
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

    public void setRatingUrl(float rating) {
        if(rating <= 1.4){
            this.ratingUrl = "/img/Other/1star.png";
        }
        else if(rating <= 2.4 && rating >= 1.5){
            this.ratingUrl = "/img/Other/2stars.png";
        }
        else if(rating <= 3.4 && rating >= 2.5){
            this.ratingUrl = "/img/Other/3stars.png";
        }
        else if(rating <= 4.4 && rating >= 3.5){
            this.ratingUrl = "/img/Other/4stars.png";
        }
        else{
            this.ratingUrl = "/img/Other/5stars.png";
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String nameRec) {
        this.imageUrl = "/img/MainImage/" + nameRec;
    }

    public void setCalories(int calories) {
        this.calories = calories + " Ккал";
    }

    public String getCalories() {
        return calories;
    }

    public String getTimeImageUrl() {
        return timeImageUrl;
    }

    public void setTimeImageUrl(String timeImageUrl) {
        this.timeImageUrl = timeImageUrl;
    }
}
