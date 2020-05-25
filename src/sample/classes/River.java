package sample.classes;

public class River extends Water {
    public float length;
    public float volume;

    public void setLength(float length) { this.length = length; }
    public float getLength() { return this.length; }

    public void setVolume(float volume) { this.volume = volume; }
    public float getVolume() { return this.volume; }

    public River() {
        super();
        this.length = 0f;
        this.volume = 0f;
    }

    @Override
    public String printInfo() {
        String name = this.getName(),
               temp = String.valueOf(this.getTemp()),
               depth = String.valueOf(this.getDepth()),
               length = String.valueOf(this.getLength()),
               volume = String.valueOf(this.getVolume());

        String info = "River:\n"+
                      "Name: "+name+";\n"+
                      "Temperature: "+temp+";\n"+
                      "Depth: "+depth+";\n"+
                      "Length: "+length+";\n"+
                      "Volume: "+volume+";\n";
        return info;
    }

    @Override
    public String[] getFieldsData() {
        String[] data = new String[5];
        data[0] = this.getName();
        data[1] = String.valueOf(this.getTemp());
        data[2] = String.valueOf(this.getDepth());
        data[3] = String.valueOf(this.getLength());
        data[4] = String.valueOf(this.getVolume());
        return data;
    }

}
