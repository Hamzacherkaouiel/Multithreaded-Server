package WithVirtualThread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperJson {

    ObjectMapper OM;


    public  <T> T ConvertToJavaObject(String Json, TypeReference<T> Type) {
        OM = new ObjectMapper();
        T Object;
        try {
            Object = OM.readValue(Json, Type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Object;
    }
    public <T> String ConvertToJson(T Type){
        OM=new ObjectMapper();
        String Json;
        try {
             Json=OM.writeValueAsString(Type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Json;
    }
}
