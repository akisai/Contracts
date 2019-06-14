package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class ExMessage extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel error;

    public ExMessage(String message) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo-yota.png"))).getImage());
        setLocationRelativeTo(null);

        error.setForeground(Color.RED);
        error.setText("<html><p>" + message + "</p></html>");
        setSize(300, 150);

        buttonOK.addActionListener(e -> onOK());

        // call onOK() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onOK() on ESCAPE
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
        //System.exit(-1);
    }
}
