package website.skylorbeck.minecraft.iconicwands.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;openAsReader(Lnet/minecraft/util/Identifier;)Ljava/io/BufferedReader;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) throws IOException {
        if (!"iconicwands".equals(id.getNamespace())) return;//filter out everything that isn't from this mod
        String idPath = id.getPath();
        if (!idPath.contains("wand")||idPath.contains("bench")||idPath.contains("pedestal")||idPath.contains("tester")) return; //filter non-wands

        String pathString = "cache/assets/iconicwands";
        String handlePath = pathString + "/textures/item/handle";
        String tipPath = pathString + "/textures/item/tip";
        String corePath = pathString + "/textures/item/core";
        String modelPath = pathString + "/models";
        try {
            Files.createDirectories(Paths.get(handlePath));
            Files.createDirectories(Paths.get(tipPath));
            Files.createDirectories(Paths.get(corePath));
            Files.createDirectories(Paths.get(modelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String modelJson = "";

        if (idPath.contains("iconicwand_item")) {
            JsonObject mainModel = createMainWandRecipe();
            boolean cached = false;
            if (Files.exists(Paths.get(modelPath + "/iconicwand_item.json"))) {
                try {
                    JsonObject json = JsonParser.parseString(Files.readString(Paths.get(modelPath + "/iconicwand_item.json"))).getAsJsonObject();
                    if (mainModel.equals(json)) {
                        cached = true;
                        modelJson = json.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!cached) {
                try {
                    Files.write(Paths.get(modelPath + "/iconicwand_item.json"), mainModel.toString().getBytes());
                    modelJson = mainModel.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            String subID = id.toString().replace("iconicwands:item/wand_model_", "");
            if (Files.exists(Paths.get(modelPath + "/wand_model_" + subID + ".json"))) {
                modelJson = Files.readString(Paths.get(modelPath + "/wand_model_" + subID + ".json"));
            } else {
                int modelInt = Integer.parseInt(subID);
                Parts.WandCluster wand = IconicWand.intToParts(modelInt);
                modelJson = createSubRecipes(Iconicwands.parts.getAllTips().indexOf(wand.getTip()),Iconicwands.parts.getAllCores().indexOf(wand.getCore()),Iconicwands.parts.getAllHandles().indexOf(wand.getHandle())).toString();
                Files.write(Paths.get(modelPath + "/wand_model_" + subID + ".json"), modelJson.getBytes());
            }

        }
        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
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
            for (int j = 0; j < Iconicwands.parts.getAllCores().size(); j++) {
                for (int k = 0; k < Iconicwands.parts.getAllHandles().size(); k++) {
                    int index = (i & 0xFF) << 16 | (j & 0xFF) << 8 | (k & 0xFF);
                    JsonObject predicate = new JsonObject();
                    JsonObject custom_model = new JsonObject();
                    custom_model.addProperty("custom_model_data", index);
                    predicate.add("predicate", custom_model);
                    predicate.addProperty("model", "iconicwands:item/wand_model_" + index);
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
        thirdperson.add("rotation", rotation);
        thirdperson.add("scale", scale);
        display.add("thirdperson_righthand", thirdperson);
        thirdperson = new JsonObject();
        rotation = new JsonArray();
        rotation.add(0);
        rotation.add(90);
        rotation.add(-45);
        thirdperson.add("rotation", rotation);
        thirdperson.add("scale", scale);
        display.add("thirdperson_lefthand", thirdperson);
        thirdperson = new JsonObject();
        rotation = new JsonArray();
        translation = new JsonArray();
        rotation.add(0);
        rotation.add(90);
        rotation.add(-25);
        thirdperson.add("rotation", rotation);
        translation.add(0);
        translation.add(5);
        translation.add(3);
        thirdperson.add("translation", translation);
        thirdperson.add("scale", scale);
        display.add("firstperson_lefthand", thirdperson);
        json.add("display", display);
        return json;
    }

    private static JsonObject createSubRecipes(int tipInt, int coreInt, int handleInt) {
        JsonObject json = new JsonObject();
        JsonObject textures = new JsonObject();
        json.addProperty("parent", "iconicwands:item/iconicwand_item");
        textures.addProperty("layer1", "iconicwands:item/tip/" + Iconicwands.parts.getAllTips().get(tipInt).getIdentifier().substring(10));
        textures.addProperty("layer0", "iconicwands:item/core/" + Iconicwands.parts.getAllCores().get(coreInt).getIdentifier().substring(10));
        textures.addProperty("layer2", "iconicwands:item/handle/" + Iconicwands.parts.getAllHandles().get(handleInt).getIdentifier().substring(10));
        json.add("textures", textures);
        return json;
    }
}