package hootisman.hurriedskillmod.skills;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import hootisman.hurriedskillmod.HurriedSkillMod;
import hootisman.hurriedskillmod.data.SkillSavedData;
import hootisman.hurriedskillmod.data.SkillSavedData.SkillType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = HurriedSkillMod.MODID)
public class SkillXPHandler{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static SkillSavedData skillData;

    @SubscribeEvent
    static void onServerStarted(ServerStartedEvent event){
        LOGGER.info("Loading save data");
        MinecraftServer server = event.getServer();
        skillData = server.overworld().getDataStorage().computeIfAbsent(SkillSavedData.getFactory(), "poo_mmo_skills");
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerLoggedInEvent event){
        Player player = event.getEntity();
        // LOGGER.info(player.level().isClientSide() ? "Is client side!!!!" : "Is server side!!!");

        if(!player.level().isClientSide()){
            LOGGER.info("ONPLAYERLOGIN");

            //assume addNewPlayer checks if player skill info is already in saveddata
            skillData.addNewPlayer(player.getUUID());
            LOGGER.info(skillData.PLAYER_XP.get(player.getUUID()).toString());
        }
    }

    @SubscribeEvent
    static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (player.level() instanceof ServerLevel && entity instanceof Mob) {
            LOGGER.info(event.getEntity().toString() + " ATTACKED MOB " + event.getTarget().toString());
            //todo only add  xp when mob is DAMAGED (right now adds xp if hitting mob, spamming gives more xp)
            skillData.addXP(player.getUUID(), SkillType.UNARMED, 10);
        }
    }
}
