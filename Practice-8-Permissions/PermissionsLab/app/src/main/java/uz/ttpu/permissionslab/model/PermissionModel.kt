package uz.ttpu.permissionslab.model

/**
 * 1-topshiriq: Kotlin modeli: rad etish holatlari ("Denied twice" modeli)
 *
 * Xossalari:
 * - granted: ruxsat berilganmi
 * - rationale: tushuntirish ko'rsatilishi kerakmi (faqat 1 marta rad etilganda true)
 * - dialogShows: dialog ko'rinadimi
 */
class PermissionModel {
    var granted: Boolean = false
        private set

    var rationale: Boolean = false
        private set

    var dialogShows: Boolean = true
        private set

    private var denialCount: Int = 0

    fun request(userAllows: Boolean) {
        if (granted) {
            println("Allaqachon ruxsat berilgan.")
            return
        }

        // Ikki marta rad etilgandan keyin tizim dialogni umuman ko'rsatmaydi (Don't ask again)
        if (denialCount >= 2) {
            dialogShows = false
            rationale = false
            println("dialog yo'q, darhol rad")
            return
        }

        dialogShows = true

        if (userAllows) {
            granted = true
            rationale = false
            println("Foydalanuvchi ruxsat berdi.")
        } else {
            denialCount++
            granted = false
            // rationale faqat bir marta rad etilganda true bo'ladi
            rationale = (denialCount == 1)
            if (denialCount >= 2) {
                dialogShows = false
            }
            println("Foydalanuvchi rad etdi (denialCount = $denialCount).")
        }
    }

    override fun toString(): String {
        return "PermissionModel(granted=$granted, rationale=$rationale, dialogShows=$dialogShows)"
    }
}

/**
 * Kotlin Playground / CLI uchun mustaqil ishga tushirish funksiyasi
 */
fun main() {
    val model = PermissionModel()
    println("--- Boshlang'ich holat ---")
    println(model)

    println("\n1-so'rov: foydalanuvchi RAD etdi")
    model.request(userAllows = false)
    println(model)

    println("\n2-so'rov: foydalanuvchi RAD etdi (ikkinchi marta)")
    model.request(userAllows = false)
    println(model)

    println("\n3-so'rov: tizim dialog chiqarishga urinadi (rad etilgan holat)")
    model.request(userAllows = false)
    println(model)

    println("\n4-so'rov: sozlamalardan keyin foydalanuvchi RUXSAT berdi")
    // Agar sozlamalardan yoqilsa:
    val freshModelAfterSettings = PermissionModel().apply { request(userAllows = true) }
    println(freshModelAfterSettings)
}
