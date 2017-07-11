/**
 * /* @author Rubén Arcos
 */
package Hilo;

import GuiControles.menuOpciones;
import java.util.ArrayList;
import java.util.Collections;

public class TorreControl extends Thread { //TUBERIA

    private Pista pista;
    private ArrayList<Avion> listaAvionesEsperaPista;
    private ArrayList<Avion> listaAvionesEnTierra;
    private int numAviones;
    private static menuOpciones menu;

    public TorreControl(Pista pista, int numAviones, menuOpciones menu) {
        this.menu = menu;
        this.numAviones = numAviones;
        this.pista = pista;
        listaAvionesEsperaPista = new ArrayList<>();
        listaAvionesEnTierra = new ArrayList<>();

    }

    public void verAvionesEnTierra() {
        if (listaAvionesEnTierra.size() > 0) {
            menu.ventanaListaAparcado.setVisible(true);
            menu.ventanaListaAparcado.setDatosAvion(listaAvionesEnTierra);
        } else {
            menu.ventanaListaAparcado.setVisible(false);
        }

    }

    private void ordenarPeticiones() {
        try {
            Collections.sort(listaAvionesEsperaPista);
        } catch (Exception e) {
            System.out.println("[LOG] - ERROR AL ORDENAR LA LISTA DE ESPERA: " + e);
        }

    }

    private void verAvionesEnEspera() {
        if (listaAvionesEsperaPista.size() > 0) {
            menu.ventanaListaEspera.setVisible(true);
            menu.ventanaListaEspera.setDatosAvion(listaAvionesEsperaPista);
        } else {
            menu.ventanaListaEspera.setVisible(false);
        }

    }

    public synchronized void peticionPista(Avion avion) {
        if (listaAvionesEsperaPista.size() < numAviones) {
            listaAvionesEsperaPista.add(avion);
            avion.setEstado(Avion.ESTADO_AVION.ESPERA_PISTA);
            menu.ventanaDialogo.setDialogo("[TC] - PETICIÓN ACEPTADA: " + avion.getMatricula() + ", pasa a estado: " + avion.getEstado() + ", " + avion.getHoraLlegada(), "#277227");
            ordenarPeticiones();
        } else {
            menu.ventanaDialogo.setDialogo("[TC] - PETICIÓN DENEGADA A: " + avion.getMatricula() + ", hora llegada: " + avion.getHoraLlegada(), "#fdac00");
            try {
                wait();//¿DORMIR HILOS O DEJAR DESPIERTOS? -> MIENTRAS ESPERA ENTRADA A PISTA, REALMENTE ESTA VOLANDO.
            } catch (InterruptedException ex) {
                System.out.println("[LOG] - ERROR AL DORMIR EL HILO EN PETICIÓN PISTA: " + ex);
            }
            avion.setEstado(Avion.ESTADO_AVION.VOLANDO);
        }
    }

