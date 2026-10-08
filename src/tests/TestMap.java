package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.map.Block;
import engine.map.Map;

/**
 * Tests sur la grille {@link Map} : initialisation, accès aux blocs et
 * détection des bords.
 */
public class TestMap {

    private Map map;

    @Before
    public void setUp() {
        map = new Map(10, 12);
    }

    @Test
    public void testDimensions() {
        assertEquals(10, map.getLineCount());
        assertEquals(12, map.getColumnCount());
    }

    @Test
    public void testEveryBlockExistsAtCorrectIndex() {
        for (int line = 0; line < map.getLineCount(); line++) {
            for (int column = 0; column < map.getColumnCount(); column++) {
                Block block = map.getBlock(line, column);
                assertNotNull(block);
                assertEquals(line, block.getLine());
                assertEquals(column, block.getColumn());
            }
        }
    }

    @Test
    public void testIsOnTop() {
        assertTrue(map.isOnTop(map.getBlock(0, 5)));
        assertFalse(map.isOnTop(map.getBlock(1, 5)));
    }

    @Test
    public void testIsOnBottom() {
        assertTrue(map.isOnBottom(map.getBlock(9, 5)));
        assertFalse(map.isOnBottom(map.getBlock(0, 5)));
    }

    @Test
    public void testIsOnLeftBorder() {
        assertTrue(map.isOnLeftBorder(map.getBlock(3, 0)));
        assertFalse(map.isOnLeftBorder(map.getBlock(3, 1)));
    }

    @Test
    public void testIsOnRightBorder() {
        assertTrue(map.isOnRightBorder(map.getBlock(3, 11)));
        assertFalse(map.isOnRightBorder(map.getBlock(3, 10)));
    }

    @Test
    public void testIsOnBorderTrueOnAnyEdge() {
        assertTrue(map.isOnBorder(map.getBlock(0, 0)));
        assertTrue(map.isOnBorder(map.getBlock(9, 11)));
        assertTrue(map.isOnBorder(map.getBlock(5, 0)));
        assertTrue(map.isOnBorder(map.getBlock(0, 7)));
    }

    @Test
    public void testIsOnBorderFalseInside() {
        assertFalse(map.isOnBorder(map.getBlock(5, 5)));
    }
}
