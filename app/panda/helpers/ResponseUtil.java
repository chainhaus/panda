package panda.helpers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class ResponseUtil {

    public static ObjectNode createResponse(
            Object response, boolean ok) {

        ObjectNode result = Json.newObject();
        if (response instanceof String) {
            result.put("data", (String) response);
        }
        else {
            result.put("data", Json.toJson(response));
        }

        return result;
    }

}
