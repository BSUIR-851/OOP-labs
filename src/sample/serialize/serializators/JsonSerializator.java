package sample.serialize.serializators;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import sample.classes.Water;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class JsonSerializator extends Serializator {
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects) {
        // serialize list of objects with JSON format
        boolean isCorrect = false;
        try {
            FileOutputStream fos = new FileOutputStream(file);

            if (fos != null) {
                // USE: gson-2.8.6
                // create json builder to serialize
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Water.class, new InterfaceAdapter());
                Gson gson = builder.create();

                // get type token of list objects
                Type typeToken = new TypeToken<java.util.List<Water>>(){}.getType();

                // write string representation of object to file
                fos.write(gson.toJson(waterObjects, typeToken).getBytes());

                isCorrect = true;
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isCorrect;
    }

    public ArrayList<Water> deserializeFromFile(File file) {
        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                // USE: gson-2.8.6
                // create json builder to deserialize
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Water.class, new InterfaceAdapter());
                Gson gson = builder.create();

                // get type token of list objects
                Type typeToken = new TypeToken<java.util.List<Water>>(){}.getType();

                // read all data from serialized file
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                waters.addAll(gson.fromJson(new String(data, StandardCharsets.UTF_8), typeToken));

                fis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return waters;
    }

    public class InterfaceAdapter implements JsonSerializer, JsonDeserializer {

        private static final String CLASSNAME = "CLASSNAME";
        private static final String DATA = "DATA";

        public Object deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();
            Class klass = getObjectClass(className);
            return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
        }
        public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
            jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
            return jsonObject;
        }
        /****** Helper method to get the className of the object to be deserialized *****/
        public Class getObjectClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }
        }
    }
}
