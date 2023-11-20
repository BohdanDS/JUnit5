package data;

public enum Queries {
    IPHONE("iPhone 15"),
    MAC("MacBook"),
    AIRPODS("AirPods");

    public final String searchValue;

    Queries(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getDescription() {
        return this.searchValue;
    }
}
