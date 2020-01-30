/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import communication.CommunicationServer;
import domain.Clan;
import domain.enumeracije.Ansambl;
import domain.enumeracije.FormMode;
import domain.enumeracije.Pol;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.form.FMain;
import ui.form.FNoviClan;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author Nemanja
 */
public class ControllerFNoviClan {

    private FNoviClan fNoviClan;
    private final Map<String, Object> map;

    public ControllerFNoviClan() {
        map = new HashMap<>();
    }

    void otvoriFormuZaUnosNovogClana(FMain fMain, FormMode formMode) {
        fNoviClan = new FNoviClan(fMain, true);
        addListenersNoviClan();
        prepareForm(formMode);
        fNoviClan.setVisible(true);
    }

    public void postaviClana(Clan clan) {
        map.put("trenutniClan", clan);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public Clan zapamtiClana(Clan clan) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.SAVE_CLAN, clan);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Clan clan1 = (Clan) responseObject.getData();
            return clan1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public void obrisiClana(Clan clan) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.OBRISI_CLANA, clan);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return;
        }
        throw new Exception(responseObject.getErrorMessage());

    }

    public Clan izmeniClana(Clan clan) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.IZMENI_CLANA, clan);

        CommunicationServer.getInstance().sendRequest(requestObject);

        ResponseObject responseObject = CommunicationServer.getInstance().receiveResponse();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Clan clan1 = (Clan) responseObject.getData();
            return clan1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private void addListenersNoviClan() {
        fNoviClan.addButtonSaveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ime = fNoviClan.getjTxtIme().getText().trim();
                String prezime = fNoviClan.getjTxtPrezime().getText().trim();
                Pol pol = (Pol) fNoviClan.getjComboBoxPol().getSelectedItem();
                double visina = (double) fNoviClan.getjComboBoxVisina().getSelectedItem();
                double brojObuce = (double) fNoviClan.getjComboBoxBrojObuce().getSelectedItem();
                Ansambl ansambl = (Ansambl) fNoviClan.getjComboBoxAnsambl().getSelectedItem();
                boolean aktivan;
                if (fNoviClan.getjComboBoxAktivan().getSelectedItem().equals("DA")) {
                    aktivan = true;
                } else {
                    aktivan = false;
                }

                Date datumRodjenja = vratiDatum(fNoviClan.getjComboBoxDan(), fNoviClan.getjComboBoxMesec(), fNoviClan.getjComboBoxGodina());
                Date datumUclanjenja = vratiDatum(fNoviClan.getjComboBoxDan1(), fNoviClan.getjComboBoxMesec1(), fNoviClan.getjComboBoxGodina1());

                fNoviClan.getJlaberrorIme().setText("");
                fNoviClan.getJlabErrorPrezime().setText("");

                if (ime.isEmpty()) {
                    fNoviClan.getJlaberrorIme().setText("Ime ne moze biti prazno!");
                    return;
                }

                if (prezime.isEmpty()) {
                    fNoviClan.getJlabErrorPrezime().setText("Prezime ne moze biti prazno!");
                    return;
                }

                Clan clan = new Clan(null, ime, prezime, pol, visina, datumRodjenja, datumUclanjenja, brojObuce, ansambl, aktivan);

                try {
                    clan = zapamtiClana(clan);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fNoviClan, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                    //Logger.getLogger(ControllerFNoviClan.class.getName()).log(Level.SEVERE, null, ex);
                }

                JOptionPane.showMessageDialog(fNoviClan, "Clan je sacuvan sa brojem clanske karte: " + clan.getBrojCK());
                postaviClana(clan);
                prepareForm(FormMode.FORM_VIEW);
            }
        });

        fNoviClan.addButtonEnableChangesListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareForm(FormMode.FORM_UPDATE);
            }
        });

        fNoviClan.addButtonDeleteListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clan clan = (Clan) getMap().get("trenutniClan");
                //Long brojCK = clan.getBrojCK();
                try {
                    obrisiClana(clan);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fNoviClan, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                    //Logger.getLogger(ControllerFNoviClan.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(fNoviClan, "Obrisan je clan sa brojem CK: " + clan.getBrojCK());
                prepareForm(FormMode.FORM_ADD);
            }
        });

        fNoviClan.addButtonCancelListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fNoviClan.dispose();
            }
        });

        fNoviClan.addButtonUbaciNovogListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareForm(FormMode.FORM_ADD);
            }
        });

        fNoviClan.addButtonUpdateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long brojCK = Long.parseLong(fNoviClan.getjTxtBrojCK().getText());
                String ime = fNoviClan.getjTxtIme().getText().trim();
                String prezime = fNoviClan.getjTxtPrezime().getText().trim();
                Pol pol = (Pol) fNoviClan.getjComboBoxPol().getSelectedItem();
                double visina = (double) fNoviClan.getjComboBoxVisina().getSelectedItem();
                double brojObuce = (double) fNoviClan.getjComboBoxBrojObuce().getSelectedItem();
                Ansambl ansambl = (Ansambl) fNoviClan.getjComboBoxAnsambl().getSelectedItem();
                boolean aktivan;
                if (fNoviClan.getjComboBoxAktivan().getSelectedItem().equals("DA")) {
                    aktivan = true;
                } else {
                    aktivan = false;
                }

                Date datumRodjenja = vratiDatum(fNoviClan.getjComboBoxDan(), fNoviClan.getjComboBoxMesec(), fNoviClan.getjComboBoxGodina());
                Date datumUclanjenja = vratiDatum(fNoviClan.getjComboBoxDan1(), fNoviClan.getjComboBoxMesec1(), fNoviClan.getjComboBoxGodina1());

                /*if (ime.isEmpty() || prezime.isEmpty()) {
                    JOptionPane.showMessageDialog(fNoviClan, "Polja ime i prezime moraju biti popunjena!");
                    return;
                }*/

                fNoviClan.getJlaberrorIme().setText("");
                fNoviClan.getJlabErrorPrezime().setText("");

                if (ime.isEmpty()) {
                    fNoviClan.getJlaberrorIme().setText("Ime ne moze biti prazno!");
                    return;
                }

                if (prezime.isEmpty()) {
                    fNoviClan.getJlabErrorPrezime().setText("Prezime ne moze biti prazno!");
                    return;
                }
                
                Clan clan = new Clan(brojCK, ime, prezime, pol, visina, datumRodjenja, datumUclanjenja, brojObuce, ansambl, aktivan);
                try {
                    clan = izmeniClana(clan);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fNoviClan, "Dogodila se greska u komunikaciji!(server je ugasen)");
                    System.exit(0);
                    //Logger.getLogger(ControllerFNoviClan.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(fNoviClan, "Izmenjen je clan sa brojem CK: " + clan.getBrojCK());
                postaviClana(clan);
                prepareForm(FormMode.FORM_VIEW);

            }
        });
    }

    private void prepareForm(FormMode form_mode) {
        fillPol();
        fillDatumRodjenja();
        fillDatumUclanjenja();
        fillAnsambl();
        fillAktivan();
        fillVisina();
        fillBrojObuce();

        if (form_mode.equals(FormMode.FORM_ADD)) {
            fNoviClan.getjBtnDelete().setEnabled(false);
            fNoviClan.getjBtnEnableChanges().setEnabled(false);
            fNoviClan.getjBtnUpdate().setEnabled(false);
            fNoviClan.getjBtnSave().setEnabled(true);
            fNoviClan.getjBtnCancel().setEnabled(true);
            fNoviClan.getjBtnUbaciNovog().setEnabled(false);

            fNoviClan.getjTxtIme().setEditable(true);
            fNoviClan.getjTxtPrezime().setEditable(true);
            fNoviClan.getjTxtIme().setText("");
            fNoviClan.getjTxtPrezime().setText("");
            fNoviClan.getjTxtBrojCK().setText("");
        }

        if (form_mode.equals(FormMode.FORM_VIEW)) {
            fNoviClan.getjBtnSave().setEnabled(false);
            fNoviClan.getjBtnUpdate().setEnabled(false);
            fNoviClan.getjBtnEnableChanges().setEnabled(true);
            fNoviClan.getjBtnDelete().setEnabled(true);
            fNoviClan.getjBtnUbaciNovog().setEnabled(true);

            fNoviClan.getjTxtIme().setEditable(false);
            fNoviClan.getjTxtPrezime().setEditable(false);
            fNoviClan.getjComboBoxPol().setEditable(false);
            fNoviClan.getjComboBoxDan().setEditable(false);
            fNoviClan.getjComboBoxMesec().setEditable(false);
            fNoviClan.getjComboBoxGodina().setEditable(false);
            fNoviClan.getjComboBoxDan1().setEditable(false);
            fNoviClan.getjComboBoxMesec1().setEditable(false);
            fNoviClan.getjComboBoxGodina1().setEditable(false);
            fNoviClan.getjComboBoxAktivan().setEditable(false);
            fNoviClan.getjComboBoxAnsambl().setEditable(false);
            fNoviClan.getjComboBoxBrojObuce().setEditable(false);
            fNoviClan.getjComboBoxVisina().setEditable(false);

            fNoviClan.getJlaberrorIme().setText("");
            fNoviClan.getJlabErrorPrezime().setText("");

            Clan clan = (Clan) getMap().get("trenutniClan");
            fillForm(clan);

        }

        if (form_mode.equals(FormMode.FORM_UPDATE)) {

            fNoviClan.getjBtnDelete().setEnabled(false);
            fNoviClan.getjBtnEnableChanges().setEnabled(false);
            fNoviClan.getjBtnUpdate().setEnabled(true);
            fNoviClan.getjBtnSave().setEnabled(false);
            fNoviClan.getjBtnCancel().setEnabled(true);
            fNoviClan.getjBtnUbaciNovog().setEnabled(false);

            fNoviClan.getjTxtIme().setEditable(true);
            fNoviClan.getjTxtPrezime().setEditable(true);

            Clan clan = (Clan) getMap().get("trenutniClan");
            fillForm(clan);

        }

    }

    private void fillPol() {
        fNoviClan.getjComboBoxPol().removeAllItems();

        fNoviClan.getjComboBoxPol().addItem(Pol.M);
        fNoviClan.getjComboBoxPol().addItem(Pol.Z);
    }

    private void fillDatumRodjenja() {
        fNoviClan.getjComboBoxDan().removeAllItems();
        fNoviClan.getjComboBoxMesec().removeAllItems();
        fNoviClan.getjComboBoxGodina().removeAllItems();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        int year = Integer.parseInt(sdf.format(date));

        for (int i = 1; i < 32; i++) {
            fNoviClan.getjComboBoxDan().addItem(i + "");
        }
        for (int i = 1; i < 13; i++) {
            fNoviClan.getjComboBoxMesec().addItem(i + "");
        }
        for (int i = 1920; i <= year; i++) {
            fNoviClan.getjComboBoxGodina().addItem(i + "");
        }

        fNoviClan.getjComboBoxGodina().setSelectedItem("1997");

    }

    private void fillDatumUclanjenja() {
        fNoviClan.getjComboBoxDan1().removeAllItems();
        fNoviClan.getjComboBoxMesec1().removeAllItems();
        fNoviClan.getjComboBoxGodina1().removeAllItems();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        int year = Integer.parseInt(sdf.format(date));

        for (int i = 1; i < 32; i++) {
            fNoviClan.getjComboBoxDan1().addItem(i + "");
        }
        for (int i = 1; i < 13; i++) {
            fNoviClan.getjComboBoxMesec1().addItem(i + "");
        }
        for (int i = 1967; i <= year; i++) {
            fNoviClan.getjComboBoxGodina1().addItem(i + "");
        }

        fNoviClan.getjComboBoxGodina1().setSelectedItem("2019");

    }

    private void fillAnsambl() {
        fNoviClan.getjComboBoxAnsambl().removeAllItems();

        fNoviClan.getjComboBoxAnsambl().addItem(Ansambl.PRVI);
        fNoviClan.getjComboBoxAnsambl().addItem(Ansambl.DRUGI);
        fNoviClan.getjComboBoxAnsambl().addItem(Ansambl.DECIJI);
    }

    private void fillAktivan() {
        fNoviClan.getjComboBoxAktivan().removeAllItems();
        fNoviClan.getjComboBoxAktivan().addItem("DA");
        fNoviClan.getjComboBoxAktivan().addItem("NE");
    }

    private void fillVisina() {
        fNoviClan.getjComboBoxVisina().removeAllItems();

        for (double i = 130; i < 220; i++) {
            fNoviClan.getjComboBoxVisina().addItem(i);
        }
        fNoviClan.getjComboBoxVisina().setSelectedItem(180.0);
    }

    private void fillBrojObuce() {
        fNoviClan.getjComboBoxBrojObuce().removeAllItems();

        for (double i = 30; i < 50; i++) {
            fNoviClan.getjComboBoxBrojObuce().addItem(i);
        }
        fNoviClan.getjComboBoxBrojObuce().setSelectedItem(42.0);
    }

    private Date vratiDatum(JComboBox<String> jComboBoxDan, JComboBox<String> jComboBoxMesec, JComboBox<String> jComboBoxGodina) {
        String dan = (String) jComboBoxDan.getSelectedItem();
        String mesec = (String) jComboBoxMesec.getSelectedItem();
        String godina = (String) jComboBoxGodina.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date datum = null;

        try {
            return datum = sdf.parse(dan + "." + mesec + "." + godina);
        } catch (ParseException ex) {
            Logger.getLogger(FNoviClan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void fillForm(Clan clan) {
        fNoviClan.getjTxtBrojCK().setText(clan.getBrojCK() + "");
        fNoviClan.getjTxtIme().setText(clan.getIme());
        fNoviClan.getjTxtPrezime().setText(clan.getPrezime());
        if (clan.isAktivan() == true) {
            fNoviClan.getjComboBoxAktivan().setSelectedItem("DA");
        } else {
            fNoviClan.getjComboBoxAktivan().setSelectedItem("NE");
        }

        fNoviClan.getjComboBoxAnsambl().setSelectedItem(clan.getAnsambl());
        fNoviClan.getjComboBoxBrojObuce().setSelectedItem(clan.getBrojObuce());
        Date datumRodjenja = new Date(clan.getDatumRodjenja().getTime());
        Date datumUclanjenja = new Date(clan.getDatumUclanjenja().getTime());
        LocalDate localDate = datumRodjenja.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate1 = datumUclanjenja.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        fNoviClan.getjComboBoxDan().setSelectedItem(localDate.getDayOfMonth() + "");
        fNoviClan.getjComboBoxDan1().setSelectedItem(localDate1.getDayOfMonth() + "");
        fNoviClan.getjComboBoxMesec().setSelectedItem(localDate.getMonthValue() + "");
        fNoviClan.getjComboBoxMesec1().setSelectedItem(localDate1.getMonthValue() + "");
        fNoviClan.getjComboBoxGodina().setSelectedItem(localDate.getYear() + "");
        fNoviClan.getjComboBoxGodina1().setSelectedItem(localDate1.getYear() + "");
        fNoviClan.getjComboBoxPol().setSelectedItem(clan.getPol());
        fNoviClan.getjComboBoxVisina().setSelectedItem(clan.getVisina());
    }

}
