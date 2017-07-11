 /* @author Rubén Arcos
 */
package Hilo;

import GuiControles.VentanaDialogo;
import GuiControles.menuOpciones;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

/*//Caso 1: obtener la hora y salida por pantalla con formato:
 DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
 System.out.println("Hora: " + hourFormat.format(horaLlegada));
 //Caso 2: obtener la fecha y salida por pantalla con formato:
 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 System.out.println("Fecha: " + dateFormat.format(horaLlegada));
 //Caso 3: obtenerhora y fecha y salida por pantalla con formato:
 DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
 System.out.println("Hora y fecha: " + hourdateFormat.format(horaLlegada));
 */
public class Avion extends Thread implements Comparable<Avion> { //HILO

    private static final int ALTO = 26;
    private static final int ANCHO = 25;
    private double x, y, velocidad, angulo, XAterrizaje, escalaXBajando, escalaYBajando;
    private Image imagenAvion, imagenAvionAparcado, imagenAvionAterrizandoIzq, imagenAvionAterrizandoArriba, imagenAvionAterrizandoDer;
    private URL urlImagenAvion, urlImagenAvionAparcado, urlImagenAvionAterrizandoIzq, urlImagenAvionAterrizandoArriba, urlImagenAvionAterrizandoDer;
    private Applet escenario;
    private TorreControl torreControl;
    private menuOpciones menu;
    private int horaLlegada, duracionTrayecto, horaSalida; //en Segundos
    private String matricula;
    private Pista pista;

    public enum ESTADO_AVION {

        VOLANDO, ESPERA_PISTA, ATERRIZANDO, APARCANDO, APARCADO
    };
    private ESTADO_AVION estado;

    public Avion(Applet escenario, TorreControl torreControl, Pista pista, menuOpciones menu) {
        this.escenario = escenario;
        this.torreControl = torreControl;
        this.pista = pista;
        this.menu = menu;
        x = 0;
        y = 0;
        escalaXBajando = 0.2;
        escalaYBajando = 0.2;
        XAterrizaje = 10;
        velocidad = 500;
        angulo = 0;
        estado = ESTADO_AVION.VOLANDO; //Los aviones se encuentran volando al comienzo de la aplicación.
        horaSalida = generarAleatorio(1, 100);
        horaLlegada = generarAleatorio(100, 200);
        duracionTrayecto = horaLlegada - horaSalida;
        matricula = generarMatricula();
        try {
            urlImagenAvion = new URL(escenario.getCodeBase(), "imagenes/Aviones/avionAterrizandoIzq.png");
            imagenAvion = escenario.getImage(urlImagenAvion);
            urlImagenAvionAparcado = new URL(escenario.getCodeBase(), "imagenes/Aviones/avion.png");
            imagenAvionAparcado = escenario.getImage(urlImagenAvionAparcado);
            urlImagenAvionAterrizandoIzq = new URL(escenario.getCodeBase(), "imagenes/Aviones/avion-remolque-izq.png");
            imagenAvionAterrizandoIzq = escenario.getImage(urlImagenAvionAterrizandoIzq);
            urlImagenAvionAterrizandoArriba = new URL(escenario.getCodeBase(), "imagenes/Aviones/avion-remolque-arr.png");
            imagenAvionAterrizandoArriba = escenario.getImage(urlImagenAvionAterrizandoArriba);
            urlImagenAvionAterrizandoDer = new URL(escenario.getCodeBase(), "imagenes/Aviones/avion-remolque-der.png");
            imagenAvionAterrizandoDer = escenario.getImage(urlImagenAvionAterrizandoDer);
        } catch (IOException e) {
            System.out.println("Error en la ruta de la imagen: " + e);
        }
    }

    @Override
    public void run() {
        while (estado != ESTADO_AVION.APARCADO) {
            //System.out.println("\t[PA] - Avión: " + matricula + ", ESTADO: " + estado);
            if (estado == ESTADO_AVION.VOLANDO) {
                menu.ventanaDialogo.setDialogo("    [PA] - Avión: " + matricula + " realiza petición a pista", "#277277");
                torreControl.peticionPista(this);
            } else if (estado == ESTADO_AVION.ESPERA_PISTA) {
                menu.ventanaDialogo.setDialogo("    [PA] - Avión: " + matricula + " realiza petición de aterrizaje", "#277277");
                torreControl.peticionAterrizaje(this);
            } else if (estado == ESTADO_AVION.ATERRIZANDO) {
                menu.ventanaDialogo.setDialogo("    [PA] - Avión: " + matricula + " realizando maniobra de aterrizaje, recorrido " + pista.getMetrosRecorridos() + "m.", "#277277");
                torreControl.aterrizar(this);
            }
            try {
                Thread.sleep((long) velocidad);
            } catch (InterruptedException ex) {
                System.out.println("[LOG] - ERROR hilo Avión: " + ex);
            }
        }
        torreControl.verAvionesEnTierra();
    }

