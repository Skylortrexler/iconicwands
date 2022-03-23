package website.skylorbeck.minecraft.iconicwands.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.fabric.mixin.resource.loader.ResourcePackManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.iconicwands.Color;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjetileEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipComponent;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipData;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        ResourceManagerHelperImpl.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleResourceReloadListener<>() {

            @Override
            public Identifier getFabricId() {
                return Iconicwands.getId("assets");
            }

            @Override
            public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
                String path = "cache/iconicwands/parts/";
                if (!Files.exists(Paths.get(path))) {
                    try {
                        Files.createDirectories(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < Iconicwands.parts.cores.size(); i++) {
                    Parts.Core part = Iconicwands.parts.cores.get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(path+sourceItem.getPath()+"_core.png"))) {
                        continue;
                    }
                    
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, path, sourceItem, sourceTexture,"core");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, path, sourceItem, sourceTexture,"core");
                        } catch (IOException ignored) {
                        }
                    }
                }
                for (int i = 0; i < Iconicwands.parts.tips.size(); i++) {
                    Parts.Tip part = Iconicwands.parts.tips.get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(path+sourceItem.getPath()+"_tip.png"))) {
                        continue;
                    }
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, path, sourceItem, sourceTexture,"tip");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, path, sourceItem, sourceTexture,"tip");
                        } catch (IOException ignored) {
                        }
                    }
                }

                for (int i = 0; i < Iconicwands.parts.handles.size(); i++) {
                    Parts.Handle part = Iconicwands.parts.handles.get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(path+sourceItem.getPath()+"_handle.png"))) {
                        continue;
                    }
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, path, sourceItem, sourceTexture,"handle");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, path, sourceItem, sourceTexture,"handle");
                        } catch (IOException ignored) {
                        }
                    }
                }

                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> apply(Object data, ResourceManager manager, Profiler profiler, Executor executor) {
//                ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER.register(new );
                //todo inject textures
                return CompletableFuture.completedFuture(null);
            }
        });
    }

    private void writeImage(ResourceManager manager, String path, Identifier sourceItem, Identifier sourceTexture,String part) throws IOException {
//        Logger.getGlobal().log(Level.INFO, "Writing image for " + sourceItem.toString());
        ArrayList<Color> colors = new ArrayList<>();
        ArrayList<Color> greys = new ArrayList<>();
        NativeImage nativeImage = NativeImage.read(manager.getResource(sourceTexture).getInputStream());
        BufferedImage template = ImageIO.read(manager.getResource(Iconicwands.getId("textures/template/"+part+".png")).getInputStream());
        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                Color color = Color.ofTransparent(template.getRGB(x,y));
                if (!greys.contains(color) && color.getAlpha()!=0)
                    greys.add(color);
            }
        }
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                Color color = Color.ofTransparent(nativeImage.getColor(x,y));
                if (!colors.contains(color) && color.getAlpha()!=0)
                   colors.add(color);
            }
        }
        Comparator<Color> comparator = (a, b) -> Float.compare(a.getRed()*0.21f + a.getBlue()*0.07f + a.getGreen()*0.72f, b.getRed()*0.21f + b.getBlue()*0.07f + b.getGreen()*0.72f);
        colors.sort(comparator);
        greys.sort(comparator);

        HashMap<Color, Color> map = new HashMap<>();
        for (int i = 0; i < greys.size(); i++) {
            map.put(greys.get(i),colors.get(i));
        }

        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                Color color = Color.ofTransparent(template.getRGB(x,y));
                if (color.getAlpha()!=0){
                    Color newColor = map.get(color);
                    template.setRGB(x,y,Color.ofRGBA(newColor.getBlue(),newColor.getGreen(),newColor.getRed(),newColor.getAlpha()).getColor());
                }
            }
        }
        ImageIO.write(template,"png",new File(path+sourceItem.getPath()+"_"+part+".png"));
    }
}