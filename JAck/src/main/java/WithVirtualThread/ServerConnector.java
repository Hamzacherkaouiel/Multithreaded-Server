package WithVirtualThread;

import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerConnector extends Thread{
    ServerSocket SS;
    int numberofClients;
    Map<String, AbstractMap.SimpleEntry<?,Method>> Apis;
    List<VirtualThreadTaker> Takers=new ArrayList<>();
    @Override
    public void run(){
        try {
            SS=new ServerSocket(7000);
           // Apis=new HashMap<>();
            this.takeAllApis();
            System.out.println(Apis);
            while (true){
                Socket Client=SS.accept();
                numberofClients++;
                Thread Maker=Thread.ofVirtual().start(new VirtualThreadTaker(Client,numberofClients,Apis));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void takeAllApis(){
        Apis=new HashMap<>();
        Reflections reflections = new Reflections("WithVirtualThread"); // Remplacez par votre package
        Set<Class<?>> anotatedClasses =reflections.getTypesAnnotatedWith(OwnAnotation.class);
        for(Class<?> Anoted : anotatedClasses){
            try {
                Object ApiClass= Anoted.getDeclaredConstructor().newInstance();
                for(Method method:Anoted.getDeclaredMethods()){
                    RequestPath Path=method.getAnnotation(RequestPath.class);
                    if(Path!=null){
                        Apis.put(Path.Url(), new AbstractMap.SimpleEntry<>(ApiClass, method));
                    }
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
