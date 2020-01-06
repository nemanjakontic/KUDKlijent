/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component.table.model;

import domain.Clan;
import domain.enumeracije.Ansambl;
import domain.enumeracije.Pol;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Nemanja
 */
public class ClanTableModel extends AbstractTableModel{

    private List<Clan> clanovi;
    private String[] naziviKolona = new String[]{"BROJ CK", "IME", "PREZIME", "POL", "VISINA", "ANSAMBL", "AKTIVAN"};
    private Class[] klase = new Class[]{Long.class, String.class, String.class, Pol.class, Double.class, Ansambl.class, Boolean.class};
    
    public ClanTableModel(List<Clan> clanovi) {
        this.clanovi = clanovi;
    }
    
    @Override
    public int getRowCount() {
        if(clanovi == null){
            return 0;
        }
        return clanovi.size();
    }

    @Override
    public int getColumnCount() {
        return naziviKolona.length;
    }

    @Override
    public Object getValueAt(int red, int kolona) {
        Clan clan = clanovi.get(red);
        switch(kolona){
            case 0: return clan.getBrojCK();
            case 1: return clan.getIme();
            case 2: return clan.getPrezime();
            case 3: return clan.getPol();
            case 4: return clan.getVisina();
            case 5: return clan.getAnsambl();
            case 6: return clan.isAktivan();
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int kolona) {
        if(kolona > naziviKolona.length){
            return "n/a";
        }
        return naziviKolona[kolona];
    }

    @Override
    public Class<?> getColumnClass(int kolona) {
        return klase[kolona];
    }
    
    public Clan getClan(int index){
        return clanovi.get(index);
    }

    public List<Clan> getClanovi() {
        return clanovi;
    }

    public void azuriraj(List<Clan> lista) {
        clanovi = lista;
        fireTableDataChanged();
    }
    
}
