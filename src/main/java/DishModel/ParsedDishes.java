package DishModel;

import java.util.ArrayList;
import java.util.List;

public class ParsedDishes {
    private String name;
    private String imageUrl;
    private String ratingUrl;

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

    public List<ParsedDishes> parseData() {
        List<ParsedDishes> parsedData = new ArrayList<>();

        return parsedData;
    }
}
