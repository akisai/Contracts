package com.yota.ui;

import com.apple.eawt.Application;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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

    private ProgressBar progressBar;
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
            Boolean complete = true;

            try {
                Utils.validateInput(iccid.getText());

                initProgressBar();

                DbService db = new DbServiceImpl();

                String caId = db.getCaId(iccid.getText());
                SwingUtilities.invokeLater(() -> updateProgressBar(20));

                Map<String, InputStream> steams = db.getCa(caId);
                SwingUtilities.invokeLater(() -> updateProgressBar(20));

                final CountDownLatch latch = new CountDownLatch(2);

                steams.forEach((k, v) -> {
                    Thread thread = new Thread(() -> {
                        try {
                            new Decoder().create(k).parse(v, new File(configuration.getSaveFolder(), iccid.getText()));
                        } catch (ParseException ex) {
                            SwingUtilities.invokeLater(() -> {
                                Utils.showExDialog(ex.getMessage());
                                updateProgressBar(30);
                            });
                        } finally {
                            latch.countDown();
                        }
                    });
                    thread.start();
                });
                latch.await();
            } catch (NumberFormatException | InterruptedException | DbException ex) {
                Utils.showExDialog(ex.getMessage());
                complete = false;
            } finally {
                progressBar.dispose();
            }

            if (complete) {
                status.setText("Done");
                status.setForeground(new Color(22, 157, 0));
            } else {
                status.setText("Error");
                status.setForeground(Color.RED);
            }
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
        if (System.getProperty("os.name").startsWith("Mac")) {
            Application.getApplication().setDockIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo-yota.png"))).getImage());
        }
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initProgressBar() {
        Thread thread =  new Thread(() -> {
            progressBar = new ProgressBar();
            progressBar.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            progressBar.setSize(400, 20);
            progressBar.setVisible(true);
        });
        thread.run();
    }

    private void updateProgressBar(Integer value) {
        int newValue = progressBar.getGenProgress().getValue() + value;
        newValue = newValue > progressBar.getGenProgress().getMaximum() ? 100 : newValue;
        progressBar.getGenProgress().setValue(newValue);
        progressBar.getGenProgress().setString(newValue + "%");
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        configButton = new JButton();
        configButton.setText("Config");
        mainPanel.add(configButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exit = new JButton();
        exit.setText("Exit");
        mainPanel.add(exit, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        iccid = new JTextField();
        panel1.add(iccid, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate");
        panel1.add(generateButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        status = new JLabel();
        status.setText("");
        status.setVerticalAlignment(0);
        panel1.add(status, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
