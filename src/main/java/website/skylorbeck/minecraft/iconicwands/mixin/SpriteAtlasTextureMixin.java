package website.skylorbeck.minecraft.iconicwands.mixin;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), method = "method_18160")
    public Resource interceptResource(ResourceManager resourceManager, Identifier identifier) throws IOException {
        return getResource(resourceManager, identifier);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), method = "loadSprite")
    public Resource interceptResource2(ResourceManager resourceManager, Identifier identifier) throws IOException {
        return getResource(resourceManager, identifier);
    }

    private Resource getResource(ResourceManager resourceManager, Identifier identifier) throws IOException {
        if (identifier.getNamespace().equals("iconicwands") && (identifier.getPath().contains("tip")) | (identifier.getPath().contains("core")) | (identifier.getPath().contains("handle"))) {
            Resource resource;
            try{
                resource = resourceManager.getResource(identifier);
            } catch (IOException ignored){
                //        Logger.getGlobal().log(Level.SEVERE, identifier.toString());
                InputStream inputStream = Files.newInputStream(Paths.get("cache/assets/iconicwands/" + identifier.getPath()));
                resource = new ResourceImpl("iconicwands", identifier, inputStream, null);
            }
            return resource;
        }
        return resourceManager.getResource(identifier);
    }
}
