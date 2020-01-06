/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;

import domain.Otpremnica;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class OtpremniceTableModel extends AbstractTableModel{

    List<Otpremnica> lista;
    private String[] nazivi = new String[]{"Sifra otpremnice", "Datum", "Aktivna", "Clan"};

    public OtpremniceTableModel(List<Otpremnica> lista) {
        this.lista = lista;
    }
    
    
    
    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return nazivi.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Otpremnica o = lista.get(rowIndex);
        switch(columnIndex){
            case 0: return o.getSifraOtpremnice();
            case 1: return o.getDatumKreiranja();
            case 2: if(o.isAktivna()){
                return "DA";
            }else{
                return "NE";
            }
            case 3: return o.getClan().getIme() + " " + o.getClan().getPrezime();
            default: return "n/a";
        }
    }

    public List<Otpremnica> getLista() {
        return lista;
    }

    @Override
    public String getColumnName(int column) {
        return nazivi[column];
    }
    
    public void azuriraj(List<Otpremnica> lista) {
        this.lista = lista;
        fireTableDataChanged();
    }
    
}
