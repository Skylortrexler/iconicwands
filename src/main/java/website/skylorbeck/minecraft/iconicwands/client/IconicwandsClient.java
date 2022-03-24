package website.skylorbeck.minecraft.iconicwands.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

        ResourceManagerHelperImpl.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleResourceReloadListener<>() {

            @Override
            public Identifier getFabricId() {
                return Iconicwands.getId("assets");
            }

            @Override
            public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
                //region models
                String path = FabricLoader.getInstance().getModContainer("iconicwands").get().findPath("assets/iconicwands/models/item").get().toString();
                JsonObject mainModel = createMainWandRecipe();
                boolean cached = false;
                if (Files.exists(Paths.get(path+"/iconicwand_item.json"))){
                    try {
                        JsonObject json = JsonParser.parseString(Files.readString(Paths.get(path+"/iconicwand_item.json"))).getAsJsonObject();
                        if (mainModel.equals(json)){
                            cached = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!cached) {
                    try {
                        Files.write(Paths.get(path + "/iconicwand_item.json"), mainModel.toString().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int tip = 0; tip < Iconicwands.parts.getAllTips().size(); tip++) {
                        for (int core = 0; core < Iconicwands.parts.getAllCores().size(); core++) {
                            for (int handle = 0; handle < Iconicwands.parts.getAllHandles().size(); handle++) {
                                int index = (tip & 0xFF) << 16 | (core & 0xFF) << 8 | (handle & 0xFF);
                                try {
                                    Files.write(Paths.get(path + "/wand_model_" + index + ".json"), createSubRecipes(tip, core, handle).toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                //endregion
                //region textures
                path = FabricLoader.getInstance().getModContainer("iconicwands").get().findPath("assets/iconicwands/textures/item").get().toString();
                for (int i = 0; i < Iconicwands.parts.getAllCores().size(); i++) {
                    Parts.Core part = Iconicwands.parts.getAllCores().get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    String partPath = path+"/core/"+sourceItem.getPath()+".png";
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(partPath))) {
                        continue;
                    }
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, partPath, sourceItem, sourceTexture,"core");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture,"core");
                        } catch (IOException ignored) {
                        }
                    }
                }
                for (int i = 0; i < Iconicwands.parts.getAllTips().size(); i++) {
                    Parts.Tip part = Iconicwands.parts.getAllTips().get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    String partPath = path+"/tip/"+sourceItem.getPath()+".png";
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(partPath))) {
                        continue;
                    }
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, partPath, sourceItem, sourceTexture,"tip");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture,"tip");
                        } catch (IOException ignored) {
                        }
                    }
                }

                for (int i = 0; i < Iconicwands.parts.getAllHandles().size(); i++) {
                    Parts.Handle part = Iconicwands.parts.getAllHandles().get(i);
                    Identifier sourceItem = new Identifier(part.getIdentifier());
                    String partPath = path+"/handle/"+sourceItem.getPath()+".png";
                    Identifier sourceTexture =null;
                    if (Files.exists(Paths.get(partPath))) {
                        continue;
                    }
                    try {
                        sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "item/" + sourceItem.getPath(), ".png"));
                        writeImage(manager, partPath, sourceItem, sourceTexture,"handle");
                    } catch (IOException e) {
                        try {
                            sourceTexture = new Identifier(sourceItem.getNamespace(), String.format("textures/%s%s", "block/" + sourceItem.getPath(), ".png"));
                            writeImage(manager, partPath, sourceItem, sourceTexture,"handle");
                        } catch (IOException ignored) {
                        }
                    }
                }
                //endregion
                //region patchouli
                //todo
                //endregion
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> apply(Object data, ResourceManager manager, Profiler profiler, Executor executor) {
                return CompletableFuture.completedFuture(null);
            }
        });
    }

    private static JsonObject createMainWandRecipe() {
        JsonObject json = new JsonObject();
        JsonObject textures = new JsonObject();
        json.addProperty("parent", "item/generated");
        textures.addProperty("layer0", "iconicwands:item/wand");
        textures.addProperty("layer1", "iconicwands:item/wand");
        textures.addProperty("layer2", "iconicwands:item/wand");
        json.add("textures", textures);
        JsonArray overrides = new JsonArray();
        for (int i = 0; i < Iconicwands.parts.getAllTips().size(); i++) {
            for (int j = 0; j <Iconicwands.parts.getAllCores().size() ; j++) {
                for (int k = 0; k <Iconicwands.parts.getAllHandles().size() ; k++) {
                    int index = (i & 0xFF)<< 16 | (j & 0xFF) << 8 | (k & 0xFF);
                    JsonObject predicate = new JsonObject();
                    JsonObject custom_model = new JsonObject();
                    custom_model.addProperty("custom_model_data", index);
                    predicate.add("predicate", custom_model);
                    predicate.addProperty("model", "iconicwands:item/wand_model_"+index);
                    overrides.add(predicate);
                }
            }
        }
        json.add("overrides", overrides);
        JsonObject display = new JsonObject();
        JsonObject thirdperson = new JsonObject();
        JsonArray rotation = new JsonArray();
        JsonArray translation = new JsonArray();
        JsonArray scale = new JsonArray();
        scale.add(0.5);
        scale.add(0.5);
        scale.add(0.5);
        rotation.add(0);
        rotation.add(-90);
        rotation.add(45);
        thirdperson.add("rotation",rotation);
        thirdperson.add("scale",scale);
        display.add("thirdperson_righthand",thirdperson);
        thirdperson = new JsonObject();
        rotation = new JsonArray();
        rotation.add(0);
        rotation.add(90);
        rotation.add(-45);
        thirdperson.add("rotation",rotation);
        thirdperson.add("scale",scale);
        display.add("thirdperson_lefthand",thirdperson);
        thirdperson = new JsonObject();
        rotation = new JsonArray();
        translation = new JsonArray();
        rotation.add(0);
        rotation.add(90);
        rotation.add(-25);
        thirdperson.add("rotation",rotation);
        translation.add(0);
        translation.add(5);
        translation.add(3);
        thirdperson.add("translation",translation);
        thirdperson.add("scale",scale);
        display.add("firstperson_lefthand",thirdperson);
        json.add("display",display);
        return json;
    }

    private static JsonObject createSubRecipes(int tipInt,int coreInt,int handleInt) {
        JsonObject json = new JsonObject();
        JsonObject textures = new JsonObject();
        json.addProperty("parent", "iconicwands:item/iconicwand_item");
        textures.addProperty("layer1","iconicwands:item/tip/"+ Iconicwands.parts.getAllTips().get(tipInt).getIdentifier().substring(10));
        textures.addProperty("layer0","iconicwands:item/core/"+ Iconicwands.parts.getAllCores().get(coreInt).getIdentifier().substring(10));
        textures.addProperty("layer2","iconicwands:item/handle/"+ Iconicwands.parts.getAllHandles().get(handleInt).getIdentifier().substring(10));
        json.add("textures", textures);
        return json;
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
                    template.setRGB(x,y, Color.ofRGBA(newColor.getBlue(),newColor.getGreen(),newColor.getRed(),newColor.getAlpha()).getColor());
                }
            }
        }
//        ImageIO.write(template,"png",new File(path+sourceItem.getPath()+"_"+part+".png"));
        ImageIO.write(template,"png",new File(path));
    }
}