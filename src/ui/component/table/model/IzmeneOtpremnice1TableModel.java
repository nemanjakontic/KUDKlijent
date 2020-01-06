/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;

import domain.IzmeneOtpremnice;
import domain.Nosnja;
import domain.Otpremnica;
import domain.StavkaOtpremnice;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class IzmeneOtpremnice1TableModel extends AbstractTableModel{

    private Otpremnica otpremnica;
    private String[] nazivi = new String[]{"Redni broj", "Naziv nosnje", "Vrsta nosnje", "Datum izmene", "Sifra nosnje"};

    public IzmeneOtpremnice1TableModel(Otpremnica otpremnica) {
        this.otpremnica = otpremnica;
    }
    
    @Override
    public int getRowCount() {
        if(otpremnica != null){
            return otpremnica.getIzmenaOtpremnice().size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return nazivi.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IzmeneOtpremnice o = otpremnica.getIzmenaOtpremnice().get(rowIndex);
        switch(columnIndex){
            case 0: return o.getRb();
            case 1: return o.getNosnja().getNazivNosnje();
            case 2: return o.getNosnja().getVrstaNosnje();
            case 3: return o.getDatumIzmene();
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

    public void addIzmena(Nosnja n) {
        IzmeneOtpremnice izmena = new IzmeneOtpremnice();
        izmena.setNosnja(n);
        izmena.setDatumIzmene(new Date());
        
        izmena.setRb(otpremnica.getIzmenaOtpremnice().size() + 1);
        otpremnica.getIzmenaOtpremnice().add(izmena);
        fireTableDataChanged();
    }
    
}
