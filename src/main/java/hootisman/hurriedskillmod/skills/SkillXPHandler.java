package hootisman.hurriedskillmod.skills;

import java.util.UUID;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import hootisman.hurriedskillmod.data.SkillSavedData;
import hootisman.hurriedskillmod.data.SkillSavedData.SkillType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags.Items;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class SkillXPHandler{
    public static SkillSavedData skillData;
    private static Logger LOGGER = LogUtils.getLogger();
    
    public static final int XP_PER_HIT = 10;

    static void initNewPlayerSkills(UUID uuid){
        skillData.addNewPlayer(uuid);
    }

    static void giveCombatXP(ServerPlayer player, SkillType type, float damage){
        int xpDrop = (int) Math.floor(damage * XP_PER_HIT);
        LOGGER.info("COMBAT XP*** dmg: " + damage + " xpdrop: " + xpDrop);

        skillData.addXP(player.getUUID(), type, xpDrop);
    }
    
    static SkillType getWeaponSkillType(ItemStack stack){
        //TODO this looks horrible
        SkillType type = null;

        if(stack.isEmpty()){
            type = SkillType.UNARMED;
        }else if(stack.is(Items.TOOLS)){
            type = SkillType.ONEHANDED;
        }
        
        //debugging
        // var itemTags = stack.getTags();
        // itemTags.forEach((item) -> LOGGER.info(item.toString()));


        return type;
    }
}
