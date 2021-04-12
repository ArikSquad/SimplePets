package simplepets.brainsynder.api.plugin;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.misc.IEntityBase;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.user.UserManagement;

import java.util.Optional;

public interface IPetsPlugin extends Plugin {
    UserManagement getUserManager ();

    ISpawnUtil getSpawnUtil();

    PetConfigManager getPetConfigManager ();

    ItemHandler getItemHandler ();

    GUIHandler getGUIHandler ();

    ParticleHandler getParticleHandler ();

    /**
     * Will check if the entity is a simplepets entity
     *
     * @param entity - Entity being checked
     */
    default boolean isPetEntity (Entity entity) {
        Optional<Object> optional = getSpawnUtil().getHandle(entity);
        return optional.filter(o -> (o instanceof IEntityBase)).isPresent();
    }
}