    public synchronized void peticionAterrizaje(Avion avion) {
        //Si es el avión que está el primero en la lista de peticiones a pista
        //System.out.println("ESTADO PISTA: " + pista.getEstado());
        if (avion.equals(listaAvionesEsperaPista.get(0))
                && avion.getEstado() == Avion.ESTADO_AVION.ESPERA_PISTA
                && pista.getEstado() == Pista.ESTADO_PISTA.LIBRE) {
            menu.ventanaDialogo.setDialogo("[TC] - ACEPTADO ATERRIZAJE: " + avion.getMatricula() + ", " + avion.getEstado() + ", " + avion.getHoraLlegada(), "#7ddf64");
            pista.setEntrarEnPista();
            menu.ventanaDialogo.setDialogo("[TC] - PISTA EN USO, CERRADAS LAS SOLICITUDES DE ATERRIZAJE...", "#ff3036");
            pista.setAvionEnUso(avion.getMatricula());
            avion.setEstado(Avion.ESTADO_AVION.ATERRIZANDO);
        } else {
            menu.ventanaDialogo.setDialogo("[TC] - DENEGADO ATERRIZAJE A: " + avion.getMatricula() + ", hora llegada: " + avion.getHoraLlegada(), "#f15928");
            avion.setEstado(Avion.ESTADO_AVION.ESPERA_PISTA);
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("[LOG] - ERROR AL DORMIR EL HILO EN PETICIÓN ATERRIZAJE: " + ex);
            }
        }
    }

    public synchronized void aterrizar(Avion avion) {
        //Si es el avión que está el primero en la lista de peticiones a pista
        if (avion.getMatricula().equals(pista.getAvionEnUso())
                && avion.getEstado() == Avion.ESTADO_AVION.ATERRIZANDO) {
            //System.out.println("REALIZANDO MANIOBRA DE ATERRIZAJE " + pista.getAvionEnUso() + " " + pista.getMetrosRecorridos() + " - " + avion.getEstado());
            //En caso de que la pista este libre y sin aviones
            if (pista.getAvionEnUso().equals(avion.getMatricula())
                    && pista.getMetrosRecorridos() == 0) {
                menu.ventanaDialogo.setDialogo("[TC] - INICIO PISTA: " + avion.getMatricula() + ", " + avion.getEstado() + ", " + avion.getHoraLlegada() + " PISTA: " + pista.getMetrosRecorridos() + " m.", "#bf4d28");
                pista.setMetrosRecorridos(pista.getMetrosRecorridos() + 1);
                //Incremente los metros de la pista de rodadura del avión
            } else if (pista.getAvionEnUso().equals(avion.getMatricula())
                    && pista.getMetrosRecorridos() < pista.getMetrosPista()) {
                menu.ventanaDialogo.setDialogo("[TC] - RODADURA: " + avion.getMatricula() + ", " + avion.getEstado() + ", " + avion.getHoraLlegada() + " PISTA: " + pista.getMetrosRecorridos() + " m.", "#bf4d28");
                pista.setMetrosRecorridos(pista.getMetrosRecorridos() + 1);
                //En caso de llegar al final de pista, ya puede aterrizar otro avión
            } else if (pista.getAvionEnUso().equals(avion.getMatricula())
                    && pista.getMetrosRecorridos() == pista.getMetrosPista()
                    && avion.getEstado() == Avion.ESTADO_AVION.ATERRIZANDO) {
                menu.ventanaDialogo.setDialogo("[TC] - FIN PISTA: " + avion.getMatricula() + ", " + avion.getEstado() + ", " + avion.getHoraLlegada() + " PISTA: " + pista.getMetrosRecorridos() + " m.", "#bf4d28");
                listaAvionesEnTierra.add(avion);
                for (int i = 0; i < listaAvionesEsperaPista.size(); i++) {
                    if (listaAvionesEsperaPista.get(i).equals(avion)) {
                        listaAvionesEsperaPista.remove(listaAvionesEsperaPista.get(i));
                        System.out.println("[LOG] - SE HA SACADO DE LA LISTA DE ESPERA AL AVIÓN: " + avion.getMatricula());
                    }
                }
                menu.ventanaDialogo.setDialogo("[TC] - APARCADO: " + avion.getMatricula() + ", " + avion.getEstado() + ", " + avion.getHoraLlegada() + " PISTA: " + pista.getMetrosRecorridos() + " m.", "#bf4d28");
                avion.setEstado(Avion.ESTADO_AVION.APARCANDO);
                menu.ventanaDialogo.setDialogo("[TC] - PISTA LIBRE, ABIERTA SOLICITUDES DE ATERRIZAJE...", "#bf4d28");
                pista.setTerminaPista();
                ordenarPeticiones();
                notifyAll();
                //Para los aviones en lista de espera para entrada en pista
            } else {
                try {
                    menu.ventanaDialogo.setDialogo("[TC] - DENEGADO USO PISTA: " + avion.getMatricula() + ", hora llegada: " + avion.getHoraLlegada(), "#bf4d28");
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println("[LOG] - ERROR AL DORMIR EL HILO: " + ex);
                }
            }
        }
        verAvionesEnEspera();
        verAvionesEnTierra();
    }

}
