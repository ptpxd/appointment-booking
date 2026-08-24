---

description: "Végrehajtható feladatlista az időpontfoglalási rendszerhez"
---

# Tasks: Idopontfoglalasi Rendszer

**Input**: Tervezesi dokumentumok a `specs/001-appointment-booking-system/` konyvtarbol

**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md), [research.md](research.md), [data-model.md](data-model.md), [contracts/rest-api.md](contracts/rest-api.md), [quickstart.md](quickstart.md)

**Tests**: A tesztek kotelezoek, mert a feature leirasa es az alkotmany eloirja az implementacio, a teszteles es a kritikus uzleti viselkedes automatikus bizonyitasat.

**Organization**: A feladatok tortenetenkent csoportositottak, hogy minden tortenet kulon megvalosithato es ellenorizheto legyen.

## Format: `[ID] [P?] [Story] Leiras`

- **[P]**: Parhuzamosan futtathato, masik befejezetlen feladattol es cel-fajltol fuggetlen feladat
- **[USn]**: A kapcsolodo user story azonositoja
- Minden feladat konkret fajlutvonalat tartalmazza

## Path Conventions

- Backend: `backend/src/main/java/com/appointmentbooking/` es `backend/src/test/java/com/appointmentbooking/`
- Frontend: `frontend/src/` es `frontend/tests/`
- Database migrations: `backend/src/main/resources/db/migration/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: A ket alkalmazas fejlesztheto, tesztelheto alapstrukturanak letrehozasa.

- [X] T001 Hozd letre a Maven Spring Boot projektet Java 21 beallitassal, a Web, Data JPA, Validation, Security, Mail, Flyway es Actuator fuggosegekkel a `backend/pom.xml` fajlban.
- [X] T002 [P] Inicializald a React 19, TypeScript, Vite es Tailwind CSS projektet a `frontend/package.json`, `frontend/vite.config.ts`, `frontend/tsconfig.json` es `frontend/src/index.css` fajlokban.
- [X] T003 [P] Allitsd be a helyi PostgreSQL es email-tesztfiok szolgaltatasokat a `docker-compose.yml` fajlban.
- [X] T004 [P] Hozd letre a backend es frontend lint-, format- es statikus ellenorzesi konfiguraciojat a `backend/pom.xml`, `frontend/eslint.config.js` es `frontend/.prettierrc.json` fajlokban.
- [X] T005 [P] Allitsd be a unit, integracios es E2E tesztparancsokat a `backend/pom.xml`, `frontend/vitest.config.ts` es `frontend/playwright.config.ts` fajlokban.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: A kozos perzisztencia-, HTTP-, teszt- es frontend-infrastruktura kialakitasa. Egyetlen user story sem kezdodhet el e fazis nelkul.

- [X] T006 Keszitsd el a Provider, BookableSlot, Reservation es ConfirmationToken tablak kezdeti Flyway migraciojat a `backend/src/main/resources/db/migration/V1__initial_schema.sql` fajlban.
- [X] T007 Alakitsd ki a domain entitasokat, statusz enumokat es alkalmazasi portokat a `backend/src/main/java/com/appointmentbooking/domain/provider/Provider.java`, `backend/src/main/java/com/appointmentbooking/domain/slot/BookableSlot.java`, `backend/src/main/java/com/appointmentbooking/domain/reservation/Reservation.java` es `backend/src/main/java/com/appointmentbooking/domain/confirmation/ConfirmationToken.java` fajlokban.
- [X] T008 Valositsd meg a JPA adattarolo adapterek alapjat, beleertve az idosav zarolasara szolgalo repository metódust a `backend/src/main/java/com/appointmentbooking/adapter/out/persistence/ProviderJpaRepository.java`, `backend/src/main/java/com/appointmentbooking/adapter/out/persistence/BookableSlotJpaRepository.java` es `backend/src/main/java/com/appointmentbooking/adapter/out/persistence/ReservationJpaRepository.java` fajlokban.
- [X] T009 [P] Hozd letre az egységes validacios hibavalaszt, az uzleti kivetelkezelot es a hibakodokat a `backend/src/main/java/com/appointmentbooking/adapter/in/web/ApiExceptionHandler.java` es `backend/src/main/java/com/appointmentbooking/adapter/in/web/ApiErrorResponse.java` fajlokban.
- [X] T010 [P] Allitsd be a publikus es kesobbi szolgaltatoi API-utvonalak keretet, a CORS-t es a CSRF strategiat a `backend/src/main/java/com/appointmentbooking/config/SecurityConfig.java` fajlban.
- [X] T011 [P] Keszits Testcontainers PostgreSQL tesztbazist es kozos tesztadat-gyarakat a `backend/src/test/java/com/appointmentbooking/integration/PostgresIntegrationTest.java` es `backend/src/test/java/com/appointmentbooking/support/AppointmentFixtures.java` fajlokban.
- [X] T012 [P] Hozd letre a frontend routert, a tipizalt HTTP kliens alapjat, a globalis hibaallapotot es az API origin konfiguraciojat a `frontend/src/app/router.tsx`, `frontend/src/api/httpClient.ts` es `frontend/src/app/App.tsx` fajlokban.

**Checkpoint**: Az alapok keszek, a backend egy valodi PostgreSQL tesztadatbazissal fut, a frontend pedig kepes API-valaszok kezelesere.

---

## Phase 3: User Story 1 - Idopont foglalasa es megerositese (Priority: P1) MVP

**Goal**: A vendeg egy szolgaltato szabad idosavara foglalasi kerelmet indit, emailes megerositest kap, majd egyetlen alkalommal veglegesiti a foglalast.

**Independent Test**: Egy elore letrehozott szolgaltato es szabad idosav mellett a vendeg kivalasztja az idosavot, elinditja a foglalast, a teszt-emailbol kapott tokennel megerositi, es a foglalas `CONFIRMED`, az idosav `BOOKED` allapotba kerul.

### Tests for User Story 1

- [X] T013 [P] [US1] Ird meg a foglalasi es token-allapotatmenetek unit tesztjeit a `backend/src/test/java/com/appointmentbooking/unit/reservation/ReservationStateMachineTest.java` fajlban.
- [X] T014 [P] [US1] Ird meg a publikus idosavlista, foglalasinditas es megerosites API contract tesztjeit a `backend/src/test/java/com/appointmentbooking/contract/PublicBookingControllerContractTest.java` fajlban.
- [X] T015 [P] [US1] Ird meg a PostgreSQL-zarolast es a ket egyideju foglalasi keresbol pontosan egy nyertest ellenorzo integracios tesztet a `backend/src/test/java/com/appointmentbooking/integration/ConcurrentReservationIntegrationTest.java` fajlban.
- [X] T016 [P] [US1] Ird meg a lejarati feladat altal felszabaditott idosav es az ujrafoglalhatosag integracios tesztet a `backend/src/test/java/com/appointmentbooking/integration/ReservationExpiryIntegrationTest.java` fajlban.
- [X] T017 [P] [US1] Ird meg a foglalasi urlap betoltesi, sikeres, idopontutkozesi es `aria-live` allapotainak komponens tesztjeit a `frontend/tests/integration/BookingFlow.test.tsx` fajlban.
- [X] T018 [P] [US1] Ird meg az emailes foglalasinditas es megerosites vegponttol vegpontig tarto tesztet a `frontend/tests/e2e/booking-confirmation.spec.ts` fajlban.

### Implementation for User Story 1

- [X] T019 [US1] Valositsd meg a foglalasinditasi use case-t tranzakcios idosavzarolassal, `PENDING_CONFIRMATION` allapotvaltasaval es 15 perces lejarattal a `backend/src/main/java/com/appointmentbooking/application/reservation/CreatePendingReservationUseCase.java` fajlban.
- [X] T020 [US1] Valositsd meg az egyszer hasznalatos, hash-elve tarolt token eloallitasat es a foglalas veglegesiteset a `backend/src/main/java/com/appointmentbooking/application/reservation/ConfirmReservationUseCase.java` es `backend/src/main/java/com/appointmentbooking/application/confirmation/ConfirmationTokenService.java` fajlokban.
- [X] T021 [US1] Keszitsd el a lejart fuggoben levo foglalasokat atomikusan lejarattato es az idosavakat felszabadito utemezett use case-t a `backend/src/main/java/com/appointmentbooking/application/reservation/ExpirePendingReservationsUseCase.java` es `backend/src/main/java/com/appointmentbooking/config/ReservationSchedulingConfig.java` fajlokban.
- [X] T022 [US1] Valositsd meg a tranzakcio utan futó, ujraprobalhato email-kuldesi adaptert es a megerositesi hivatkozas sablonjat a `backend/src/main/java/com/appointmentbooking/adapter/out/email/SpringMailConfirmationSender.java` es `backend/src/main/resources/templates/confirmation-email.html` fajlokban.
- [X] T023 [US1] Valositsd meg a `GET /api/public/providers/{providerId}/slots`, `POST /api/public/bookings` es `POST /api/public/bookings/confirm` szerzodes szerinti controllereket es DTO-kat a `backend/src/main/java/com/appointmentbooking/adapter/in/web/PublicBookingController.java` es `backend/src/main/java/com/appointmentbooking/adapter/in/web/dto/PublicBookingDtos.java` fajlokban.
- [X] T024 [P] [US1] Keszitsd el a publikus foglalasi API-kliens tipizalt kereseit es valaszait a `frontend/src/api/publicBookingApi.ts` fajlban.
- [X] T025 [US1] Epitsd meg a szolgaltato idosavlistajat, a vendeg emailes foglalasi urlapjat, valamint az ures, betoltesi es hibaallapotokat a `frontend/src/features/booking/SlotPicker.tsx` es `frontend/src/features/booking/BookingForm.tsx` fajlokban.
- [X] T026 [US1] Epitsd meg a tokenes megerositesi oldalt, amely a hivatkozasbol egyszer kuldi el a tokent, es egyertelmu siker-, lejarati- vagy hibaallapotot ad a `frontend/src/pages/BookingConfirmationPage.tsx` fajlban.
- [X] T027 [US1] Kotesd be a foglalasi es megerositesi oldalakat a nyilvanos utvonalakba a `frontend/src/app/router.tsx` fajlban.

**Checkpoint**: Az US1 kulon demozhato: egy fixtures-bol letrehozott szolgaltato szabad idosavat a vendeg emailes megerositessel lefoglalhatja.

---

## Phase 4: User Story 2 - Szabad idopontok letrehozasa es kezelese (Priority: P1)

**Goal**: A szolgaltato regisztral, biztonsagosan bejelentkezik, es a sajat kezelo feluleten letrehozza, modositja vagy torli a foglalas nelkuli idosavakat.

**Independent Test**: Egy uj szolgaltato regisztral es bejelentkezik, majd letrehoz, modosit es torol egy jovobeli idosavot; egy masik szolgaltato ugyanazt az eroforrast nem tudja megtekinteni vagy modositani.

### Tests for User Story 2

- [X] T028 [P] [US2] Ird meg a szolgaltatoi regisztracio jelszo-hashelési es duplikalt email szabalyainak unit tesztjeit a `backend/src/test/java/com/appointmentbooking/unit/provider/ProviderRegistrationServiceTest.java` fajlban.
- [X] T029 [P] [US2] Ird meg a multbeli, atfedo es nem modosithato foglalt idosavak kezelesenek unit tesztjeit a `backend/src/test/java/com/appointmentbooking/unit/slot/ManageBookableSlotUseCaseTest.java` fajlban.
- [X] T030 [P] [US2] Ird meg a bejelentkezes, tulajdonjogi ellenorzes es szolgaltatoi idosavmuveletek MockMvc contract tesztjeit a `backend/src/test/java/com/appointmentbooking/contract/ProviderDashboardControllerContractTest.java` fajlban.
- [X] T031 [P] [US2] Ird meg a ket szolgaltato kozotti jogosulatlan idosavhozzaferest ellenorzo PostgreSQL integracios tesztet a `backend/src/test/java/com/appointmentbooking/integration/ProviderOwnershipIntegrationTest.java` fajlban.
- [X] T032 [P] [US2] Ird meg a regisztracios, bejelentkezesi es idosavkezelo felulet validacios, ures es sikeralapotu komponens tesztjeit a `frontend/tests/integration/ProviderDashboard.test.tsx` fajlban.
- [X] T033 [P] [US2] Ird meg a szolgaltato regisztraciojatol az idosav letrehozasig, modositasaig es torleseig tarto E2E tesztet a `frontend/tests/e2e/provider-slot-management.spec.ts` fajlban.

### Implementation for User Story 2

- [X] T034 [US2] Valositsd meg a szolgaltatoi fiok regisztracios es bejelentkezesi use case-eit hashelt jelszoval a `backend/src/main/java/com/appointmentbooking/application/provider/RegisterProviderUseCase.java` es `backend/src/main/java/com/appointmentbooking/application/provider/AuthenticateProviderUseCase.java` fajlokban.
- [X] T035 [US2] Egészitsd ki a session alapu Spring Security konfiguraciot `PROVIDER` szerepkorrel, CSRF-vedelemmel es tulajdonjogi ellenorzest tamogato autentikacios kontextussal a `backend/src/main/java/com/appointmentbooking/config/SecurityConfig.java` es `backend/src/main/java/com/appointmentbooking/config/ProviderPrincipal.java` fajlokban.
- [X] T036 [US2] Valositsd meg a sajat idosavak letrehozasat, listazasat, modositását es torleset, beleertve az atfedes- es allapotvalidaciot a `backend/src/main/java/com/appointmentbooking/application/slot/ManageBookableSlotUseCase.java` fajlban.
- [X] T037 [US2] Valositsd meg az `/api/auth/register`, `/api/auth/login`, `/api/auth/logout` es `/api/provider/slots` szerzodes szerinti controllereket es DTO-kat a `backend/src/main/java/com/appointmentbooking/adapter/in/web/AuthController.java`, `backend/src/main/java/com/appointmentbooking/adapter/in/web/ProviderSlotController.java` es `backend/src/main/java/com/appointmentbooking/adapter/in/web/dto/ProviderDtos.java` fajlokban.
- [X] T038 [P] [US2] Keszitsd el a session-hitelesites es a szolgaltatoi idosavkezeles tipizalt API-klienset a `frontend/src/api/authApi.ts` es `frontend/src/api/providerSlotsApi.ts` fajlokban.
- [X] T039 [US2] Epitsd meg a szolgaltatoi regisztracios es bejelentkezesi oldalt, billentyuzettel kezelheto validacios uzenetekkel a `frontend/src/pages/ProviderRegistrationPage.tsx` es `frontend/src/pages/ProviderLoginPage.tsx` fajlokban.
- [X] T040 [US2] Epitsd meg a szolgaltatoi naptar- es idosavkezelo feluletet az ures, betoltesi, sikeres es hibaallapotokkal a `frontend/src/features/provider-dashboard/SlotManagementPanel.tsx` es `frontend/src/pages/ProviderDashboardPage.tsx` fajlokban.
- [X] T041 [US2] Vedd fel a vedett szolgaltatoi utvonalat es az autentikacio utani navigaciot a `frontend/src/app/router.tsx` es `frontend/src/app/RequireProviderSession.tsx` fajlokban.

**Checkpoint**: Az US2 kulon demozhato: a szolgaltato sajat fiokjaval kezeli a naptarat, es az idegen vagy foglalt idosavak muveletei egyertelmu hibaval elutasitottak.

---

## Phase 5: User Story 3 - Intelligens idopontajanlas (Priority: P2)

**Goal**: Ha a vendeg altal valasztott idosav nem foglalhato, a rendszer relevans, idorendben rangsorolt alternativakat jelenit meg ugyanazon szolgaltato szabad idosavai kozul.

**Independent Test**: Egy mar nem elerheto idosavra kuldott foglalasi keres 409-es valaszt ad, es legalabb harom aktualis alternativa eseten az azonos datum/napszak preferenciat elore sorolja.

### Tests for User Story 3

- [X] T042 [P] [US3] Ird meg a datum-, napszak- es legkorabbi idopont szerinti rangsorolo szabalyok unit tesztjeit a `backend/src/test/java/com/appointmentbooking/unit/recommendation/AppointmentRecommendationServiceTest.java` fajlban.
- [X] T043 [P] [US3] Ird meg a `SLOT_UNAVAILABLE` 409-es valasz es az alternativak szerzodes szerinti alakjat ellenorzo contract tesztet a `backend/src/test/java/com/appointmentbooking/contract/BookingRecommendationContractTest.java` fajlban.
- [X] T044 [P] [US3] Ird meg a bounded, maximum harom eredmenyt ado ajanlasi lekerdezes PostgreSQL integracios tesztet a `backend/src/test/java/com/appointmentbooking/integration/AppointmentRecommendationIntegrationTest.java` fajlban.
- [X] T045 [P] [US3] Ird meg az alternativakat megjelenito, billentyuzettel valaszthato es kepernyoolvaso-altal bejelentett komponens tesztet a `frontend/tests/integration/AppointmentAlternatives.test.tsx` fajlban.
- [X] T046 [P] [US3] Ird meg a parhuzamos foglalasbol fakado utkozes es alternativ idopontvalasztas E2E tesztet a `frontend/tests/e2e/booking-alternatives.spec.ts` fajlban.

### Implementation for User Story 3

- [X] T047 [US3] Valositsd meg a preferencia alapjan rangsorolo, legfeljebb harom szabad idosavat valaszto alkalmazasi szolgaltatast a `backend/src/main/java/com/appointmentbooking/application/recommendation/AppointmentRecommendationService.java` fajlban.
- [X] T048 [US3] Egészitsd ki a perzisztencia adaptert lapozott, datum- es napszak szerint rendezetten szurt szabad idosavlekerdezessel a `backend/src/main/java/com/appointmentbooking/adapter/out/persistence/BookableSlotJpaRepository.java` fajlban.
- [X] T049 [US3] Kotesd be az ajanlasokat a foglalasinditasi utkozesvalaszba a `backend/src/main/java/com/appointmentbooking/application/reservation/CreatePendingReservationUseCase.java` es `backend/src/main/java/com/appointmentbooking/adapter/in/web/ApiExceptionHandler.java` fajlokban.
- [X] T050 [US3] Epitsd meg az alternativ idopontok listajat, preferenciavalasztasat es ujrafoglalasi muveletet a `frontend/src/features/booking/AppointmentAlternatives.tsx` es `frontend/src/features/booking/BookingForm.tsx` fajlokban.

**Checkpoint**: Az US3 kulon demozhato: az utkozo vendeg nem kettos foglalast kap, hanem azonnal hozzaferheto es relevans uj idopontokat.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: A teljes rendszer minosege, biztonsaga, teljesitmenye es uzemeltethetosege.

- [X] T051 [P] Futtass es javits kritikus akadalymentessegi ellenorzeseket a foglalasi es szolgaltatoi kulcsoldalakon a `frontend/tests/e2e/accessibility.spec.ts` fajlban.
- [X] T052 [P] Keszits legalabb 100 egyideju, azonos idosavra iranyulo kereset futtato terhelesi tesztet es meresi kimenetet a `backend/src/test/java/com/appointmentbooking/performance/ConcurrentBookingLoadTest.java` fajlban.
- [X] T053 [P] Allitsd be az egeszsegugyi ellenorzo, strukturalt naplozasi es biztonsagos konfiguracios alapokat a `backend/src/main/resources/application.yml` es `backend/src/main/resources/application-local.yml.example` fajlokban.
- [X] T054 [P] Dokumentald a helyi inditast, a tesztelest, a Docker elofelteteleket es a konfiguracios valtozokat a `README.md` fajlban.
- [ ] T055 Futtasd vegig a quickstart forgatokonyveit, a backend es frontend teljes tesztcsomagjat, majd rogzitsd az eredmenyeket a `specs/001-appointment-booking-system/quickstart-validation.md` fajlban.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 - Setup**: Azonnal indithato; T001 es T002 utan a parhuzamos konfiguracios feladatok futtathatok.
- **Phase 2 - Foundational**: A Setup fazis utan kotelzo; blokkolja az osszes user story implementaciojat.
- **US1, US2 es US3**: Mindegyik a Foundational fazis utan kezdheto. A megvalositas sorrendje US1, majd US2, majd US3; US1 fixtures-szel fuggetlenul ellenorizheto, US2 sajat szolgaltatoi fiokkal fuggetlenul ellenorizheto, US3 pedig egy mar letezo nem elerheto idosavval ellenorizheto.
- **Phase 6 - Polish**: Az osszes kivant user story checkpointja utan fut.

### User Story Dependencies

- **US1 (P1)**: A Foundational fazisra epul, a foglalasi forgatokonyvhöz teszt-fixture szolgaltato es idosav elegendo.
- **US2 (P1)**: A Foundational fazisra epul, es nem igenyli az US1 emailes foglalasi folyamatanak mukodeset a sajat idosav-kezelési tesztjehez.
- **US3 (P2)**: A Foundational fazisra epul, az ajanlasi logika az US1 foglalasi utkozesvalaszahoz kapcsolodik, ezert az US1 implementacioja utan integralando.

### Parallel Opportunities

- A Setup fazis T002-T005 feladatai parhuzamosan vegezhetok T001 utan.
- A Foundational fazisban T009-T012 kulon fajlokon dolgozik, igy T006-T008 elkeszulte utan parhuzamosithato.
- Az egyes user story-k tesztfeladatai `[P]` jelolest kaptak, kulon tesztfajlokon parhuzamosan irhatok es kezdetben bukniuk kell.
- Az US1 T024 es az US2 T038 frontend API-kliens feladatai parhuzamosan vegezhetok a megfelelo backend szerzodes stabilizalasa utan.
- A Phase 6 T051-T054 feladatai egymastol fuggetlenek.

## Parallel Example: User Story 1

```text
Task: "T013 foglalasi allapotgepes unit tesztek a backend/src/test/java/com/appointmentbooking/unit/reservation/ReservationStateMachineTest.java fajlban"
Task: "T014 publikus booking API contract tesztek a backend/src/test/java/com/appointmentbooking/contract/PublicBookingControllerContractTest.java fajlban"
Task: "T015 konkurens foglalasi integracios teszt a backend/src/test/java/com/appointmentbooking/integration/ConcurrentReservationIntegrationTest.java fajlban"
Task: "T017 foglalasi frontend komponens tesztek a frontend/tests/integration/BookingFlow.test.tsx fajlban"
```

## Implementation Strategy

### MVP First (US1)

1. Fejezd be a Setup es Foundational fazist.
2. Ird meg az US1 tesztjeit, majd implementald T019-T027 feladatait.
3. Ellenorizd kulon az emailes megerositett foglalast, a lejaratot es a parhuzamos foglalasi utkozest.
4. A sikeres US1 checkpoint utan a rendszer MVP-kent demozhato fixtures szolgaltatoval.

### Incremental Delivery

1. US1: Biztonsagos, emailben megerositett vendegfoglalas.
2. US2: Onkiszolgalo szolgaltatoi regisztracio es idosavkezeles.
3. US3: Az utkozo foglalasi esetekbol relevans idopontajanlas.
4. Polish: Akadalymentesseg, teljesitmeny, megfigyelhetoseg es a teljes quickstart bizonyitek.

## Notes

- Minden tesztfeladatot az implementacio elott kell megirni, es a megfelelo implementacio elott buknia kell.
- Az ID-k folytonosak, minden feladat checkboxot, azonosítot, ahol alkalmazhato `[P]` jelolest, user story feladatnal `[USn]` cimket es konkret fajlutvonalat tartalmaz.
- A T055 csak akkor zarhato le, ha a formatter, lint, tipusellenorzes es valamennyi relevans automatikus teszt sikeres.