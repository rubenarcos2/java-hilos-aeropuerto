package GuiControles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Rubén
 */
public class menuOpciones extends JPanel implements ActionListener {

    private JToggleButton[] listaBtn;
    private ImageIcon[] listaIconos;
    private String[] listaTitulosBtn = {"Ver diálogo", "Ver aviones a la espera", "Ver aviones en tierra"};
    public VentanaDialogo ventanaDialogo;
    public VentanaListaEspera ventanaListaEspera;
    public VentanaListaEspera ventanaListaAparcado;

    public menuOpciones(ImageIcon[] listaIconos) {
        this.listaIconos = listaIconos;
        ventanaDialogo = new VentanaDialogo();
        ventanaDialogo.setBounds(800, 20, 450, 450);
        ventanaDialogo.setVisible(true);
        listaBtn = new JToggleButton[listaTitulosBtn.length];
        for (int i = 0; i < listaTitulosBtn.length; i++) {
            listaBtn[i] = new JToggleButton(listaIconos[i], true);
            listaBtn[i].addActionListener(this);
            add(listaBtn[i]);
        }
        add(new JLabel("Velocidad : 5x"));
        ventanaListaEspera = new VentanaListaEspera();
        ventanaListaEspera.setTitle("Lista aviones a la espera de entrada en pista");
        ventanaListaEspera.setBounds(800, 465, 450, 120);
        ventanaListaEspera.setVisible(true);
        ventanaListaAparcado = new VentanaListaEspera();
        ventanaListaAparcado.setTitle("Lista de aviones en tierra");
        ventanaListaAparcado.setBounds(800, 580, 450, 120);
        ventanaListaAparcado.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton boton = (JToggleButton) e.getSource();
        if (boton.equals(listaBtn[0])) {
            if (ventanaDialogo.isVisible()) {
                ventanaDialogo.setVisible(false);
            } else {
                ventanaDialogo.setVisible(true);
            }
        }else if(boton.equals(listaBtn[1])){
            if (ventanaListaEspera.isVisible()) {
                ventanaListaEspera.setVisible(false);
            } else {
                ventanaListaEspera.setVisible(true);
            }
        }else if(e.equals(listaBtn[2])){
            if (ventanaListaAparcado.isVisible()) {
                ventanaListaAparcado.setVisible(false);
            } else {
                ventanaListaAparcado.setVisible(true);
            }
        }
    }
}
