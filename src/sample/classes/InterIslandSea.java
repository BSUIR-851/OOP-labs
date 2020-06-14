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
}
