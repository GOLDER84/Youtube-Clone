package Model;

public enum PremiumSubscriptionPackages {
    BRONZE(30 , 5) , SILVER(60 , 9) , GOLD(180 , 14) ;
    private final int days;
    private final double price;
    PremiumSubscriptionPackages(int days, double price) {
        this.days = days;
        this.price = price;
    }
    public int getDays() {
        return days;
    }
    public double getPrice() {
        return price;
    }
}
