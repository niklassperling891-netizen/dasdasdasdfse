# Spieler-Umrandung

Eine Fabric-Mod für **Minecraft Java 1.21.11**. Sie zeigt Spieler mit einer farbigen Umrandung auf deinem eigenen Bildschirm. Freunde können eine eigene Farbe bekommen; Einstellungen werden lokal gespeichert.

## Menü und Tasten

- **O** öffnet das Einstellungsmenü.
- **P** schaltet die Umrandung ein oder aus.
- Beide Tasten kannst du in Minecraft unter **Optionen → Steuerung → Tastenbelegung** ändern.
- Im Menü kannst du Umrandung und „Alle Spieler“ ein- und ausschalten, die Standardfarbe ändern sowie Freunde mit eigener Farbe hinzufügen oder entfernen.
- Farben werden als sechsstelliger Hex-Wert eingegeben, zum Beispiel `#33AAFF`.

Die Einstellungen liegen in `config/spielerumrandung.json`.

## Bauen und installieren

Du brauchst Java 21 und Gradle 9.2.1. Öffne das Projekt in IntelliJ IDEA als Gradle-Projekt oder führe im Projektordner `gradle build` aus. Die fertige Mod liegt anschließend unter `build/libs/spieler-umrandung-1.0.0.jar`.

Installiere Fabric Loader und Fabric API passend zu Minecraft 1.21.11 und kopiere die JAR in den `mods`-Ordner. Auf GitHub baut der Workflow unter **Actions** das Projekt bei jedem Push und Pull Request und stellt die JAR als Artefakt bereit.

## GitHub

Erstelle auf GitHub ein leeres Repository und lade den Inhalt dieses Ordners hoch.

## Hinweis

Das ist eine clientseitige Mod: Andere Spieler sehen deine Umrandungen nicht. Sie funktioniert in Welten und auf Servern, bei denen clientseitige Fabric-Mods erlaubt sind.
