package GuiControles;

import Hilo.Avion;
import java.awt.HeadlessException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rubén
 */
public class VentanaListaEspera extends JFrame {

    private String[] nombreColumnas = {"Matrícula", "Estado", "Hora llegada", "Hora salida", "Duración"};
    private DefaultTableModel dtm;
    private JTable tabla;
    private ArrayList<Avion> listaAviones;
    private JScrollPane barraScroll;
    private JLabel etiqueta;

    public VentanaListaEspera() throws HeadlessException {
        setTitle("Lista de aviones en espera de pista");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tabla = new JTable();
        barraScroll = new JScrollPane(tabla);
        DefaultTableModel dtm = (DefaultTableModel) tabla.getModel();
        for (int i = 0; i < nombreColumnas.length; i++) {
            dtm.addColumn(nombreColumnas[i]);
        }
        add(barraScroll);
    }

    public void setDatosAvion(ArrayList<Avion> listaAviones) {
        this.listaAviones = listaAviones;
        dtm = new DefaultTableModel(nombreColumnas, 0);
        for (int i = 0; i < listaAviones.size(); i++) {
            String[] datos = new String[nombreColumnas.length];
            datos[0] = String.valueOf(listaAviones.get(i).getMatricula());
            datos[1] = String.valueOf(listaAviones.get(i).getEstado());
            datos[2] = String.valueOf(listaAviones.get(i).getHoraLlegada());
            datos[3] = String.valueOf(listaAviones.get(i).getHoraSalida());
            datos[4] = String.valueOf(listaAviones.get(i).getDuracionTrayecto());
            dtm.addRow(datos);
        }
        tabla.setModel(dtm);
        //barraScroll.getVerticalScrollBar().setValue(barraScroll.getVerticalScrollBar().getMaximum());
    }
}
