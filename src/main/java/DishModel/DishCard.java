package DishModel;

import javafx.scene.control.Button;

public class DishCard {
    private String name;
    private String ratingUrl;
    private String imageUrl;
    private static DishCard selectedDish;
    private Button nextButton;
    private DishCategory category;

    public enum DishCategory {
        HotDishes,
        Salade,
        Snack,
        Soup,
        Bakery,
        Dessert,
        Drinks
    }

    public DishCategory getCategory() {
        return category;
    }

    public void setSelected() {
        selectedDish = this;
    }

    public static DishCard getSelectedDish() {
        return selectedDish;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
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

    public void setRatingUrl(String ratingUrl) {
        this.ratingUrl = ratingUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
