package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import config.ImprevuConfiguration;
import config.RoutineConfiguration;
import process.imprevu.ImprevuManager;
import process.routine.RoutineManager;
import process.routine.RoutinePlan;
import process.routine.RoutineStep;

/**
 * Tests d'integration entre {@link RoutineManager} et l'imprevu du jour.
 * Verifie que le tirage est appele a chaque nouvelle journee.
 */
public class TestRoutineManagerImprevu {

    private RoutineManager routineManager;
    private ImprevuManager imprevuManager;

    @Before
    public void setUp() {
        // graine choisie : produit un imprevu pour le 1er roll
        imprevuManager = new ImprevuManager(new Random(123L)) {
            @Override
            public boolean rollForImprevu(int dayIndex) {
                selectImprevu(ImprevuConfiguration.RUSH_DAY);
                return true;
            }
        };
        routineManager = new RoutineManager(imprevuManager);
    }

    @Test
    public void testImprevuModifiesActiveDayPlan() {
        boolean has = routineManager.evaluateDailyImprevu(0);
        assertTrue(has);
        RoutinePlan plan = routineManager.getCurrentPlan();

        for (int i = 0; i < plan.size(); i++) {
            RoutineStep step = plan.getStep(i);
            int original = originalWeekdayDurationFor(i);
            assertEquals("step " + i, original / 2, step.getDurationInMinutes());
        }
    }

    @Test
    public void testEvaluateIsIdempotentSameDay() {
        routineManager.evaluateDailyImprevu(0);
        RoutineStep firstSnapshot = routineManager.getCurrentStep();
        routineManager.evaluateDailyImprevu(0);
        RoutineStep secondSnapshot = routineManager.getCurrentStep();
        assertEquals(firstSnapshot.getDurationInMinutes(), secondSnapshot.getDurationInMinutes());
    }

    @Test
    public void testNewDayRebuildsFromTemplate() {
        routineManager.evaluateDailyImprevu(0);
        routineManager.evaluateDailyImprevu(1);

        assertNotNull(routineManager.getCurrentStep());
        assertEquals(RoutineConfiguration.WEEKDAY, routineManager.getCurrentDayType());
    }

    @Test
    public void testWithoutImprevuManagerEvaluationDoesNothing() {
        RoutineManager bare = new RoutineManager();
        assertFalse(bare.evaluateDailyImprevu(0));
    }

    private int originalWeekdayDurationFor(int stepIndex) {
        RoutineManager fresh = new RoutineManager();
        return fresh.getCurrentPlan().getStep(stepIndex).getDurationInMinutes();
    }
}
