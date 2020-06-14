package sample.classes;

import sample.factory.RiverFactory;
import sample.factory.WaterFactory;

public class Sea extends Water {
    protected int amOfStraits;
    protected int amOfBays;
    public River longestRiver;

    public void setAmOfStraits(int amOfStraits) { this.amOfStraits = amOfStraits; }
    public int getAmOfStraits() { return this.amOfStraits; }

    public void setAmOfBays(int amOfBays) { this.amOfBays = amOfBays; }
    public int getAmOfBays() { return this.amOfBays; }

    public void setLongestRiver(River river) { this.longestRiver = river; }
    public River getLongestRiver() { return this.longestRiver; }

    public Sea() {
        super();
        this.setAmOfStraits(0);
        this.setAmOfBays(0);
        River river = new River();
        this.setLongestRiver(river);
    }

}
