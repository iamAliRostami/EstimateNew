package com.leon.estimate_new.tables;

import java.util.ArrayList;
import java.util.List;

public class Arzeshdaraei {
    public ArrayList<Block> blocks;
    public ArrayList<Formula> formulas;
    public ArrayList<Zarib> zaribs;

    public Arzeshdaraei() {
        this.blocks = new ArrayList<>();
        this.formulas = new ArrayList<>();
        this.zaribs = new ArrayList<>();
    }

    public Arzeshdaraei(List<Block> blocks, List<Formula> formulas, List<Zarib> zaribs) {
        this.blocks = new ArrayList<>();
        this.blocks.addAll(blocks);
        this.formulas = new ArrayList<>();
        this.formulas.addAll(formulas);
        this.zaribs = new ArrayList<>();
        this.zaribs.addAll(zaribs);
    }
}
