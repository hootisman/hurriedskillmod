package hootisman.hurriedskillmod.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import lombok.Getter;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerPlayer;

enum SkillType {
    ONEHANDED,
    TWOHANDED,
    UNARMED,
    ARCHERY,
    MAGIC,
    DEFENSE,
    AGILITY,
    CRAFTING,
    BUILDING,
    FISHING,
}
public class Skills {
   public static Map<UUID, Map<SkillType, Integer>> PLAYER_XP;
   // public int getXP(SkillType type) {
   //    return PLAYER_XP.get(type);
   // }
   public static final Codec<SkillType> SKILLTYPE_CODEC = Codec.STRING.xmap(SkillType::valueOf, SkillType::name);
   public static final Codec<Map<UUID, Map<SkillType, Integer>>> SKILL_CODEC = Codec.unboundedMap(UUIDUtil.CODEC, Codec.unboundedMap(Skills.SKILLTYPE_CODEC, Codec.INT));
}
