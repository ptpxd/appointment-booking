# Quickstart: Idopontfoglalasi Rendszer Validalasa

## Elofeltetelek

- Java 21, Node.js 22 LTS, Docker Desktop es egy futtathato PostgreSQL 16 peldaany.
- A backend es frontend konfiguracioja tartalmazza a helyi adatbazis-, email-tesztfiok- es origin-beallitasokat. Fejleszteshez email-fogadast szimulalo helyi mailbox hasznalhato.

## Build es automatikus ellenorzes

1. A `backend/` konyvtarban futtasd az alkalmazas buildjet, formatalasat, statikus ellenorzeset es a unit, integracios, valamint contract teszteket a projekt buildeszkozevel.
2. A `frontend/` konyvtarban telepitsd a fuggosegeket, majd futtasd a lintet, TypeScript ellenorzest es a Vitest teszteket.
3. Inditsd el a backend alkalmazast es a frontend fejlesztoi szervert a dokumentalt helyi konfiguracioval.
4. Futtasd a Playwright E2E teszteket, amelyek mindket alkalmazast es a teszt email-fiokot hasznaljak.

## Validalasi forgatokonyvek

### 1. Szolgaltato es naptar kezelese

1. Regisztralj egy uj szolgaltatot, majd jelentkezz be.
2. A kezelo feluleten hozz letre egy jovobeli, 30 perces idosavot.
3. Ellenorizd, hogy az idosav a nyilvanos foglalasi oldalon megjelenik.
4. Modositsd az idosavot, majd ellenorizd a frissult nyilvanos adatot.
5. Torold a foglalas nelkuli idosavot; ellenorizd, hogy tobbe nem foglalhato.

Elvart eredmeny: csak a bejelentkezett tulajdonos tudja kezelni a sajat idosavat; a valtozasok a nyilvanos listaban kovetkezetesen latszanak.

### 2. Emailes foglalas megerositese

1. Hozz letre egy uj foglalhato idosavot szolgaltatokent.
2. Vendegkent indits foglalast ervenyes email-cimmel.
3. Ellenorizd, hogy a valasz megerositesre varo foglalast jelez es az email-fiok megkapja a megerositesi hivatkozast.
4. Nyisd meg a hivatkozast, majd engedd, hogy a frontend elkuldje a tokent a [REST-szerzodesben](contracts/rest-api.md) meghatarozott vegpontnak.
5. Ellenorizd a vegleges visszaigazolast es a szolgaltatoi feluleten a `CONFIRMED` foglalast.

Elvart eredmeny: az idosav `BOOKED`, a foglalas `CONFIRMED`; ugyanazzal a tokennel ujra nem lehet megerositeni.

### 3. Parhuzamos foglalas es ajanlas

1. Hozz letre legalabb negy azonos szolgaltatohoz tartozo, jovobeli idosavot.
2. Ket fuggetlen bongeszo-munkamenetbol vagy egyideju API tesztbol indits foglalast ugyanarra az elso idosavra.
3. Szinkronizald a ket keres kuldeset, majd ellenorizd a valaszokat.
4. A sikertelen keresnel ellenorizd az alternativ idopontokat es azok sorrendjet.

Elvart eredmeny: pontosan egy keres kap 202-es valaszt; a masik 409-es `SLOT_UNAVAILABLE` hibat es legfeljebb harom relevans alternativat kap. Az idosavhoz pontosan egy fuggoben levo foglalas tartozik.

### 4. Lejart foglalas felszabaditasa

1. Indits foglalast, de ne erositsd meg az emailes hivatkozast.
2. Tesztkornyezetben mozgasd az idot a 15 perces lejarat utanra, majd futtasd a lejaratkezelot.
3. Kerj le ujra nyilvanos idosavakat, es probalj uj foglalast inditani ugyanarra az idosavra.

Elvart eredmeny: az elozo foglalas `EXPIRED`, az idosav ujra `AVAILABLE`, es uj foglalasi keres elfogadhato.

## Meresi bizonyitek

- A parhuzamos foglalasi integracios teszt legalabb 100 azonos idosavra indulo kiserletben pontosan egy nyertes foglalast igazol.
- A terhelesi ellenorzes dokumentalja, hogy az alternativak lekerdezese a keresek legalabb 95%-aban 2 masodperc alatt teljesul a kijelolt tesztkornyezetben.
- Az akadalymentessegi ellenorzes a foglalasi es szolgaltatoi kritikus oldalakon nem jelez kritikus axe szabalysertest.