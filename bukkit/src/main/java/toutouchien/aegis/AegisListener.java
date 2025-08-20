package toutouchien.aegis;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import toutouchien.aegis.common.AegisCommon;

import java.util.concurrent.TimeUnit;

public class AegisListener implements Listener
{
    private final AegisCommon aegis;

    public AegisListener(AegisBukkit bukkit)
    {
        this.aegis = new AegisCommon(bukkit.getSLF4JLogger());

        Bukkit.getAsyncScheduler().runAtFixedRate(bukkit, task -> {
            var newBlockedIPs = this.aegis.fetchBotIPs();
            if (newBlockedIPs != null)
                this.aegis.updateBlockedIPs(newBlockedIPs);
        }, 1L, 3L * 60L * 60L, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPaperServerListPing(PaperServerListPingEvent event)
    {
        String hostAddress = event.getAddress().getHostAddress();
        if (!this.aegis.isBlocked(hostAddress))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event)
    {
        String hostAddress = event.getAddress().getHostAddress();
        if (!this.aegis.isBlocked(hostAddress))
            return;

        TranslatableComponent kickMessage = Component.translatable("multiplayer.disconnect.generic"); // "Disconnected"
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
    }
}
