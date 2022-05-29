package com.leon.estimate_new.tables;

import androidx.room.Ignore;

public class SecondForm {
    public int khakiAb;
    public int khakiFazelab;
    public int asphalutAb;
    public int asphalutFazelab;
    public int sangFarshAb;
    public int sangFarshFazelab;
    public int otherAb;
    public int otherFazelab;
    public String jenseLoole;
    public int noeMasraf;
    public String qotreLoole;
    public String noeMasrafString;
    public int vaziatNasbePomp;
    public int omqeZirzamin;
    public boolean etesalZirzamin;
    public int omqFazelab;
    public boolean chahAbBaran;

    public boolean ezhaNazarA;
    public boolean ezhaNazarF;
    public int qotreLooleI;
    public int jenseLooleI;
    public boolean looleA;
    public boolean looleF;

    public String chahDescription;
    public String masrafDescription;
    @Ignore
    public String eshterak;

    public SecondForm(int khakiAb, int khakiFazelab, int asphalutAb, int asphalutFazelab,
                      int sangFarshAb, int sangFarshFazelab, int otherAb, int otherFazelab,
                      String qotreLoole, String jenseLoole, int noeMasraf, String noeMasrafString,
                      int vaziatNasbePomp, int omqeZirzamin, boolean etesalZirzamin,
                      int omqFazelab, boolean chahAbBaran, boolean ezhaNazarA, boolean ezhaNazarF,
                      int qotreLooleI, int jenseLooleI, boolean looleA, boolean looleF,
                      String masrafDescription, String chahDescription, String eshterak) {

        this.ezhaNazarA = ezhaNazarA;
        this.ezhaNazarF = ezhaNazarF;
        this.qotreLooleI = qotreLooleI;
        this.jenseLooleI = jenseLooleI;
        this.looleA = looleA;
        this.looleF = looleF;

        this.khakiAb = khakiAb;
        this.khakiFazelab = khakiFazelab;
        this.asphalutAb = asphalutAb;
        this.asphalutFazelab = asphalutFazelab;
        this.sangFarshAb = sangFarshAb;
        this.sangFarshFazelab = sangFarshFazelab;
        this.otherAb = otherAb;
        this.otherFazelab = otherFazelab;
        this.qotreLoole = qotreLoole;
        this.jenseLoole = jenseLoole;
        this.noeMasraf = noeMasraf;
        this.noeMasrafString = noeMasrafString;
        this.vaziatNasbePomp = vaziatNasbePomp;
        this.omqeZirzamin = omqeZirzamin;
        this.omqFazelab = omqFazelab;
        this.etesalZirzamin = etesalZirzamin;
        this.chahAbBaran = chahAbBaran;

        this.chahDescription = chahDescription;
        this.masrafDescription = masrafDescription;
        this.eshterak = eshterak;

    }
}
