# Appointment Booking

Webes idopontfoglalasi rendszer Spring Boot backenddel es React frontenddel. A vendegek emailes megerositessel foglalnak, a szolgaltatok pedig sajat idosavaikat kezelik.

## Elofeltetelek

- Java 21
- Apache Maven 3.9 vagy ujabb
- Node.js 22 LTS es npm
- Docker Desktop a PostgreSQL, Mailpit es Testcontainers integracios tesztekhez

## Helyi inditas

1. Inditsd el a helyi szolgaltatasokat:

   ```powershell
   docker compose up -d
   ```

2. Masik terminalban inditsd a backend alkalmazast:

   ```powershell
   cd backend
   mvn spring-boot:run
   ```

3. Inditsd el a frontend fejlesztoi szervert:

   ```powershell
   cd frontend
   npm install
   npm run dev
   ```

4. Nyisd meg a frontend altal kiirt cimet. A helyi Mailpit postalada alapertelmezetten a `http://localhost:8025` cimen erheto el.

## Konfiguracio

A backend alapertelmezett fejlesztoi adatbazis- es email-beallitasai az `application.yml` fajlban vannak. Helyi, sajat ertekekhez masold az `backend/src/main/resources/application-local.yml.example` fajlt `application-local.yml` nevvel, majd allitsd be a kovetkezo valtozokat:

- PostgreSQL kapcsolat es hitelesitesi adatok
- SMTP szerver es kuldo cim
- `app.frontend-base-url`, amely a megerosito hivatkozas celoldala

Titkokat es helyi konfiguraciot ne commitolj.

## Ellenorzes

Frontend:

```powershell
cd frontend
npm test
npm run build
npm run lint
```

Backend:

```powershell
cd backend
mvn test
```

Az integracios tesztek PostgreSQL Testcontainerst hasznalnak, ezert Docker Desktopot igenyelnek. A teljes validacios forgatokonyvek a [quickstart.md](specs/001-appointment-booking-system/quickstart.md) dokumentumban szerepelnek.

## Funkcionalis attekintes

- Szolgaltatoi regisztracio es session alapu bejelentkezes
- Foglalhato idosavak letrehozasa, modositasa es torlese
- Vendegfoglalas emailes, egyszer hasznalatos megerositessel
- Adatbazis-zarolassal kezelt parhuzamos foglalasi utkozes
- Preferencia alapjan rangsorolt alternativ idopontok