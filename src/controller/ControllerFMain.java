/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.enumeracije.FormMode;
import domain.enumeracije.FormModeVrstaNosnje;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.form.FMain;

/**
 *
 * @author Nemanja
 */
public class ControllerFMain {
   
    private FMain fMain;
    
    public ControllerFMain() {
    }

    void otvoriFormuFMain() {
        fMain = new FMain();
        addListenersFmain();
        fMain.setVisible(true);
    }

    private void addListenersFmain() {
        fMain.addButtonNoviClanListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().inicijalizujKonstruktorKontroleraNoviClan();
                GUICoordinator.getInstance().otvoriNovogClana(fMain, FormMode.FORM_ADD);
                System.out.println("otvaranje forme za unos clana");
            }
        });
        
        fMain.addButtonPretragaClanovaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().otvoriPretraguClana(fMain);
                System.out.println("otvaranje forme za pretragu clana");
            }
        });
        
        fMain.addButtonDodajNosnjuListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().otvoriNovuNosnju(fMain, FormModeVrstaNosnje.FORM_POCETNO, FormMode.FORM_ADD);
                System.out.println("otvaranje forme za unos nosnje");
            }
        });
        
        fMain.addButtonPretragaNosnjeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().otvoriPretraguNosnje(fMain);
                System.out.println("otvaranje forme za prikaz nosnje");
            }
        });
        
        fMain.addButtonIzdavanjeNosnjeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().otvoriIzdavanjeNosnje(fMain, FormMode.FORM_ADD);
                System.out.println("otvaranje forme za izdavanje nosnje");
            }
        });
        
        fMain.addButtonPretragaOtpremnicaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICoordinator.getInstance().otvoriPretraguOtpremnica(fMain);
            }
        });
        
    }
    
    
    
}
