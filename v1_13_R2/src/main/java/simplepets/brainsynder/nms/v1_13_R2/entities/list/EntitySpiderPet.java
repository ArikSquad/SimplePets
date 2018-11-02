package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntitySpider}
 */
@Size(width = 1.4F, length = 0.9F)
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    public EntitySpiderPet(World world, IPet pet) {
        super(Types.SPIDER, world, pet);
    }
    public EntitySpiderPet(World world) {
        super(Types.SPIDER, world);
    }
    public EntitySpiderPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntitySpiderPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
