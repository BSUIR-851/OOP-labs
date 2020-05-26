package sample.serialize.factories;

import sample.serialize.serializators.BinSerializator;

public class BinSerializatorFactory extends SerializatorFactory {
    public BinSerializator Create() { return new BinSerializator(); }
}
