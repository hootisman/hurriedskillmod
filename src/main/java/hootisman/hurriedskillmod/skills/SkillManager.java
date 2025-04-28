package hootisman.hurriedskillmod.skills;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import hootisman.hurriedskillmod.HurriedSkillMod;
import hootisman.hurriedskillmod.data.SkillSavedData;
import hootisman.hurriedskillmod.data.SkillSavedData.SkillType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags.Items;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class SkillManager{
    public static SkillSavedData skillData;
    private static Logger LOGGER = LogUtils.getLogger();

    public static final int BASE_XP = 300;
    // public static final UnaryOperator<Integer> XP_TO_LEVEL = xp -> (int) Math.floor(BASE_XP * Math.pow(1.01, xp));
    
    // y = x^0.99/280 where x is xp and y is level
    public static final UnaryOperator<Integer> XP_TO_LEVEL = xp -> (int) Math.floor(Math.pow(xp, 0.99) / 280);
    public static final int XP_PER_HIT = 10;
    public static final ResourceLocation UNARMED_DAMAGE_MOD = ResourceLocation.fromNamespaceAndPath(HurriedSkillMod.MODID, "unarmed_damage");

    static void initNewPlayerSkills(UUID uuid){
        skillData.addNewPlayer(uuid);
    }

    static void giveCombatXP(ServerPlayer player, SkillType type, float damage){
        int xpDrop = (int) Math.floor(damage * XP_PER_HIT);
        LOGGER.info("COMBAT XP*** dmg: " + damage + " xpdrop: " + xpDrop);
        LOGGER.info(player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).hasModifier(UNARMED_DAMAGE_MOD) ? "Has unarmed mod!" : "NO unarmed mod!");

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

        return type;
    }
    
    static int getLevel(UUID uuid, SkillType type){
        return XP_TO_LEVEL.apply(skillData.getXP(uuid, type));
    }

    static int getDamageBonus(UUID uuid, SkillType type){
        int level = getLevel(uuid, type);
        LOGGER.info("LEVEL*** " + level);
        return level;
    }
}
