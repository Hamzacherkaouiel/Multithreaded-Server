package WithVirtualThread;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VirtualThreadTaker implements  Runnable{
    Socket S;
    int ClientNumber;
    List<VirtualThreadTaker> Clients;

    List<Car> Cars= new ArrayList<>();
    public void setClients(List<VirtualThreadTaker> clients) {
        Clients = clients;
    }
    public VirtualThreadTaker(Socket S, int n){
        this.S=S;
        this.ClientNumber=n;
        Cars.add(new Car("MERCEDS",1234));
        Cars.add(new Car("BMW",5678));
    }

    @Override
    public void run() {

        try {
            InputStream I = S.getInputStream();
            OutputStream O=S.getOutputStream();
            BufferedReader B=new BufferedReader(new InputStreamReader(I));
            PrintWriter P=new PrintWriter(O,true);
            String Hello="Hello user number "+ClientNumber;
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("HTTP/1.1 200 OK\r\n");
            responseBuilder.append("Content-Type: text/plain\r\n");
            responseBuilder.append("Content-Length: ").append(Hello.length()).append("\r\n");
            responseBuilder.append("\r\n");  // Ligne vide entre les en-têtes HTTP et le corps
            responseBuilder.append(Hello);
            P.println(responseBuilder.toString());
            while (true){
                List<String>  Lines=new ArrayList<>();
                List<String[]> Strings=new ArrayList<>();
                String line;
                StringBuilder RequestBuilder=new StringBuilder();
                int contentLength = -1;
                while ((line = B.readLine()) != null && !line.isEmpty()){
                    RequestBuilder.append(line).append("/n");
                    Lines.add(line);
                    String [] mots=line.split(" ");
                    Strings.add(mots);
                    System.out.println(line);
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                    }
                }
                if (Lines.isEmpty()) {
                    continue; // Passe à la prochaine itération si rien n'est reçu
                }
                if(!Lines.isEmpty()) {

                    StringBuilder bodyBuilder = new StringBuilder();
                    if (contentLength > 0) {
                        char[] bodyBuffer = new char[contentLength];
                        B.read(bodyBuffer, 0, contentLength);
                        bodyBuilder.append(bodyBuffer);
                    }
                    String body = bodyBuilder.toString();

                    String Request = RequestBuilder.toString();
                    System.out.println(Request);
                    if (Request != null) {
                        /*if(Request.length()>1) {*/
                        String method = "";
                        String Path = "";
                        String[] Array = {};
                        if (!Strings.isEmpty() && Strings.get(0).length > 1) {


                            System.out.println(Strings.get(0)[0] + "hd");
                            method = Strings.get(0)[0];
                            //Array = Strings.get(0)[0].split("/n");
                            Path = Strings.get(0)[1];
                        }

                        if (method.equals("POST") && Path.equals("/PostCar")) {
                            MapperJson MJ = new MapperJson();
                            Car Created = MJ.ConvertToJavaObject(body, new TypeReference<Car>() {
                            });
                            Cars.add(Created);
                            P.println("HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: text/plain\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: keep-alive\r\n" + // Connexion persistante
                                    "\r\n");

                        } else {
                            if (method.equals("GET") && Path.equals("/GetCars")) {
                                MapperJson MJ = new MapperJson();
                                String Json = MJ.ConvertToJson(Cars);
                                StringBuilder GetBuilder = new StringBuilder();
                                GetBuilder.append("HTTP/1.1 200 OK\r\n");
                                GetBuilder.append("Content-Type: application/json\r\n");
                                GetBuilder.append("Content-Length: ").append(Json.length()).append("\r\n");
                                GetBuilder.append("Connection: keep-alive\r\n"); // Connexion persistante

                                GetBuilder.append("\r\n");  // Ligne vide entre les en-têtes HTTP et le corps
                                GetBuilder.append(Json);

                                P.println(GetBuilder.toString());
                            }
                            //}


                        }
                    }
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
