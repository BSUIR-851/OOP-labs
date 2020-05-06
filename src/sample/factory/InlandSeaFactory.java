package sample.factory;

import sample.classes.InlandSea;
import sample.classes.Water;

public class InlandSeaFactory extends SeaFactory {
    public Water Create() { return new InlandSea(); }
}
