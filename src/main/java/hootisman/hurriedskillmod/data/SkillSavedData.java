package hootisman.hurriedskillmod.data;

import com.mojang.serialization.DataResult;

import hootisman.hurriedskillmod.skills.Skills;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

public class SkillSavedData extends SavedData {
    public static final String XP_TAGKEY = "xp";

    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        DataResult<Tag> xpTag = Skills.SKILL_CODEC.encodeStart(NbtOps.INSTANCE, Skills.PLAYER_XP);
        tag.put(XP_TAGKEY, xpTag.result().orElse(new CompoundTag()));
        return tag;
    }
}
