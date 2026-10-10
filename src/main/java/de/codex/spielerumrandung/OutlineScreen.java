package de.codex.spielerumrandung;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OutlineScreen extends Screen {
    private TextFieldWidget defaultColorField;
    private TextFieldWidget friendNameField;
    private TextFieldWidget friendColorField;
    private TextFieldWidget removeNameField;
    private String status = "";

    public OutlineScreen() { super(Text.literal("Spieler-Umrandung")); }

    @Override
    protected void init() {
        int left = width / 2 - 150;
        int top = 34;
        addDrawableChild(ButtonWidget.builder(Text.literal("Umrandung: " + (OutlineConfig.enabled ? "AN" : "AUS")), button -> {
            OutlineConfig.enabled = !OutlineConfig.enabled;
            OutlineConfig.save();
            clearAndInit();
        }).dimensions(left, top, 145, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Alle Spieler: " + (OutlineConfig.outlineEveryone ? "AN" : "AUS")), button -> {
            OutlineConfig.outlineEveryone = !OutlineConfig.outlineEveryone;
            OutlineConfig.save();
            clearAndInit();
        }).dimensions(left + 155, top, 145, 20).build());

        defaultColorField = new TextFieldWidget(textRenderer, left, top + 48, 190, 20, Text.literal("Standardfarbe"));
        defaultColorField.setText(String.format(Locale.ROOT, "#%06X", OutlineConfig.defaultColor));
        addDrawableChild(defaultColorField);
        addDrawableChild(ButtonWidget.builder(Text.literal("Farbe speichern"), button -> {
            try {
                OutlineConfig.defaultColor = parseColor(defaultColorField.getText());
                OutlineConfig.save();
                status = "Standardfarbe gespeichert.";
            } catch (IllegalArgumentException exception) { status = "Farbe muss z. B. #33AAFF sein."; }
        }).dimensions(left + 200, top + 48, 100, 20).build());

        friendNameField = new TextFieldWidget(textRenderer, left, top + 96, 125, 20, Text.literal("Spielername"));
        friendNameField.setMaxLength(16);
        friendNameField.setPlaceholder(Text.literal("Spielername"));
        addDrawableChild(friendNameField);
        friendColorField = new TextFieldWidget(textRenderer, left + 135, top + 96, 65, 20, Text.literal("Farbe"));
        friendColorField.setMaxLength(7);
        friendColorField.setText("#FF55AA");
        addDrawableChild(friendColorField);
        addDrawableChild(ButtonWidget.builder(Text.literal("Hinzufügen"), button -> addFriend())
                .dimensions(left + 210, top + 96, 90, 20).build());

        removeNameField = new TextFieldWidget(textRenderer, left, top + 128, 205, 20, Text.literal("Name zum Entfernen"));
        removeNameField.setMaxLength(16);
        removeNameField.setPlaceholder(Text.literal("Name zum Entfernen"));
        addDrawableChild(removeNameField);
        addDrawableChild(ButtonWidget.builder(Text.literal("Entfernen"), button -> {
            String name = removeNameField.getText().trim();
            if (name.isEmpty()) { status = "Gib einen Namen ein."; return; }
            status = OutlineConfig.removeFriend(name) ? name + " entfernt." : "Name nicht gefunden.";
            clearAndInit();
        }).dimensions(left + 215, top + 128, 85, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Fertig"), button -> close())
                .dimensions(width / 2 - 100, height - 30, 200, 20).build());
    }

    private void addFriend() {
        String name = friendNameField.getText().trim();
        if (name.isEmpty()) { status = "Gib einen Spielernamen ein."; return; }
        try {
            OutlineConfig.addFriend(name, parseColor(friendColorField.getText()));
            status = name + " mit eigener Farbe hinzugefügt.";
            clearAndInit();
        } catch (IllegalArgumentException exception) { status = "Farbe muss z. B. #FF55AA sein."; }
    }

    private static int parseColor(String input) {
        String value = input.trim().replace("#", "");
        if (!value.matches("(?i)[0-9a-f]{6}")) throw new IllegalArgumentException("Invalid hex color");
        return Integer.parseInt(value, 16);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        int left = width / 2 - 150;
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("Standardfarbe (Hex)"), left, 68, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("Freund mit eigener Farbe hinzufügen"), left, 116, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("Freund entfernen"), left, 164, 0xFFFFFF);
        List<String> friends = new ArrayList<>();
        OutlineConfig.friends.forEach((name, color) -> friends.add(name + "  #" + String.format(Locale.ROOT, "%06X", color)));
        if (friends.isEmpty()) {
            context.drawTextWithShadow(textRenderer, Text.literal("Freundesliste ist leer."), left, 198, 0xDDDDDD);
        } else {
            int shown = Math.min(friends.size(), 5);
            for (int i = 0; i < shown; i++) context.drawTextWithShadow(textRenderer, Text.literal(friends.get(i)), left, 198 + i * 12, 0xDDDDDD);
            if (friends.size() > shown) context.drawTextWithShadow(textRenderer, Text.literal("… und " + (friends.size() - shown) + " weitere"), left, 198 + shown * 12, 0xAAAAAA);
        }
        if (!status.isEmpty()) context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 64, 0xFFFF55);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Tasten kannst du unter Optionen > Steuerung ändern."), width / 2, height - 44, 0xAAAAAA);
    }

    @Override
    public boolean shouldPause() { return false; }
}
