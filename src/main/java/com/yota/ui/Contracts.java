package com.yota.ui;

import com.yota.config.ConfigException;
import com.yota.config.Configuration;
import com.yota.db.DbException;
import com.yota.db.DbService;
import com.yota.db.DbServiceImpl;
import com.yota.decoder.Decoder;
import com.yota.decoder.ParseException;
import com.yota.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class Contracts extends JPanel {
    private JButton configButton;
    private JPanel mainPanel;
    private JButton exit;
    private JTextField iccid;
    private JButton generateButton;
    private JLabel status;
    private JFrame frame;

    private Configuration configuration;

    public Contracts() {
        frame = new JFrame("Contracts");
        status.setForeground(Color.RED);

        try {
            configuration = new Configuration();
        } catch (ConfigException e) {
            Utils.showExDialog(e.getMessage());
        }

        configButton.addActionListener(e -> {
            JDialog configDialog = new Config(configuration);
            configDialog.setTitle("Configuration");
            configDialog.setSize(400, 200);
            configDialog.setVisible(true);
        });

        exit.addActionListener(e -> System.exit(0));

        generateButton.addActionListener(e -> {
            status.setText("");

            try {
                Utils.validateInput(iccid.getText());
            } catch (NumberFormatException ex) {
                Utils.showExDialog(ex.getMessage());
            }

            DbService db = new DbServiceImpl();

            try {
                String caId = db.getCaId(iccid.getText());
                Map<String, InputStream> steams = db.getCa(caId);

                final CountDownLatch latch = new CountDownLatch(2);
                try {
                    steams.forEach((k, v) -> {
                        Thread thread = new Thread(() -> {
                            try {
                                new Decoder().create(k).parse(v, new File(configuration.getSaveFolder(), iccid.getText()));
                            } catch (ParseException ex) {
                                Utils.showExDialog(ex.getMessage());
                            } finally {
                                latch.countDown();
                            }
                        });
                        thread.start();
                    });
                    latch.await();
                } catch (InterruptedException ex) {
                    Utils.showExDialog(ex.getMessage());
                }
            } catch (DbException ex) {
                Utils.showExDialog(ex.getMessage());
            }

            status.setText("Done");
            status.setForeground(new Color(22, 157, 0));
        });
    }

    public void buildUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            /* */
        }
        frame.setContentPane(new Contracts().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo-yota.png"))).getImage());

        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
