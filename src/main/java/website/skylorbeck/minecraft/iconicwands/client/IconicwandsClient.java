package website.skylorbeck.minecraft.iconicwands.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.*;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjetileEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipComponent;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipData;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreen;
import website.skylorbeck.minecraft.skylorlib.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
public class IconicwandsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistryImpl.register(Declarar.MAGIC_PROJECTILE, MagicProjetileEntityRenderer::new);
        TooltipComponentCallback.EVENT.register(data -> new WandTooltipComponent((WandTooltipData) data));
        ScreenRegistry.register(Declarar.WANDING, WandBenchScreen::new);
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Declarar.WAND_BENCH, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Declarar.WAND_PEDESTAL, RenderLayer.getTranslucent());
        BlockEntityRendererRegistryImpl.register(Declarar.WAND_BENCH_ENTITY, WandBenchEntityRenderer::new);
        BlockEntityRendererRegistryImpl.register(Declarar.WAND_PEDESTAL_ENTITY, WandPedestalEntityRenderer::new);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleResourceReloadListener<Boolean>() {

            @Override
            public Identifier getFabricId() {
                return Iconicwands.getId("resources");
            }

            @Override
            public CompletableFuture<Boolean> load(ResourceManager manager, Profiler profiler, Executor executor) {
                CompletableFuture<Boolean> done = new CompletableFuture<>();
                executor.execute(()-> {
                    Path prepath = Paths.get("cache/assets/iconicwands/");
                    String path = prepath.toString().replace("\\", "/") + "/textures/item";
                    boolean refresh = false;
                    //region textures
//                path = FabricLoader.getInstance().getModContainer("iconicwands").get().findPath("assets/iconicwands/textures/item").get().toString();
                    for (int i = 0; i < Iconicwands.parts.getAllCores().size(); i++) {
                        Parts.Core part = Iconicwands.parts.getAllCores().get(i);
                        Identifier sourceItem = new Identifier(part.getIdentifier());
                        String partPath = path + "/core/" + sourceItem.getPath() + ".png";
                        Identifier sourceTexture = null;
                        if (Files.exists(Paths.get(partPath))) {
                            continue;
                        }
                        refresh = true;
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture, "core");
                        } catch (IOException e) {
                            try {
                                sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                                writeImage(manager, partPath, sourceItem, sourceTexture, "core");
                            } catch (IOException ignored) {
                            }
                        }
                    }
                    for (int i = 0; i < Iconicwands.parts.getAllTips().size(); i++) {
                        Parts.Tip part = Iconicwands.parts.getAllTips().get(i);
                        Identifier sourceItem = new Identifier(part.getIdentifier());
                        String partPath = path + "/tip/" + sourceItem.getPath() + ".png";
                        Identifier sourceTexture = null;
                        if (Files.exists(Paths.get(partPath))) {
                            continue;
                        }
                        refresh = true;
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture, "tip");
                        } catch (IOException e) {
                            try {
                                sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                                writeImage(manager, partPath, sourceItem, sourceTexture, "tip");
                            } catch (IOException ignored) {
                            }
                        }
                    }

                    for (int i = 0; i < Iconicwands.parts.getAllHandles().size(); i++) {
                        Parts.Handle part = Iconicwands.parts.getAllHandles().get(i);
                        Identifier sourceItem = new Identifier(part.getIdentifier());
                        String partPath = path + "/handle/" + sourceItem.getPath() + ".png";
                        Identifier sourceTexture = null;
                        if (Files.exists(Paths.get(partPath))) {
                            continue;
                        }
                        refresh = true;
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture, "handle");
                        } catch (IOException e) {
                            try {
                                sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                                writeImage(manager, partPath, sourceItem, sourceTexture, "handle");
                            } catch (IOException ignored) {
                            }
                        }
                    }
                    done.complete(refresh);
                });
                return done;
            }

            @Override
            public CompletableFuture<Void> apply(Boolean data, ResourceManager manager, Profiler profiler, Executor executor) {
                if (data){
                    executor.execute(()->MinecraftClient.getInstance().reloadResources());
                }
                return CompletableFuture.completedFuture(null);
            }
        });


    }


    private void writeImage(ResourceManager manager, String path, Identifier sourceItem, Identifier sourceTexture, String part) throws IOException {
//        Logger.getGlobal().log(Level.INFO, "Writing image for " + sourceItem.toString());
        ArrayList<Color> colors = new ArrayList<>();
        ArrayList<Color> greys = new ArrayList<>();
        NativeImage nativeImage = NativeImage.read(manager.getResource(sourceTexture).getInputStream());
        NativeImage template = NativeImage.read(manager.getResource(Iconicwands.getId("textures/template/" + part + ".png")).getInputStream());

        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                Color color = Color.ofTransparent(template.getColor(x, y));
                if (!greys.contains(color) && color.getAlpha() != 0)
                    greys.add(color);
            }
        }
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                Color color = Color.ofTransparent(nativeImage.getColor(x, y));
                if (!colors.contains(color) && color.getAlpha() != 0)
                    colors.add(color);
            }
        }
        Comparator<Color> comparator = (a, b) -> Float.compare(a.getRed() * 0.21f + a.getBlue() * 0.07f + a.getGreen() * 0.72f, b.getRed() * 0.21f + b.getBlue() * 0.07f + b.getGreen() * 0.72f);
        colors.sort(comparator);
        greys.sort(comparator);

        HashMap<Color, Color> map = new HashMap<>();
        for (int i = 0; i < greys.size(); i++) {
            map.put(greys.get(i), colors.get(i));
        }

        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                Color color = Color.ofTransparent(template.getColor(x, y));
                if (color.getAlpha() != 0) {
                    Color newColor = map.get(color);
                    template.setColor(x, y, newColor.getColor());
                }
            }
        }
        template.writeTo(path);
    }

}