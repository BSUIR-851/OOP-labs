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

    @Override
    public String printInfo() {
        String name = this.getName(),
               temp = String.valueOf(this.getTemp()),
               depth = String.valueOf(this.getDepth()),
               amOfStraitsString = String.valueOf(this.getAmOfStraits()),
               amOfBaysString = String.valueOf(this.getAmOfBays());

        String info = "Sea:\n"+
                      "Name: "+name+";\n"+
                      "Temperature: "+temp+";\n"+
                      "Depth: "+depth+";\n"+
                      "Amount of straits: "+amOfStraitsString+";\n"+
                      "Amount of bays: "+amOfBaysString+";\n"+
                      "Longest river:\n"+
                      this.getLongestRiver().printInfo()+"\n";
        return info;
    }

    @Override
    public String[] getFieldsData() {
        String[] data = new String[10];
        data[0] = this.getName();
        data[1] = String.valueOf(this.getTemp());
        data[2] = String.valueOf(this.getDepth());
        data[3] = String.valueOf(this.getAmOfStraits());
        data[4] = String.valueOf(this.getAmOfBays());
        longestRiver = this.getLongestRiver();
        data[5] = longestRiver.getName();
        data[6] = String.valueOf(longestRiver.getTemp());
        data[7] = String.valueOf(longestRiver.getDepth());
        data[8] = String.valueOf(longestRiver.getLength());
        data[9] = String.valueOf(longestRiver.getVolume());
        return data;
    }
}
