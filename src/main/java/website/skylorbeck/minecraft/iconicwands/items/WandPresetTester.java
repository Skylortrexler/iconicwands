package website.skylorbeck.minecraft.iconicwands.items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WandPresetTester extends Item {
    public WandPresetTester(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) {
            for (Iconicwands.Presets wand : Iconicwands.Presets.values()) {
                ItemStack stack = new ItemStack(Declarar.ICONIC_WAND);
                IconicWand.saveComponents(stack, wand.getWand());
                user.getInventory().offerOrDrop(stack);
            }
            user.sendMessage(Text.of(Iconicwands.Presets.values().length + " Wands have been placed in your inventory."), false);
        } else {
            if (world.isClient) {
                //pregenerate wand images
                try {
                    Files.createDirectories(Paths.get("cache/generatedWands/presets/"));
                    Files.createDirectories(Paths.get("cache/generatedWands/random/"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (Iconicwands.Presets preset : Iconicwands.Presets.values()) {
                    Parts.WandCluster wand = preset.getWand();
                    try {
                        generateWandImage(wand).writeTo(Paths.get("cache/generatedWands/presets/" + preset.name() + ".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                user.sendMessage(Text.of(Iconicwands.Presets.values().length + " Images saved to cache/generatedWands/presets/"), false);
                for (int i = 0; i < 100 ; i++) {
                    Parts.WandCluster wand = new Parts.WandCluster(Iconicwands.parts.getAllTips().get(world.random.nextInt(Iconicwands.parts.getAllTips().size())),Iconicwands.parts.getAllCores().get(world.random.nextInt(Iconicwands.parts.getAllCores().size())),Iconicwands.parts.getAllHandles().get(world.random.nextInt(Iconicwands.parts.getAllHandles().size())));
                    try {
                        generateWandImage(wand).writeTo(Paths.get("cache/generatedWands/random/wand_" + i + ".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                user.sendMessage(Text.of("100 Images saved to cache/generatedWands/random/"), false);

            }
        }

        return super.use(world, user, hand);
    }

    private NativeImage generateWandImage(Parts.WandCluster wand) throws IOException {
        InputStream inputStream;
        try {
            inputStream = MinecraftClient.getInstance().getResourceManager().getResource(Iconicwands.getId("textures/item/core/" + wand.getCore().getIdentifier().substring(10)+".png")).get().getInputStream();
        } catch (IOException ignored) {
            inputStream = Files.newInputStream(Paths.get("cache/assets/iconicwands/textures/item/core/" + wand.getCore().getIdentifier().substring(10) + ".png"));
        }
        NativeImage presetImage = NativeImage.read(inputStream);
        try {
            inputStream = MinecraftClient.getInstance().getResourceManager().getResource(Iconicwands.getId("textures/item/handle/" + wand.getHandle().getIdentifier().substring(10)+".png")).get().getInputStream();
        } catch (IOException ignored) {
            inputStream = Files.newInputStream(Paths.get("cache/assets/iconicwands/textures/item/handle/" + wand.getHandle().getIdentifier().substring(10) + ".png"));
        }
        NativeImage tempPart = NativeImage.read(inputStream);
        for (int x = 0; x < tempPart.getWidth(); x++) {
            for (int y = 0; y < tempPart.getHeight(); y++) {
                if (tempPart.getColor(x, y) != 0)
                    presetImage.setColor(x, y, tempPart.getColor(x, y));
            }
        }
        try {
            inputStream = MinecraftClient.getInstance().getResourceManager().getResource(Iconicwands.getId("textures/item/tip/" + wand.getTip().getIdentifier().substring(10)+".png")).get().getInputStream();
        } catch (IOException ignored) {
            inputStream = Files.newInputStream(Paths.get("cache/assets/iconicwands/textures/item/tip/" + wand.getTip().getIdentifier().substring(10) + ".png"));
        }
        tempPart = NativeImage.read(inputStream);
        for (int x = 0; x < tempPart.getWidth(); x++) {
            for (int y = 0; y < tempPart.getHeight(); y++) {
                if (tempPart.getColor(x, y) != 0)
                    presetImage.setColor(x, y, tempPart.getColor(x, y));
            }
        }
        return presetImage;
    }
}
