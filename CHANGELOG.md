# Changelog

All notable changes to LiveExposure are recorded here.

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
