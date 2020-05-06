package sample.factory;

import sample.classes.InterIslandSea;
import sample.classes.Water;

public class InterIslandSeaFactory extends SeaFactory {
    public Water Create() { return new InterIslandSea(); }
}
