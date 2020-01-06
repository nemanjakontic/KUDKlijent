/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;


import domain.Nosnja;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class NosnjaTableModel extends AbstractTableModel{

    List<Nosnja> lista;
    String[] nazivi = new String[]{"Sifra nosnje", "Vrsta nosnje", "Pol", "Naziv"/*, "Vrsta beline", "Vrsta opanaka", "Velicina"*/};

    public NosnjaTableModel(List<Nosnja> lista) {
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
        Nosnja n = lista.get(rowIndex);
        switch(columnIndex){
            case 0: return n.getSifraNosnje();
            case 1: return n.getVrstaNosnje();
            case 2: return n.getPol();
            case 3: return n.getNazivNosnje();
            /*case 4: return n.getVrstaBeline();
            case 5: return n.getVrstaOpanaka();
            case 6: return n.getVelicina();*/
            
            default: return "n/a";
        }
    }
   
    public List<Nosnja> getLista() {
        return lista;
    }

    @Override
    public String getColumnName(int column) {
        return nazivi[column];
    }

    public Nosnja getNosnja(int red) {
        return lista.get(red);
    }

    public void azuriraj(List<Nosnja> lista) {
        this.lista = lista;
        fireTableDataChanged();
    }

    
}
