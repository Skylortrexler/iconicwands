package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;

public class Iconicwands implements ModInitializer {
    public static Parts parts = new Parts();
    public enum Presets{
        overworld(new Parts.WandCluster(Iconicwands.parts.tips.get(6),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(8))),
        nether(new Parts.WandCluster(Iconicwands.parts.tips.get(12),Iconicwands.parts.cores.get(8),Iconicwands.parts.handles.get(9))),
        end(new Parts.WandCluster(Iconicwands.parts.tips.get(18),Iconicwands.parts.cores.get(14),Iconicwands.parts.handles.get(15))),
        food(new Parts.WandCluster(Iconicwands.parts.tips.get(8),Iconicwands.parts.cores.get(7),Iconicwands.parts.handles.get(0))),
        forest(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0))),
        magus(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0))),
        rapid(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0))),
        scarlet(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0))),
        kynan(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0))),
        boomstick(new Parts.WandCluster(Iconicwands.parts.tips.get(0),Iconicwands.parts.cores.get(0),Iconicwands.parts.handles.get(0)));

        final Parts.WandCluster wand;
        Presets(Parts.WandCluster wand){
            this.wand = wand;
        }

        public Parts.WandCluster getWand() {
            return wand;
        }
        public int getWandInt(){
            return wand.getInt();
        }
    }
    static {

        //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
        //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
        //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6

        //Stat Name || Base stat || per point spent
        //rate      10 -1
        //delay     10 -1
        //amount    5  +5
//firerate/cooldown 20 -5
        //range     10 +10

        parts.addCores(
                //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
                new Parts.Core("minecraft:oak_planks",6,2,0,2,0),//0
                new Parts.Core("minecraft:spruce_planks",3,3,0,2, 2),
                new Parts.Core("minecraft:birch_planks",2,2,6,0,0),//2
                new Parts.Core("minecraft:jungle_planks",5,5,0,0,0),
                new Parts.Core("minecraft:acacia_planks",2,2,0,3,3),//4
                new Parts.Core("minecraft:dark_oak_planks",0,0,5,0,5),
                new Parts.Core("minecraft:bone",10,5,0,-5,0),//6
                new Parts.Core("minecraft:carrot",2,2,2,2,2),

                //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
                new Parts.Core("minecraft:blaze_rod",5,10,0,5,0),//8
                new Parts.Core("minecraft:amethyst_shard",5,5,0,5,5),
                new Parts.Core("minecraft:bamboo",10,10,0,0,0),//10
                new Parts.Core("minecraft:crimson_planks",5,5,0,10,0),
                new Parts.Core("minecraft:warped_planks",0,0,10,0,10),//12
//                new Parts.Core("minecraft:copper_ingot",7,7,1,2,3),

                //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6
                new Parts.Core("minecraft:end_rod",0,0,5,5,10),//14
                new Parts.Core("minecraft:golden_carrot",6,6,6,6,6)

        );
        //Stat Name || Base stat || per point spent
        //speed      0.5f  +0.05f
        //amount     0     +5
        //cost       50    -5
        //divergence 2f    -0.1f
        //crit       0.05f +0.01f
        parts.addTips(
                //t1 5/5 | 6/2/2 | 3/3/2/2 | 2/2/2/2/2
                new Parts.Tip("minecraft:oak_planks",3,2,0,2,3),//0
                new Parts.Tip("minecraft:spruce_planks",3,3,0,2, 2),
                new Parts.Tip("minecraft:birch_planks",2,2,3,3,0),//2
                new Parts.Tip("minecraft:jungle_planks",2,3,2,0,3),
                new Parts.Tip("minecraft:acacia_planks",2,2,0,3,3),//4
                new Parts.Tip("minecraft:dark_oak_planks",0,2,3,2,3),
                new Parts.Tip("minecraft:glow_berries",3,3,2,2,0),//6
                new Parts.Tip("minecraft:lapis_lazuli",0,0,0,0,5),
                new Parts.Tip("minecraft:potato",0,0,5,0,0),//8
                new Parts.Tip("minecraft:gold_nugget",0,5,5,0,0),
                new Parts.Tip("minecraft:redstone",6,2,0,2,0),//10
                new Parts.Tip("minecraft:glass",2,0,2,0,6),

                //t2 10/10 | 10/5/5 | 5/5/5/5 | 4/4/4/4/4
                new Parts.Tip("minecraft:ghast_tear",5,0,5,5,5),//12
                new Parts.Tip("minecraft:quartz",5,5,10,0,0 ),
                new Parts.Tip("minecraft:emerald",0,5,5,0,10),//14
                new Parts.Tip("minecraft:redstone_block",10,0,0,10,0),
                new Parts.Tip("minecraft:crimson_planks",5,5,0,10,0),//16
                new Parts.Tip("minecraft:warped_planks",0,0,10,0,10),

                //t3 20/10 | 10/10/10 | 10/10/5/5 | 6/6/6/6/6
                new Parts.Tip("minecraft:shulker_shell",10,0,0,20,0),//18
                new Parts.Tip("minecraft:prismarine_crystals",0,0,0,0,0),
                new Parts.Tip("minecraft:nether_star",0,0,0,0,0)//20
        );
        // 5 points per level
        //Stat Name || Base stat || per point spent
        //damage   1   +1
        //cooldown 20  -5
        //cost     50  -5
        //crit     0   +0.01f
        parts.addHandles(
                //t1 3/2 | 3/1/1 | 2/1/1/1
                new Parts.Handle("minecraft:oak_planks",2,1,1,1),//0
                new Parts.Handle("minecraft:spruce_planks",1,2,1, 1),
                new Parts.Handle("minecraft:birch_planks",1,1,2,1),//2
                new Parts.Handle("minecraft:jungle_planks",1,1,1,2),
                new Parts.Handle("minecraft:acacia_planks",1,3,0,1),//4
                new Parts.Handle("minecraft:dark_oak_planks",1,0,3,1),
                new Parts.Handle("minecraft:kelp",0,2,3,0),//6
                new Parts.Handle("minecraft:glass",3,0,0,2),

                //t2 5/5 | 6/2/2 | 3/3/2/2
                new Parts.Handle("minecraft:gold_ingot",3,3,2,2),//8
                new Parts.Handle("minecraft:iron_ingot",2,2,3,3),
                new Parts.Handle("minecraft:leather",2,6,2,0),//10
                new Parts.Handle("minecraft:quartz",5,0,5,0),
                new Parts.Handle("minecraft:emerald",2,0,2,6),//12
                new Parts.Handle("minecraft:crimson_planks",5,5,0,0),
                new Parts.Handle("minecraft:warped_planks",0,0,5,5),//14

                //t3 10/5 | 6/6/3 | 6/3/3/3
                new Parts.Handle("minecraft:shulker_shell",3,6,6,0),
                new Parts.Handle("minecraft:prismarine_shard",5,0,10,0)//16
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regBlock("timed_light_",Declarar.TIMED_LIGHT,MODID);
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
        Registrar.regBlock("wand_bench_",Declarar.WAND_BENCH,MODID);
        Registrar.regItem("wand_bench_",Declarar.WAND_BENCH_ITEM,MODID);
        Registrar.regBlock("wand_pedestal_",Declarar.WAND_PEDESTAL,MODID);
        Registrar.regItem("wand_pedestal_",Declarar.WAND_PEDESTAL_ITEM,MODID);
        Registrar.regBlock("wand_display_pedestal_",Declarar.WAND_PEDESTAL_DISPLAY,MODID);
        Registrar.regItem("wand_display_pedestal_",Declarar.WAND_PEDESTAL_DISPLAY_ITEM,MODID);

        Registrar.regItem("wand_tester_",Declarar.WAND_TESTER,MODID);
        Registrar.regItem("wand_preset_tester_",Declarar.WAND_PRESET_TESTER,MODID);

        Declarar.WANDING = ScreenHandlerRegistry.registerSimple(Iconicwands.getId("wand_crafting"),((syncId, inventory) -> new WandBenchScreenHandler(syncId,inventory,new SimpleInventory(4))));

        try {
            parts = ConfigFileHandler.initConfigFile("iconic_wands.json",parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String MODID = "iconicwands";

    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }


}

//todo Fix REI related crash?
//todo redo iconics