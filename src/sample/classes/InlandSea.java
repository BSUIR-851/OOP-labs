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

    @Override
    public String printInfo() {
        String name = this.getName(),
                temp = String.valueOf(this.getTemp()),
                depth = String.valueOf(this.getDepth()),
                amOfCountries = String.valueOf(this.getAmOfCountries()),
                amOfTradeRoutes = String.valueOf(this.getAmOfTradeRoutes());

        String info = "River:\n" +
                "Name: " + name + ";\n" +
                "Temperature: " + temp + ";\n" +
                "Depth: " + depth + ";\n" +
                "Length: " + amOfCountries + ";\n" +
                "Volume: " + amOfTradeRoutes + ";\n";
        return info;
    }

    @Override
    public String[] getFieldsData() {
        String[] data = new String[10];
        data[0] = this.getName();
        data[1] = String.valueOf(this.getTemp());
        data[2] = String.valueOf(this.getDepth());
        data[3] = String.valueOf(this.getAmOfCountries());
        data[4] = String.valueOf(this.getAmOfTradeRoutes());
        return data;

    }
}