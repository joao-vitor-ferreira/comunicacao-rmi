import java.rmi.RemoteException;

public class Wavelet implements WaveletInterface {
	private int nivel;

	public Wavelet() {
		this.nivel = 0;
	}

	public void setNivel(int nivel) throws RemoteException {
		this.nivel = nivel;
	}

	public byte[][] wavelet(byte[][] pic, int IMG_SIZE, int nivel) throws RemoteException {
        byte[][] npic = new byte[IMG_SIZE][IMG_SIZE];
		if(this.nivel < nivel) {
			this.nivel = nivel;
		}

		int nivelAux = (int) Math.pow(2, nivel);

		for(int x=0; x<IMG_SIZE/(nivelAux/2); x+=2) {
			for(int y=0; y<IMG_SIZE/(nivelAux/2); y+=2) {
				npic[IMG_SIZE/nivelAux+x/nivelAux][y/nivelAux] = (byte) Math.abs(((pic[x][y]-pic[x+1][y]+pic[x][y+1]-pic[x+1][y+1])/4));
				npic[x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux] = (byte) Math.abs(((pic[x][y]+pic[x+1][y]-pic[x][y+1]-pic[x+1][y+1])/4));
				npic[IMG_SIZE/nivelAux+x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux] = (byte) Math.abs(((pic[x][y]-pic[x+1][y]-pic[x][y+1]+pic[x+1][y+1])/4));
			}
		}

		return(npic);
	}
}
