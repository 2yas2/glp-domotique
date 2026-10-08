package tests;


import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import config.NeedConfiguration;
import config.SimulationConfiguration;
import engine.state.MasterState;

public class TestMasterState {



/**
 * Teste les besoins du maître (énergie, faim, soif, hygiène) et le calcul d'humeur.
 * Vérifie la décroissance, la détection de besoin critique et la récupération.
 */


    private MasterState state;

    @Before
    public void setUp() {
        state = new MasterState();
        // toutes les valeurs démarrent à 80
    }

    @Test
    public void testInitialValuesAre80() {
        assertEquals(80, state.getEnergy().getValue());
        assertEquals(80, state.getHunger().getValue());
        assertEquals(80, state.getThirst().getValue());
        assertEquals(80, state.getHygiene().getValue());
    }

    @Test
    public void testNeedsDecreaseAfterEnoughRounds() {
        // il faut UPDATE_STATE_SPEED rounds pour déclencher un tick
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        for (int i = 0; i < speed; i++) {
            state.updateStates();
        }
        // après un tick, les valeurs ont baissé
        assertTrue(state.getThirst().getValue() < 80);
        assertTrue(state.getHunger().getValue() < 80);
        assertTrue(state.getEnergy().getValue() < 80);
        assertTrue(state.getHygiene().getValue() < 80);
    }

    @Test
    public void testNoCriticalNeedAtStart() {
        assertEquals(-1, state.getMostCriticalNeed());
    }

    @Test
    public void testThirstBecomeCritical() {
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        for (int i = 0; i < speed * 7; i++) {
            state.updateStates();
        }
        assertTrue("La soif doit être sous 20", state.getThirst().getValue() < 20);
        assertTrue(state.getThirst().isCritical());
        assertEquals(NeedConfiguration.THIRST, state.getMostCriticalNeed());
    }


    @Test
    public void testAugmenterRestoresNeed() {
        // forcer la soif à 0
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        for (int i = 0; i < speed * 10; i++) {
            state.updateStates();
        }
        assertEquals(0, state.getThirst().getValue()); // vérifier qu'elle est bien à 0
        state.getThirst().increase(50);
        assertEquals(50, state.getThirst().getValue()); // vérifier la valeur exacte
    }

    @Test
    public void testAugmenterCannotExceed100() {
        state.getEnergy().increase(200);
        assertEquals(100, state.getEnergy().getValue());
    }

    @Test
    public void testMoodIsPositiveAtStart() {
        assertTrue(state.getMood() > 60);
    }

    @Test
    public void testMoodLabelAtStart() {
        String label = state.getMoodLabel();
        // 80% sur tout, humeur >= 80, on doit être en "Excellent" ou "Happy"
        assertTrue(label.equals("Excellent") || label.equals("Happy"));
    }

    @Test
    public void testMostCriticalNeedPriorityWhenAllCritical() {
        // tirer la soif, faim, hygiène et énergie en dessous de 20
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        for (int i = 0; i < speed * 10; i++) {
            state.updateStates();
        }
        // la priorité commence par THIRST
        assertEquals(NeedConfiguration.THIRST, state.getMostCriticalNeed());
    }

    @Test
    public void testMoodLabelDecreasesWithNeeds() {
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        for (int i = 0; i < speed * 10; i++) {
            state.updateStates();
        }
        String label = state.getMoodLabel();
        // tous les besoins à 0, donc humeur très basse
        assertTrue(label.equals("Very unhappy") || label.equals("Uneasy"));
    }

    @Test
    public void testIncreaseHungerToFull() {
        state.getHunger().increase(50);
        assertEquals(100, state.getHunger().getValue());
    }

    @Test
    public void testHygieneCriticalAfterEnoughTicks() {
        int speed = SimulationConfiguration.UPDATE_STATE_SPEED;
        // hygiène baisse de 5 par tick, 14 ticks = 70, soit 80 - 70 = 10
        for (int i = 0; i < speed * 14; i++) {
            state.updateStates();
        }
        assertTrue(state.getHygiene().isCritical());
    }
}