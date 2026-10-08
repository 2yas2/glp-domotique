package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Oven;
import engine.item.furniture.Sofa;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;
import engine.map.Block;
import engine.state.Energy;
import engine.state.Hunger;
import engine.state.Hygiene;
import engine.state.Thirst;

/**
 * Tests des meubles. Chaque meuble doit pouvoir avancer son cycle (tick),
 * appliquer son effet sur le besoin associé et savoir quand il a fini.
 */
public class TestFurniture {

    private ArrayList<Block> blocks;

    @Before
    public void setUp() {
        blocks = new ArrayList<Block>();
        blocks.add(new Block(0, 0));
        blocks.add(new Block(0, 1));
    }

    @Test
    public void testBedTickAndDone() {
        Bed bed = new Bed(blocks);
        for (int i = 0; i < 15; i++) {
            bed.tick();
        }
        assertTrue(bed.isDone());
        assertEquals(15, bed.getTotalSleptRounds());
    }

    @Test
    public void testBedRecoversEnergy() {
        Bed bed = new Bed(blocks);
        Energy energy = new Energy();
        for (int i = 0; i < 100; i++) {
            energy.tick();
        }
        bed.applyEffects(energy);
        assertTrue(energy.getValue() > 0);
    }

    @Test
    public void testBedReset() {
        Bed bed = new Bed(blocks);
        bed.tick();
        bed.reset();
        assertFalse(bed.isDone());
    }

    @Test
    public void testBathtubFinishesAfterDuration() {
        Bathtub bathtub = new Bathtub(blocks);
        bathtub.tick();
        bathtub.tick();
        assertTrue(bathtub.isDone());
    }

    @Test
    public void testBathtubAppliesHygiene() {
        Bathtub bathtub = new Bathtub(blocks);
        Hygiene hygiene = new Hygiene();
        hygiene.tick();
        int before = hygiene.getValue();
        bathtub.applyEffects(hygiene);
        assertTrue(hygiene.getValue() > before);
    }

    @Test
    public void testWaterBottleAppliesThirst() {
        WaterBottle bottle = new WaterBottle(blocks);
        Thirst thirst = new Thirst();
        thirst.tick();
        int before = thirst.getValue();
        bottle.applyEffects(thirst);
        assertTrue(thirst.getValue() > before);
    }

    @Test
    public void testOvenAppliesHunger() {
        Oven oven = new Oven(blocks);
        Hunger hunger = new Hunger();
        hunger.tick();
        int before = hunger.getValue();
        oven.applyEffects(hunger);
        assertTrue(hunger.getValue() > before);
    }

    @Test
    public void testOvenInitialDishesNotEmpty() {
        Oven oven = new Oven(blocks);
        assertFalse(oven.getDishes().isEmpty());
    }

    @Test
    public void testOvenAddDishAccepted() {
        Oven oven = new Oven(blocks);
        int before = oven.getDishes().size();
        oven.addDish("Couscous");
        assertEquals(before + 1, oven.getDishes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOvenAddDishRejectsEmpty() {
        Oven oven = new Oven(blocks);
        oven.addDish("");
    }

    @Test
    public void testOvenSetCurrentDishOverridesNext() {
        Oven oven = new Oven(blocks);
        oven.setCurrentDish("Tagine");
        assertEquals("Tagine", oven.getCurrentDish());
        assertTrue(oven.getStatus().contains("Tagine"));
    }

    @Test
    public void testOvenIgnoresNullDishOverride() {
        Oven oven = new Oven(blocks);
        oven.setCurrentDish("Tagine");
        oven.setCurrentDish(null);
        assertEquals("Tagine", oven.getCurrentDish());
    }

    @Test
    public void testOvenResetClearsDish() {
        Oven oven = new Oven(blocks);
        oven.tick();
        oven.reset();
        assertEquals(0, oven.getCurrentRounds());
    }

    @Test
    public void testTelevisionStandbyShowsOff() {
        Television tv = new Television(blocks);
        assertEquals("TV off", tv.getStatus());
    }

    @Test
    public void testTelevisionShowSelection() {
        Television tv = new Television(blocks);
        tv.activate();
        tv.setCurrentShow("Friends");
        assertTrue(tv.getStatus().contains("Friends"));
    }

    @Test
    public void testTelevisionResetClearsShow() {
        Television tv = new Television(blocks);
        tv.activate();
        tv.setCurrentShow("Friends");
        tv.reset();
        assertEquals(null, tv.getCurrentShow());
    }

    @Test
    public void testSofaActivatesTelevision() {
        Television tv = new Television(blocks);
        Sofa sofa = new Sofa(blocks, tv);
        sofa.activate();
        assertTrue(tv.isActive());
    }

    @Test
    public void testSofaStandbyTurnsTvOff() {
        Television tv = new Television(blocks);
        Sofa sofa = new Sofa(blocks, tv);
        sofa.activate();
        sofa.standby();
        assertFalse(tv.isActive());
    }

    @Test
    public void testSofaResetTurnsTvOff() {
        Television tv = new Television(blocks);
        Sofa sofa = new Sofa(blocks, tv);
        sofa.activate();
        sofa.tick();
        sofa.reset();
        assertFalse(tv.isActive());
    }

    @Test
    public void testFurnitureHasBlocks() {
        Bed bed = new Bed(blocks);
        assertNotNull(bed.getBlocks());
        assertEquals(2, bed.getBlocks().size());
    }

    @Test
    public void testFurnitureOccupied() {
        Bed bed = new Bed(blocks);
        assertTrue(bed.occupied(blocks.get(0)));
        assertFalse(bed.occupied(new Block(99, 99)));
    }
}
