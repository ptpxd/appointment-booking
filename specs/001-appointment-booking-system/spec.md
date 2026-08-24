# Feature Specification: Idopontfoglalasi Rendszer

**Feature Branch**: `Nincs letrehozva`

**Created**: 2026-08-24

**Status**: Draft

**Input**: Felhasznaloi leiras: "Idopontfoglalasi rendszer parhuzamos foglalasok kezelesere, intelligens idopontajanlasra, emailes megerositesre, valamint szolgaltatoi fiokok es kezelo felulet biztositasara."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Idopont foglalasa es megerositese (Priority: P1)

Vendegkent egy szolgaltato szabad idopontjai kozul kivalasztok egy megfelelo idopontot, megadom az email-cimemet, majd az emailben kapott megerositesi lehetoseggel veglegesitem a foglalast.

**Why this priority**: Ez a rendszer alapvető erteke: ugyfel es szolgaltato kozott egyertelmu, megbizhato idopont jöjjön letre.

**Independent Test**: Egy vendeg kiválaszt egy szabad idopontot, elkuldi a foglalast, majd az emailes megerosites utan megtekinti a vegleges foglalasat.

**Acceptance Scenarios**:

1. **Given** egy szolgaltatohoz tartozik foglalhato idopont, **When** a vendeg megadja a kért adatait es elkuldi a foglalast, **Then** a rendszer ideiglenesen lefoglalja az idopontot es emailes megerositest kuld.
2. **Given** a vendeg ideiglenes foglalasa meg nem jart le, **When** a vendeg az emailes megerositest elvegzi, **Then** a rendszer veglegesiti a foglalast es egyertelmu visszaigazolast mutat.
3. **Given** ket vendeg ugyanazt a szabad idopontot probalja lefoglalni, **When** az egyik foglalasi kerelmet a rendszer elobb elfogadja, **Then** a masik vendeg nem tudja ugyanazt az idopontot lefoglalni es aktualis alternativakat kap.

---

### User Story 2 - Szabad idopontok letrehozasa es kezelese (Priority: P1)

Regisztralt szolgaltatokent, peldaul orvoskent, a sajat kezelo feluletemen beallitom a foglalhato idosavjaimat, majd ezeket megtekintem, modositom vagy torlom.

**Why this priority**: Foglalhato idopontok nelkul a vendegoldali foglalas nem biztosithat erteket; a szolgaltatonak sajat rendelkezesu naptarra van szuksege.

**Independent Test**: Egy uj szolgaltato regisztral, bejelentkezik, letrehoz egy idosavot, modositja, majd torli azt; a torolt idosav nem jelenik meg foglalhatokent.

**Acceptance Scenarios**:

1. **Given** egy szolgaltato meg nem rendelkezik fiokkal, **When** sikeresen regisztral, **Then** be tud jelentkezni es hozzafer a sajat kezelo feluletehez.
2. **Given** egy bejelentkezett szolgaltato, **When** foglalhato idosavot ad meg, **Then** az idosav megjelenik a vendegek szamara foglalhatokent.
3. **Given** egy bejelentkezett szolgaltato meglevő, foglalas nelkuli idosava, **When** modositja vagy torli azt, **Then** a vendegoldali foglalhatosag a valtozast koveti.
4. **Given** egy bejelentkezett szolgaltato foglalassal rendelkezo idosava, **When** megprobalja torolni azt, **Then** a rendszer nem engedi csendben torolni a foglalast, es egyertelmu tajekoztatast ad a szukseges lepesrol.

---

### User Story 3 - Intelligens idopontajanlas (Priority: P2)

Vendegkent, ha nincs megfelelo szabad idopont a preferalt idopontban, a rendszer relevans kovetkezo lehetosegeket ajanl, hogy gyorsabban talaljak idopontot.

**Why this priority**: Az ajanlas csokkenti a sikertelen keresest, es segit a szolgaltatoi kapacitas jobb kihasznalasaban.

**Independent Test**: Egy vendeg olyan idopontot keres, amely nem foglalhato; a rendszer legalabb harom, a keresesi preferenciahoz legkozelebbi elerheto lehetoseget jelenit meg.

**Acceptance Scenarios**:

1. **Given** a vendeg kivalasztott idopontja mar nem foglalhato, **When** a rendszer ellenorzi a szabad idosavokat, **Then** a szolgaltato legkozelebbi harom szabad idopontjat ajanlja idorendi sorrendben.
2. **Given** a vendeg datum- vagy napszakpreferenciat adott meg, **When** a rendszer elerheto idopontokat ajanl, **Then** eloszor a megadott preferencianak megfelelo lehetosegek jelennek meg.

### Edge Cases

