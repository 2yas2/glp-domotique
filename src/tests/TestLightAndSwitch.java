package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.item.device.Light;
import engine.item.device.Switch;
import engine.map.Block;

/**
 * Tests des classes {@link Light} et {@link Switch}.
 * L'interrupteur expose le même état que la lumière sous-jacente.
 */
public class TestLightAndSwitch {

    private Light light;
    private Switch interrupter;
    private Block block;

    @Before
    public void setUp() {
        light = new Light();
        block = new Block(2, 3);
        interrupter = new Switch(light, block);
    }

    @Test
    public void testLightStartsOff() {
        assertFalse(light.isOn());
    }

    @Test
    public void testTurnOnAndOff() {
        light.turnOn();
        assertTrue(light.isOn());
        light.turnOff();
        assertFalse(light.isOn());
    }

    @Test
    public void testSwitchReflectsLightState() {
        assertFalse(interrupter.isOn());
        interrupter.turnOn();
        assertTrue(interrupter.isOn());
        assertTrue(light.isOn());
        interrupter.turnOff();
        assertFalse(interrupter.isOn());
    }

    @Test
    public void testSwitchPosition() {
        assertEquals(block, interrupter.getPosition());
    }

    @Test
    public void testSwitchExposesLight() {
        assertTrue(interrupter.getLight() == light);
    }
}
