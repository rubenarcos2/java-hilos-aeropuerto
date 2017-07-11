package Hilo;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Rubén
 */
public class Pista {

    public enum ESTADO_PISTA {

        LIBRE, OCUPADA
    };
    private ESTADO_PISTA estado; //Libre(T)/Ocupada(F)
    private String avionEnUso; //Matrícula del avión que se encuentra en pista.
    private int metrosPista, metrosRecorridos, x, y, altoPista, anchoPista;
    private Applet escenario;

    public Pista(Applet escenario) {
        this.escenario = escenario;
        estado = ESTADO_PISTA.LIBRE;
        metrosPista = 30; //metros que mide la pista de largo
        metrosRecorridos = 0;
        altoPista = 10;
        anchoPista = 420;
        x = 75;
        y = 380;
    }

    public void paint(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g;
        dibujarMetrosPista(g2d);
        escenario.repaint();
    }

    public void dibujarMetrosPista(Graphics2D g2d) {
        if (estado == ESTADO_PISTA.OCUPADA) {
            g2d.setColor(Color.yellow);
            g2d.fillRect(x, y, anchoPista - (metrosRecorridos * 13), altoPista);
            g2d.drawString(metrosRecorridos + " m.", anchoPista - (metrosRecorridos * 13) + 50, y + 20);
            g2d.setColor(Color.black);
            g2d.drawString(getEstado().toString(), x, y+10);
        }else{
            g2d.setColor(Color.green);
            g2d.fillRect(x, y, anchoPista - (metrosRecorridos * 13), altoPista);
            g2d.setColor(Color.black);
            g2d.drawString(getEstado().toString(), x, y+10);
        }
    }

    public ESTADO_PISTA getEstado() {
        return estado;
    }

    public int getMetrosPista() {
        return metrosPista;
    }

    public int getMetrosRecorridos() {
        return metrosRecorridos;
    }

    public void setMetrosRecorridos(int metrosRecorridos) {
        this.metrosRecorridos = metrosRecorridos;
    }

    //Se llamará cuando una avión termine de aterrizar (termine de recorrer la pista)
    public void setTerminaPista() {
        estado = ESTADO_PISTA.LIBRE;
        metrosRecorridos = 0;
    }

    //Se llamará cuando una avión empieze de aterrizar (comience a recorrer la pista)
    public void setEntrarEnPista() {
        estado = ESTADO_PISTA.OCUPADA;
        metrosRecorridos = 0;
    }

    public String getAvionEnUso() {
        return avionEnUso;
    }

    public void setAvionEnUso(String matricula) {
        this.avionEnUso = matricula;
    }
}