    public void paint(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g;
        switch (estado) {
            case VOLANDO:
                dibujarVolando(g2d);
                break;
            case ESPERA_PISTA:
                dibujarVolando(g2d);
                break;
            case ATERRIZANDO:
                dibujarAterrizaje(500, 350, g2d);
                break;
            case APARCANDO:
                dibujarAparcamiento(g2d);
                break;
            case APARCADO:
                g2d.setColor(Color.WHITE);
                g2d.drawImage(imagenAvionAparcado, (int) x, (int) y, ANCHO, ALTO, escenario);
                break;
        }
        g2d.drawString(matricula, (int) x, (int) y);
        escenario.repaint();
    }

    public void dibujarVolando(Graphics2D g2d) {
        int radio = (escenario.getWidth() / 2) - (ANCHO * ALTO) + generarAleatorio(1, escenario.getWidth());
        y = ((escenario.getHeight() / 2) - ALTO) + radio * Math.cos(angulo);
        x = ((escenario.getWidth() / 2) - ANCHO) + radio * Math.sin(angulo);
        angulo += velocidad / 1000;
        g2d.setColor(Color.blue);
        g2d.drawRect((int) x, (int) y, ANCHO * 2, ALTO * 2);
        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);
        at.scale(0.2, 0.2);
        g2d.drawImage(imagenAvion, at, escenario);
    }

    private void dibujarAterrizaje(int x, int y, Graphics2D g2d) {
        this.x = x - XAterrizaje;
        this.y = y;
        if (this.x >= 55) {
            XAterrizaje += velocidad / 350;
        }
        g2d.setColor(Color.YELLOW);
        if (escalaXBajando > 0.1) {
            g2d.drawRect((int) this.x, (int) this.y, ANCHO, ALTO);
            AffineTransform at = new AffineTransform();
            at.translate(this.x, this.y);
            at.scale(escalaXBajando -= 0.001, escalaYBajando -= 0.001);
            g2d.drawImage(imagenAvion, at, escenario);
        } else {
            g2d.drawRect((int) this.x, (int) this.y, ANCHO, ALTO);
            g2d.drawImage(imagenAvion, (int) this.x, (int) this.y, ANCHO, ALTO, escenario);
        }

    }

    private void dibujarAparcamiento(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawRect((int) x, (int) y, ANCHO, ALTO);
        if (x != 256 && y != 240) {
            if (x < 255 && x <= 450 && y >= 325) { //Fin de pista
                y--;//Sube (1º vez)    
                g2d.drawImage(imagenAvionAterrizandoArriba, (int) x, (int) y, ANCHO, ALTO, escenario);
            } else if (x <= 255 && y < 325) {
                x++;//Hacia la derecha (1º vez)
                g2d.drawImage(imagenAvionAterrizandoDer, (int) x, (int) y, ANCHO, ALTO, escenario);
            } else if (x > 255 && x <= 450) {
                y--;//Sube
                g2d.drawImage(imagenAvionAterrizandoArriba, (int) x, (int) y, ANCHO, ALTO, escenario);
            }
        } else {
            x += generarAleatorio(0, 5) * 20;
            y -= generarAleatorio(0, 5) * 10;
            estado = ESTADO_AVION.APARCADO;
        }
    }

    public ESTADO_AVION getEstado() {
        return estado;
    }

    public void setEstado(ESTADO_AVION estado) {
        this.estado = estado;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getHoraLlegada() {
        return horaLlegada;
    }

    public int getHoraSalida() {
        return horaSalida;
    }

    public int getDuracionTrayecto() {
        return duracionTrayecto;
    }

    private String generarMatricula() {
        String mat_avion = "EC-" + String.valueOf((char) generarAleatorio(65, 87))
                + String.valueOf((char) generarAleatorio(65, 90))
                + String.valueOf((char) generarAleatorio(65, 90));
        return mat_avion;
    }

    private int generarAleatorio(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    //Proceso de ordenamiento en la llamada al "Array.sort" en la clase torreControl.
    @Override
    public int compareTo(Avion o) {
        if (horaLlegada < o.getHoraLlegada()) {
            return -1;
        }
        if (horaLlegada > o.getHoraLlegada()) {
            return 1;
        }
        return 0;
    }
}
