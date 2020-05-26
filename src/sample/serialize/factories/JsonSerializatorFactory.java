package sample.serialize.factories;

import sample.serialize.serializators.JsonSerializator;

public class JsonSerializatorFactory extends SerializatorFactory{
    public JsonSerializator Create() { return new JsonSerializator(); }
}
