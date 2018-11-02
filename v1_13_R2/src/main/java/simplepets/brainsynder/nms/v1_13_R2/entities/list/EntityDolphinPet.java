package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.*;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityDolphin}
 */
public class EntityDolphinPet extends EntityPet implements IEntityDolphinPet {
    private static final DataWatcherObject<BlockPosition> TREASURE;
    private static final DataWatcherObject<Boolean> HAS_FISH;
    private static final DataWatcherObject<Integer> MOIST;

    public EntityDolphinPet(World world, IPet pet) {
        super(Types.DOLPHIN, world, pet);
        this.lookController = new ControllerLookDolphin(this, 10);
    }

    public EntityDolphinPet(World world) {
        super(Types.DOLPHIN,world);
        this.lookController = new ControllerLookDolphin(this, 10);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TREASURE, BlockPosition.ZERO);
        this.datawatcher.register(HAS_FISH, false);
        this.datawatcher.register(MOIST, 2400);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packetPlayOutRelEntityMoveLook = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook();

    }

    static {
        TREASURE = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.l);
        HAS_FISH = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.i);
        MOIST = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.b);
    }
}
