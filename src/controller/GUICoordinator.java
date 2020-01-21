/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Clan;
import domain.Nosnja;
import domain.Otpremnica;
import domain.enumeracije.FormMode;
import domain.enumeracije.FormModeVrstaNosnje;
import java.util.List;
import ui.form.FMain;

/**
 *
 * @author Nemanja
 */
public class GUICoordinator {

    private static GUICoordinator instance;

    private ControllerFLogin controllerFLogin;
    private ControllerFMain controllerFMain;
    private ControllerFNoviClan controllerFNoviClan;
    private ControllerFPretragaClana controllerFPretragaClana;
    private ControllerFDodajNosnju controllerFDodajNosnju;
    private ControllerFPretragaNosnje controllerFPretragaNosnje;
    private ControllerFIzdavanjeNosnje controllerFIzdavanjeNosnje;
    private ControllerFPretragaOtpremnica controllerFPretragaOtpremnica;

    public static GUICoordinator getInstance() {
        if (instance == null) {
            instance = new GUICoordinator();
        }
        return instance;
    }

    public GUICoordinator() {

    }

    public void otvoriLoginFormu() {
        if (controllerFLogin == null) {
            controllerFLogin = new ControllerFLogin();
        }
        controllerFLogin.otvoriFormuLogin();
    }

    public void otvoriMainFormu() {
        if (controllerFMain == null) {
            controllerFMain = new ControllerFMain();
        }
        controllerFMain.otvoriFormuFMain();
    }

    public void inicijalizujKonstruktorKontroleraNoviClan() {
        if (controllerFNoviClan == null) {
            controllerFNoviClan = new ControllerFNoviClan();
        }
    }

    public void inicijalizujKonstruktorKontroleraNovaNosnja() {
        if (controllerFDodajNosnju == null) {
            controllerFDodajNosnju = new ControllerFDodajNosnju();
        }
    }

    public void otvoriNovogClana(FMain fMain, FormMode formMode) {
        if (controllerFNoviClan == null) {

            controllerFNoviClan = new ControllerFNoviClan();

        }
        controllerFNoviClan.otvoriFormuZaUnosNovogClana(fMain, formMode);
    }

    public void otvoriPretraguClana(FMain fMain) {
        if (controllerFPretragaClana == null) {
            controllerFPretragaClana = new ControllerFPretragaClana();
        }
        controllerFPretragaClana.otvoriFormuZaPretraguClana(fMain);
    }

    public void otvoriNovuNosnju(FMain fMain, FormModeVrstaNosnje formModeVrsta, FormMode formMode) {
        if (controllerFDodajNosnju == null) {
            controllerFDodajNosnju = new ControllerFDodajNosnju();
        }
        controllerFDodajNosnju.otvoriFormuZaDodavanjeNosnje(fMain, formModeVrsta, formMode);
    }

    public void otvoriPretraguNosnje(FMain fMain) {
        if (controllerFPretragaNosnje == null) {
            controllerFPretragaNosnje = new ControllerFPretragaNosnje();
        }
        controllerFPretragaNosnje.otvoriFormuZaPretraguNosnje(fMain);
    }

    public void otvoriIzdavanjeNosnje(FMain fMain, FormMode formMode) throws Exception {
        if (controllerFIzdavanjeNosnje == null) {
            controllerFIzdavanjeNosnje = new ControllerFIzdavanjeNosnje();
        }
        controllerFIzdavanjeNosnje.otvoriFormuZaIzdavanjeNosnje(fMain, formMode);
    }

    void pozoviPostavljanjeClana(Clan clan) {
        controllerFNoviClan.postaviClana(clan);
    }

    void pozoviPostavljanjeNosnje(Nosnja nosnja) {
        controllerFDodajNosnju.postaviNosnju(nosnja);
    }

    List<Clan> vratiSveClanove() throws Exception {
        if (controllerFPretragaClana == null) {
            controllerFPretragaClana = new ControllerFPretragaClana();
        }
        return controllerFPretragaClana.vratiListuClanova();
    }

    List<Nosnja> vratiSvuNosnju() throws Exception {
        if (controllerFPretragaNosnje == null) {
            controllerFPretragaNosnje = new ControllerFPretragaNosnje();
        }
        return controllerFPretragaNosnje.vratiListuNosnje();
    }

    void otvoriPretraguOtpremnica(FMain fMain) throws Exception {
        if (controllerFPretragaOtpremnica == null) {
            controllerFPretragaOtpremnica = new ControllerFPretragaOtpremnica();
        }
        controllerFPretragaOtpremnica.otvoriFormuZaPretraguOtpremnica(fMain);
    }

    void pozoviPostavljanjeOtpremnice(Otpremnica otpremnica) {
        if (controllerFIzdavanjeNosnje == null) {
            controllerFIzdavanjeNosnje = new ControllerFIzdavanjeNosnje();
        }
        controllerFIzdavanjeNosnje.postaviOtpremnicu(otpremnica);
    }

}
