# PS5 Pro Radar – Komplettes Design-Makeover & 3-Menü-Architektur (Shops • Diagramm • Scalper)

Vollständiger Neuaufbau des App-Designs mit **kräftigen, leuchtenden Farben**, **maximaler Text-Lesbarkeit**, einer neuen **3-Tab-Navigation am unteren Bildschirmrand (`Shops`, `Diagramm`, `Scalper / Scraper`)** sowie einem **vollautomatischen Hintergrund-Refresh**, der die Verfügbarkeit bei MediaMarkt, Saturn, OTTO, Amazon und PlayStation.com laufend von selbst aktuell hält.

## User Review & Critical Decisions

> [!IMPORTANT]
> Alle Vorgaben für den kompletten Design-Umbau und die neue Menüstruktur sind im Plan verankert:

- **Neue untere Menüleiste (Bottom Navigation) mit 3 separaten Bereichen per Klick**:
  1. **„Shops“**: Zeigt exklusiv und übersichtlich **MediaMarkt, Saturn, OTTO, Amazon.de und die offizielle PlayStation-Website** mit dem kleinen PS5-Foto im Kästchen, großem Verfügbarkeits-Status („Verfügbar“ vs. „Derzeit nicht verfügbar“) und vollautomatischem Hintergrund-Refresh.
  2. **„Diagramm“**: Eigene, großzügige Ansicht für das **Live-Preisdiagramm + PS5-Foto daneben**, inklusive klarer Kaufempfehlung („Warten wegen Scalpern“ vs. „Empfohlen: Jetzt kaufen!“) und Zeitraum-Wahl (`Heute (Uhrzeit)`, `7 Tage`, `30 Tage`).
  3. **„Scalper & Scraper“**: Eigene Ansicht für den **Live eBay-Scraper** (überteuerte eBay-Scalper-Preise > 1.000 € vs. normale/gebrauchte eBay-Angebote), das Live-Auto-Refresh-Protokoll sowie die Handy-Benachrichtigungen.
- **Vollautomatischer Selbst-Refresh (Auto-Update)**: Keine manuellen „Refresh“-Pflichtklicks mehr – ein permanenter Auto-Updater prüft MediaMarkt, Saturn, OTTO, Amazon, PlayStation.com und eBay selbstständig im Hintergrund und zeigt einen Live-Countdown bis zum nächsten automatischen Check an.
- **Farbenfrohes High-Contrast Design**: Mehrfarbige Akzent-Karten (MediaMarkt-Rot, Saturn-Orange, OTTO-Karmesin, Amazon-Gold, PlayStation-Kobaltblau, eBay-Violett/Smaragd) kombiniert mit extragroßer, reinweißer Schrift und kräftigen Kontrasten.

---

## 1. Overview & Core Concept

- **Was sich durch den kompletten Neuaufbau ändert**:
  1. **Klare Trennung in 3 Hauptmenüs unten**: Statt alles untereinander zu quetschen, hat der Nutzer unten drei klar beschriftete Menü-Buttons: **Shops**, **Diagramm** und **Scalper / Scraper**.
  2. **Farbenfrohes „Neon Aurora & PlayStation Prism“ Design**: Jede Shop-Karte und jedes Modul erhält leuchtende Farbverläufe, helle Status-Badges, klare Rahmen und gestochen scharfe Typografie, damit jeder Text sofort ins Auge springt.
  3. **Autonomer Live-Availability-Scraper**: Die App führt selbstständig regelmäßige Hintergrund-Abfragen für MediaMarkt, Saturn, OTTO, Amazon und PlayStation.com durch, speichert jede Änderung dauerhaft in Room SQLite und hält alle Verfügbarkeits-Karten ohne Nutzer-Zutun aktuell.

---

## 2. User Experience & Visual Design

- **Menü 1: „Shops“ (MediaMarkt, Saturn, OTTO, Amazon & PlayStation.com)**:
  - Oben ein farbenfrohes **Auto-Update-Status-Banner**, das live anzeigt: *„Automatischer Shop-Scanner aktiv • Hält MediaMarkt, Saturn, Amazon & Co. von selbst aktuell“*.
  - Darunter die farblich gestalteten Shop-Karten mit jeweils:
    - Dem **PlayStation 5 Bild im kleinen Kästchen** direkt neben dem Shopnamen,
    - Einem großen, leuchtenden Status-Badge (**„DERZEIT NICHT VERFÜGBAR“** in Korallenrot oder **„VERFÜGBAR (799,99 €) – JETZT KAUFEN!“** in Smaragdgrün),
    - Gut lesbarer Detail-Info und direktem Button zum Öffnen des Shops.
- **Menü 2: „Diagramm“ (Live-Preisdiagramm & Kaufempfehlung)**:
  - Zeigt die große Hero-Karte mit dem **PS5-Foto links** und dem **echten Zeit-Diagramm rechts daneben** (`Heute nach Uhrzeit`, `7 Tage`, `30 Tage`).
  - Darunter groß und farbig die **automatische Kaufempfehlung** (ob sich ein Kauf aktuell wegen Scalpern nicht lohnt oder ob man zuschlagen sollte) sowie eine kompakte Statistik-Kachel (Offizielle UVP 799,99 € • Aktueller eBay-Scalper-Schnitt • eBay Gebraucht/Normalpreis).
- **Menü 3: „Scalper / Scraper“ (eBay Live-Preise & Handy-Alarm)**:
  - Zeigt die automatisch gescrapten **eBay Scalper-Preise (Neu/OVP > 1.000 € – Nicht empfohlen!)** und die **normalen/gebrauchten eBay-Preise (< 799 €)** in eigenen farbigen Karten.
  - Darunter das **Live-Scraper-Protokoll** (wann welcher Shop zuletzt automatisch abgefragt wurde) und die **Handy-Benachrichtigungs-Steuerung**.

---

## 3. Technical Architecture & Data Strategy *(Technical Reference)*

```text
┌────────────────────────────────────────────────────────────────────────────┐
│                   Vollautomatischer Auto-Refresh-Service                   │
│  • Läuft selbstständig im Hintergrund (mit Live-Countdown-Anzeige)         │
│  • Prüft MediaMarkt, Saturn, OTTO, Amazon, PlayStation.com & eBay live     │
│  • Speichert alle Preise & Verfügbarkeiten dauerhaft in Room SQLite        │
└───────────────────────────────────────┬────────────────────────────────────┘
                                        │ StateFlow
                                        ▼
┌────────────────────────────────────────────────────────────────────────────┐
│               Neues Farbenfrohes UI mit 3-Menü-Navigation                  │
│                                                                            │
│  ┌──────────────────────┬───────────────────────┬───────────────────────┐  │
│  │   Menü 1: [SHOPS]    │  Menü 2: [DIAGRAMM]   │ Menü 3: [SCALPER]     │  │
│  │  • MediaMarkt        │  • [PS5 PNG] ◄► Chart │  • eBay Scalper (>1k€)│  │
│  │  • Saturn            │  • Zeitachse (Uhrzeit)│  • eBay Normal/Gebr.  │  │
│  │  • OTTO & Amazon     │  • Kaufempfehlung:    │  • Auto-Scraper-Log   │  │
│  │  • PlayStation.com   │    Warten vs. Kaufen  │  • Handy-Push-Alarm   │  │
│  └──────────────────────┴───────────────────────┴───────────────────────┘  │
└────────────────────────────────────────────────────────────────────────────┘
```
