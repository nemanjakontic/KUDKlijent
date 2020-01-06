/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;


import domain.Nosnja;

import domain.Otpremnica;
import domain.StavkaOtpremnice;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class StavkeOtpremniceTableModel extends AbstractTableModel{

    private Otpremnica otpremnica;
    private String[] nazivi = new String[]{"Redni broj", "Naziv nosnje", "Vrsta nosnje", "Kolicina", "Sifra nosnje"};

    public StavkeOtpremniceTableModel(Otpremnica otpremnica) {
        this.otpremnica = otpremnica;
    }
    
    
    
    @Override
    public int getRowCount() {
        if(otpremnica != null){
            return otpremnica.getStavkaOtpremnice().size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return nazivi.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StavkaOtpremnice o = otpremnica.getStavkaOtpremnice().get(rowIndex);
        switch(columnIndex){
            case 0: return o.getRedniBroj();
            case 1: return o.getNosnja().getNazivNosnje();
            case 2: return o.getNosnja().getVrstaNosnje();
            case 3: return o.getKolicina();
            case 4: return o.getNosnja().getSifraNosnje();
            default: return "n/a";
            }
        }

    @Override
    public String getColumnName(int column) {
        return nazivi[column];
    }

    public Otpremnica getOtpremnica() {
        return otpremnica;
    }

    public void addNosnja(Nosnja n, Long kolicina) {
        StavkaOtpremnice stavka = new StavkaOtpremnice();
        stavka.setNosnja(n);
        stavka.setKolicina(kolicina);
        stavka.setRedniBroj(otpremnica.getStavkaOtpremnice().size() + 1);
        otpremnica.getStavkaOtpremnice().add(stavka);
        fireTableDataChanged();
    }
    
    private void setRedneBrojeve() {
        int redniBroj = 0;
        for (StavkaOtpremnice stavka : otpremnica.getStavkaOtpremnice()) {
            stavka.setRedniBroj(++redniBroj);
        }
    }

    public void removeNosnja(int selectedRow) {
        otpremnica.getStavkaOtpremnice().remove(selectedRow);
        setRedneBrojeve();
        fireTableDataChanged();
    }

    
    
}
    

