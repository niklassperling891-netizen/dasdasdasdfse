package de.codex.spielerumrandung;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class OutlineConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("spielerumrandung.json");
    public static int defaultColor = 0x33AAFF;
    public static boolean outlineEveryone = false;
    public static boolean enabled = true;
    public static Map<String, Integer> friends = new LinkedHashMap<>();
    private OutlineConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) { save(); return; }
        try (Reader reader = Files.newBufferedReader(FILE)) {
            Stored stored = GSON.fromJson(reader, Stored.class);
            if (stored != null) {
                defaultColor = stored.defaultColor;
                outlineEveryone = stored.outlineEveryone;
                enabled = stored.enabled;
                friends = stored.friends == null ? new LinkedHashMap<>() : new LinkedHashMap<>(stored.friends);
            }
        } catch (Exception exception) { System.err.println("[Spieler-Umrandung] Einstellungen konnten nicht gelesen werden: " + exception.getMessage()); }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Stored stored = new Stored(); stored.defaultColor = defaultColor; stored.outlineEveryone = outlineEveryone; stored.enabled = enabled; stored.friends = friends;
            try (Writer writer = Files.newBufferedWriter(FILE)) { GSON.toJson(stored, writer); }
        } catch (Exception exception) { System.err.println("[Spieler-Umrandung] Einstellungen konnten nicht gespeichert werden: " + exception.getMessage()); }
    }

    public static boolean isFriend(String name) { return friends.keySet().stream().anyMatch(friend -> friend.equalsIgnoreCase(name)); }
    public static int colorFor(String name) {
        return friends.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(name)).map(Map.Entry::getValue).findFirst().orElse(defaultColor);
    }
    public static void setDefaultColor(int color) { defaultColor = color; save(); }
    public static void addFriend(String name, int color) { friends.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase(name)); friends.put(name, color); save(); }
    public static boolean removeFriend(String name) { boolean removed = friends.keySet().removeIf(friend -> friend.equalsIgnoreCase(name)); if (removed) save(); return removed; }
    public static String describeFriends() {
        return friends.entrySet().stream().map(entry -> entry.getKey() + " (#" + String.format(Locale.ROOT, "%06X", entry.getValue()) + ")").reduce((a, b) -> a + ", " + b).orElse("");
    }
    private static final class Stored { int defaultColor = 0x33AAFF; boolean outlineEveryone; boolean enabled = true; Map<String, Integer> friends = new LinkedHashMap<>(); }
}
