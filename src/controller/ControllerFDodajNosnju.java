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
import domain.enumeracije.Pol;
import domain.enumeracije.VrstaNosnje;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.form.FDodajNosnju;
import ui.form.FMain;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFDodajNosnju {

    private FDodajNosnju fDodajNosnju;

    //private final ServiceNosnja serviceNosnja;
    private final Map<String, Object> map;

    public ControllerFDodajNosnju() {
        //serviceNosnja = new ServiceNosnjaImpl();
        map = new HashMap<>();
    }

    void otvoriFormuZaDodavanjeNosnje(FMain fMain, FormModeVrstaNosnje formModeVrsta, FormMode formMode) {
        fDodajNosnju = new FDodajNosnju(fMain, true);
        fillVrstaNosnjeNovo();

        prepareFormVrstaNosnja(formModeVrsta);
        prepareFormMode(formMode);
        addListenersNosnja();
        addListenersButtonNosnja();
        fDodajNosnju.setVisible(true);
    }

    public Nosnja sacuvajNosnju(Nosnja n) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.SAVE_NOSNJA, n);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Nosnja nosnja = (Nosnja) responseObject.getData();
            return nosnja;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Nosnja izmeniNosnju(Nosnja n) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.IZMENI_NOSNJU, n);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Nosnja nosnja = (Nosnja) responseObject.getData();
            return nosnja;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public void obrisiNosnju(Nosnja nosnja) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.OBRISI_NOSNJU, nosnja);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public void postaviNosnju(Nosnja b) {
        map.put("trenutnaNosnja", b);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    private void addListenersNosnja() {
        fDodajNosnju.addVrstaNosnjeItemStateChanged(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.BELINA)) {
                        isprazniFormu();
                        prepareFormMode(FormMode.FORM_ADD);
                        prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_BELINA);
                    }
                    if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.NOSNJA)) {
                        isprazniFormu();
                        prepareFormMode(FormMode.FORM_ADD);
                        prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_NOSNJA);
                    }
                    if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.OPANCI)) {
                        isprazniFormu();
                        prepareFormMode(FormMode.FORM_ADD);
                        prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_OPANCI);
                    }
                    if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.Izaberite_vrstu_nosnje)) {
                        //fDodajNosnju.getjBtnSave().setEnabled(false);
                        isprazniFormu();
                        prepareFormMode(FormMode.FORM_ADD);
                        prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_POCETNO);
                    }
                }
            }

        });

    }

    private void addListenersButtonNosnja() {
        fDodajNosnju.addButtonSaveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.Izaberite_vrstu_nosnje)) {
                    JOptionPane.showMessageDialog(fDodajNosnju, "Izaberite vrstu nosnje");
                    return;
                }

                VrstaNosnje vrstaNosnje = (VrstaNosnje) fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem();
                Pol pol = (Pol) fDodajNosnju.getjComboBoxPol().getSelectedItem();
                String nazivNosnje = fDodajNosnju.getjTxtNazivNosnje().getText();
                int kolicina = (int) fDodajNosnju.getjComboBoxKolicina().getSelectedItem();
                String opis = fDodajNosnju.getjTxtOpisNosnje().getText();
                String vrstaBeline = (String) fDodajNosnju.getjComboBoxVrstaBeline().getSelectedItem();
                String velicina = (String) fDodajNosnju.getjComboBoxVelicina().getSelectedItem();
                String vrstaOpanaka = (String) fDodajNosnju.getjComboBoxVrstaOpanaka().getSelectedItem();

                if (nazivNosnje.isEmpty()) {

                    fDodajNosnju.getjLabgreskaNaziv().setText("Naziv nosnje ne moze biti prazan!");
                    return;
                }

                Nosnja n = new Nosnja(null, vrstaNosnje, pol, nazivNosnje, kolicina, opis, vrstaBeline, String.valueOf(velicina), vrstaOpanaka);

                try {
                    n = sacuvajNosnju(n);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fDodajNosnju, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                    //Logger.getLogger(ControllerFDodajNosnju.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(fDodajNosnju, "Uspesno uneta nosnja!");

                postaviNosnju(n);
                prepareFormMode(FormMode.FORM_VIEW);
            }
        });

        fDodajNosnju.addButtonDeleteListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nosnja n = (Nosnja) getMap().get("trenutnaNosnja");
                //Long sifra = n.getSifraNosnje();
                try {
                    obrisiNosnju(n);
                } catch (Exception ex) {
                    //Logger.getLogger(ControllerFDodajNosnju.class.getName()).log(Level.SEVERE, null, ex);
                    /*JOptionPane.showMessageDialog(fDodajNosnju, "Nosnja ne moze biti obrisana zato sto je izdata!");
                    return;*/
                    JOptionPane.showMessageDialog(fDodajNosnju, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                }
                JOptionPane.showMessageDialog(fDodajNosnju, "Obrisana je nosnja sa sifrom: " + n.getSifraNosnje());

                isprazniFormu();

                fDodajNosnju.getjComboBoxVrstaNosnje().setEnabled(true);
                fDodajNosnju.getjComboBoxVrstaNosnje().setSelectedItem(VrstaNosnje.Izaberite_vrstu_nosnje);
                prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_POCETNO);
                prepareFormMode(FormMode.FORM_ADD);
            }
        });

        fDodajNosnju.addButtonEnableChangesListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareFormMode(FormMode.FORM_UPDATE);
            }
        });

        fDodajNosnju.addButtonUpdateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long sifra = Long.parseLong(fDodajNosnju.getjTxtSifraNosnje().getText());
                VrstaNosnje vrstaNosnje = (VrstaNosnje) fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem();
                Pol pol = (Pol) fDodajNosnju.getjComboBoxPol().getSelectedItem();
                String nazivNosnje = fDodajNosnju.getjTxtNazivNosnje().getText();
                int kolicina = (int) fDodajNosnju.getjComboBoxKolicina().getSelectedItem();
                String opis = fDodajNosnju.getjTxtOpisNosnje().getText();
                String vrstaBeline = (String) fDodajNosnju.getjComboBoxVrstaBeline().getSelectedItem();
                String velicina = (String) fDodajNosnju.getjComboBoxVelicina().getSelectedItem();
                String vrstaOpanaka = (String) fDodajNosnju.getjComboBoxVrstaOpanaka().getSelectedItem();

                Nosnja n = new Nosnja(sifra, vrstaNosnje, pol, nazivNosnje, kolicina, opis, vrstaBeline, String.valueOf(velicina), vrstaOpanaka);

                try {
                    n = izmeniNosnju(n);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fDodajNosnju, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                    //Logger.getLogger(ControllerFDodajNosnju.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(fDodajNosnju, "Izmenjena je nosnja sa sifrom: " + n.getSifraNosnje());

                postaviNosnju(n);
                prepareFormMode(FormMode.FORM_VIEW);
            }
        });

        fDodajNosnju.addButtonUbaciNovogListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //otvoriFormuZaDodavanjeNosnje(null, FormModeVrstaNosnje.FORM_POCETNO, FormMode.FORM_ADD);
                isprazniFormu();
                prepareFormMode(FormMode.FORM_ADD);
                prepareFormVrstaNosnja(FormModeVrstaNosnje.FORM_POCETNO);
            }
        });
    }

    private void isprazniFormu() {
        fDodajNosnju.getjComboBoxPol().removeAllItems();
        fDodajNosnju.getjComboBoxVrstaBeline().removeAllItems();
        fDodajNosnju.getjComboBoxVelicina().removeAllItems();
        fDodajNosnju.getjComboBoxVrstaOpanaka().removeAllItems();
        fDodajNosnju.getjComboBoxKolicina().removeAllItems();

        fDodajNosnju.getjTxtSifraNosnje().setText("");
        fDodajNosnju.getjTxtNazivNosnje().setText("");
        fDodajNosnju.getjTxtOpisNosnje().setText("");
    }

    private void prepareFormVrstaNosnja(FormModeVrstaNosnje formModeVrstaNosnje) {

        if (formModeVrstaNosnje.equals(FormModeVrstaNosnje.FORM_POCETNO)) {
            fDodajNosnju.getjTxtSifraNosnje().setEnabled(false);
            fDodajNosnju.getjComboBoxPol().setEnabled(false);
            fDodajNosnju.getjComboBoxVrstaBeline().setEnabled(false);
            fDodajNosnju.getjComboBoxVelicina().setEnabled(false);
            fDodajNosnju.getjComboBoxKolicina().setEnabled(false);
            fDodajNosnju.getjComboBoxVrstaOpanaka().setEnabled(false);
            fDodajNosnju.getjTxtNazivNosnje().setEnabled(false);
            fDodajNosnju.getjTxtOpisNosnje().setEnabled(false);

            fDodajNosnju.getjBtnDelete().setEnabled(false);
            fDodajNosnju.getjBtnEnableChanges().setEnabled(false);
            fDodajNosnju.getjBtnUpdate().setEnabled(false);
            fDodajNosnju.getjBtnSave().setEnabled(false);
            fDodajNosnju.getjBtnCancel().setEnabled(true);
            fDodajNosnju.getjBtnUbaciNovog().setEnabled(false);
            fDodajNosnju.getjComboBoxVrstaNosnje().setSelectedItem(VrstaNosnje.Izaberite_vrstu_nosnje);
            fDodajNosnju.getjComboBoxVrstaNosnje().setEnabled(true);

        } else if (formModeVrstaNosnje.equals(FormModeVrstaNosnje.FORM_NOSNJA)) {
            fDodajNosnju.getjTxtSifraNosnje().setEnabled(false);
            fDodajNosnju.getjComboBoxPol().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaBeline().setEnabled(false);
            fDodajNosnju.getjComboBoxVelicina().setEnabled(false);
            fDodajNosnju.getjComboBoxKolicina().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaOpanaka().setEnabled(false);
            fDodajNosnju.getjTxtNazivNosnje().setEnabled(true);
            fDodajNosnju.getjTxtOpisNosnje().setEnabled(true);

            fillPol();
            fillKolicina();

        } else if (formModeVrstaNosnje.equals(FormModeVrstaNosnje.FORM_BELINA)) {
            fDodajNosnju.getjTxtSifraNosnje().setEnabled(false);
            fDodajNosnju.getjComboBoxPol().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaBeline().setEnabled(true);
            fDodajNosnju.getjComboBoxVelicina().setEnabled(false);
            fDodajNosnju.getjComboBoxKolicina().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaOpanaka().setEnabled(false);
            fDodajNosnju.getjTxtNazivNosnje().setEnabled(true);
            fDodajNosnju.getjTxtOpisNosnje().setEnabled(true);

            fillPol();
            fillKolicina();
            fillVrstaBeline();

        } else if (formModeVrstaNosnje.equals(FormModeVrstaNosnje.FORM_OPANCI)) {
            fDodajNosnju.getjTxtSifraNosnje().setEnabled(false);
            fDodajNosnju.getjComboBoxPol().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaBeline().setEnabled(false);
            fDodajNosnju.getjComboBoxVelicina().setEnabled(true);
            fDodajNosnju.getjComboBoxKolicina().setEnabled(true);
            fDodajNosnju.getjComboBoxVrstaOpanaka().setEnabled(true);
            fDodajNosnju.getjTxtNazivNosnje().setEnabled(true);
            fDodajNosnju.getjTxtOpisNosnje().setEnabled(true);

            fillPol();
            fillKolicina();
            fillVelicina();
            fillVrstaOpanaka();
        }

    }

    private void prepareFormMode(FormMode form_mode) {
        if (form_mode.equals(FormMode.FORM_ADD)) {
            fDodajNosnju.getjBtnDelete().setEnabled(false);
            fDodajNosnju.getjBtnEnableChanges().setEnabled(false);
            fDodajNosnju.getjBtnUpdate().setEnabled(false);
            fDodajNosnju.getjBtnSave().setEnabled(true);
            fDodajNosnju.getjBtnCancel().setEnabled(true);
            fDodajNosnju.getjBtnUbaciNovog().setEnabled(false);

            fDodajNosnju.getjTxtNazivNosnje().setEditable(true);
            fDodajNosnju.getjTxtOpisNosnje().setEditable(true);

        } else if (form_mode.equals(FormMode.FORM_VIEW)) {
            fDodajNosnju.getjBtnSave().setEnabled(false);
            fDodajNosnju.getjBtnUpdate().setEnabled(false);
            fDodajNosnju.getjBtnEnableChanges().setEnabled(true);
            fDodajNosnju.getjBtnDelete().setEnabled(true);
            fDodajNosnju.getjBtnUbaciNovog().setEnabled(true);

            fDodajNosnju.getjTxtNazivNosnje().setEditable(false);
            fDodajNosnju.getjTxtOpisNosnje().setEditable(false);
            fDodajNosnju.getjTxtSifraNosnje().setEditable(false);
            fDodajNosnju.getjComboBoxKolicina().setEditable(false);
            fDodajNosnju.getjComboBoxPol().setEditable(false);
            fDodajNosnju.getjComboBoxVelicina().setEditable(false);
            fDodajNosnju.getjComboBoxVrstaNosnje().setEditable(false);
            fDodajNosnju.getjComboBoxVrstaOpanaka().setEditable(false);
            fDodajNosnju.getjComboBoxVrstaNosnje().setEnabled(false);
            fDodajNosnju.getjLabgreskaNaziv().setText(" ");

            Nosnja n = (Nosnja) getMap().get("trenutnaNosnja");
            postaviNosnjuZaPrikaz(n);

        } else if (form_mode.equals(FormMode.FORM_UPDATE)) {
            fDodajNosnju.getjBtnDelete().setEnabled(false);
            fDodajNosnju.getjBtnEnableChanges().setEnabled(false);
            fDodajNosnju.getjBtnUpdate().setEnabled(true);
            fDodajNosnju.getjBtnSave().setEnabled(false);
            fDodajNosnju.getjBtnCancel().setEnabled(true);

            fDodajNosnju.getjTxtSifraNosnje().setEditable(false);
            fDodajNosnju.getjComboBoxPol().setEnabled(true);
            fDodajNosnju.getjTxtNazivNosnje().setEditable(true);
            fDodajNosnju.getjTxtOpisNosnje().setEditable(true);
            fDodajNosnju.getjComboBoxKolicina().setEnabled(true);

            if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.BELINA)) {
                fDodajNosnju.getjComboBoxVrstaBeline().setEnabled(true);
            } else if (fDodajNosnju.getjComboBoxVrstaNosnje().getSelectedItem().equals(VrstaNosnje.OPANCI)) {
                fDodajNosnju.getjComboBoxVelicina().setEnabled(true);
                fDodajNosnju.getjComboBoxVrstaOpanaka().setEnabled(true);
            }

            Nosnja n = (Nosnja) getMap().get("trenutnaNosnja");
            postaviNosnjuZaPrikaz(n);
        }
    }

    private void fillVrstaNosnjeNovo() {
        fDodajNosnju.getjComboBoxVrstaNosnje().addItem(VrstaNosnje.Izaberite_vrstu_nosnje);
        fDodajNosnju.getjComboBoxVrstaNosnje().addItem(VrstaNosnje.BELINA);
        fDodajNosnju.getjComboBoxVrstaNosnje().addItem(VrstaNosnje.OPANCI);
        fDodajNosnju.getjComboBoxVrstaNosnje().addItem(VrstaNosnje.NOSNJA);

        fDodajNosnju.getjComboBoxVrstaNosnje().setEnabled(true);
    }

    private void fillPol() {
        fDodajNosnju.getjComboBoxPol().removeAllItems();

        fDodajNosnju.getjComboBoxPol().addItem(Pol.M);
        fDodajNosnju.getjComboBoxPol().addItem(Pol.Z);
    }

    private void fillVelicina() {
        fDodajNosnju.getjComboBoxVelicina().removeAllItems();

        for (int i = 30; i < 50; i++) {
            fDodajNosnju.getjComboBoxVelicina().addItem(i + "");
        }
    }

    private void fillVrstaOpanaka() {
        fDodajNosnju.getjComboBoxVrstaOpanaka().removeAllItems();

        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("SUMADIJSKI");
        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("PRESNI");
        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("OROVSKI");
        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("GLAMOCKI");
        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("CIZME");
        fDodajNosnju.getjComboBoxVrstaOpanaka().addItem("CIPELE");

    }

    private void fillKolicina() {
        fDodajNosnju.getjComboBoxKolicina().removeAllItems();

        for (int i = 1; i < 100; i++) {
            fDodajNosnju.getjComboBoxKolicina().addItem(i);
        }
    }

    private void fillVrstaBeline() {
        fDodajNosnju.getjComboBoxVrstaBeline().removeAllItems();

        fDodajNosnju.getjComboBoxVrstaBeline().addItem("MARAMA");
        fDodajNosnju.getjComboBoxVrstaBeline().addItem("KOSULJA");
        fDodajNosnju.getjComboBoxVrstaBeline().addItem("PANTALONE");
        fDodajNosnju.getjComboBoxVrstaBeline().addItem("SUKNJA");
    }

    private void postaviNosnjuZaPrikaz(Nosnja n) {
        fDodajNosnju.getjComboBoxVrstaNosnje().setSelectedItem(n.getVrstaNosnje());
        fDodajNosnju.getjTxtSifraNosnje().setText(String.valueOf(n.getSifraNosnje()));
        fDodajNosnju.getjComboBoxPol().setSelectedItem(n.getPol());
        fDodajNosnju.getjComboBoxVrstaBeline().setSelectedItem(n.getVrstaBeline());
        fDodajNosnju.getjComboBoxVelicina().setSelectedItem(n.getVelicina());
        fDodajNosnju.getjComboBoxKolicina().setSelectedItem(n.getKolicina());
        fDodajNosnju.getjComboBoxVrstaOpanaka().setSelectedItem(n.getVrstaOpanaka());
        fDodajNosnju.getjTxtNazivNosnje().setText(n.getNazivNosnje());
        fDodajNosnju.getjTxtOpisNosnje().setText(n.getOpis());
    }

}
