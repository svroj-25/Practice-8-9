package uz.ttpu.permissionslab

import org.junit.Assert.*
import org.junit.Test
import uz.ttpu.permissionslab.model.PermissionModel

class PermissionModelTest {

    @Test
    fun testDeniedTwiceWorkflow() {
        val model = PermissionModel()
        assertTrue("Boshida dialogShows true bo'lishi kerak", model.dialogShows)
        assertFalse("Boshida granted false bo'lishi kerak", model.granted)
        assertFalse("Boshida rationale false bo'lishi kerak", model.rationale)

        // 1-so'rov: rad
        model.request(userAllows = false)
        assertFalse(model.granted)
        assertTrue("Birinchi marta rad etilganda rationale true bo'lishi shart", model.rationale)

        // 2-so'rov: rad (ikkinchi marta)
        model.request(userAllows = false)
        assertFalse(model.granted)
        assertFalse("Ikki marta rad etilganda rationale yana false bo'ladi", model.rationale)
        assertFalse("Ikki marta rad etilgandan keyin dialogShows false bo'ladi", model.dialogShows)

        // 3-so'rov: dialog yo'q, darhol rad
        model.request(userAllows = false)
        assertFalse(model.granted)
        assertFalse(model.dialogShows)
    }
}
