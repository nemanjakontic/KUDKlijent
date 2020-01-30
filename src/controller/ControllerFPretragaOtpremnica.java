/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import communication.CommunicationServer;
import domain.Clan;
import domain.Otpremnica;
import domain.enumeracije.FormMode;
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
import ui.component.table.model.OtpremniceTableModel;
import ui.form.FMain;
import ui.form.FPretragaOtpremnica;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFPretragaOtpremnica {

    private FPretragaOtpremnica fPretragaOtpremnica;

    public ControllerFPretragaOtpremnica() {
    }

    void otvoriFormuZaPretraguOtpremnica(FMain fMain) throws Exception {
        fPretragaOtpremnica = new FPretragaOtpremnica(fMain, true);
        fillTabela();
        fillClanove();
        addListeners();
        fPretragaOtpremnica.setVisible(true);
    }

    private List<Otpremnica> vratiSveOtpremnice() throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_SVЕ_OTPREMNICE);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        List<Otpremnica> otpremnice;
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            otpremnice = (List<Otpremnica>) responseObject.getData();
            return otpremnice;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private List<Otpremnica> vratiOtpremnicePoKriterijumu(Otpremnica otpremnica) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_OTPREMNICE_PO_KRITERIJUMU);
        /*Map<String, String> otpremnicaMap = new HashMap<>();
        otpremnicaMap.put("sifra", sifra);
        otpremnicaMap.put("clan", clan);*/
        requestObject.setData(otpremnica);

        CommunicationServer.getInstance().sendRequest(requestObject);

        List<Otpremnica> otpremnice;
        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            otpremnice = (List<Otpremnica>) responseObject.getData();
            return otpremnice;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private Otpremnica vratiOtpremnicu(Otpremnica otpremnica) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.VRATI_JEDNU_OTPREMNICE);
        requestObject.setData(otpremnica);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Otpremnica otp = (Otpremnica) responseObject.getData();
            return otp;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private void fillTabela() throws Exception {
        List<Otpremnica> lista = null;

        try {
            lista = vratiSveOtpremnice();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(fPretragaOtpremnica, "Dogodila se greska u komunikaciji!(server je ugasen)");
            System.exit(0);
            //Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
        }

        fPretragaOtpremnica.getjTable1().setModel(new OtpremniceTableModel(lista));
    }

    private void addListeners() {
        fPretragaOtpremnica.addButtonDetailsListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fPretragaOtpremnica.getjTable1().getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(fPretragaOtpremnica, "Molimo vas selektujte clana");
                } else {
                    Long sifraOtpremnice = (Long) fPretragaOtpremnica.getjTable1().getValueAt(selectedRow, 0);
                    Otpremnica otp = new Otpremnica();
                    otp.setSifraOtpremnice(sifraOtpremnice);
                    Otpremnica otpremnica = null;
                    try {
                        otpremnica = vratiOtpremnicu(otp);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(fPretragaOtpremnica, "Dogodila se greska u komunikaciji!(server je ugasen)");
                        System.exit(0);
                        //Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GUICoordinator.getInstance().pozoviPostavljanjeOtpremnice(otpremnica);
                    try {
                        GUICoordinator.getInstance().otvoriIzdavanjeNosnje(null, FormMode.FORM_VIEW);
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    OtpremniceTableModel otm = (OtpremniceTableModel) fPretragaOtpremnica.getjTable1().getModel();
                    List<Otpremnica> lista = null;
                    try {
                        lista = vratiSveOtpremnice();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(fPretragaOtpremnica, "Dogodila se greska u komunikaciji!(server je ugasen)");
                        System.exit(0);
                        //Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    otm.azuriraj(lista);
                }
            }
        });

        fPretragaOtpremnica.addButtonPretraziListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fPretragaOtpremnica.getJtxtSifra().getText().isEmpty() && fPretragaOtpremnica.getJcmbClan().getSelectedItem().equals("Izaberite clana")) {
                    OtpremniceTableModel otm = (OtpremniceTableModel) fPretragaOtpremnica.getjTable1().getModel();
                    List<Otpremnica> lista = null;
                    try {
                        lista = vratiSveOtpremnice();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(fPretragaOtpremnica, "Dogodila se greska u komunikaciji!(server je ugasen)");
                        System.exit(0);
                        //Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    otm.azuriraj(lista);
                } else {
                    String sifra = fPretragaOtpremnica.getJtxtSifra().getText();
                    String clan = String.valueOf(fPretragaOtpremnica.getJcmbClan().getSelectedItem());
                    //
                    Clan clan1 = null;
                    if (!clan.equals("Izaberite clana")) {
                        clan1 = (Clan) fPretragaOtpremnica.getJcmbClan().getSelectedItem();
                    }

                    Otpremnica otpremnica = new Otpremnica();

                    if (!sifra.equals("")) {
                        Long sifra1 = Long.parseLong(sifra);
                        otpremnica.setSifraOtpremnice(sifra1);
                    }
                    //mozda da ide provera da li je equals Izaberite clana
                    if (clan != null) {
                        otpremnica.setClan(clan1);
                    }

                    List<Otpremnica> listaOtp = null;
                    try {
                        listaOtp = vratiOtpremnicePoKriterijumu(otpremnica);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(fPretragaOtpremnica, "Dogodila se greska u komunikaciji!(server je ugasen)");
                        System.exit(0);
                        //Logger.getLogger(ControllerFPretragaOtpremnica.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    OtpremniceTableModel otm = new OtpremniceTableModel(listaOtp);
                    fPretragaOtpremnica.getjTable1().setModel(otm);
                }
            }
        });

    }

    private void fillClanove() throws Exception {
        fPretragaOtpremnica.getJcmbClan().removeAllItems();

        List<Clan> clanovi = GUICoordinator.getInstance().vratiSveClanove();

        fPretragaOtpremnica.getJcmbClan().addItem("Izaberite clana");

        for (Clan clan : clanovi) {
            fPretragaOtpremnica.getJcmbClan().addItem(clan);
        }

    }

}
