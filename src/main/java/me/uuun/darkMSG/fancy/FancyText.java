package me.uuun.darkMSG.fancy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;

public final class FancyText {

    private static final Map<Character, String> MAP = Map.ofEntries(
            Map.entry('a', "ᴀ"),
            Map.entry('b', "ʙ"),
            Map.entry('c', "ᴄ"),
            Map.entry('d', "ᴅ"),
            Map.entry('e', "ᴇ"),
            Map.entry('f', "ꜰ"),
            Map.entry('g', "ɢ"),
            Map.entry('h', "ʜ"),
            Map.entry('i', "ɪ"),
            Map.entry('j', "ᴊ"),
            Map.entry('k', "ᴋ"),
            Map.entry('l', "ʟ"),
            Map.entry('m', "ᴍ"),
            Map.entry('n', "ɴ"),
            Map.entry('o', "ᴏ"),
            Map.entry('p', "ᴘ"),
            Map.entry('q', "ǫ"),
            Map.entry('r', "ʀ"),
            Map.entry('s', "ꜱ"),
            Map.entry('t', "ᴛ"),
            Map.entry('u', "ᴜ"),
            Map.entry('v', "ᴠ"),
            Map.entry('w', "ᴡ"),
            Map.entry('x', "x"),
            Map.entry('y', "ʏ"),
            Map.entry('z', "ᴢ")
    );

    private FancyText() {
    }

    public static String toFancy(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int i = 0;

        while (i < text.length()) {
            char c = text.charAt(i);

            if (c == '§') {
                if (i + 13 < text.length() && text.charAt(i + 1) == 'x') {
                    sb.append(text, i, i + 14);
                    i += 14;
                    continue;
                }
                if (i + 1 < text.length()) {
                    sb.append(text, i, i + 2);
                    i += 2;
                    continue;
                }
            }

            sb.append(MAP.getOrDefault(Character.toLowerCase(c), String.valueOf(c)));
            i++;
        }
        return sb.toString();
    }

    public static Component parse(String text) {
        return MiniMessage.miniMessage()
                .deserialize("<!i>" + text.replace("<prefix>", "<yellow>⚡ <white>"));
    }

    public static String parseString(String text) {
        return text.replace("<prefix>", "")
                .replace("<black>", "§0")
                .replace("<dark_blue>", "§1")
                .replace("<dark_green>", "§2")
                .replace("<dark_aqua>", "§3")
                .replace("<dark_red>", "§4")
                .replace("<dark_purple>", "§5")
                .replace("<gold>", "§6")
                .replace("<gray>", "§7")
                .replace("<dark_gray>", "§8")
                .replace("<blue>", "§9")
                .replace("<green>", "§a")
                .replace("<aqua>", "§b")
                .replace("<red>", "§c")
                .replace("<light_purple>", "§d")
                .replace("<yellow>", "§e")
                .replace("<white>", "§f")
                .replace("<obf>", "§k")
                .replace("<bold>", "§l")
                .replace("<italic>", "§o")
                .replace("<underlined>", "§n")
                .replace("<strike>", "§m")
                .replace("<reset>", "§r");
    }
}