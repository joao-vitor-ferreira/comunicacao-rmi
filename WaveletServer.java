import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;


public class WaveletServer extends Wavelet {
    public static void main(String[] args) {
        System.setProperty("java.security.policy","file:./java.policy");
        System.setSecurityManager(new SecurityManager());
        try {
            if (args.length!=2) {
                System.err.println("Usage: WaveletServer host port");
                System.exit(-1);
            }

            Wavelet obj = new Wavelet();
            
            WaveletInterface stub = (WaveletInterface) UnicastRemoteObject.exportObject(obj, 0);
            
            Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            
            System.setProperty("java.rmi.server.hostname","localhost");
            registry.bind("WaveletInterface", stub);
            System.err.println("Server ready");
        } catch(Exception e) {
            System.err.println("Server exception: "+e.toString());
            e.printStackTrace();
        }
    }
}
