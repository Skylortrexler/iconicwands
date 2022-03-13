package website.skylorbeck.minecraft.iconicwands.config;

import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

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
        public Tip(String identifier, float speed, int rechargeAmount, int manaCost, float divergence, float criticalChance, int red) {
            this.identifier = identifier;
            this.speed = speed;
            this.rechargeAmount = rechargeAmount;
            this.manaCost = manaCost;
            this.divergence = divergence;
            this.criticalChance = criticalChance;
            this.red = red;
        }

        String identifier;
        float speed;
        int rechargeAmount;
        int manaCost;
        float divergence;
        float criticalChance;
        int red;

        public String getIdentifier() {
            return identifier;
        }

        public float getSpeed() {
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

        public float getCriticalChance() {
            return criticalChance;
        }

        public int getRed() {
            return red;
        }
    }

    public static class Core {
        public Core(String identifier, int rechargeRate, int rechargeDelay, int rechargeAmount, int firerate, int range, int green) {
            this.identifier = identifier;
            this.rechargeRate = rechargeRate;
            this.rechargeDelay = rechargeDelay;
            this.rechargeAmount = rechargeAmount;
            this.firerate = firerate;
            this.range = range;
            this.green = green;
        }

        String identifier;
        int rechargeRate;
        int rechargeDelay;
        int rechargeAmount;
        int firerate;
        int range;
        int green;

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

        public int getGreen() {
            return green;
        }
    }

    public static class Handle {
        public Handle(String identifier, float damage, int firerate, int manaCost, float criticalChance, int blue) {
            this.identifier = identifier;
            this.damage = damage;
            this.firerate = firerate;
            this.manaCost = manaCost;
            this.criticalChance = criticalChance;
            this.blue = blue;
        }

        String identifier;
        float damage;
        int firerate;
        int manaCost;
        float criticalChance;
        int blue;

        public String getIdentifier() {
            return identifier;
        }

        public float getDamage() {
            return damage;
        }

        public int getFirerate() {
            return firerate;
        }

        public int getManaCost() {
            return manaCost;
        }

        public float getCriticalChance() {
            return criticalChance;
        }

        public int getBlue() {
            return blue;
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
            return IconicWand.partsToInt(tip,core,handle);
        }

    }
}

