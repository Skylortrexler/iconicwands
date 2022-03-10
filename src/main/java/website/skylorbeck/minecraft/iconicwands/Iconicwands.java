package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;

public class Iconicwands implements ModInitializer {
    public static Parts parts = new Parts();
    static {
        parts.addCores(
                new Parts.Core(
                        "minecraft:stick",
                        1,
                        10,
                        5,
                        10,
                        10
                ),new Parts.Core(
                        "minecraft:blaze_rod",
                        1,
                        5,
                        10,
                        20,
                        10
                ),new Parts.Core(
                        "minecraft:end_rod",
                        5,
                        5,
                        50,
                        10,
                        10
                )
        );
        parts.addTips(
                new Parts.Tip(
                        "minecraft:glow_berries",
                        1,
                        5,
                        50,
                        1
                ),
                new Parts.Tip(
                        "minecraft:ghast_tear",
                        1,
                        75,
                        25,
                        2
                ),
                new Parts.Tip(
                        "minecraft:shulker_shell",
                        2,
                        25,
                        75,
                        1.5f
                )
        );
        parts.addHandles(
                new Parts.Handle(
                        "minecraft:iron_nugget",
                        1,
                        10,
                        50
                ),
                new Parts.Handle(
                        "minecraft:gold_nugget",
                        1,
                        5,
                        25
                ),
                new Parts.Handle(
                        "minecraft:shulker_shell",
                        2,
                        10,
                        50
                )
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
        Registrar.regBlock("wand_bench_",Declarar.WAND_BENCH,MODID);
        Registrar.regItem("wand_bench_",Declarar.WAND_BENCH_ITEM,MODID);
        Declarar.WANDING = ScreenHandlerRegistry.registerSimple(Iconicwands.getId("wand_crafting"), WandBenchScreenHandler::new);
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

    //todo crafting book
    //todo balance
    //todo model for bench
    //todo projectile
    //todo particles

    //todo rewrite nbt storage to use bitmasking
}
