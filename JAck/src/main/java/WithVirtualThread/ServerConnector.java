package WithVirtualThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnector extends Thread{
    ServerSocket SS;
    int numberofClients;
    List<VirtualThreadTaker> Takers=new ArrayList<>();
    @Override
    public void run(){
        try {
            SS=new ServerSocket(7000);
            while (true){
                Socket Client=SS.accept();
                numberofClients++;
                Thread Maker=Thread.ofVirtual().start(new VirtualThreadTaker(Client,numberofClients));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
