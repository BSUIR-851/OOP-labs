package sample.serialize.factories;

import sample.serialize.serializators.Serializator;

public abstract class SerializatorFactory {
    abstract public Serializator Create();
}
