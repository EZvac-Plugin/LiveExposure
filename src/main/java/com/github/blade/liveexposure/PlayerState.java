package com.github.blade.liveexposure;

import com.github.blade.liveexposure.model.ExposureSettings;
import com.github.blade.liveexposure.model.SicknessModel;
import com.github.blade.liveexposure.model.Stage;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * One player's body and illness. Kept in memory while they're online and in
 * their own persistent data, so relogs and restarts keep it. Main thread only.
 */
final class PlayerState {
    double bodyF = ExposureSettings.Thresholds.REST_F;
    Stage stage = Stage.NORMAL;
    /** Server tick of the last step; steps measure the real time between updates. */
    long lastTick = -1;
    double damageTimer;
    SicknessModel.Illness illness;
    double nauseaTimer = Double.NaN;
    /** Today's tally, for tomorrow's sickness roll. */
    double daySeconds;
    double coldSeconds;
    double contactSeconds;

    /** The persistent-data keys, made once per plugin. */
    record Keys(NamespacedKey body, NamespacedKey severity, NamespacedKey days, NamespacedKey water,
                NamespacedKey daySeconds, NamespacedKey coldSeconds, NamespacedKey contactSeconds) {
        static Keys of(Plugin plugin) {
            return new Keys(new NamespacedKey(plugin, "body_f"), new NamespacedKey(plugin, "sick_severity"),
                    new NamespacedKey(plugin, "sick_days_left"), new NamespacedKey(plugin, "sick_water_saved"),
                    new NamespacedKey(plugin, "day_seconds"), new NamespacedKey(plugin, "cold_seconds"),
                    new NamespacedKey(plugin, "contact_seconds"));
        }
    }

    static PlayerState load(Player player, Keys keys) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        PlayerState state = new PlayerState();
        Double body = data.get(keys.body(), PersistentDataType.DOUBLE);
        if (body != null && Double.isFinite(body)) state.bodyF = body;
        SicknessModel.Severity severity = SicknessModel.Severity.parse(data.get(keys.severity(), PersistentDataType.STRING));
        Double days = data.get(keys.days(), PersistentDataType.DOUBLE);
        if (severity != null && days != null && days > 0) {
            Double water = data.get(keys.water(), PersistentDataType.DOUBLE);
            state.illness = new SicknessModel.Illness(severity, days, water == null ? 0.0 : water);
        }
        state.daySeconds = data.getOrDefault(keys.daySeconds(), PersistentDataType.DOUBLE, 0.0);
        state.coldSeconds = data.getOrDefault(keys.coldSeconds(), PersistentDataType.DOUBLE, 0.0);
        state.contactSeconds = data.getOrDefault(keys.contactSeconds(), PersistentDataType.DOUBLE, 0.0);
        return state;
    }

    void save(Player player, Keys keys) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(keys.body(), PersistentDataType.DOUBLE, bodyF);
        if (illness == null) {
            data.remove(keys.severity());
            data.remove(keys.days());
            data.remove(keys.water());
        } else {
            data.set(keys.severity(), PersistentDataType.STRING, illness.severity().name());
            data.set(keys.days(), PersistentDataType.DOUBLE, illness.daysLeft());
            data.set(keys.water(), PersistentDataType.DOUBLE, illness.waterSaved());
        }
        data.set(keys.daySeconds(), PersistentDataType.DOUBLE, daySeconds);
        data.set(keys.coldSeconds(), PersistentDataType.DOUBLE, coldSeconds);
        data.set(keys.contactSeconds(), PersistentDataType.DOUBLE, contactSeconds);
    }

    /** A new day: the tally starts again. */
    void resetDay() {
        daySeconds = 0;
        coldSeconds = 0;
        contactSeconds = 0;
    }
}
