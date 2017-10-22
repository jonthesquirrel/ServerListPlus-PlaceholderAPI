package io.github.jonthesquirrel.serverlistplusplaceholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecrell.serverlistplus.core.ServerListPlusCore;
import net.minecrell.serverlistplus.core.replacement.PatternPlaceholder;
import net.minecrell.serverlistplus.core.replacement.ReplacementManager;
import net.minecrell.serverlistplus.core.replacement.util.Patterns;
import net.minecrell.serverlistplus.core.status.StatusResponse;
import net.minecrell.serverlistplus.core.util.ContinousIterator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerListPlusPlaceholderAPI extends JavaPlugin {
    @Override
    public void onLoad() {
        ReplacementManager.getDynamic().add(new PatternPlaceholder(Pattern.compile("%placeholderapi_players(?:,(\\d+))?(?:`(.*)`)(?:\\|([^%]+))?%", Pattern.MULTILINE)) {
            @Override
            public String replace(StatusResponse response, String s) {
                final Matcher matcher = matcher(s);
                return Patterns.replace(matcher, s, new ContinousIterator<Object>() {
                    @Override
                    public Object next() {
                        final Integer limit = matcher.group(1) == null ? Integer.MAX_VALUE : Integer.parseInt(matcher.group(1));
                        final String placeholder = matcher.group(2) == null ? "" : matcher.group(2).replaceAll("[{}]", "%");
                        final String delimiter = matcher.group(3) == null ? "" : matcher.group(3);

                        return Bukkit.getOnlinePlayers().stream()
                                .limit(limit)
                                .map(player -> PlaceholderAPI.setPlaceholders(player, placeholder))
                                .collect(Collectors.joining(delimiter));
                    }
                });
            }

            @Override
            public String replace(ServerListPlusCore core, String s) {
                return "???";
            }
        });
    }
}
