package WithVirtualThread;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.util.*;

public class VirtualThreadTaker implements  Runnable{
    Socket S;
    int ClientNumber;
    List<VirtualThreadTaker> Clients;
    RequestHttp Http;

    List<Car> Cars= new ArrayList<>();
    Map<String, AbstractMap.SimpleEntry<?,Method>> Apis;
    public void setClients(List<VirtualThreadTaker> clients) {
        Clients = clients;
    }
    public VirtualThreadTaker(Socket S, int n,Map Classes){
        this.S=S;
        this.ClientNumber=n;
        Cars.add(new Car("MERCEDS",1234));
        Cars.add(new Car("BMW",5678));
        Http=new RequestHttp();
        Apis=Classes;
    }

    @Override
    public void run() {

        try {
            InputStream I = null;

            I = S.getInputStream();

            OutputStream O=S.getOutputStream();
            BufferedReader B=new BufferedReader(new InputStreamReader(I));
            PrintWriter P=new PrintWriter(O,true);
            String Hello="Hello user number "+ClientNumber;
            String responseBuilder=this.Http.ResponseText(Hello);
            P.println(responseBuilder);
            while (S.isConnected()&&S.isBound()){
                String Request=this.Reader(B);
                if(Request!=null&&Request.length()>1) {
                    RequestHttp HttpComing = this.Http.parsingHttpRequest(Request);
                    String method =HttpComing.getMethod();
                    String Path = HttpComing.getPath();

                    if(!Apis.isEmpty()){
                        if(Apis.containsKey(Path)){

                            String response= null;
                            try {
                                response = (String) Apis.get(Path).getValue().invoke(Apis.get(Path).getKey(),HttpComing);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            } catch (InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }

                            P.println(response);
                        }
                    }
                        }
                       }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public String Reader(BufferedReader Request) throws IOException {
        String line;
        StringBuilder requestBuilder = new StringBuilder();
        while ((line = Request.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }
        String headers = requestBuilder.toString();
        int contentLength = 0;
        if (headers.contains("Content-Length:")) {
            String[] headerLines = headers.split("\r\n");
            for (String header : headerLines) {
                if (header.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(header.split(": ")[1]);
                    break;
                }
            }
        }
        char[] body = new char[contentLength];
        if (contentLength > 0) {
            Request.read(body, 0, contentLength);
            requestBuilder.append("\r\n").append(new String(body));
        }
        return requestBuilder.toString();


    }

}
