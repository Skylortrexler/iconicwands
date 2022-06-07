package website.skylorbeck.minecraft.iconicwands.mixin;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"), method = "method_18160")
    public Optional<Resource> interceptResource(ResourceManager resourceManager, Identifier identifier) throws IOException {
        return getResource(resourceManager, identifier);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;open(Lnet/minecraft/util/Identifier;)Ljava/io/InputStream;"), method = "loadSprite")
    public InputStream interceptResource2(ResourceManager resourceManager, Identifier identifier) throws IOException {
        if (identifier.getNamespace().equals("iconicwands") && (identifier.getPath().contains("tip")) | (identifier.getPath().contains("core")) | (identifier.getPath().contains("handle"))) {
            Optional<Resource> resource;
            resource = resourceManager.getResource(identifier);
            if (resource.isEmpty()) {
                return Files.newInputStream(Paths.get("cache/assets/iconicwands/" + identifier.getPath()));
            }
        }
        return resourceManager.open(identifier);
    }

    private Optional<Resource> getResource(ResourceManager resourceManager, Identifier identifier) throws IOException {
        if (identifier.getNamespace().equals("iconicwands") && (identifier.getPath().contains("tip")) | (identifier.getPath().contains("core")) | (identifier.getPath().contains("handle"))) {
            Optional<Resource> resource;
                resource = resourceManager.getResource(identifier);
                if (resource.isEmpty()) {
                    //        Logger.getGlobal().log(Level.SEVERE, identifier.toString());
                    InputStream inputStream = Files.newInputStream(Paths.get("cache/assets/iconicwands/" + identifier.getPath()));
                    resource = Optional.of(new Resource("iconicwands", () -> inputStream));
                }
            return resource;
        }
        return resourceManager.getResource(identifier);
    }
}
