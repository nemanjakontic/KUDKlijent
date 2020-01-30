/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;

/**
 *
 * @author Nemanja
 */
public class CommunicationServer {

    private static CommunicationServer instance;
    private Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    private CommunicationServer() {
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 9000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Dogodila se greska u komunikaciji!(server je ugasen)");
            System.exit(0);
            System.out.println("Greska u povezivanju! (server nije upaljen)");
        }
    }

    public static CommunicationServer getInstance() {
        if (instance == null) {
            instance = new CommunicationServer();
        }
        return instance;
    }

    public void sendRequest(RequestObject requestObject) {
        try {
            oos.writeObject(requestObject);
        } catch (IOException ex) {
            System.out.println("Greska u komunikaciji!");
        }
    }

    public ResponseObject receiveResponse() {
        ResponseObject responseObject = new ResponseObject();
        try {
            responseObject = (ResponseObject) ois.readObject();
        } catch (IOException ex) {
            System.out.println("Greska u komunikaciji!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CommunicationServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseObject;
    }

}
