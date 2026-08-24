# Research: Idopontfoglalasi Rendszer

## R1 - Párhuzamos foglalasok

**Decision**: A foglalhato idosav onallo, elore letrehozott egyseg. Foglalasi keresnel az alkalmazasi reteg egy rovid PostgreSQL tranzakcioban kizárolag zarolja az idosav rekordjat, ellenorzi, hogy az `AVAILABLE` allapotban van-e, majd `PENDING_CONFIRMATION` allapotba valtja es letrehozza a foglalast.

**Rationale**: Az idosav allapotat egyetlen tartos rekord kepviseli, ezert az adatbazis-zar es az egy tranzakcioban torteno allapotvaltas pontosan egy nyertes kereset enged. A masodik keres 409-es utkozesvalaszt es idopontajanlasokat kap. Ez teljesiti az SC-002 kovetelmenyt anelkul, hogy a bongeszo oldali allapotra hagyatkozna.

**Alternatives considered**:

- Optimista verziozas: kisebb zarolasi idot adhat, de ujraprobalast es osszetettebb utkozeskezelesi logikat igenyel.
- Alkalmazas-szintu memoriazar: tobb szerverpeldaanynal nem biztonsagos.
- Egyedi adatbazis-kulcs kizarolag: az atfedo intervallumokat nehezen fejezi ki; az elore letrehozott idosavmodell egyszerubb es ellenorizhetobb.

## R2 - Emailes megerosites es lejárat

**Decision**: A foglalas `PENDING_CONFIRMATION`, `CONFIRMED` es `EXPIRED` allapotokat hasznal. Minden fuggoben levo foglalashoz egyszer hasznalatos, kriptografiailag eros veletlen token tartozik; az adatbazis csak a token hash-et es lejaratat tarolja. Egy utemezett alkalmazasi feladat rendszeresen lejarttatja a 15 percen tul meg nem erositetett foglalasokat, es felszabaditja az idosavot.

**Rationale**: A hash-elt token adatbazis-szivargas eseten sem hasznalhato megerositesre. Az elkulonitett megerositesi rekord lehetove teszi a token felhasznalasanak, lejaratanak es egyszeri jellegenek egyertelmu kezeleset. Az emailkuldes tranzakcio utani, ujraprobalhato feladat, igy nem tart adatbazis-zarat kulso szolgaltatas hivasa kozben.

**Alternatives considered**:

- Allapot nelkuli JWT: egyszerubb tarolas, de visszavonas es egyszeri felhasznalas kezelese bonyolultabb.
- Token tarolasa a foglalason: kevesebb tabla, de a token eletciklus es audit adatai osszekeverednek a foglalas adataival.

## R3 - Szolgaltatoi hitelesites es jogosultsag

**Decision**: A szolgaltato email-cimes regisztracioval es jelszoval jelentkezik be. A Spring Security szerveroldali, `HttpOnly`, `Secure`, `SameSite` munkamenet-sutivel tartja a bejelentkezest, CSRF-vedelemmel. A szolgaltatoi vegpontok `PROVIDER` szerepet es tulajdonjogi ellenorzest is kovetelnek.

**Rationale**: A frontend ugyanennek a termeknek a felulete, igy a biztonsagos session cookie kevesebb kliensoldali titokkezelessel jar, mint a bongeszoben tarolt token. A szerepkor nem elegendo: a tulajdonjogi ellenorzes garantalja, hogy egy szolgaltato csak a sajat idosavait es foglalasait erje el.

**Alternatives considered**:

- Bongeszoben tarolt JWT: XSS eseten konnyebben ellophato.
- Kulso OIDC szolgaltato: kesobb hozzaadhato, de az elso kiadas egyszeru szolgaltatoi fiokkezelesehez indokolatlanul boviti a scope-ot.

## R4 - Tesztelesi strategia

**Decision**: Unit tesztek fedik a foglalasi allapotgepet, idosav-ervenyesitest, ajanlasi rangsort es jogosultsagi szabalyokat. PostgreSQL Testcontainers integracios tesztek igazoljak a zarolast, egyideju keresek viselkedeset es a lejarati takaritast. MockMvc/API contract tesztek fedik a statuszkodokat es valaszokat. A frontend Vitest es React Testing Library tesztjei a felhasznaloi allapotokat; Playwright E2E tesztek az emailes megerositesi es szolgaltatoi naptarfolyamatokat fedik.

**Rationale**: A foglalasi utkozes viselkedese csak a tenyleges PostgreSQL-adatbazissal hiteles; az uzleti szabalyok viszont gyors unit tesztekkel ellenorizhetok. A retegenkenti tesztek gyors visszajelzest es vegponttol vegpontig tarto bizonyitekot adnak.

**Alternatives considered**:

- In-memory adatbazis: SQL- es konkurenciaviselkedese elter a cel-adatbazistol.
- Csak E2E tesztek: lassuak es rosszul szukitik le a hibak okat.

## R5 - Frontend allapotok es akadalymentesseg

**Decision**: A foglalasi es szolgaltatoi feluletek egyertelmu `loading`, `empty`, `success` es `error` allapotokat kezelnek. Szemantikus urlapmezok, cimkek, hibaosszekapcsolasok, billentyuzet-navigacio es `aria-live` visszajelzesek kotelezoek az aszinkron foglalasi muveletekhez.

**Rationale**: Ez kozvetlenul teljesiti az FR-012-t es az alkotmany UX-kovetelmenyeit. A statuszvaltozasokat a kepernyoolvaso-hasznalok is megkapjak, az utkozes utani ajanlasok pedig elerhetok maradnak.

**Alternatives considered**:

- Csak vizualis szinjeloles: nem elegendo minden felhasznalo szamara.
- Egyetlen altalanos hiba-uzenet: nem segit a vendeget az uj idopont kivalasztasaban.