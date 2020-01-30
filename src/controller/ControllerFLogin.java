/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import communication.CommunicationServer;
import domain.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.form.FLogin;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFLogin {

    private FLogin fLogin;
    private final Map<String, Object> map;

    public ControllerFLogin() {
        map = new HashMap<>();

    }

    void otvoriFormuLogin() {
        fLogin = new FLogin();
        addListenersFLogin();
        fLogin.setVisible(true);
    }

    private void addListenersFLogin() {
        fLogin.addButtonLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validate(getfLogin().getjTxtUsername(), getfLogin().getjTxtPassword());
                    CommunicationServer.getInstance().connect();
                    User user = new User();
                    user.setUsername(getfLogin().getjTxtUsername().getText().trim());
                    User user1 = login(user/*fLogin.getjTxtUsername().getText().trim(), String.valueOf(fLogin.getjTxtPassword().getPassword())*/);
                    if(user1 == null){
                        JOptionPane.showMessageDialog(fLogin, "Username ili password nisu ispravni!");
                        return;
                    }
                    GUICoordinator.getInstance().otvoriMainFormu();
                    getfLogin().dispose();

                    System.out.println("otvaranje main forme");
                } catch (Exception ex) {
                    if (ex instanceof IOException) {
                        JOptionPane.showMessageDialog(fLogin, "Dogodila se greska u komunikaciji!(server je ugasen)");
                        System.exit(0);
                    }else if(ex instanceof Exception){
                        JOptionPane.showMessageDialog(fLogin, "Username ili password nisu ispravni!");
                    }
                    //ovo else moze da se brise - proveriti
                }
            }
        });

        fLogin.addButtonCancelListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    private void validate(JTextField jTxtUsername, JTextField jTxtPassword) throws Exception {
        fLogin.getjLabUsernameError().setText(" ");
        fLogin.getjLabPasswordError().setText(" ");

        if (fLogin.getjTxtUsername().getText().isEmpty() && String.valueOf(fLogin.getjTxtPassword().getPassword()).isEmpty()) {
            fLogin.getjLabUsernameError().setText("Please enter username");
            fLogin.getjLabPasswordError().setText("Please enter password");
            throw new Exception();
        }
        if (fLogin.getjTxtUsername().getText().isEmpty()) {
            fLogin.getjLabUsernameError().setText("Please enter username");
            throw new Exception();
        }
        if (String.valueOf(fLogin.getjTxtPassword().getPassword()).isEmpty()) {
            fLogin.getjLabPasswordError().setText("Please enter password");
            throw new Exception();
        }
    }
//menjam da ne bude void nego da vrati null
    public User login(User user) throws IOException, ClassNotFoundException, Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.LOGIN);

        /*Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("password", password);*/
        requestObject.setData(user);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();

        ResponseStatus responseStatus = responseObject.getStatus();
        if (responseStatus == ResponseStatus.SUCCESS) {
            map.put("user", responseObject.getData());
            User user1 = (User) responseObject.getData();
            return user1;
        } else {
            //JOptionPane.showMessageDialog(fLogin, "Username ili password nisu ispravni!");
            throw new Exception(responseObject.getErrorMessage());
        }

    }

    public FLogin getfLogin() {
        return fLogin;
    }

}
