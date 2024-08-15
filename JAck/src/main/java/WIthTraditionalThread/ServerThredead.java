package WIthTraditionalThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import WIthTraditionalThread.ServerThredead;
import WithVirtualThread.VirtualThreadTaker;

public class ServerThredead extends Thread{
    ServerSocket SS;
    int numberofclients;
    @Override
    public void run(){
        try {
            SS=new ServerSocket(1234);

            while (true){
                Socket S=SS.accept();
                numberofclients++;
               // Thread T=new Thread(new VirtualThreadTaker(S,numberofclients));
               // T.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
