package hootisman.hurriedskillmod.skills;

import hootisman.hurriedskillmod.HurriedSkillMod;
import hootisman.hurriedskillmod.data.SkillSavedData.SkillType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = HurriedSkillMod.MODID)
public class SkillAttributeEvents {

    @SubscribeEvent
    public static void onIncomingDamagePre(LivingDamageEvent.Pre event){
        DamageSource source = event.getSource();
        LivingEntity victim = event.getEntity();
        if(!(source.getEntity() instanceof ServerPlayer player)) return;

        AttributeInstance attribute = player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
        boolean isHandEmpty = player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();

        if(isHandEmpty){
            int damageBonus = SkillManager.getDamageBonus(player.getUUID(), SkillType.UNARMED);

            attribute.addOrUpdateTransientModifier(
                new AttributeModifier(
                    SkillManager.UNARMED_DAMAGE_MOD, 
                    damageBonus,
                    Operation.ADD_VALUE
                )
            );
        }else{
            attribute.removeModifier(SkillManager.UNARMED_DAMAGE_MOD);
        }

    }
}
