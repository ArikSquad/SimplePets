package simplepets.brainsynder.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IImpossaPet;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class MainListeners implements Listener {
    @EventHandler
    public void onhurt(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            if (handle instanceof IImpossaPet) {
                e.setCancelled(true);
            }
        } else {
            Player p = (Player) e.getEntity();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (owner.hasPet()) {
                if (p.isInsideVehicle()) {
                    if (p.getVehicle() == owner.getPet().getEntity().getEntity()) {
                        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION || e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onhurt(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            e.setCancelled(handle instanceof Player);
        } else {
            if (!(e.getDamager() instanceof Player)) {
                Entity ent = e.getDamager();
                Object handle = ReflectionUtil.getEntityHandle(ent);
                e.setCancelled(handle instanceof IEntityPet);
            }
        }
    }

    @EventHandler
    public void onhurt(EntityDamageByBlockEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            e.setCancelled(handle instanceof IImpossaPet);
        }
    }



    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        PetOwner owner = PetOwner.getPetOwner(event.getPlayer());
        if (owner.isRenaming()) {
            owner.setPetName(event.getMessage(), false);
            owner.setRenaming(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
                IEntityPet entityPet = (IEntityPet) handle;
                if (entityPet instanceof IEntityShulkerPet) return;
                if (entityPet instanceof IEntityParrotPet) return;

                if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                    PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(entityPet.getOwner()));
                }
            }
        }
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
        if (handle instanceof IEntityPet) {
            e.setCancelled(true);
            IEntityPet entityPet = (IEntityPet) handle;
            if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(entityPet.getOwner()));
            }
        }
    }

    @EventHandler
    public void onInteract(EntityMountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getMount());
        if (handle instanceof IEntityPet) {
            IEntityPet entityPet = (IEntityPet) handle;
            e.setCancelled(!entityPet.getPet().isVehicle());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p != null) {
            PetOwner owner = PetOwner.getPetOwner(p);
            if (owner.hasPet()) {
                if (owner.getPet().getVisableEntity() == null) return;
                if (!owner.hasPetToRespawn()) {
                    owner.setPetToRespawn(owner.getPet().getVisableEntity().asCompound());
                }
                owner.removePet();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (p == null) {
            final PetOwner owner = PetOwner.getPetOwner(p);
            if (owner.hasPetToRespawn()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        owner.respawnPet();
                    }
                }.runTaskLater(PetCore.get(), 40);
            }
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        final PetOwner owner = PetOwner.getPetOwner(p);
        if (owner.hasPet()) {
            if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                if (p.getPassenger() != null) {
                    e.setCancelled(true);
                    return;
                }
                IPet pet = owner.getPet();
                if (pet.getVisableEntity() == null) return;
                if (!owner.hasPetToRespawn()) {
                    owner.setPetToRespawn(pet.getVisableEntity().asCompound());
                    owner.removePet();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (owner.hasPetToRespawn()) {
                                if (!p.isOnline()) {
                                    owner.setPetToRespawn(null);
                                    return;
                                }
                                owner.respawnPet();
                            }
                        }
                    }.runTaskLater(PetCore.get(), 40);
                }
            }
        }
    }

    @EventHandler
    public void onExit(EntityDismountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getDismounted());
        if (handle instanceof IEntityPet) {
            IEntityPet pet = (IEntityPet) handle;
            if (pet.getOwner().getName().equals(e.getEntity().getName())) {
                pet.getPet().setVehicle(false);
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        final PetOwner owner = PetOwner.getPetOwner(p);
        if (owner.hasPet()) {
            if (PetCore.get().getConfiguration().getBoolean("RemovePetsOnWorldChange")) {
                owner.removePet();
            } else {
            //    final UUID uuid = p.getUniqueId();
                final IPet pet = owner.getPet();
                if (owner.hasPet()) {
                    if (pet.getVisableEntity() == null) return;
                    if (!owner.hasPetToRespawn()) {
                        owner.setPetToRespawn(pet.getVisableEntity().asCompound());
                        owner.removePet();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (owner.hasPetToRespawn()) {
                                    if (!p.isOnline()) {
                                        owner.setPetToRespawn(null);
                                        return;
                                    }
                                    owner.respawnPet();
                                }
                            }
                        }.runTaskLater(PetCore.get(), 40);
                    }
                }
            }
        }
    }
}
