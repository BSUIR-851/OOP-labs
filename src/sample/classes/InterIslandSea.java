package sample.classes;

public class InterIslandSea extends Sea {
    public String country;
    private int amOfIslands;

    public void setCountry(String country) { this.country = country; }
    public String getCountry() { return this.country; }

    public void setAmOfIslands(int amOfIslands) { this.amOfIslands = amOfIslands; }
    public int getAmOfIslands() { return this.amOfIslands; }

    public InterIslandSea() {
        super();
        this.country = "country";
        this.amOfIslands = 0;
    }

    @Override
    public String printInfo() {
        String name = this.getName(),
                temp = String.valueOf(this.getTemp()),
                depth = String.valueOf(this.getDepth()),
                country = this.getCountry(),
                amOfIslands = String.valueOf(this.getAmOfIslands());

        String info = "River:\n" +
                "Name: " + name + ";\n" +
                "Temperature: " + temp + ";\n" +
                "Depth: " + depth + ";\n" +
                "Country: " + country + ";\n" +
                "Amount of islands: " + amOfIslands + ";\n";
        return info;
    }

    @Override
    public String[] getFieldsData() {
        String[] data = new String[10];
        data[0] = this.getName();
        data[1] = String.valueOf(this.getTemp());
        data[2] = String.valueOf(this.getDepth());
        data[3] = this.getCountry();
        data[4] = String.valueOf(this.getAmOfIslands());
        return data;

    }
}
