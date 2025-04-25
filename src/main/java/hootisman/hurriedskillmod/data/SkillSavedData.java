package hootisman.hurriedskillmod.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;


public class SkillSavedData extends SavedData {
    public enum SkillType {
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
    private static Logger LOGGER = LogUtils.getLogger();

    public static final String XP_TAGKEY = "xp";

    

    public static Map<UUID, Map<SkillType, Integer>> PLAYER_XP;

    public static final Codec<Map<UUID, Map<SkillType, Integer>>> SKILLS_CODEC = Codec.unboundedMap(
        UUIDUtil.STRING_CODEC, 
        Codec.unboundedMap(
            Codec.STRING.xmap(SkillType::valueOf, SkillType::name), 
            Codec.INT
        ).xmap(HashMap::new, HashMap::new)
    );


    // public static final Codec<Map<SkillType, Integer>> DELETE_THIS = Codec.unboundedMap(SKILLTYPE_CODEC, Codec.INT);

    public void addXP(UUID uuid, SkillType type, int xpDrop) {
        PLAYER_XP.get(uuid).compute(type, (key, value) -> value + xpDrop);
        // PLAYER_XP.get(uuid).put(type, xpDrop);
        setDirty();
    }

    // only call when new player joins/stats reset
    public void addNewPlayer(UUID uuid) {
        if (PLAYER_XP.get(uuid) != null)
            return;

        Map<SkillType, Integer> xpMap = new HashMap<>();
        for (SkillType type : SkillType.values()) {
            xpMap.put(type, 0);
        }

        PLAYER_XP.put(uuid, xpMap);
        setDirty();
    }

    public static SkillSavedData initSkillData() {
        PLAYER_XP = new HashMap<>();
        return new SkillSavedData();
    }

    public static SkillSavedData loadSkillData(CompoundTag tag, Provider provider) {
        SkillSavedData data = SkillSavedData.initSkillData();
        var xpMap = SKILLS_CODEC.parse(NbtOps.INSTANCE, tag.getCompound(XP_TAGKEY));
        PLAYER_XP = new HashMap<>(xpMap.result().orElse(new HashMap<>()));

        // var xpMap = SKILLTYPE_CODEC.parse(NbtOps.INSTANCE, tag.getCompound(XP_TAGKEY));
        // LOGGER.info(xpMap.result().toString());
        // PLAYER_XP = new HashMap<>();
        return data;
    }

    public static Factory<SkillSavedData> getFactory() {
        return new Factory<>(SkillSavedData::initSkillData, SkillSavedData::loadSkillData);
    }

    // Saves NBT data -> level file
    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        LOGGER.info("SAVING TO LEVEL");
        DataResult<Tag> xpTag = SKILLS_CODEC.encodeStart(NbtOps.INSTANCE, PLAYER_XP);
        LOGGER.info(xpTag.toString());

        //todo initialize file with empty skills if doesnt exist, otherwise keep data thats there.
        tag.put(XP_TAGKEY, xpTag.result().orElse(new CompoundTag()));

        //todo delete
        // DataResult<Tag> tempy = SKILLTYPE_CODEC.encodeStart(NbtOps.INSTANCE, SkillType.MAGIC);
        // LOGGER.info(tempy.toString());
        // tag.put(XP_TAGKEY, tempy.result().orElse(new CompoundTag()));

        LOGGER.info(tag.toString());
        return tag;
    }

}
