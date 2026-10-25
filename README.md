# LiveExposure

**Heat and cold that wear down survival players caught in the elements.** An
addon for [EZvac](https://github.com/EZvac-Plugin/EZvac), the realistic HVAC
and indoor-climate plugin for Paper and Purpur.

Every player has a **body temperature**. It rests at 98.6 °F, falls in the
cold, rises in the heat, and settles back once they're comfortable. How cold
or hot it feels comes straight from EZvac, so all of these count:
- the biome, altitude, season and weather;
- the humidity and wind;
- a heated or air-conditioned room built with EZvac.

Stay out too long and it starts to hurt.

> **Requires** EZvac ALPHA 2.1 Pilot 4 or newer (EZvac API 1.x), on Paper or
> Purpur 1.21.1 with Java 21. The body temperature on EZvac's HUD needs
> ALPHA 2.1 or newer.

## Stages

| Body temperature | Stage | Possible from (feels like) | What happens |
| --- | --- | ---: | --- |
| below 97 °F | Chilled | ~44 °F | A warning, and frost creeping in |
| below 95 °F | Hypothermia | ~36 °F | Slowness |
| below 93 °F | Severe hypothermia | ~28 °F | Slowness II, Weakness, Mining Fatigue |
| below 91 °F | Freezing | ~20 °F | All of that, and damage |
| above 99.5 °F | Overheating | ~90 °F | A warning |
| above 100.5 °F | Heat exhaustion | ~94 °F | Slowness, hunger |
| above 102 °F | Severe heat | ~100 °F | Weakness, Mining Fatigue, a wave of nausea |
| above 104 °F | Heatstroke | ~108 °F | All of that, and damage |

The body settles where the climate puts it: a warm 92 °F day only makes you run
a little warm, and only real extremes can bring on heatstroke or freezing
(`balance:` in config.yml). Leather, shade and a fever move these too.

- **Pace:** at 20 °F outside, the body loses about 2 °F a minute. A minute or two out in the snow is fine; staying out there isn't.
- **Lethal by default:** heat and cold can kill. `lethal: false` keeps players at half a heart instead.
- **Feedback:** the game's own frost overlay shows the cold, and overheating players visibly sweat. The action bar shows your body temperature whenever it's off.
- **On EZvac's HUD** (`/hvac hud`): your body temperature, with a meter under it like EZvac's feels-like bar, coloured by stage. It goes on EZvac's `{addons}` line, under the season; `{section:liveexposure:body}` puts it anywhere else, including the action bar and boss bar formats. Change it under `hud:` in config.yml.

## Staying safe

- **Leather armour** keeps the cold out:
  - chestplate and leggings 3.5 points each, helmet and boots 1.5;
  - every point is 1.5 °F warmer, so a full set is 15 °F, against cold only.
- **An EZvac room:** a room an EZvac system heats or cools is comfortable, and the body recovers there.
- **Shade:** while the sun is up, a roof, an overhang or a tree overhead takes 10 °F off the heat. Glass doesn't count, since sunlight goes straight through it.
- **Water** pulls body heat three times as fast as air, so cold water is dangerous.
- **Fire Resistance** stops heat from warming you.
- **Who's never affected:** creative and spectator players, and anyone with `liveexposure.bypass`.

## Sickness (optional, on by default)

Every day, each player may fall ill:
- **The chance:**
  - it depends on the season: 1% in summer, up to 8% in winter;
  - a day spent cold makes it up to 4× as likely;
  - spending time within 10 blocks of a sick player adds 35 percentage points.
- **A fever:**
  - it raises the body's resting temperature, so heat hits harder;
  - it brings slowness and waves of nausea;
  - it lasts 2-3 days.
- **Water helps:** drinking water bottles shortens an illness by up to a day.
- **The days** follow EZvac's calendar.

Switch it off completely with `sickness.enabled: false`.

## Commands and permissions

| Command | Permission | Does |
| --- | --- | --- |
| `/bodytemp` | `liveexposure.use` (everyone) | Your body temperature, stage and any illness |
| `/liveexposure reload` | `liveexposure.admin` (ops) | Reloads config.yml |
| `/liveexposure set <player> <°F>` | `liveexposure.admin` | Sets a body temperature |
| `/liveexposure sick <player> <mild\|moderate\|severe>` | `liveexposure.admin` | Makes someone sick |
| `/liveexposure cure <player>` | `liveexposure.admin` | Cures someone |
| `/liveexposure roll <player>` | `liveexposure.admin` | Rolls today's sickness chance now, and logs it |

`liveexposure.bypass` (no one by default): untouched by heat, cold and
sickness.

## Configuration

Everything is in `plugins/LiveExposure/config.yml`, explained line by line:
- rates, comfort band and stages;
- damage, and the armour points;
- worlds and game modes;
- every number of the sickness system;
- the body temperature on EZvac's HUD.

A bad value warns in the console and that section uses its defaults. After an
update, config.yml gains the new settings with their comments; the settings
you changed stay as they are, and the old file is kept as
`config.yml.before-<version>.bak`.

## Building

```shell
mvn clean package
```

The jar is written to `target/LiveExposure-<version>.jar`. EZvac's API comes
from [JitPack](https://jitpack.io/#EZvac-Plugin/EZvac); see
[EZvac's API guide](https://github.com/EZvac-Plugin/EZvac/blob/main/docs/api.md).

## License

MIT; see [LICENSE](LICENSE).
