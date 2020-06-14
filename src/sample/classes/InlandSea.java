package sample.classes;

public class InlandSea extends Sea {
    public int amOfCountries;
    private int amOfTradeRoutes;

    public void setAmOfCountries(int amOfCountries) { this.amOfCountries = amOfCountries; }
    public int getAmOfCountries() { return this.amOfCountries; }

    public void setAmOfTradeRoutes(int amOfTradeRoutes) { this.amOfTradeRoutes = amOfTradeRoutes; }
    public int getAmOfTradeRoutes() { return this.amOfTradeRoutes; }

    public InlandSea() {
        super();
        this.amOfCountries = 0;
        this.amOfTradeRoutes = 0;
    }
}