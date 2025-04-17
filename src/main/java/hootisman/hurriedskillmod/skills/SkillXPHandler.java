package hootisman.hurriedskillmod.skills;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import hootisman.hurriedskillmod.HurriedSkillMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = HurriedSkillMod.MODID)
public class SkillXPHandler{
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (player.level() instanceof ServerLevel && entity instanceof Mob) {
            LOGGER.info(event.getEntity().toString() + " ATTACKED MOB " + event.getTarget().toString());
        }
    }
}
