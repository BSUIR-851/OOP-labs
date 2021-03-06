package sample.classes;

import java.io.Serializable;

public abstract class Water implements Serializable {

    public String name;
    public float temp;
    public float depth;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public float getTemp() { return this.temp; }
    public void setTemp(float temp) { this.temp = temp; }

    public float getDepth() { return this.depth; }
    public void setDepth(float depth) { this.depth = depth; }

    public Water () {
        this.name = "water";
        this.temp = 0f;
        this.depth = 0f;
    }
}
