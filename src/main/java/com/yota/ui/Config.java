package com.yota.ui;

import com.yota.config.ConfigException;
import com.yota.config.Configuration;
import com.yota.db.DataBase;
import com.yota.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class Config extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton saveFolderButton;
    private JButton CDIConfigButton;
    private JButton CAConfigButton;
    private JLabel cdiConf;
    private JLabel caConf;
    private JLabel saveFolder;
    private JFileChooser fileChooser;

    private Configuration configuration;

    public Config(Configuration configuration) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo-yota.png"))).getImage());
        pack();
        setLocationRelativeTo(null);

        this.configuration = configuration;

        updateStatus();

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setLocale(Locale.getDefault());

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        saveFolderButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveFolder.setText(file.getPath());
                configuration.setSaveFolder(file);
                try {
                    configuration.updateConfig();
                } catch (ConfigException ex) {
                    Utils.showExDialog(ex.getMessage());
                }
            }
        });

        CDIConfigButton.addActionListener(e -> {
            JDialog cdiDialog = new DbWindow(DataBase.CDI, configuration, cdiConf);
            cdiDialog.setTitle("CDI config");
            cdiDialog.setVisible(true);
        });

        CAConfigButton.addActionListener(e -> {
            JDialog caDialog = new DbWindow(DataBase.CA, configuration, caConf);
            caDialog.setTitle("CA config");
            caDialog.setVisible(true);
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            configuration.updateConfig();
        } catch (ConfigException e) {
            Utils.showExDialog(e.getMessage());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void updateStatus() {
        if (configuration.getExist()) {
            saveFolder.setText(configuration.getSaveFolder().getPath());

            if (configuration.getCdiDb().getLastStatus()) {
                cdiConf.setText("OK");
                cdiConf.setForeground(new Color(22,157,0));
            } else {
                cdiConf.setText("Fail");
                cdiConf.setForeground(Color.RED);
            }

            if (configuration.getCaDb().getLastStatus()) {
                caConf.setText("OK");
                caConf.setForeground(new Color(22,157,0));
            } else {
                caConf.setText("Fail");
                caConf.setForeground(Color.RED);
            }

        }
    }
}
