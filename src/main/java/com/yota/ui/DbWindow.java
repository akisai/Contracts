package com.yota.ui;

import com.yota.config.ConfigException;
import com.yota.config.Configuration;
import com.yota.config.DbConfig;
import com.yota.db.DataBase;
import com.yota.db.DbException;
import com.yota.utils.SqlUtils;
import com.yota.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class DbWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField url;
    private JTextField user;
    private JPasswordField pass;
    private JButton testConnectionButton;
    private JButton edit;
    private JLabel testStatus;

    private Configuration configuration;
    private DataBase dataBase;
    private Boolean statusFlag = false;

    public DbWindow(DataBase dataBase, Configuration configuration, JLabel status) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo-yota.png"))).getImage());
        pack();
        setLocationRelativeTo(null);

        this.configuration = configuration;
        this.dataBase = dataBase;

        if (configuration.getExist()) {
            switch (dataBase) {
                case CDI:
                    url.setText(configuration.getCdiDb().getUrl());
                    user.setText(configuration.getCdiDb().getUser());
                    pass.setText(configuration.getCdiDb().getPass());
                    break;
                case CA:
                    url.setText(configuration.getCaDb().getUrl());
                    user.setText(configuration.getCaDb().getUser());
                    pass.setText(configuration.getCaDb().getPass());
                    break;
                default:
                    break;
            }
        }

        url.setEditable(false);
        user.setEditable(false);
        pass.setEditable(false);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        testConnectionButton.addActionListener(e -> {
            url.setEditable(false);
            user.setEditable(false);
            pass.setEditable(false);
            setDbConfig();

            try {
                if (SqlUtils.testConnection(dataBase)) {
                    testStatus.setText("OK");
                    testStatus.setForeground(new Color(22,157,0));
                    status.setText("OK");
                    status.setForeground(new Color(22,157,0));
                    setLastStatus(true);
                } else {
                    setFail(testStatus);
                    setFail(status);
                    setLastStatus(false);
                }
            } catch (DbException ex) {
                setFail(testStatus);
                setFail(status);
                setLastStatus(false);
                Utils.showExDialog(ex.getMessage());
            }
        });

        edit.addActionListener(e -> {
            url.setEditable(true);
            user.setEditable(true);
            pass.setEditable(true);
        });
    }

    private void onOK() {
        url.setEditable(false);
        user.setEditable(false);
        pass.setEditable(false);
        setDbConfig();
        this.dispose();
    }

    private void onCancel() {
        url.setEditable(false);
        user.setEditable(false);
        pass.setEditable(false);
        this.dispose();
    }

    private void setDbConfig() {
        switch (dataBase) {
            case CDI:
                configuration.setCdiDb(new DbConfig(url.getText(), user.getText(), String.valueOf(pass.getPassword()), statusFlag));
                break;
            case CA:
                configuration.setCaDb(new DbConfig(url.getText(), user.getText(), String.valueOf(pass.getPassword()), statusFlag));
                break;
            default:
                break;
        }
        try {
            configuration.updateConfig();
        } catch (ConfigException e) {
            Utils.showExDialog(e.getMessage());
        }
    }

    private void setLastStatus(Boolean lastStatus) {
        this.statusFlag = lastStatus;
        switch (dataBase) {
            case CDI:
                configuration.getCdiDb().setLastStatus(lastStatus);
                break;
            case CA:
                configuration.getCaDb().setLastStatus(lastStatus);
                break;
            default:
                break;
        }
        try {
            configuration.updateConfig();
        } catch (ConfigException e) {
            Utils.showExDialog(e.getMessage());
        }
    }

    private void setFail(JLabel in) {
        in.setText("Fail");
        in.setForeground(Color.RED);
    }
}