- Az emailes megerositesi ido lejarta utan az ideiglenes foglalas felszabadul, es az idopont ujra foglalhatova valik.
- A rendszer nem engedi, hogy egy szolgaltato atfedo vagy multbeli foglalhato idosavot hozzon letre.
- Ha egy vendeg megnyitott foglalasi oldala kozben az idopont mas altal lefoglalasra kerul, a rendszer nem hoz letre kettos foglalast, es uj idopontot kinal.
- Ha az emailes megerosites nem kuldheto el vagy nem ervenyes, a vendeg tajekoztatast kap, es a foglalas nem valik veglegesse.
- Egy szolgaltato csak a sajat idosavait es foglalasait kezelheti.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: A rendszernek lehetove KELL tennie, hogy a szolgaltato regisztraljon, bejelentkezzen, es csak a sajat kezelo feluletehez ferjen hozza.
- **FR-002**: A rendszernek lehetove KELL tennie, hogy a bejelentkezett szolgaltato megadja a foglalhato idosavjai kezdetet, veget es foglalhatosagi adatait.
- **FR-003**: A rendszernek meg KELL jelenitenie a vendegeknek a valasztott szolgaltato aktualisan foglalhato idosavjait.
- **FR-004**: A rendszernek lehetove KELL tennie, hogy a szolgaltato megtekintse, modositja es torolje a sajat, foglalas nelkuli idosavjait.
- **FR-005**: A rendszernek lehetove KELL tennie, hogy egy vendeg email-cim megadasaval kivalasszon es foglalasi kerelmet inditson egy szabad idosavra.
- **FR-006**: A rendszernek egy foglalasi kerelem elfogadasakor ideiglenesen foglaltnak KELL jelolnie az idosavot, hogy masik parhuzamos kerelem ne hozhasson letre ugyanarra az idosavra foglalast.
- **FR-007**: A rendszernek emailes megerositest KELL kuldenie a vendegnek minden uj foglalasi kerelemhez.
- **FR-008**: A rendszernek csak az emailes megerositest kovetoen KELL vegleges foglalast letrehoznia, es annak sikererol a vendegnek egyertelmu visszajelzest adnia.
- **FR-009**: A rendszernek fel KELL szabaditania azokat az ideiglenes foglalasokat, amelyeket a megerositesi idon belul nem veglegesitettek.
- **FR-010**: A rendszernek egy mar nem elerheto kivalasztott idopont helyett legalabb harom, a vendeg preferenciaihoz legkozelebbi szabad idopontot KELL felkinalnia, ha ennyi elerheto.
- **FR-011**: A rendszernek eloszor a vendeg altal megadott datum- es napszakpreferencianak megfelelo idopontokat KELL ajanlania; ha nincs ilyen, a legkorabbi elerheto lehetosegeket kell megjelenitenie.
- **FR-012**: A rendszernek egyertelmu, hasznalhato allapotokat KELL megjelenitenie ures naptar, betoltes, sikeres muvelet es sikertelen muvelet eseten.
- **FR-013**: A rendszernek el kell utasitania a szolgaltato altal megadott multbeli vagy a sajat meglevo idosavjaval atfedo foglalhato idosavot.
- **FR-014**: A rendszernek meg KELL oriznie a szolgaltato, vendeg, idosav es foglalas kapcsolatat, hogy a felek az adott foglalas reszleteit megtekinthessek.

### Key Entities *(include if feature involves data)*

- **Szolgaltatoi fiok**: Egy szolgaltato, peldaul orvos azonositott fiokja; a sajat kezelo feluletehez, idosavaihoz es foglalasaihoz kapcsolodik.
- **Vendeg**: Az a szemely, aki email-cimevel foglalasi kerelmet indit egy szolgaltatohoz.
- **Foglalhato idosav**: Egy szolgaltato altal meghatarozott kezdeti es vegidopont, amely szabad, ideiglenesen foglalt, veglegesen foglalt vagy nem elerheto allapotban lehet.
- **Foglalas**: A vendeg, a szolgaltato es egy idosav kozotti foglalasi kapcsolat; statusza jelzi a megerositesre varo, vegleges vagy lejart allapotot.
- **Emailes megerosites**: A foglalasi kerelemhez kotott, egyszer felhasznalhato megerositesi lehetoseg, amely a foglalas veglegesiteset igazolja.
- **Idopontpreferencia**: A vendeg kivanatos datuma es/vagy napszakja, amely alapjan a rendszer rangsorolja az ajanlott idopontokat.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A vendegek legalabb 90%-a legfeljebb 3 perc alatt el tud jutni a szabad idopont kivalasztasatol az emailes megerositesre varo foglalasig.
- **SC-002**: Egyideju, ugyanarra az idosavra iranyulo foglalasi kiserletek soran a rendszer az esetek 100%-aban legfeljebb egy ideiglenes vagy vegleges foglalast enged letrehozni.
- **SC-003**: Az emailes megerositest megnyito vendegek legalabb 95%-a az elso megnyitasra egyertelmuen meg tudja allapitani, hogy a foglalasa veglegesse valt-e.
- **SC-004**: Ha nincs foglalhato kivalasztott idopont, a rendszer a keresestol szamitott 2 masodpercen belul megjeleniti az elerheto ajanlasokat az esetek legalabb 95%-aban.
- **SC-005**: A szolgaltatok legalabb 90%-a kulso segitseg nelkul letre tud hozni, modositani vagy torolni egy foglalas nelkuli idosavot a kezelo feluleten.
- **SC-006**: Az elerheto foglalhato idosavval rendelkezo sikertelen keresesi esetek legalabb 95%-aban a vendeg relevans alternativ idopontot kap.

## Assumptions

- A szolgaltato gyujtofogalom, amely magaban foglalja az orvosokat es mas idopontalapu szolgaltatast nyujto szakembereket.
- Az elso kiadasban a vendegnek nincs szuksege sajat regisztralt fiokra; a foglalas email-cimhez kotodik, mig a szolgaltatoi kezelo felulet bejelentkezest igenyel.
- A meg nem erositetett foglalas 15 percig tartja fenn az idosavot, utana automatikusan felszabadul.
- A szolgaltato a meglevő foglalassal rendelkezo idosavat nem torolheti kozvetlenul; a foglalas kezelese kulon, jovobeli munkafolyamat resze.
- Az elso kiadasban az ajanlasok ugyanazon szolgaltato szabad idosavai kozul valasztanak datum, napszak es idobeli kozelseg alapjan; szolgaltatok kozotti ajanlas nem resze a funkcionak.
- A rendszer webes feluleten asztali es mobil eszkozokrol is hasznalhato, es alapveto akadalymentessegi, betoltesi, ures, sikeres es hibauzeneti allapotokat biztosit.