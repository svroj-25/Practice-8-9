# Android Jetpack Compose: Practice 8 & Practice 9

**Fan:** Android Development / Software Modeling and Design (SMD)  
**Talaba:** Sirojbek Xolmatov  
**Guruh:** AK2024010  
**Sana:** 30.10.2026  
**GitHub Repozitoriya:** `Practice-8-9` (`git@github.com:svroj-25/Practice-8-9.git`)  

---

## 📌 Mundarija

1. [Practice 8 — Permissions (Ruxsatnomalar)](#-practice-8--permissions-ruxsatnomalar)
   - [A qism: Nazariy Savollar](#a-qism-nazariy-savollar-19-ta-savol)
   - [B qism: PermissionsLab Loyihasi](#b-qism-amaliy-topshiriqlar-permissionslab)
   - [Hujjatlar (Word va PDF)](#hujjatlar-practice-8)
2. [Practice 9 — Side-effects (Nojo'ya ta'sirlar va hodisalar)](#-practice-9--side-effects-compose-hodisalari)
   - [A qism: Nazariy Savollar](#a-qism-nazariy-savollar-19-ta-savol-1)
   - [B qism: SideEffectsLab Loyihasi](#b-qism-amaliy-topshiriqlar-sideeffectslab)
   - [Hujjatlar (Word va PDF)](#hujjatlar-practice-9)

---

## 🛡️ Practice 8 — Permissions (Ruxsatnomalar)

Install-time, Runtime va Special ruxsatnomalarning Android 6.0 dan Android 14 gacha bo'lgan ishlash mexanizmlari.

### A qism: Nazariy Savollar (19 ta savol)
- Ruxsatnoma so'rashdan oldingi workflow qarorlari (Photo Picker, Intent orqali ruxsatsiz yechim).
- Install-time, Runtime va Special toifalarining kod hajmiga ta'siri.
- Normal ruxsatnomalar (`INTERNET`) va `SecurityException`.
- `Signature` ruxsatnomalari va `protectionLevel`.
- `shouldShowRationale` holatlar zanjiri va «Don't ask again» da Sozlamalarga yo'naltirish.
- `rememberPermissionState` va Compose recomposition xotirasi.
- `RequestMultiplePermissions` (Aniq va taxminiy joylashuv).
- Special ruxsatnomalar jadvali (`SYSTEM_ALERT_WINDOW`, `WRITE_SETTINGS` va h.k.).
- «Tekshir -> Sozlamalarni och -> qaytganda yana tekshir» andozasi (`LifecycleEventEffect(ON_RESUME)`).
- Fon joylashuvi (`ACCESS_BACKGROUND_LOCATION`) ning 2-bosqichli tartibi.
- Permission guruhlari va `<uses-feature>` ning `required="false"` atributi.
- Bir martalik ruxsatnomalar («Only this time», API 30+) va Auto-reset.
- Android 13 (API 33) `POST_NOTIFICATIONS` va `revokeSelfPermissionOnKill`.

### B qism: Amaliy topshiriqlar (`PermissionsLab`)
Paket: `uz.ttpu.permissionslab`
- **1-topshiriq:** `PermissionModel.kt` — 2 marta rad etilgandan so'ng dialogni bloklovchi Kotlin modeli va testlari.
- **2-topshiriq:** `ConnectivityScreen.kt` — Normal ruxsatnomalar (`INTERNET`, `ACCESS_NETWORK_STATE`) va online/offline monitoringi.
- **3-topshiriq:** `CameraScreen.kt` — Kamera ruxsatining 3 ta holati (berilgan, rationale, rad etilgan -> Sozlamalarga o'tish tugmasi).
- **4-topshiriq:** `OverlayScreen.kt` — `SYSTEM_ALERT_WINDOW` ruxsatini Switch orqali boshqarish va `ON_RESUME` da tekshirish.
- **5-topshiriq:** `LocationScreen.kt` — Bir martalik joylashuv («Faqat bu safar»), fonga o'tib qaytganda ruxsat bekor bo'lishini `ON_RESUME` da aniqlash.
- **6-topshiriq (Bonus):** `NotificationScreen.kt` — Android 13+ da `POST_NOTIFICATIONS` so'rash va test bildirishnomasi jo'natish.

### Hujjatlar (Practice 8):
- 📄 [To'liq Markdown hisoboti](Practice-8-Permissions/PRACTICE_8_JAVOBLAR_VA_HISOBOT.md)
- 📝 [To'ldirilgan Word hujjati](Practice-8-Permissions/Practice%208%20%20Permissions.docx)
- 📑 [To'ldirilgan PDF hujjati](Practice-8-Permissions/Practice%208%20%20Permissions.pdf)

---

## ⚡ Practice 9 — Side-effects (Compose hodisalari)

Jetpack Compose-dagi 8 ta asosiy Effect API'larining arxitekturasini o'rganish.

### A qism: Nazariy Savollar (19 ta savol)
- Composable tanasida side-effect yozish nega xavfli ekani va UDF printsipi.
- 8 ta API jadvali (`LaunchedEffect`, `rememberCoroutineScope`, `rememberUpdatedState`, `DisposableEffect`, `SideEffect`, `produceState`, `derivedStateOf`, `snapshotFlow`).
- `LaunchedEffect` va `isActive` orqali korutinalarni bekor qilish.
- Nega `LaunchedEffect(true)` "while(true)" kabi shubhali ekani.
- Callback ichidan `LaunchedEffect` chaqirib bo'lmasligi va `rememberCoroutineScope` zarurati.
- `rememberUpdatedState` orqali eski lambdalarni (closure capture) yangilash.
- `DisposableEffect` va `onDispose` qoidalari, memory leak larni oldini olish.
- `MiniDisposable` modelining konsol chiqishi tahlili.
- `SideEffect` ning muvaffaqiyatli commit dan keyin ishlashi.
- `produceState` va `conflation` tushunchasi.
- `derivedStateOf` orqali scroll recomposition sonini 9 tadan 3 taga tushirish hisobi.
- String birlashtirishda `derivedStateOf` ishlatish nega xato ekani (1:1 chastota).
- Audio waveform da `derivedStateOf` ishlatish va ishlatilmaslik shartlari.
- `snapshotFlow` (cold Flow) va filtrlar orqali 0-elementdan o'tganda faqat bitta analytics yuborish.
- Effect kalitlari tahlili (`keys = []`, `[owner]`, `[owner, tick]`).
- `ChatScreen.kt` kodidagi 4 ta xatoni tuzatish.
- 8 ta API ning to'liq yakuniy jadvali.

### B qism: Amaliy topshiriqlar (`SideEffectsLab`)
Paket: `uz.ttpu.sideeffectslab`
- **1-topshiriq:** `LandingScreen.kt` — 2 soniyalik Splash ekranida `rememberUpdatedState` bilan eng oxirgi callback lambdasini ushlash.
- **2-topshiriq:** `SnackbarScreen.kt` — Tugma onClick callback ichidan `rememberCoroutineScope` orqali Snackbar chiqarish.
- **3-topshiriq:** `LifecycleScreen.kt` — `DisposableEffect` orqali `LifecycleEventObserver` ni ulash va `onDispose` da toza xotiradan o'chirish.
- **4-topshiriq:** `AnalyticsScreen.kt` — Compose holatini tashqi ob'yektga `SideEffect` orqali xavfsiz uzatish va recomposition hisoblagichi.
- **5-topshiriq:** `ScrollScreen.kt` — 100 ta elementli `LazyColumn`da `derivedStateOf` bilan "Tepaga qaytish" tugmasi hamda `snapshotFlow` bilan yagona analytics hodisasi.
- **6-topshiriq (Bonus):** `ImageScreen.kt` — Soxta asinxron yuklash funksiyasini `produceState` yordamida `Loading -> Success / Error` holatlariga o'tkazish.

### Hujjatlar (Practice 9):
- 📄 [To'liq Markdown hisoboti](Practice-9-Side-effects/PRACTICE_9_JAVOBLAR_VA_HISOBOT.md)
- 📝 [To'ldirilgan Word hujjati](Practice-9-Side-effects/Practice%209%20%20Side-effects.docx)
- 📑 [To'ldirilgan PDF hujjati](Practice-9-Side-effects/Practice%209%20%20Side-effects.pdf)

---

## 🛠️ Loyihalarni Ishga Tushirish (Build & Run)

Har ikkala loyiha ham mustaqil Android Gradle loyihasi sifatida sozlangan:

```bash
# Practice 8 loyihasini tekshirish va yig'ish:
cd Practice-8-Permissions/PermissionsLab
./gradlew testDebugUnitTest assembleDebug

# Practice 9 loyihasini tekshirish va yig'ish:
cd Practice-9-Side-effects/SideEffectsLab
./gradlew assembleDebug
```
