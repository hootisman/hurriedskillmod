package hootisman.hurriedskillmod.skills;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import hootisman.hurriedskillmod.HurriedSkillMod;
import hootisman.hurriedskillmod.data.SkillSavedData;
import hootisman.hurriedskillmod.data.SkillSavedData.SkillType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = HurriedSkillMod.MODID)
public class SkillXPEvents{
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    static void onServerStarted(ServerStartedEvent event){
        LOGGER.info("Loading save data");
        MinecraftServer server = event.getServer();
        SkillXPHandler.skillData = server.overworld().getDataStorage().computeIfAbsent(SkillSavedData.getFactory(), "poo_mmo_skills");
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerLoggedInEvent event){
        Player player = event.getEntity();
        
        if(!player.level().isClientSide()){
            //TODO cleanup
            LOGGER.info("Player joined");
            //assume addNewPlayer checks if player skill info is already in saveddata
            SkillXPHandler.initNewPlayerSkills(player.getUUID());
            LOGGER.info(SkillSavedData.PLAYER_XP.get(player.getUUID()).toString());
        }
    }

    @SubscribeEvent
    static void onIncomingDamagePost(LivingDamageEvent.Post event){
        DamageSource source = event.getSource();
        ItemStack item;
        SkillType typeUsed;

        if(!(source.getEntity() instanceof ServerPlayer player) || 
        (item = source.getWeaponItem()) == null ||
        (typeUsed = SkillXPHandler.getWeaponSkillType(item)) == null) return;

        SkillXPHandler.giveCombatXP(player, typeUsed, event.getNewDamage());
    }

}
