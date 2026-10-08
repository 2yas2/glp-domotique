package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import config.ImprevuConfiguration;
import engine.map.Map;
import process.furniture.FurnitureManager;
import process.game.GameBuilder;
import process.imprevu.ImprevuManager;
import process.mobile.MobileElementManager;
import process.mobile.MobileInterface;
import process.room.RoomManager;
import process.routine.RoutineManager;

/**
 * Tests d'integration legers pour {@link MobileElementManager}.
 * On verifie surtout le cablage des composants.
 */
public class TestMobileElementManager {

    private MobileInterface mobile;
    private MobileElementManager concreteMobile;

    @Before
    public void setUp() {
        Map map = GameBuilder.buildMap();
        RoomManager roomManager = GameBuilder.buildRoomManager(map);
        FurnitureManager furnitureManager = GameBuilder.buildFurnitureManager(map);
        roomManager.setFurnitureManager(furnitureManager);
        mobile = GameBuilder.buildInitMobile(map, roomManager, furnitureManager);
        concreteMobile = (MobileElementManager) mobile;
    }

    @Test
    public void testMasterStateIsAvailable() {
        assertNotNull(mobile.getMasterState());
        assertEquals(80, mobile.getMasterState().getEnergy().getValue());
    }

    @Test
    public void testClockIsAvailable() {
        assertNotNull(mobile.getClock());
        assertEquals("07:30", mobile.getClock().getFormattedTime());
    }

    @Test
    public void testImprevuManagerIsWired() {
        ImprevuManager imprevuManager = concreteMobile.getImprevuManager();
        assertNotNull(imprevuManager);
        assertEquals(ImprevuConfiguration.IMPREVU_COUNT, imprevuManager.getAvailable().size());
    }

    @Test
    public void testRoutineManagerIsWired() {
        RoutineManager routineManager = concreteMobile.getRoutineManager();
        assertNotNull(routineManager);
        assertNotNull(routineManager.getCurrentStep());
    }

    @Test
    public void testNotificationsContainStartupMessage() {
        String text = mobile.getNotifications();
        assertTrue(text.contains("simulation"));
    }

    @Test
    public void testIsAtHomeAtStart() {
        assertTrue(mobile.isAtHome());
    }

    @Test
    public void testNextRoundAdvancesClock() {
        String before = mobile.getClock().getFormattedTime();
        mobile.nextRound();
        String after = mobile.getClock().getFormattedTime();
        assertFalse(before.equals(after));
    }

    @Test
    public void testRoutineStatusContainsImprevuLine() {
        String text = mobile.getRoutineStatus();
        assertTrue(text.contains("Imp"));
    }
}
