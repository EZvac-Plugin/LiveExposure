# Changelog

All notable changes to LiveExposure are recorded here.

## 0.2.0

Your body temperature on EZvac's climate HUD, and faster heat and cold.
Needs EZvac ALPHA 2.1 or newer for the HUD; on ALPHA 2.1 Pilot 4 everything
else works as before.

### Added

- **Body temperature on the HUD.** Under EZvac's season line, the sidebar
  shows `Body: 98.6°F` and a meter under it, drawn with EZvac's own bar like
  the feels-like one:
  - the meter is empty at 91 °F and full at 106 °F, so normal sits in the
    middle;
  - it's coloured by stage, in EZvac's comfort colours, with the stage beside
    it;
  - `(fever)` shows while ill;
  - it's hidden for players heat and cold don't affect (creative, spectator,
    bypass, other worlds).
- **Where it goes:** EZvac's `{addons}` line, or `{section:liveexposure:body}`
  wherever you put it. Action-bar and boss-bar HUDs show it on one line where
  their format has that token.
- **Settings:** the new `hud:` section (`enabled`, `lines`, `inline`, `fever`,
  `meter.cold-f` and `hot-f`, and the colours).
- **config.yml stays up to date:** on startup it gains every setting added
  since it was written, with its comments. Nothing you set is changed, and the
  previous file is kept as `config.yml.before-<version>.bak`.

### Changed

- **Heat and cold work twice as fast:**
  - `rates.cold-f-per-minute` is now 0.068 (was 0.034), so at 20 °F outside
    the body loses about 2 °F a minute;
  - `rates.heat-f-per-minute` is now 0.054 (was 0.027);
  - where the body settles is unchanged, and so is the recovery rate.

  A config.yml from 0.1.0 that still has the stock rates moves to the new
  ones by itself; rates you changed are kept.
- **The EZvac API** it builds against is now ALPHA 2.1 (API 1.1).

### Fixed

- **A fever never ended where the clock stands still.** Illnesses only moved
  on when the world's day changed, so with `doDaylightCycle` off, or EZvac's
  calendar paused, a fever (and the heat it brings) lasted forever. A full
  day of game time (20 minutes) now counts as a day there too.

## 0.1.0

The first release: body temperature and sickness, built on EZvac's API 1.0.
Needs EZvac ALPHA 2.1 Pilot 4 or newer.

### Added

- **Body temperature:**
  - every survival player has one;
  - it falls in the cold, rises in the heat, and recovers when comfortable;
  - it's driven by EZvac's "feels like", or the water's temperature when swimming;
  - it settles at a balance point for the climate, so a warm day only makes you run a little warm. Heatstroke needs about 108 °F feels-like, and freezing about 20 °F.
- **Stages:**
  - cold: chilled, hypothermia, severe hypothermia, freezing;
  - heat: overheating, heat exhaustion, severe heat, heatstroke;
  - effects include slowness, weakness, mining fatigue, hunger and nausea, with damage at the extremes;
  - deadly by default, and `lethal: false` keeps players at half a heart.
- **Feedback:** the game's own frost overlay for cold, sweat drops falling from overheating players (`sweat.enabled`), and action-bar warnings.
- **Shade:** while the sun is up, a solid block or leaves overhead takes 10 °F off the heat (`shade:`).
- **Leather armour:** it resists cold, at 3.5 points for a chestplate or leggings and 1.5 for a helmet or boots, at 1.5 °F a point.
- **Sickness (optional, on by default):**
  - a daily chance of falling ill, higher in winter and after a day spent cold, with +35 points after time near a sick player;
  - fevers raise the body's resting temperature, with slowness and nausea;
  - illnesses last 2-3 days, and water bottles shorten them by up to a day.
- **Commands:** `/bodytemp`; `/liveexposure reload | set | sick | cure | roll`.
