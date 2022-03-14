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
    static {
        // 20 points
        //Stat Name || Base stat || per point spent
        //rate      10 -1
        //delay     10 -1
        //amount    5  +5
//firerate/cooldown 20 -5
        //range     10 +10

        parts.addCores(
                new Parts.Core(//average
                        "minecraft:stick",
                        5,
                        5,
                        5,
                        2,
                        3
                ),
                new Parts.Core(//always takes 5s to recharge but has zero delay
                        "minecraft:blaze_rod",
                        5,
                        10,
                        1,
                        3,
                        1
                ),
                new Parts.Core(//long range, high recharge amount
                        "minecraft:end_rod",
                        0,
                        0,
                        9,
                        2,
                        9
                ),
                new Parts.Core(//low recharge amount, rapid recharge rate
                        "minecraft:bone",
                        10,
                        5,
                        0,
                        1,
                        4
                ),
                new Parts.Core(//fast recharge
                        "minecraft:copper_ingot",
                        7,
                        7,
                        1,
                        2,
                        3
                ),
                new Parts.Core(//high firerate
                        "minecraft:amethyst_shard",
                        6,
                        6,
                        0,
                        4,
                        4
                ),
                new Parts.Core(//instant recharge , low firerate
                        "minecraft:bamboo",
                        10,
                        10,
                        0,
                        -2,
                        2
                ),
                new Parts.Core(//joke, all range
                        "minecraft:carrot",
                        0,
                        0,
                        0,
                        0,
                        20
                )
        );
        // 20 points
        //Stat Name || Base stat || per point spent
        //speed      0.5f  +0.05f
        //amount     0     +5
        //cost       50    -5
        //divergence 2f    -0.1f
        //crit       0.05f +0.01f
        parts.addTips(
                new Parts.Tip(//average
                        "minecraft:glow_berries",
                        -5,
                        5,
                        10,
                        15,
                        0
                ),
                new Parts.Tip(//high recharge amount
                        "minecraft:ghast_tear",
                        -2,
                        15,
                        5,
                        0,
                        2
                ),
                new Parts.Tip(//low crit, low divergence, medium mana cost and recharge
                        "minecraft:shulker_shell",
                        4,
                        5,
                        5,
                        8,
                        -2
                ),
                new Parts.Tip(//low mana cost, medium speed and recharge
                        "minecraft:quartz",
                        5,
                        5,
                        10,
                        0,
                        0
                ),
                new Parts.Tip(//high recharge amount
                        "minecraft:emerald",
                        0,
                        20,
                        0,
                        4,
                        -4
                ),
                new Parts.Tip(//high crit
                        "minecraft:lapis_lazuli",
                        0,
                        5,
                        0,
                        5,
                        10
                ),
                new Parts.Tip(//high speed
                        "minecraft:redstone_block",
                        15,
                        2,
                        0,
                        5,
                        -2
                ),
                new Parts.Tip(//joke, low mana cost
                        "minecraft:potato",
                        0,
                        11,
                        9,
                        0,
                        0
                )
        );
        //10 points
        //Stat Name || Base stat || per point spent
        //damage   1   +1
        //cooldown 20  -5
        //cost     50  -5
        //crit     0   +0.01f
        parts.addHandles(
                new Parts.Handle(//average, damage focus
                        "minecraft:gold_ingot",
                        4,
                        3,
                        2,
                        1
                ),
                new Parts.Handle(//low mana cost
                        "minecraft:iron_ingot",
                        2,
                        2,
                        5,
                        1
                ),
                new Parts.Handle(//higher crit
                        "minecraft:shulker_shell",
                        3,
                        2,
                        0,
                        5
                ),
                new Parts.Handle(//firerate
                        "minecraft:leather",
                        2,
                        4,
                        2,
                        2
                ),
                new Parts.Handle(
                        "minecraft:kelp",
                        1,
                        0,
                        4,
                        5
                )
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

        Registrar.regItem("wand_tester_",Declarar.WAND_TESTER,MODID);
        Registrar.regItem("wand_preset_tester_",Declarar.WAND_PRESET_TESTER,MODID);

//        Registrar.regItem("overworld_wand_preset_",Declarar.OVERWORLD_WAND_REI,MODID);
//        Registrar.regItem("nether_wand_preset_",Declarar.NETHER_WAND_REI,MODID);
//        Registrar.regItem("end_wand_preset_",Declarar.END_WAND_REI,MODID);
//        Registrar.regItem("food_wand_preset_",Declarar.FOOD_WAND_REI,MODID);
//        Registrar.regItem("forest_wand_preset_",Declarar.FOREST_WAND_REI,MODID);
//        Registrar.regItem("magus_wand_preset_",Declarar.MAGUS_WAND_REI,MODID);
//        Registrar.regItem("boomstick_wand_preset_",Declarar.BOOMSTICK_WAND_REI,MODID);

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
