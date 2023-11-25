package DishModel;

import java.util.List;

public class TotalIngredientsCard {
    private List<String> listOfIngredients;

    private List<String> listOfCalories;

    public List<String> getListOfCalories() {
        return listOfCalories;
    }

    public void setListOfCalories(List<String> listOfCalories) {
        this.listOfCalories = listOfCalories;
    }

    public List<String> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(List<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }
}
