package DishModel;

import java.util.ArrayList;
import java.util.List;

public class ParsedDishes {
    private String name;
    private String imageUrl;
    private String ratingUrl;
    private DishCard.DishCategory category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRatingUrl() {
        return ratingUrl;
    }

    public void setRatingUrl(String ratingUrl) {
        this.ratingUrl = ratingUrl;
    }

    public DishCard.DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCard.DishCategory category) {
        this.category = category;
    }

    public List<ParsedDishes> parseData() {
        List<ParsedDishes> parsedData = new ArrayList<>();

        return parsedData;
    }
}
