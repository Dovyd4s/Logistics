package webControllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import entities.Manager;
import entities.User;

import java.lang.reflect.Type;

public class GsonSerializer implements JsonSerializer<Manager> {
    @Override
    public JsonElement serialize(Manager user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJsonObject = new JsonObject();
        userJsonObject.addProperty("id", user.getId());
        userJsonObject.addProperty("name", user.getName());
        userJsonObject.addProperty("lastName", user.getLastName());
        userJsonObject.addProperty("email", user.getEmail());
        return userJsonObject;
    }
}
