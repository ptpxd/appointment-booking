# REST API Contract: Idopontfoglalasi Rendszer

## Conventions

- Base path: `/api`.
- Idopontok ISO 8601 UTC formatumban szerepelnek.
- A sikeres valaszok JSON objektumok; a hibak legalabb `code`, `message` es opcionálisan `fieldErrors` mezot tartalmaznak.
- A szolgaltatoi vegpontok hitelesitett `PROVIDER` munkamenetet es eroforras-tulajdonjogi ellenorzest igenyelnek.
- Lapozott lista vegpontok `page` es `size` parametert fogadnak, maximum 100-as `size` ertekkel.

## Public booking endpoints

### `GET /public/providers/{providerId}/slots?date={YYYY-MM-DD}`

Visszaadja a megadott szolgaltato adott napon foglalhato idosavjait. Csak `AVAILABLE` idosav jelenhet meg.

**200 response**

```json
{
  "provider": { "id": "uuid", "displayName": "Dr. Example" },
  "slots": [{ "id": "uuid", "startsAt": "2026-09-01T09:00:00Z", "endsAt": "2026-09-01T09:30:00Z" }]
}
```

### `POST /public/bookings`

Ideiglenes foglalast indit es emailes megerositest ker.

**Request**

```json
{
  "slotId": "uuid",
  "guestEmail": "guest@example.com",
  "preference": { "preferredDate": "2026-09-01", "preferredTimeOfDay": "MORNING" }
}
```

**202 response**: `reservationId`, `expiresAt`, es felhasznaloi uzenet az elkuldott megerosito emailrol.

**409 response**: `SLOT_UNAVAILABLE`, valamint legfeljebb harom alternativa `id`, `startsAt` es `endsAt` mezokkel.

### `POST /public/bookings/confirm`

Az emailes hivatkozasbol kapott egyszer hasznalatos tokent ellenorzi es veglegesiti a foglalast.

**Request**

```json
{ "token": "emailben-kapott-egyszeri-token" }
```

**200 response**: A `reservationId`, `status: "CONFIRMED"`, a szolgaltato neve es az idosav adatai.

**400 response**: `CONFIRMATION_INVALID`, `CONFIRMATION_EXPIRED` vagy `CONFIRMATION_ALREADY_USED`.

## Provider authentication endpoints

### `POST /auth/register`

Szolgaltatoi fiokot hoz letre. A keret `email`, `password` es `displayName` mezoket tartalmaz. Siker eseten 201-et ad vissza; mar letezo email eseten 409-et.

### `POST /auth/login`

Szolgaltatot jelentkeztet be. Siker eseten 204-et es biztonsagos munkamenet-sutit ad; ervenytelen hitelesitesi adatok eseten 401-et.

### `POST /auth/logout`

Lezarja az aktualis munkamenetet. Siker eseten 204-et ad.

## Provider dashboard endpoints

### `GET /provider/slots?from={instant}&to={instant}&page={n}&size={n}`

A bejelentkezett szolgaltato sajat idosavait adja vissza, a foglalhatosagi allapottal egyutt.

### `POST /provider/slots`

Uj foglalhato idosav letrehozasa `startsAt` es `endsAt` mezokkel. Ervenytelen vagy atfedo intervallum eseten 422-t ad.

### `PATCH /provider/slots/{slotId}`

Csak a sajat, `AVAILABLE` allapotu idosav kezdetet es veget modositja. Mas szolgaltato eroforrasa eseten 404; nem modosithato allapot eseten 409.

### `DELETE /provider/slots/{slotId}`

Csak a sajat, `AVAILABLE` allapotu idosavat torli. Fuggoben vagy vegleges foglalas eseten 409 `SLOT_HAS_RESERVATION` hibat ad.

### `GET /provider/reservations?from={instant}&to={instant}&page={n}&size={n}`

A bejelentkezett szolgaltato sajat foglalasait listazza idosavval, vendege-maillel es foglalasi statuszszal.