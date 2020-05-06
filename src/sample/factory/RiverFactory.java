package sample.factory;

import sample.classes.River;
import sample.classes.Water;

public class RiverFactory extends WaterFactory {
    public Water Create() { return new River(); }

}
