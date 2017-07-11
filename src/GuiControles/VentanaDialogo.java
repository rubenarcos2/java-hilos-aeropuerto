package GuiControles;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rubén
 */
public class VentanaDialogo extends JFrame {

    private JScrollPane barraScroll;
    private JLabel etiqueta;

    public VentanaDialogo() throws HeadlessException {
        setTitle("Diálogo Torre de control - Piloto avión");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        etiqueta = new JLabel("<HTML>");
        barraScroll = new JScrollPane(etiqueta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        etiqueta.setSize(getWidth(), getHeight());
        add(barraScroll);
    }

    public void setDialogo(String dialogo, String color) {
        String textoEtiqueta = etiqueta.getText();
        textoEtiqueta += "<p><font color="+color+">" + dialogo + "</font></p>";
        etiqueta.setText(textoEtiqueta);
        barraScroll.getVerticalScrollBar().setValue(barraScroll.getVerticalScrollBar().getMaximum());
    }
}
