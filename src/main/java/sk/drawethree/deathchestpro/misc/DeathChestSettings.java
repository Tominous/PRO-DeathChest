package sk.drawethree.deathchestpro.misc;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import sk.drawethree.deathchestpro.DeathChestPro;
import sk.drawethree.deathchestpro.utils.Common;
import sk.drawethree.deathchestpro.utils.comp.CompMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DeathChestSettings {

    private final int CONFIG_VERSION = 1;
    private final int DEFAULT_EXPIRE_TIME;

    private final DeathChestPro plugin;

    private LinkedHashMap<String, DeathChestExpireGroup> expireGroups;
    private List<String> disabledworlds;
    private List<String> disabledRegions;
    private List<String> disabledMaterials;
    private boolean allowBreakChests;
    private boolean deathchestFireworks;
    private boolean spawnChestOnHighestBlock;
    private boolean dropItemsAfterExpire;
    private boolean clickableMessage;
    private boolean lavaProtection;
    private boolean oldLavaProtection;
    private boolean voidSpawning;
    private boolean autoEquipArmor;
    private boolean lavaSpawning;
    private boolean debugMode;
    private boolean hologramEnabled;
    private boolean startTimerAtDeath;
    private boolean storeExperience;
    private boolean spawnOnPVP;
    //private boolean displayChestsForOthers;
    private SimpleDateFormat deathDateFormat;
    private List<String> hologramLines;
    private String deathChestInvTitle;
    private int fireworkInterval;
    private int maxPlayerChests;
    private int teleportCooldown;
    private double storeExperiencePercentage;
    private boolean allowKillerLooting;

    public DeathChestSettings(DeathChestPro plugin) {
        this.plugin = plugin;
        deathchestFireworks = true;
        allowBreakChests = false;
        disabledRegions = new ArrayList<>();
        disabledworlds = new ArrayList<>();
        disabledMaterials = new ArrayList<>();
        expireGroups = new LinkedHashMap<>();
        DEFAULT_EXPIRE_TIME = 60;
        storeExperiencePercentage = 50.0;
        spawnChestOnHighestBlock = true;
        dropItemsAfterExpire = false;
        clickableMessage = false;
        lavaProtection = false;
        voidSpawning = false;
        autoEquipArmor = true;
        lavaSpawning = true;
        debugMode = false;
        hologramEnabled = true;
        storeExperience = false;
        //displayChestsForOthers = true;
        startTimerAtDeath = false;
        deathDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        hologramLines = new ArrayList<>();
        deathChestInvTitle = "&7%player%'s DeathChest";
        fireworkInterval = 5;
        allowKillerLooting = false;
        maxPlayerChests = 1;
        spawnOnPVP = true;
    }

    public void loadSettings() {

        int configVersion = this.plugin.getFileManager().getConfig("config.yml").get().getInt("config_version");

        if (configVersion > this.CONFIG_VERSION) {
            this.plugin.broadcast(DeathChestPro.BroadcastType.WARN, "Config version %d is invalid ! Loading default config values.", configVersion);
            return;
        }

        if (configVersion == 1) {
            allowBreakChests = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("allow_break_chests");
            disabledworlds = this.plugin.getFileManager().getConfig("config.yml").get().getStringList("disabled_worlds");
            disabledRegions = this.plugin.getFileManager().getConfig("config.yml").get().getStringList("disabled_regions");
            deathchestFireworks = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("deathchest_fireworks.enabled");
            fireworkInterval = this.plugin.getFileManager().getConfig("config.yml").get().getInt("deathchest_fireworks.interval");
            hologramLines = Common.color(this.plugin.getFileManager().getConfig("config.yml").get().getStringList("hologram.lines"));
            spawnChestOnHighestBlock = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("spawn_chest_on_highest_block");
            disabledMaterials = this.plugin.getFileManager().getConfig("config.yml").get().getStringList("blacklisted_blocks");
            dropItemsAfterExpire = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("drop_items_after_expire");
            clickableMessage = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("clickable_message");
            deathDateFormat = new SimpleDateFormat(this.plugin.getFileManager().getConfig("config.yml").get().getString("hologram.death_date_format"));
            deathChestInvTitle = ChatColor.translateAlternateColorCodes('&', this.plugin.getFileManager().getConfig("config.yml").get().getString("deathchest_inv_title"));
            oldLavaProtection = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("old_lava_protection");
            lavaProtection = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("lava_protection");
            voidSpawning = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("void_spawning_chest");
            autoEquipArmor = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("auto_equip_armor");
            lavaSpawning = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("lava_spawning");
            teleportCooldown = this.plugin.getFileManager().getConfig("config.yml").get().getInt("teleport_cooldown");
            //displayChestsForOthers = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("display_chests_for_others");
            debugMode = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("debug_messages");
            hologramEnabled = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("hologram.enabled");
            allowKillerLooting = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("allow_killer_looting");
            startTimerAtDeath = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("start_timer_at_death");
            storeExperience = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("store_experience");
            storeExperiencePercentage = this.plugin.getFileManager().getConfig("config.yml").get().getDouble("store_experience_percent");
            maxPlayerChests = this.plugin.getFileManager().getConfig("config.yml").get().getInt("max_player_chests");
            spawnOnPVP = this.plugin.getFileManager().getConfig("config.yml").get().getBoolean("spawn_chest_on_pvp_kill");
            //saveXP = fileManager.getConfig("config.yml").get().getBoolean("save_xp");
            this.loadExpireGroups();
        }
    }

    private void loadExpireGroups() {
        expireGroups = new LinkedHashMap<>();
        for (String key : this.plugin.getFileManager().getConfig("config.yml").get().getConfigurationSection("expire_groups").getKeys(false)) {
            String permission = this.plugin.getFileManager().getConfig("config.yml").get().getString("expire_groups." + key + ".permission");
            int time = this.plugin.getFileManager().getConfig("config.yml").get().getInt("expire_groups." + key + ".time");
            int unlockAfter = this.plugin.getFileManager().getConfig("config.yml").get().getInt("expire_groups." + key + ".unlock_after");
            double teleportCost = this.plugin.getFileManager().getConfig("config.yml").get().getDouble("expire_groups." + key + ".teleport_cost");

            DeathChestExpireGroup group = new DeathChestExpireGroup(key, permission, unlockAfter,time, teleportCost);
            this.plugin.debug(null, group.toString());

            expireGroups.put(permission, group);
        }
    }

    public DeathChestExpireGroup getExpireGroup(Player p) {

        if (p == null) {
            return DeathChestExpireGroup.DEFAULT_GROUP;
        }

        for (DeathChestExpireGroup group : this.expireGroups.values()) {
            if (p.hasPermission(group.getRequiredPermission())) {
                return group;
            }
        }

        return DeathChestExpireGroup.DEFAULT_GROUP;
    }

}
	