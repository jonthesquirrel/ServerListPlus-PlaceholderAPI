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

public class BukkitMain extends JavaPlugin {
    @Override
    public void onLoad() {
        //TODO deduplicate
        ReplacementManager.getDynamic().add(new PatternPlaceholder(Pattern.compile("%placeholderapi_players(?:@(\\w+))?(?:,(\\d+))?(?:`(.*)`)(?:\\|([^%]*))?%", Pattern.MULTILINE)) {
            @Override
            public String replace(StatusResponse response, String s) {
                final Matcher matcher = matcher(s);
                return Patterns.replace(matcher, s, new ContinousIterator<Object>() {
                    @Override
                    public Object next() {
                        final String server = matcher.group(1);
                        final Integer limit = matcher.group(2) == null ? Integer.MAX_VALUE : Integer.parseInt(matcher.group(2));
                        final String format = matcher.group(3) == null ? "" : matcher.group(3);
                        final String delimiter = matcher.group(4) == null ? "" : matcher.group(4);

                        return Bukkit.getOnlinePlayers().stream()
                                //@server filter has no effect on bukkit
                                .limit(limit)
                                .map(player -> PlaceholderAPI.setBracketPlaceholders(player, format))
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
