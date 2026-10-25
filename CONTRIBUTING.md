# Contributing to LiveExposure

Bug reports, ideas and pull requests are welcome.

## Requirements

- Paper 1.21.1 (or a fork such as Purpur) with EZvac ALPHA 2.1 Pilot 4 or newer
- Java 21
- Maven 3.9 or newer

## Build and test

```shell
mvn clean package
```

This compiles, runs the tests, and writes `target/LiveExposure-<version>.jar`.
Every push and pull request is built and tested by GitHub Actions.

## How it fits together

- **The code only touches EZvac through its public API** (`ezvac-api`, from JitPack). Never reach into EZvac's internals: that is what keeps LiveExposure working across EZvac releases.
- **The pure, unit-tested logic is in `model/`:**
  - `BodyModel` steps a body temperature;
  - `Stage` names it;
  - `SicknessModel` holds the sickness arithmetic;
  - `ExposureSettings` reads and checks config.yml.

  Add tests there when you change them.
- **The Bukkit side is three classes:**
  - `ExposureListener` reacts to EZvac's `PlayerClimateUpdateEvent`;
  - `SicknessService` handles days, contagion and water;
  - `Commands`.
