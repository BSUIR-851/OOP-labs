package sample.serialize.factories;

import sample.serialize.serializators.CustomSerializator;

public class CustomSerializatorFactory extends SerializatorFactory{
    public CustomSerializator Create() { return new CustomSerializator(); }
}
