package sample.serialize.serializators;

import com.Plugin.Plugin;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import sample.classes.Water;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class JsonSerializator extends Serializator {
    public boolean serializeToFile(File file, ArrayList<Water> waterObjects, Plugin plugin, String key)
            throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException {
        // serialize list of objects with JSON format
        boolean isCorrect = false;
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
            byte[] objects = gson.toJson(waterObjects, typeToken).getBytes();

            // encrypt objects if choosed to encrypt
            if (plugin != null) {
                objects = plugin.encrypt(objects, key);
            }

            fos.write(objects);
            isCorrect = true;
            fos.close();
        }

        return isCorrect;
    }

    public ArrayList<Water> deserializeFromFile(File file, Plugin plugin, String key)
            throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException {
        // init list for deserialized objects
        ArrayList<Water> waters = new ArrayList<>();
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

            // decrypt objects if choosed to decrypt
            if (plugin != null) {
                data = plugin.decrypt(data, key);
            }
            waters.addAll(gson.fromJson(new String(data, StandardCharsets.UTF_8), typeToken));

            fis.close();
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
