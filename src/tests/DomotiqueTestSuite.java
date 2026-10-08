package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite globale des tests unitaires du projet Domotique.
 *
 * Regroupe l'ensemble des tests : besoins, horloge, routine, imprévus,
 * meubles et intégration.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestSimulationClock.class,
    TestMasterState.class,
    TestRoomManager.class,
    TestMap.class,
    TestLightAndSwitch.class,
    TestFurniture.class,
    TestRoutineManagerImprevu.class,
    TestMobileElementManager.class
})
public class DomotiqueTestSuite {
}
