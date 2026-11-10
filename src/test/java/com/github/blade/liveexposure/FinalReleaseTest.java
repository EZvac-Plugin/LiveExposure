package com.github.blade.liveexposure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** The last release: the shade steps aside where EZvac counts the sun, and 26.x servers are pointed on. */
class FinalReleaseTest {
    @Test
    void ezvacCountsTheSunFromApi19() {
        assertFalse(LiveExposurePlugin.countsSun("1.0"));
        assertFalse(LiveExposurePlugin.countsSun("1.8"), "Pilot 3");
        assertTrue(LiveExposurePlugin.countsSun("1.9"), "Pilot 4");
        assertTrue(LiveExposurePlugin.countsSun("1.12"));
        assertFalse(LiveExposurePlugin.countsSun("2.0"), "a new major version promises nothing");
        assertFalse(LiveExposurePlugin.countsSun("odd"));
        assertFalse(LiveExposurePlugin.countsSun(null));
    }

    @Test
    void minecraft26AndNewerIsEzclimatesGround() {
        assertFalse(LiveExposurePlugin.onNewMinecraft("1.21.1"));
        assertFalse(LiveExposurePlugin.onNewMinecraft("1.21.11"));
        assertTrue(LiveExposurePlugin.onNewMinecraft("26.1.2"));
        assertTrue(LiveExposurePlugin.onNewMinecraft("26.3"));
        assertFalse(LiveExposurePlugin.onNewMinecraft(null));
    }
}
