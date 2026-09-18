package io.sniperjohnny.github.saodaltsmpio.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class LiberatorKeybinds {
    public static final KeyMapping TOGGLE_LIBERATOR = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        "key.saodaltsmpio.toggle_liberator",
        GLFW.GLFW_KEY_RIGHT_SHIFT,
        KeyMapping.CATEGORY_MISC
    ));

    public static void register() {
    }
}