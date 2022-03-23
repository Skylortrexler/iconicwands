package website.skylorbeck.minecraft.iconicwands.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) throws IOException {
//        if (!"iconicwands:item/iconicwand_item".equals(id.toString())) return;
        if (!"iconicwands".equals(id.getNamespace())) return;//filter out everything that isn't from this mod
        if (id.getPath().contains("bench")||id.getPath().contains("pedestal")||id.getPath().contains("tester")||id.getPath().contains("preset")) return;//filter out bench block and pedestal block
//        Logger.getGlobal().log(Level.SEVERE,id.toString());
        String path = "cache/iconicwands/models/";
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }
        String modelJson = "";
        if ("item/iconicwand_item".equals(id.getPath())) {
            if (Files.exists(Paths.get(path+"wand_model.json"))) {
                modelJson = Files.readString(Paths.get(path+"wand_model.json"));
            } else {
                modelJson = createMainWandRecipe(id.toString());
                Files.write(Paths.get(path+"wand_model.json"), modelJson.getBytes());
            }
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
            model.id = id.toString();
            cir.setReturnValue(model);
        } else if (id.getPath().contains("item/wand")) {
            String subID = id.toString().replace("iconicwands:item/wand", "");
            if (Files.exists(Paths.get(path+"wand_model_"+subID+".json"))) {
                modelJson = Files.readString(Paths.get(path+"wand_model_"+subID+".json"));
            } else {
                modelJson = createSubRecipes(id.toString());
                Files.write(Paths.get(path+"wand_model_"+subID+".json"), modelJson.getBytes());
            }
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
            model.id = id.toString();
            cir.setReturnValue(model);
        }
    }


    private static String createMainWandRecipe(String id) {
        JsonObject json = new JsonObject();
        JsonObject textures = new JsonObject();
        json.addProperty("parent", "item/generated");
        textures.addProperty("layer0", "iconicwands:item/wand");
        textures.addProperty("layer1", "iconicwands:item/wand");
        textures.addProperty("layer2", "iconicwands:item/wand");
        json.add("textures", textures);
        JsonArray overrides = new JsonArray();
        for (int i = 0; i < Iconicwands.parts.tips.size(); i++) {
            for (int j = 0; j <Iconicwands.parts.cores.size() ; j++) {
                for (int k = 0; k <Iconicwands.parts.handles.size() ; k++) {
                    int index = (i & 0xFF)<< 16 | (j & 0xFF) << 8 | (k & 0xFF);

                    JsonObject predicate = new JsonObject();
                    JsonObject custom_model = new JsonObject();
                    custom_model.addProperty("custom_model_data", index);
                    predicate.add("predicate", custom_model);
                    predicate.addProperty("model", "iconicwands:item/wand"+index);
                    overrides.add(predicate);
                }
            }
        }
        json.add("overrides", overrides);
//        Logger.getGlobal().log(Level.SEVERE,json.toString());
        return json.toString();
    }
    private static String createSubRecipes(String id) {
        int shiftedParts = Integer.parseInt(id.replace("iconicwands:item/wand", ""));
        JsonObject json = new JsonObject();
        JsonObject textures = new JsonObject();
        int tipInt = shiftedParts >> 16 & 0xFF;
        int coreInt = shiftedParts >> 8 & 0xFF;
        int handleInt = shiftedParts & 0xFF;
        json.addProperty("parent", "iconicwands:item/iconicwand_item");
        textures.addProperty("layer1","iconicwands:item/tip/"+ tipInt);
        textures.addProperty("layer0","iconicwands:item/core/"+ coreInt);
        textures.addProperty("layer2","iconicwands:item/handle/"+ handleInt);
        json.add("textures", textures);
        return json.toString();
    }
}
