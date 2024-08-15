package WithVirtualThread;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

@OwnAnotation
public class ControllerCars {
    List< Car> Cars=new ArrayList<>();

    @RequestPath(Url="/PostCar")
    public String CreateCar(RequestHttp request) {
        MapperJson MJ=new MapperJson();
        Car C=MJ.ConvertToJavaObject(request.getBody(), new TypeReference<Car>() {
        });
        Cars.add(C);
        String response=RequestHttp.ResponseText(null);
        return  response;
    }
    @RequestPath(Url="/GetCars")
    public String GetAllCars(RequestHttp request){
        MapperJson MJ=new MapperJson();
        String JsonCars=MJ.ConvertToJson(Cars);
        String response=RequestHttp.RessponseJson(JsonCars);
        return response;
    }

}
