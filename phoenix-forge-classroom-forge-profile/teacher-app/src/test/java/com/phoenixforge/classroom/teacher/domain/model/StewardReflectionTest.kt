package com.phoenixforge.classroom.teacher.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StewardReflectionTest {

    @Test
    fun `catalog has five L3 axes`() {
        assertEquals(5, StewardReflectionCatalog.axes.size)
    }

    @Test
    fun `withValue updates only targeted axis`() {
        val base = StewardReflection()
        val mentalAxis = StewardReflectionCatalog.axes.first { it.id == StewardReflectionAxis.AxisId.MENTAL }
        val updated = StewardReflectionCatalog.withValue(base, mentalAxis, 80)

        assertEquals(80, updated.mental)
        assertEquals(null, updated.emotional)
    }

    @Test
    fun `poleLabel maps low mid and high bands`() {
        val axis = StewardReflectionCatalog.axes.first { it.id == StewardReflectionAxis.AxisId.MENTAL }

        assertEquals("Distracted", StewardReflectionCatalog.poleLabel(axis, 10))
        assertEquals("Balanced", StewardReflectionCatalog.poleLabel(axis, 50))
        assertEquals("Focused", StewardReflectionCatalog.poleLabel(axis, 90))
    }

    @Test
    fun `toTileFields round trips through IntentTile`() {
        val reflection = StewardReflection(
            mental = 20,
            emotional = 40,
            physical = 60,
            educational = 80,
            behavioral = 10,
        )
        val tile = IntentTile(
            title = "Test",
            reflectionMental = reflection.toTileFields().reflectionMental,
            reflectionEmotional = reflection.toTileFields().reflectionEmotional,
            reflectionPhysical = reflection.toTileFields().reflectionPhysical,
            reflectionEducational = reflection.toTileFields().reflectionEducational,
            reflectionBehavioral = reflection.toTileFields().reflectionBehavioral,
        )

        assertEquals(reflection, StewardReflection.fromTile(tile))
    }
}
