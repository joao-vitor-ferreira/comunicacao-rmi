import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WaveletInterface extends Remote {
    public void setNivel(int nivel) throws RemoteException;
    public byte[][] wavelet(byte[][] pic, int IMG_SIZE, int nivel) throws RemoteException;
}
