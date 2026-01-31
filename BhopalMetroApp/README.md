Bhopal Metro App — Resume-ready MVP

What I changed (high level)

- Offline-first Android app (Java)
- Cleaned data layer: Repository → Local data source (parses packaged JSON) → Room cache
- MVVM: `StationViewModel` + `TrainTimeViewModel`
- Features added: Favorites (Room), Next‑train ETA chips, Trip planner (existing Dijkstra reused), serviceable UI
- Database seeded on first run from packaged `res/raw/bhopal_metro.json` (stations, schedules, sample favorites)

How the seeded data works (for your resume)

- On first app launch the bundled `bhopal_metro.json` is parsed and persisted into Room (`stations`, `schedules`).
- The app ships with 2 sample favorites so the `Favorites` screen is not empty in demos.
- Routing uses the packaged adjacency list and runs on-device (Dijkstra).

How to run locally (Windows)

1. Install JDK 17 and set JAVA_HOME (PowerShell):
   - setx JAVA_HOME "C:\\Program Files\\Java\\jdk-17"
   - Restart terminal and verify: `java -version`
2. Build the app:
   - cd "d:\\Silent Night\\App Development\\Bhopal Metro Application\\BhopalMetroApp"
   - .\\gradlew.bat assembleDebug
3. Install on device or run an emulator via Android Studio.

Key implementation notes (suitable for a resume)

- Implemented offline-first data layer (Room + JSON seed) and a clear Repository boundary.
- Added MVVM for core screens and persisted user favorites (Room) with simple seeded demo data.
- Kept routing and timetable parsing deterministic and testable (client-side Dijkstra + packaged timetable).

Suggested resume bullets

- Built an offline-first Android transit app (Java, Room, MVVM) with on-device routing and seeded production-like timetable data.
- Implemented persistent Favorites, ETA calculations from real timetables, and a client-side trip planner (Dijkstra).

Next steps (optional)

- Add small backend (FastAPI + Postgres) for alerts/favorites sync (I can provide schema + endpoints).
- Add unit/instrumentation tests and a short demo video for the portfolio.
