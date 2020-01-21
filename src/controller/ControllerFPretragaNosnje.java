/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import communication.CommunicationServer;
import domain.Nosnja;
import domain.enumeracije.FormMode;
import domain.enumeracije.FormModeVrstaNosnje;
import domain.enumeracije.VrstaNosnje;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.component.table.model.NosnjaTableModel;
import ui.form.FMain;
import ui.form.FPretragaNosnje;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFPretragaNosnje {

    private FPretragaNosnje fPretragaNosnje;

    public ControllerFPretragaNosnje() {
    }

    void otvoriFormuZaPretraguNosnje(FMain fMain) {
        fPretragaNosnje = new FPretragaNosnje(fMain, true);
        fillTabela();
        fillVrsteNosnje();
        addListenersPretragaNosnje();
        fPretragaNosnje.setVisible(true);
    }

    private void fillTabela() {
        List<Nosnja> lista = null;
        try {
            lista = vratiListuNosnje();
        } catch (Exception ex) {
            Logger.getLogger(ControllerFPretragaNosnje.class.getName()).log(Level.SEVERE, null, ex);
        }

        fPretragaNosnje.getjTblNosnja().setModel(new NosnjaTableModel(lista));
    }

    public Nosnja vratiNosnju(Nosnja nosnja) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_JEDNU_NOSNJU);
        requestObject.setData(nosnja);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Nosnja nosnja1 = (Nosnja) responseObject.getData();
            return nosnja1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Nosnja> vratiNosnjePoKriterijumu(Nosnja nosnja) throws Exception{
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_NOSNJE_PO_KRITERIJUMU);
        /*Map<String, String> nosnjaMap = new HashMap<>();
        nosnjaMap.put("sifraNosnje", kriterijumPretrage);
        nosnjaMap.put("naziv", naziv);
        nosnjaMap.put("vrsta", vrsta);*/
        requestObject.setData(nosnja);

        CommunicationServer.getInstance().sendRequest(requestObject);

        List<Nosnja> nosnje;
        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            nosnje = (List<Nosnja>) responseObject.getData();
            return nosnje;
        }
        throw new Exception(responseObject.getErrorMessage());
    }
    
    public List<Nosnja> vratiListuNosnje() throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_SVЕ_NOSNJE);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        List<Nosnja> nosnje;
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            nosnje = (List<Nosnja>) responseObject.getData();
            return nosnje;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private void addListenersPretragaNosnje() {
        fPretragaNosnje.addButtonDetailsListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fPretragaNosnje.getjTblNosnja().getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(fPretragaNosnje, "Molimo vas selektujte clana");
                } else {
                    Long sifraNosnje = (Long) fPretragaNosnje.getjTblNosnja().getValueAt(selectedRow, 0);
                    Nosnja nosnjica = new Nosnja();
                    nosnjica.setSifraNosnje(sifraNosnje);
                    
                    Nosnja nosnja = null;
                    try {
                        nosnja = vratiNosnju(nosnjica);
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaNosnje.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    GUICoordinator.getInstance().inicijalizujKonstruktorKontroleraNovaNosnja();
                    GUICoordinator.getInstance().pozoviPostavljanjeNosnje(nosnja);//.postaviNosnju(nosnja);

                    if (nosnja.getVrstaNosnje().equals(VrstaNosnje.BELINA)) {
                        GUICoordinator.getInstance().otvoriNovuNosnju(null, FormModeVrstaNosnje.FORM_BELINA, FormMode.FORM_VIEW);
                    } else if (nosnja.getVrstaNosnje().equals(VrstaNosnje.OPANCI)) {
                        GUICoordinator.getInstance().otvoriNovuNosnju(null, FormModeVrstaNosnje.FORM_OPANCI, FormMode.FORM_VIEW);
                    } else if (nosnja.getVrstaNosnje().equals(VrstaNosnje.NOSNJA)) {
                        GUICoordinator.getInstance().otvoriNovuNosnju(null, FormModeVrstaNosnje.FORM_NOSNJA, FormMode.FORM_VIEW);
                    }
                    NosnjaTableModel ctm = (NosnjaTableModel) fPretragaNosnje.getjTblNosnja().getModel();
                    List<Nosnja> lista = null;
                    try {
                        lista = vratiListuNosnje();
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaNosnje.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ctm.azuriraj(lista);
                }
            }
        });

        fPretragaNosnje.addButtonPretraziListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fPretragaNosnje.getjTxtPretraga1().getText().isEmpty() && fPretragaNosnje.getJtxtNaziv().getText().isEmpty() && fPretragaNosnje.getJcmbVrsta().getSelectedItem().equals(VrstaNosnje.Izaberite_vrstu_nosnje)) {
                    NosnjaTableModel ctm = (NosnjaTableModel) fPretragaNosnje.getjTblNosnja().getModel();
                    List<Nosnja> lista = null;
                    try {
                        lista = vratiListuNosnje();
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaNosnje.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ctm.azuriraj(lista);
                } else {
                    String kriterijumPretrage = fPretragaNosnje.getjTxtPretraga1().getText();
                    String naziv = fPretragaNosnje.getJtxtNaziv().getText();
                    String vrsta = String.valueOf(fPretragaNosnje.getJcmbVrsta().getSelectedItem());
                    
                    Nosnja nosnja = new Nosnja();
                    if(!kriterijumPretrage.equals("")){
                        Long sifra = Long.parseLong(kriterijumPretrage);
                        nosnja.setSifraNosnje(sifra);
                    }
                    nosnja.setNazivNosnje(naziv);
                    nosnja.setVrstaNosnje(VrstaNosnje.valueOf(vrsta));
                    
                    List<Nosnja> nosnje = null;
                    try {
                        nosnje = vratiNosnjePoKriterijumu(nosnja);
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaNosnje.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    NosnjaTableModel ctm = new NosnjaTableModel(nosnje);
                    fPretragaNosnje.getjTblNosnja().setModel(ctm);
                }
            }
        });

    }

    private void fillVrsteNosnje() {
        fPretragaNosnje.getJcmbVrsta().removeAllItems();
        
        fPretragaNosnje.getJcmbVrsta().addItem(VrstaNosnje.Izaberite_vrstu_nosnje);
        fPretragaNosnje.getJcmbVrsta().addItem(VrstaNosnje.BELINA);
        fPretragaNosnje.getJcmbVrsta().addItem(VrstaNosnje.NOSNJA);
        fPretragaNosnje.getJcmbVrsta().addItem(VrstaNosnje.OPANCI);
    }

}
