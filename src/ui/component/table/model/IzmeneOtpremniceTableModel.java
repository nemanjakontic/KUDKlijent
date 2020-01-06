/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;

import domain.IzmeneOtpremnice;
import domain.Otpremnica;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class IzmeneOtpremniceTableModel extends AbstractTableModel{
    private Otpremnica o;
    private String[] nazivi = new String[]{"Redni broj", "Datum izmene"};

    public IzmeneOtpremniceTableModel(Otpremnica o) {
        this.o = o;
    }

    @Override
    public int getRowCount() {
        return o.getIzmenaOtpremnice().size();
    }

    @Override
    public int getColumnCount() {
        return nazivi.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IzmeneOtpremnice iz = o.getIzmenaOtpremnice().get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        switch(columnIndex){
            case 0: return iz.getRb();
            case 1: return sdf.format(iz.getDatumIzmene());
            default: return "n/a";
        }
    }

    public Otpremnica getO() {
        return o;
    }

    @Override
    public String getColumnName(int column) {
        return nazivi[column];
    }
    
    public void addIzmena() {
        IzmeneOtpremnice stavka = new IzmeneOtpremnice();
        stavka.setDatumIzmene(new Date());
        stavka.setRb(o.getIzmenaOtpremnice().size() + 1);
        o.getIzmenaOtpremnice().add(stavka);
        fireTableDataChanged();
    }

    public void izbrisiSve() {
        List<IzmeneOtpremnice> lista = o.getIzmenaOtpremnice();
        lista.removeAll(lista);
        fireTableDataChanged();
    }

    
    
    
}
