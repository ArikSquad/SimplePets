package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sitting")
public class SittingData extends PetData<ITameable> {
    public SittingData() {
        addDefaultItem("true", new ItemBuilder(Material.OAK_STAIRS)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.OAK_STAIRS)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(ITameable entity) {
        entity.setSitting(!entity.isSitting());
    }

    @Override
    public Object value(ITameable entity) {
        return entity.isSitting();
    }
}
