package WithVirtualThread;

import java.util.HashMap;
import java.util.Map;
public class RequestHttp {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public RequestHttp parsingHttpRequest(String request){
        RequestHttp Http=new RequestHttp();
        String [] Lines=request.split("\r\n");
        String [] line1=Lines[0].split(" ");
        Http.setMethod(line1[0]);
        Http.setPath(line1[1]);
        Http.setVersion(line1[2]);
        int i = 1;
        while (i < Lines.length &&!Lines[i].isEmpty()) {
            String[] header = Lines[i].split(": ");
            Http.getHeaders().put(header[0], header[1]);
            i++;
        }
        if (Http.getMethod().equals("POST") || Http.getMethod().equals("PUT")) {
            StringBuilder body = new StringBuilder();
            for (int j = i + 1; j < Lines.length; j++) {
                body.append(Lines[j]).append("\r\n");
            }
            Http.setBody(body.toString().trim());
        }
        return  Http;
    }
    public static String ResponseText(String Body){
        StringBuilder ResponseBuilder=new StringBuilder();
        ResponseBuilder.append("HTTP/1.1 200 OK\r\n");
        ResponseBuilder.append("Content-Type: text/plain\r\n");
        if(Body!=null) {
            ResponseBuilder.append("Content-Length: ").append(Body.length()).append("\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
            ResponseBuilder.append(Body);
        }
        else{
            ResponseBuilder.append("Content-Length:  0\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
        }
        return ResponseBuilder.toString();
    }
    public static String ResponseHtml(String Body){
        StringBuilder ResponseBuilder=new StringBuilder();
        ResponseBuilder.append("HTTP/1.1 200 OK\r\n");
        ResponseBuilder.append("Content-Type: text/html\r\n");
        if(Body!=null) {
            ResponseBuilder.append("Content-Length: ").append(Body.length()).append("\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
            ResponseBuilder.append(Body);
        }
        else{
            ResponseBuilder.append("Content-Length:  0\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
        }

        return ResponseBuilder.toString();
    }
    public static String RessponseJson(String Body){
        StringBuilder ResponseBuilder=new StringBuilder();
        ResponseBuilder.append("HTTP/1.1 200 OK\r\n");
        ResponseBuilder.append("Content-Type: application/json\r\n");
        if(Body!=null) {
            ResponseBuilder.append("Content-Length: ").append(Body.length()).append("\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
            ResponseBuilder.append(Body);
        }
        else{
            ResponseBuilder.append("Content-Length:  0\r\n");
            ResponseBuilder.append("Connection: keep-alive\r\n");
            ResponseBuilder.append("\r\n");
        }
        return ResponseBuilder.toString();
    }
}
