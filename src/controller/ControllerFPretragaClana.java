/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import communication.CommunicationServer;
import domain.Clan;
import domain.enumeracije.FormMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.component.table.model.ClanTableModel;
import ui.form.FMain;
import ui.form.FNoviClan;
import ui.form.FPretragaClana;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFPretragaClana {

    private FPretragaClana fPretragaClana;


    public ControllerFPretragaClana() {
       
    }

    public List<Clan> vratiListuClanova() throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_SVЕ_CLANOVE);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        List<Clan> clanovi;
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            clanovi = (List<Clan>) responseObject.getData();
            return clanovi;
        }
        throw new Exception(responseObject.getErrorMessage());
        
    }

    public Clan vratiClana(Long brojCK) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_JEDNOG_CLANA);
        requestObject.setData(brojCK);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Clan clan1 = (Clan) responseObject.getData();
            return clan1;
        }
        throw new Exception(responseObject.getErrorMessage());

    }

    public List<Clan> vratiClanovePoKriterijumu(String sifra, String ime, String prezime) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_CLANOVE_PO_KRITERIJUMU);
        Map<String, String> clanMap = new HashMap<>();
        clanMap.put("sifra", sifra);
        clanMap.put("ime", ime);
        clanMap.put("prezime", prezime);
        requestObject.setData(clanMap);

        CommunicationServer.getInstance().sendRequest(requestObject);

        List<Clan> clanovi;
        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            clanovi = (List<Clan>) responseObject.getData();
            return clanovi;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    void otvoriFormuZaPretraguClana(FMain fMain) {
        fPretragaClana = new FPretragaClana(fMain, true);
        fillForm();
        addListenersPretragaClana();
        fPretragaClana.setVisible(true);
    }

    private void addListenersPretragaClana() {
        fPretragaClana.addButtonDetailsListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fPretragaClana.getjTblClanovi().getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(fPretragaClana, "Molimo vas selektujte clana");
                } else {
                    Long brojCK = (Long) fPretragaClana.getjTblClanovi().getValueAt(selectedRow, 0);
                    Clan clan = null;
                    try {
                        clan = vratiClana(brojCK);
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaClana.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GUICoordinator.getInstance().inicijalizujKonstruktorKontroleraNoviClan();
                    GUICoordinator.getInstance().pozoviPostavljanjeClana(clan);
                    GUICoordinator.getInstance().otvoriNovogClana(null, FormMode.FORM_VIEW);

                    ClanTableModel ctm = (ClanTableModel) fPretragaClana.getjTblClanovi().getModel();
                    List<Clan> lista = null;
                    try {
                        lista = vratiListuClanova();
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaClana.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ctm.azuriraj(lista);
                }
            }
        });

        fPretragaClana.addButtonPretraziListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fPretragaClana.getjTxtPretraga().getText().isEmpty() && fPretragaClana.getJtxtIme().getText().isEmpty() && fPretragaClana.getJtxtPrezime().getText().isEmpty()) {
                    ClanTableModel ctm = (ClanTableModel) fPretragaClana.getjTblClanovi().getModel();
                    List<Clan> lista = null;
                    try {
                        lista = vratiListuClanova();
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaClana.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ctm.azuriraj(lista);
                } else {
                    String kriterijumPretrage = fPretragaClana.getjTxtPretraga().getText();
                    String ime = fPretragaClana.getJtxtIme().getText();
                    String prezime = fPretragaClana.getJtxtPrezime().getText();
                    
                    List<Clan> clanoviPoKriterijumu = null;
                    try {
                        clanoviPoKriterijumu = vratiClanovePoKriterijumu(kriterijumPretrage, ime, prezime);
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaClana.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    ClanTableModel ctm = new ClanTableModel(clanoviPoKriterijumu);
                    fPretragaClana.getjTblClanovi().setModel(ctm);
                }
            }
        });

    }

    private void fillForm() {
        List<Clan> clanovi = null;
        try {
            clanovi = vratiListuClanova();
        } catch (Exception ex) {
            Logger.getLogger(ControllerFPretragaClana.class.getName()).log(Level.SEVERE, null, ex);
        }
        fPretragaClana.getjTblClanovi().setModel(new ClanTableModel(clanovi));
    }

}
