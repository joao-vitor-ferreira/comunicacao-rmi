public class WaveletClientThread implements Runnable {
    private WaveletInterface remoteInterface;
    private int IMG_SIZE;
    private final int nivel;
	private byte[][] pic;
	private byte[][] npic;

    public WaveletClientThread(int nivel, WaveletInterface remoteInterface, int IMG_SIZE) {
        this.nivel = nivel;
        this.remoteInterface = remoteInterface;
        this.IMG_SIZE = IMG_SIZE;
    }

    public byte[][] getNpic() {

        return this.npic;
    }

    public void setpic(byte[][] pic) {
        this.pic = pic;
    }

    @Override
    public void run() {
        try {
            this.npic = remoteInterface.wavelet(this.pic, this.IMG_SIZE, this.nivel);
        } catch(Exception e) {
            System.err.println("Client exception: "+ e.toString());
            e.printStackTrace();
        }
    }
}
