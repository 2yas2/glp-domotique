package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import process.routine.SimulationClock;

/**
 * Teste l'horloge de simulation.
 * Vérifie l'avancement du temps, le passage à l'heure suivante et le reset.
 */
public class TestSimulationClock {

    private SimulationClock clock;

    @Before
    public void setUp() {
        clock = new SimulationClock();
        // horloge commence à 07:30, dayIndex=0
    }

    @Test
    public void testInitialValues() {
        assertEquals(7, clock.getHour());
        assertEquals(30, clock.getMinute());
        assertEquals(0, clock.getDayIndex());
        assertEquals(0, clock.getTotalRounds());
    }

    @Test
    public void testOneRoundIncrementsMinute() {
        clock.nextRound();
        assertEquals(31, clock.getMinute());
        assertEquals(1, clock.getTotalRounds());
    }

    @Test
    public void testHourChangesAfter60Minutes() {
        // avancer de 30 rounds pour atteindre 08:00
        for (int i = 0; i < 30; i++) {
            clock.nextRound();
        }
        assertEquals(8, clock.getHour());
        assertEquals(0, clock.getMinute());
    }

    @Test
    public void testDayChangesAfter24Hours() {
        // avancer jusqu'au lendemain : (24h - 7h30) * 60 = 990 rounds
        for (int i = 0; i < 990; i++) {
            clock.nextRound();
        }
        assertEquals(1, clock.getDayIndex());
        assertEquals(0, clock.getHour());
        assertEquals(0, clock.getMinute());
    }

    @Test
    public void testFormattedTime() {
        assertEquals("07:30", clock.getFormattedTime());
        clock.nextRound();
        assertEquals("07:31", clock.getFormattedTime());
    }

    @Test
    public void testReset() {
        for (int i = 0; i < 100; i++) {
            clock.nextRound();
        }
        clock.reset();
        assertEquals(7, clock.getHour());
        assertEquals(30, clock.getMinute());
        assertEquals(0, clock.getDayIndex());
        assertEquals(0, clock.getTotalRounds());
    }
}