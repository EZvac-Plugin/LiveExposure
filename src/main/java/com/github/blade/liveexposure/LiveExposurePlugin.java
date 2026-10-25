package com.github.blade.liveexposure;

import com.github.blade.hvac.api.EzvacApi;
import com.github.blade.hvac.api.TemperatureUnit;
import com.github.blade.liveexposure.model.ExposureSettings;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * LiveExposure: heat and cold that wear down survival players caught in the
 * elements, and an optional sickness system. Built only on EZvac's public API.
 */
public final class LiveExposurePlugin extends JavaPlugin {
    private EzvacApi ezvac;
    private ExposureSettings settings;
    private PlayerState.Keys keys;
    private final Map<UUID, PlayerState> states = new HashMap<>();
    private final Random random = new Random();
    private SicknessService sickness;
    private BukkitTask saveTask;

    @Override
    public void onEnable() {
        try {
            ezvac = EzvacApi.get();
        } catch (IllegalStateException missing) {
            getLogger().severe(missing.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!ezvac.apiVersion().startsWith("1.")) {
            getLogger().severe("This LiveExposure needs EZvac API 1.x, but EZvac " + ezvac.pluginVersion()
                    + " offers API " + ezvac.apiVersion() + ". Update LiveExposure.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        loadSettings();
        keys = PlayerState.Keys.of(this);
        for (Player player : getServer().getOnlinePlayers()) state(player);
        sickness = new SicknessService(this);
        getServer().getPluginManager().registerEvents(new ExposureListener(this), this);
        sickness.start();
        Commands commands = new Commands(this);
        for (String name : new String[] {"bodytemp", "liveexposure"}) {
            PluginCommand command = Objects.requireNonNull(getCommand(name), name + " is missing from plugin.yml");
            command.setExecutor(commands);
            command.setTabCompleter(commands);
        }
        // Saved now and then as well as on leaving, so a crash costs at most a minute.
        saveTask = getServer().getScheduler().runTaskTimer(this, this::saveAll, 1200L, 1200L);
        getLogger().info("LiveExposure enabled on EZvac " + ezvac.pluginVersion() + " (API " + ezvac.apiVersion()
                + "); sickness " + (settings.sickness().enabled() ? "on" : "off") + ", "
                + (settings.damage().lethal() ? "deadly" : "never deadly") + ".");
    }

    @Override
    public void onDisable() {
        if (saveTask != null) saveTask.cancel();
        if (sickness != null) sickness.stop();
        if (keys != null) saveAll();
        states.clear();
    }

    /** Reads config.yml again; a bad value warns and keeps its section's defaults. */
    void loadSettings() {
        reloadConfig();
        settings = ExposureSettings.load(getConfig(), getLogger()::warning);
    }

    void saveAll() {
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerState state = states.get(player.getUniqueId());
            if (state != null) state.save(player, keys);
        }
    }

    PlayerState state(Player player) {
        return states.computeIfAbsent(player.getUniqueId(), ignored -> PlayerState.load(player, keys));
    }

    void forget(Player player) {
        PlayerState state = states.remove(player.getUniqueId());
        if (state != null) state.save(player, keys);
    }

    /** True when heat, cold and sickness apply to this player where they are now. */
    boolean affected(Player player) {
        return !player.hasPermission("liveexposure.bypass")
                && settings.scope().appliesIn(player.getWorld().getName(), player.getGameMode().name());
    }

    boolean sicknessOn() { return settings.sickness().enabled(); }

    /** {@code 95.8 °F}, or °C when EZvac shows Celsius. */
    String degrees(double fahrenheit) {
        TemperatureUnit units = ezvac.units();
        return String.format(Locale.US, "%.1f °%s", units.fromFahrenheit(fahrenheit), units.name());
    }

    EzvacApi ezvac() { return ezvac; }
    ExposureSettings settings() { return settings; }
    Random random() { return random; }
    SicknessService sickness() { return sickness; }
}
