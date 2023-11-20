package data;

public enum Queries {
    IPHONE("iPhone 15"),
    MAC("MacBook"),
    AIRPODS("AirPods");

    public final String description;

    Queries(String description) {
        this.description = description;
    }
}
