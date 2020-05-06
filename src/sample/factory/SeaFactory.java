package sample.factory;

import sample.classes.Water;
import sample.classes.Sea;

public class SeaFactory extends WaterFactory {
    public Water Create() { return new Sea(); }
}
