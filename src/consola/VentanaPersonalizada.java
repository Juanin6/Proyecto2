package consola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPersonalizada extends JFrame {

    public VentanaPersonalizada() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Quita la barra de título predeterminada
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Crea un panel para la barra de título personalizada
        JPanel barraTitulo = new JPanel(new BorderLayout());
        barraTitulo.setBackground(Color.BLUE); // Color de fondo de la barra de título

        // Agrega botones personalizados de control de ventana
        JButton botonCerrar = new JButton("X");
        botonCerrar.setForeground(Color.WHITE); // Color del texto
        botonCerrar.setBackground(Color.RED);   // Color de fondo del botón
        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Acción de cerrar la ventana
            }
        });

        // Agrega los componentes a la barra de título
        barraTitulo.add(new JLabel("Ventana Personalizada"), BorderLayout.CENTER);
        barraTitulo.add(botonCerrar, BorderLayout.EAST);

        // Agrega la barra de título personalizada al contenedor principal
        getContentPane().add(barraTitulo, BorderLayout.NORTH);

        // Resto de la interfaz
        JPanel contenido = new JPanel();
        contenido.setBackground(Color.WHITE);
        contenido.add(new JLabel("Contenido de la ventana"));
        getContentPane().add(contenido, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPersonalizada();
        });
    }
}
