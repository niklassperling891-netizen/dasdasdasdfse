package de.codex.spielerumrandung;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SpielerUmrandungClient implements ClientModInitializer {
    private static final KeyBinding.Category KEY_CATEGORY =
        KeyBinding.Category.create(Identifier.of("spielerumrandung", "main"));
    private static KeyBinding openMenuKey;
    private static KeyBinding toggleOutlinesKey;

    @Override
    public void onInitializeClient() {
        OutlineConfig.load();
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.spielerumrandung.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY));
        toggleOutlinesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.spielerumrandung.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) client.setScreen(new OutlineScreen());
            while (toggleOutlinesKey.wasPressed()) {
                OutlineConfig.enabled = !OutlineConfig.enabled;
                OutlineConfig.save();
            }
            if (client.world == null || client.player == null) return;
            for (PlayerEntity player : client.world.getPlayers()) {
                if (player == client.player) continue;
                player.setGlowing(OutlineConfig.enabled && (OutlineConfig.isFriend(player.getName().getString()) || OutlineConfig.outlineEveryone));
            }
        });
    }
}
