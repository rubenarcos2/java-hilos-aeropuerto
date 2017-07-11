 /* @author Rubén Arcos
 */

import GuiControles.menuOpciones;
import Hilo.Pista;
import Hilo.Avion;
import Hilo.TorreControl;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Main extends Applet {

    private static final int ANCHOPANTALLA = 800;
    private static final int ALTOPANTALLA = 600;
    private static final int NUMAVIONES = 5; //Constante con el número de aviones
    private BufferedImage fondoPantalla;
    private URL urlFondoPantalla;
    private Graphics2D pantalla;
    private Image offscreen;
    private ImageIcon[] listaIconos;
    private ArrayList<Avion> listaAviones;
    private Pista pista;
    private TorreControl torreControl;
    private menuOpciones menu;

    @Override
    public void init() {
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        offscreen = createImage(getWidth(), getHeight());
        pantalla = (Graphics2D) offscreen.getGraphics();
        listaIconos = new ImageIcon[3];
        try {
            urlFondoPantalla = new URL(getCodeBase(), "imagenes/aeropuerto.jpg");
            fondoPantalla = ImageIO.read(urlFondoPantalla);
            listaIconos[0] = new ImageIcon(new URL(getCodeBase(), "imagenes/iconoLog.png"));
            listaIconos[1] = new ImageIcon(new URL(getCodeBase(), "imagenes/iconoEspera.png"));
            listaIconos[2] = new ImageIcon(new URL(getCodeBase(), "imagenes/iconoPista.png"));
        } catch (MalformedURLException ex) {
            System.out.println("[LOG] - Error en la ruta de la imagen: " + ex);
        } catch (IOException ex) {
            System.out.println("[LOG] - Error en la ruta de la imagen: " + ex);
        }
        listaAviones = new ArrayList<>();
        pista = new Pista(this);
        menu = new menuOpciones(listaIconos);
        add(menu);
        torreControl = new TorreControl(pista, NUMAVIONES, menu);
    }

    @Override
    public void start() {
        for (int i = 0; i < NUMAVIONES; i++) {
            listaAviones.add(new Avion(this, torreControl, pista, menu));
            listaAviones.get(i).start();
            System.out.println("[LOG] - Creado e iniciado avión: " + listaAviones.get(i).getMatricula());
        }
    }

    @Override
    public void stop() {
        torreControl = null;
        listaAviones.removeAll(listaAviones);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        pantalla = (Graphics2D) g;
        super.paintComponents(pantalla);
        pantalla.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        pantalla.drawImage(fondoPantalla, 0, 0, this);
        for (int i = 0; i < listaAviones.size(); i++) {
            listaAviones.get(i).paint((Graphics2D) pantalla);
        }
        try {
            pista.paint(pantalla);
            Thread.sleep(47);
        } catch (InterruptedException ex) {
            System.out.println("[LOG] - ERROR EN FPS: " + ex);
        }
    }
}
