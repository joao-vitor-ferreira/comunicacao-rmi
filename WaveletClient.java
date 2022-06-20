import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFrame;

public class WaveletClient extends JFrame {
    final int IMG_SIZE=512;
    public byte[][] pic = new byte[IMG_SIZE][IMG_SIZE];
    public int[] lut = new int[256];

    BufferedImage img = new BufferedImage(IMG_SIZE, IMG_SIZE,BufferedImage.TYPE_INT_BGR);

    public byte[][] waveResult(byte[][] npic, int IMG_SIZE) {
		double tot;
		int nivelAux = (int) Math.pow(2,3);
		byte[][] apic = new byte[IMG_SIZE][IMG_SIZE];
        byte[][] resultNpic = new byte[IMG_SIZE][IMG_SIZE];

		for(int x=0; x<IMG_SIZE; x+=nivelAux) {
			for(int y=0; y<IMG_SIZE; y+=nivelAux) { 
				npic[x/nivelAux][y/nivelAux] = (byte) ((this.pic[x][y]+this.pic[x+1][y]+this.pic[x][y+1]+this.pic[x+1][y+1])/(nivelAux*2));
			}
		}

		for(int x=0; x<IMG_SIZE; x++) {
			for(int y=0; y<IMG_SIZE; y++) {
				apic[x][y] = npic[x][y];
			}
		}
		for(int x=10; x<IMG_SIZE-10; x++) {
			for(int y=10; y<IMG_SIZE-10; y++) {
				tot=0;
				for(int xi=-10; xi<10; xi++) {
					for(int yi=-10; yi<10; yi++) {
						tot += npic[x+xi][y+yi];
					}
				}
				tot = tot / (20*20);
				if (npic[x][y]<tot)
					apic[x][y]=0;
			}
		}

		return(apic);
	}

    public byte[][] waveletJoin(byte[][] npic_aux, byte[][] npic, int IMG_SIZE, int nivel) {
		int nivelAux = (int) Math.pow(2, nivel);

		for(int x=0; x<IMG_SIZE/(nivelAux/2); x+=2) {
			for(int y=0; y<IMG_SIZE/(nivelAux/2); y+=2) {
				npic[IMG_SIZE/nivelAux+x/nivelAux][y/nivelAux] = npic_aux[IMG_SIZE/nivelAux+x/nivelAux][y/nivelAux];
				npic[x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux] = npic_aux[x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux];
				npic[IMG_SIZE/nivelAux+x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux] = npic_aux[IMG_SIZE/nivelAux+x/nivelAux][IMG_SIZE/nivelAux+y/nivelAux];
			}
		}

		return(npic);
	}
 
    public void start(List<WaveletInterface> wList) {
        setTitle("Wavelet");
        setSize(IMG_SIZE,IMG_SIZE+30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // criar um mapa de cores (tons de cinzento)
        for (int i=0; i<256; i++) lut[i]=2*i+256*i*2+256*256*i*2;
     
        Image im = getImage("im2.png");
        int[] pixels = new int[IMG_SIZE * IMG_SIZE];

        // converts Image to array of byte
        PixelGrabber pg = new PixelGrabber(im, 0, 0, IMG_SIZE, IMG_SIZE, pixels, 0, IMG_SIZE); 
        try { 
            pg.grabPixels();   
        } catch (InterruptedException ee) {
            System.err.println("interrupted waiting for pixels!");
             return;
        }
        for(int i=0; i<IMG_SIZE; i++)
           for(int j=0; j<IMG_SIZE; j++)
                pic[i][j] = (byte) (((pixels[j*IMG_SIZE + i])&0xff)/2);
           	//if ((pixels[j*IMG_SIZE + i]&0x80)==0x80) pic[i][j]=127;
            //else pic[i][j]=0;

        byte[][] npic0 = new byte[IMG_SIZE][IMG_SIZE];
        byte[][] npic1 = new byte[IMG_SIZE][IMG_SIZE];
        byte[][] npic2 = new byte[IMG_SIZE][IMG_SIZE];
        WaveletClientThread threads[] = new WaveletClientThread[3];

        for (int i=0; i<threads.length; i++) {
            threads[i] = new WaveletClientThread(i+1, wList.get(i), IMG_SIZE);
        }

        threads[0].setpic(pic);
        threads[1].setpic(pic);
        threads[2].setpic(pic);

        double start = new Date().getTime();
        try {
            Thread t1 = new Thread(threads[0]);
            Thread t2 = new Thread(threads[1]);
            Thread t3 = new Thread(threads[2]);
            t1.start();
            t2.start();
            t3.start();
            t1.join();
            t2.join();
            t3.join();

            npic0 = threads[0].getNpic();
            npic1 = threads[1].getNpic();
            npic2 = threads[2].getNpic();
            npic1 = this.waveletJoin(npic1, npic0, this.IMG_SIZE, 2);
            npic2 = this.waveletJoin(npic2, npic1, this.IMG_SIZE, 3);
            pic = this.waveResult(npic2, this.IMG_SIZE);
        } catch(Exception e) {
            System.err.println("Client exception: "+ e.toString());
            e.printStackTrace();
        }
        System.out.println("Demorou " + (new Date().getTime()-start) + " ms");
        repaint();
    }
    
    public void paint(Graphics g) {
        for(int i=0; i<IMG_SIZE; i++)
           for(int j=0; j<IMG_SIZE; j++) {
               //System.out.print(pic[i][j] + " ");
               img.setRGB(i,j,lut[pic[i][j]]);
            }
       Graphics2D g2 = (Graphics2D) g;
       g2.drawImage(img,0,25,this);
    }
 
    protected Image getImage(String fileName) {
        Image img = getToolkit().getImage(fileName);
        try {
           MediaTracker tracker = new MediaTracker(this);
           tracker.addImage(img, 0);
           tracker.waitForID(0);
           tracker.removeImage(img,0);
        } catch (Exception e) { e.printStackTrace(); }
        return img;
    }
    
    public static void main(String[] args) {
        System.setProperty("java.security.policy","file:./java.policy");
        System.setSecurityManager(new SecurityManager());
        WaveletClient wc = new WaveletClient();
        if (args.length!=6) {
            System.err.println("Usage: WaveletClient host1 port1 host2 port2 host3 port3");
            System.exit(-1);
        }



        try {
            Registry registry1 = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            Registry registry2 = LocateRegistry.getRegistry(args[2], Integer.parseInt(args[3]));
            Registry registry3 = LocateRegistry.getRegistry(args[4], Integer.parseInt(args[5]));
            
            List<WaveletInterface> wList = new ArrayList<WaveletInterface>(); 

            wList.add((WaveletInterface) registry1.lookup("WaveletInterface"));
            wList.add((WaveletInterface) registry2.lookup("WaveletInterface"));
            wList.add((WaveletInterface) registry3.lookup("WaveletInterface"));
            
            wc.start(wList);
        } catch(Exception e) {
            System.err.println("Client exception: "+ e.toString());
            e.printStackTrace();
        }
    }
}
