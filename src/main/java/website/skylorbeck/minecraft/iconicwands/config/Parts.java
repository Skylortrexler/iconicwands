package website.skylorbeck.minecraft.iconicwands.config;

import website.skylorbeck.minecraft.iconicwands.Iconicwands;

import java.util.ArrayList;
import java.util.Arrays;

public class Parts {
    public ArrayList<Tip> tips = new ArrayList<Tip>();
    public ArrayList<Core> cores = new ArrayList<Core>();
    public ArrayList<Handle> handles = new ArrayList<Handle>();
    public void addTips(Tip... tip) {
        tips.addAll(Arrays.asList(tip));
    }
    public void addCores(Core... core) {
        cores.addAll(Arrays.asList(core));
    }
    public void addHandles(Handle... handle) {
        handles.addAll(Arrays.asList(handle));
    }

    public static class Tip {
        public Tip(String identifier, int speed,int rechargeAmount, int manaCost, float divergence) {
            this.identifier = identifier;
            this.speed = speed;
            this.rechargeAmount = rechargeAmount;
            this.manaCost = manaCost;
            this.divergence = divergence;
        }

        String identifier;
        int speed;
        int rechargeAmount;
        int manaCost;
        float divergence;

        public String getIdentifier() {
            return identifier;
        }

        public int getSpeed() {
            return speed;
        }

        public int getRechargeAmount() {
            return rechargeAmount;
        }

        public int getManaCost() {
            return manaCost;
        }

        public float getDivergence() {
            return divergence;
        }
    }

    public static class Core {
        public Core(String identifier, int rechargeRate, int rechargeDelay,int rechargeAmount, int firerate, int range) {
            this.identifier = identifier;
            this.rechargeRate = rechargeRate;
            this.rechargeDelay = rechargeDelay;
            this.rechargeAmount = rechargeAmount;
            this.firerate = firerate;
            this.range = range;
        }

        String identifier;
        int rechargeRate;
        int rechargeDelay;
        int rechargeAmount;
        int firerate;
        int range;

        public String getIdentifier() {
            return identifier;
        }

        public int getRechargeRate() {
            return rechargeRate;
        }

        public int getRechargeDelay() {
            return rechargeDelay;
        }

        public int getRechargeAmount() {
            return rechargeAmount;
        }

        public int getRange() {
            return range;
        }

        public int getFirerate() {
            return firerate;
        }
    }

    public static class Handle {
        public Handle(String identifier, int damage, int firerate, int manaCost) {
            this.identifier = identifier;
            this.damage = damage;
            this.firerate = firerate;
            this.manaCost = manaCost;
        }

        String identifier;
        int damage;
        int firerate;
        int manaCost;

        public String getIdentifier() {
            return identifier;
        }

        public int getDamage() {
            return damage;
        }

        public int getFirerate() {
            return firerate;
        }

        public int getManaCost() {
            return manaCost;
        }

    }
    public static class WandCluster{
        Tip tip;
        Core core;
        Handle handle;
        public WandCluster(Tip tip, Core core, Handle handle) {
            this.tip = tip;
            this.core = core;
            this.handle = handle;
        }

        public Tip getTip() {
            return tip;
        }

        public Core getCore() {
            return core;
        }

        public Handle getHandle() {
            return handle;
        }

        public int getInt(){
            return Integer.parseInt(1 + String.format("%02d", Iconicwands.parts.tips.indexOf(tip)) + String.format("%02d", Iconicwands.parts.cores.indexOf(core)) + String.format("%02d", Iconicwands.parts.handles.indexOf(handle)));
        }
    }
}

