# Data Model: Idopontfoglalasi Rendszer

## Provider

| Field | Description | Rules |
|-------|-------------|-------|
| id | Egyedi azonositó | Nem modosithato |
| email | Szolgaltato bejelentkezesi email-cime | Egyedi, ervenyes email-formatum |
| passwordHash | Jelszo nem visszafejtheto tarolt valtozata | Soha nem kerul API-valaszba |
| displayName | A vendegnek megjeleno nev | Kotelezo |
| role | Jogosultsagi szerep | Az elso kiadasban `PROVIDER` |
| createdAt | Letrehozas idopontja | Nem modosithato |

## BookableSlot

| Field | Description | Rules |
|-------|-------------|-------|
| id | Egyedi azonositó | Nem modosithato |
| providerId | A tulajdonos szolgaltato | Kotelezo, Providerhez tartozik |
| startsAt | Az idosav kezdete | Jovobeli idopont letrehozaskor |
| endsAt | Az idosav vege | Szigoruan kesobbi, mint `startsAt` |
| status | Foglalhatosagi allapot | `AVAILABLE`, `PENDING_CONFIRMATION`, `BOOKED` |
| version | Konkurrenciaellenorzeshez hasznalt valtozat | Minden modositasnal no |

**Validation**: Uj idosav nem lehet multbeli es nem fedhet at ugyanahhoz a szolgaltatohoz tartozo masik idosavval. Az alkalmazas tranzakcioban zarolja a rekordot allapotvaltas elott; a `version` masodlagos vedelem a nem zarolasi utvonalakhoz.

## Reservation

| Field | Description | Rules |
|-------|-------------|-------|
| id | Egyedi azonositó | Nem modosithato |
| slotId | A lefoglalt BookableSlot | Egy foglalas pontosan egy idosavhoz tartozik |
| guestEmail | Vendeg kapcsolattartasi cime | Kotelezo, ervenyes email-formatum |
| status | Foglalas eletciklus-allapota | `PENDING_CONFIRMATION`, `CONFIRMED`, `EXPIRED` |
| createdAt | Foglalasi kerelem ideje | Nem modosithato |
| confirmedAt | Veglegesites ideje | Csak `CONFIRMED` allapotban kitoltott |
| expiresAt | Ideiglenes foglalas lejarata | A letrehozastol 15 perc |

**Relationships**: Egy Providernek sok BookableSlotja van. Egy BookableSlothoz legfeljebb egy aktiv Reservation tartozhat. Egy Reservation egy vendeghez, egy idosavhoz es annak szolgaltatojahoz kapcsolodik.

## ConfirmationToken

| Field | Description | Rules |
|-------|-------------|-------|
| id | Egyedi azonositó | Nem modosithato |
| reservationId | A megerositendo foglalas | Egy fuggoben levo foglalashoz egy aktiv token |
| tokenHash | A token nem visszafejtheto lenyomata | Egyedi, nyers token nem tarolhato |
| expiresAt | Ervenyesseg vege | Nem lehet kesobbi a foglalas `expiresAt` ertekenel |
| usedAt | Felhasznalas ideje | Egyszer allithato be |

## AppointmentPreference

| Field | Description | Rules |
|-------|-------------|-------|
| preferredDate | Vendeg kivanatos napja | Opcionális |
| preferredTimeOfDay | Kivanatos napszak | `MORNING`, `AFTERNOON`, `EVENING` vagy ures |

**Use**: Nem tartos vendegprofil; a foglalasi kereshez kapcsolodo bemenet. Az ajanlasi use case elobb azonos napszakot es datumot, majd a legkorabbi szabad idosavakat rangsorolja.

## Allapotatmenetek

```text
BookableSlot: AVAILABLE -> PENDING_CONFIRMATION -> BOOKED
                                         |               
                                         -> AVAILABLE (foglalas lejar)

Reservation: PENDING_CONFIRMATION -> CONFIRMED
                                      |
                                      -> EXPIRED
```

- Csak a megerositesi use case valthat `PENDING_CONFIRMATION` allapotbol `CONFIRMED` allapotba.
- Csak a lejaratkezelo valthat `PENDING_CONFIRMATION` allapotbol `EXPIRED` allapotba, es ezzel egy idoben az idosav `AVAILABLE` lesz.
- A szolgaltato nem torolhet es nem modosithat `PENDING_CONFIRMATION` vagy `BOOKED` idosavot.