package website.skylorbeck.minecraft.iconicwands.config;

import net.minecraft.text.TranslatableText;
import website.skylorbeck.minecraft.iconicwands.Color;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parts {
    public ArrayList<Tip> tips = new ArrayList<>();
    public ArrayList<Core> cores = new ArrayList<>();
    public ArrayList<Handle> handles = new ArrayList<>();
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
        public Tip(String identifier, int speed, int rechargeAmount, int manaCost, int divergence, int criticalChance) {
            this.identifier = identifier;
            this.speed = 0.5f + (speed*0.05f);
            this.rechargeAmount = rechargeAmount*5;
            this.manaCost =50-(5*manaCost);
            this.divergence = 2f- (divergence*0.1f);
            this.criticalChance = 0.05f +(0.01f*criticalChance);
            String num = Math.abs(speed)+""+Math.abs(rechargeAmount)+""+Math.abs(manaCost)+""+Math.abs(divergence)+""+Math.abs(criticalChance);
            this.red = Integer.parseInt(num)%255;
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
        public Core(String identifier, int rechargeRate, int rechargeDelay, int rechargeAmount, int firerate, int range) {
            this.identifier = identifier;
            this.rechargeRate = 10 - rechargeRate;
            this.rechargeDelay = 10 - rechargeDelay;
            this.rechargeAmount = 5 + (5 * rechargeAmount);
            this.firerate = 20 - (firerate * 5);
            this.range = 10 + (range * 10);
            String num = Math.abs(rechargeRate)+""+Math.abs(rechargeDelay)+""+Math.abs(rechargeAmount)+""+Math.abs(firerate)+""+Math.abs(range);
            this.green = Integer.parseInt(num)%255;
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
        public Handle(String identifier, int damage, int firerate, int manaCost, int criticalChance) {
            this.identifier = identifier;
            this.damage = 2* (1 + damage);
            this.firerate = 20 -(5*firerate);
            this.manaCost = 50-(5*manaCost);
            this.criticalChance = 0.01f*criticalChance;
            String num = Math.abs(damage)+""+Math.abs(firerate)+""+Math.abs(manaCost)+""+Math.abs(criticalChance);
            this.blue = Integer.parseInt(num)%255;
        }

        String identifier;
        int damage;
        int firerate;
        int manaCost;
        float criticalChance;
        int blue;

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
        String name;
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

