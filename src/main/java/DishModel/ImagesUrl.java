package DishModel;

public enum ImagesUrl {
    HEART_BLACK("/img/icons8-heart-black-50.png"),
    HEART_GREEN("/img/icons8-heart-green.png"),
    BASKET_GRAY("/img/icons8-add-to-basket-shop-96.png"),
    BASKET_GREEN("/img/icons8-add-to-basket-shop-green.png");

    private final String url;

    ImagesUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
