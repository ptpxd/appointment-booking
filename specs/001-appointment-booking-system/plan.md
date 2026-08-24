# Implementation Plan: Idopontfoglalasi Rendszer

**Branch**: `001-appointment-booking-system` | **Date**: 2026-08-24 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from [spec.md](spec.md)

## Summary

Keszitsunk olyan webes idopontfoglalasi rendszert, amelyben a vendeg szabad idosavot foglal es emailben megerositi azt, mig a szolgaltato a sajat kezeloi feluleten kezeli idosavjait. A backend Spring Boot retegzett architekturaju REST szolgaltatas PostgreSQL adattarolassal; a frontend React es Tailwind CSS alkalmazas. Az egy idosavra indulo parhuzamos foglalasokat egyetlen adatbazis-tranzakcioban, az idosav zarolasaval es atomikus allapotvaltasaval kezeli a rendszer. A reszletes dontesek a [research.md](research.md) dokumentumban szerepelnek.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 21 LTS a backendhez; TypeScript 5.x es React 19 a frontendhez

**Primary Dependencies**: Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security, Spring Validation, Spring Mail, Flyway; React, Vite, Tailwind CSS 4, React Router

**Storage**: PostgreSQL 16 relacios adatbazis; fejlesztesi es integracios tesztekhez kontenerizalt PostgreSQL

**Testing**: JUnit 5, Mockito, Spring Boot Test, Testcontainers es MockMvc; Vitest, React Testing Library, MSW, Playwright es axe-core

**Target Platform**: Kontenerizalt Linux szerver es modern, asztali vagy mobil bongeszo

**Project Type**: Ket alkalmazasbol allo webalkalmazas: REST backend es egyoldalas frontend

**Performance Goals**: Az alternativ idopontok a keresek legalabb 95%-aban 2 masodpercen belul jelennek meg; a kritikus foglalasi tranzakcio megorzi az egy idosavhoz tartozo egyetlen aktiv foglalast parhuzamos keresek mellett is.

**Constraints**: A vendeg emailes megerositese elott az idosav legfeljebb 15 percig lehet ideiglenesen foglalt; a szolgaltato csak a sajat eroforrasait kezelheti; a kliens nem tarolhat tartos hitelesitesi tokent hozzaferheto bongeszo-tarhelyen.

**Scale/Scope**: Elso kiadasban szolgaltatonkenti, elore letrehozott foglalhato idosavak; vendegfiok, fizetes, szolgaltatok kozotti ajanlas es foglalaslemondas nem resze a scope-nak.

## Constitution Check

*GATE: Passed before Phase 0 research; rechecked and passed after Phase 1 design.*

| Principle | Plan evidence | Status |
|-----------|---------------|--------|
| Code Quality Is Non-Negotiable | A domain, alkalmazasi, adapter/infrastruktura es prezentacios felelossegek elkulonulnek; a hatarokon explicit DTO-k es portok vannak. | PASS |
| Testing Proves Behaviour | Az uzleti szabalyokhoz unit tesztek, a tranzakcios es perzisztencia-hatarokhoz PostgreSQL Testcontainers integracios tesztek, az API-hoz contract tesztek, a kulcsfolyamatokhoz E2E tesztek tartoznak. | PASS |
| Consistent User Experience | A frontend specifikalja az ures, betoltesi, sikeres es hibaallapotokat, szemantikus elemeket, billentyuzetkezelest es kepernyoolvaso-visszajelzest. | PASS |
| Performance Is a Product Requirement | Az idopontajanlas lapozott es rendezetten lekerdezett; a foglalas rovid adatbazis-tranzakcio; a 2 masodperces cel es a parhuzamos foglalasi terheles meresi forgatokonyvvel ellenorizheto. | PASS |
| Transparency and Layered Architecture | A controller es a React UI csak use case-eket/API-klienset hiv; az uzleti allapotgepek es jogosultsagi szabalyok az alkalmazasi/domain retegben maradnak. | PASS |

Nincs olyan alkotmanyi elteres, amely komplexitasi indoklast igenyelne.

## Project Structure

### Documentation (this feature)

```text
specs/001-appointment-booking-system/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
backend/
├── src/
│   ├── main/java/com/appointmentbooking/
│   │   ├── domain/             # Entitasok, allapotok, uzleti invariansok, portok
│   │   ├── application/        # Use case-ek es tranzakcios hatarok
│   │   ├── adapter/in/web/     # REST controllerek, DTO-k, hibatervalaszok
│   │   ├── adapter/out/        # JPA, email es egyeb infrastruktura implementaciok
│   │   └── config/             # Biztonsag, utemezes es alkalmazas-konfiguracio
│   ├── main/resources/db/migration/
│   └── test/java/com/appointmentbooking/
│       ├── unit/
│       ├── integration/
│       └── contract/

frontend/
├── src/
│   ├── app/                   # Router, global szolgaltatasok es stilusok
│   ├── features/              # Foglalas es szolgaltatoi naptar funkcionalis moduljai
│   ├── components/            # Ujrahasznalhato, akadalymentes UI elemek
│   ├── pages/                 # Nyilvanos es szolgaltatoi oldalak
│   └── api/                   # Tipizalt HTTP kliens es szerzodes-lekepezes
└── tests/
  ├── unit/
  ├── integration/
  └── e2e/
```

**Structure Decision**: Ket egymas melle helyezett alkalmazas keszul. A `backend/` retegzett szerkezete az uzleti szabalyokat es a portokat fuggetleniti a HTTP-, JPA- es email-infrastrukturatol. A `frontend/` funkcionalis modulokra bontja a foglalasi es szolgaltatoi munkafolyamatokat, mikozben a kozos UI es API-infrastruktura elkulonul.
