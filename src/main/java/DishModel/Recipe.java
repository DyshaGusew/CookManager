package DishModel;

import java.util.List;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private String imageUrl;

    public Recipe(String name, List<Ingredient> ingredients, String description, String imageUrl) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.imageUrl = imageUrl;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
