package toutouchien.aegis;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import toutouchien.aegis.common.AegisCommon;

public class AegisListener implements Listener
{
    private final AegisCommon aegis;

    public AegisListener(AegisBungeeCord bungee)
    {
        this.aegis = new AegisCommon(bungee.getLogger());

        bungee.getProxy().getScheduler().schedule(bungee, () -> {
            var newBlockedIPs = this.aegis.fetchBotIPs();
            if (newBlockedIPs != null)
                this.aegis.updateBlockedIPs(newBlockedIPs);
        }, 1L, 3L * 60L * 60L, java.util.concurrent.TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProxyPing(ProxyPingEvent event)
    {
        String hostAddress = event.getConnection().getAddress().getAddress().getHostAddress();
        if (!this.aegis.isBlocked(hostAddress))
            event.getConnection().disconnect();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(LoginEvent event)
    {
        String hostAddress = event.getConnection().getAddress().getAddress().getHostAddress();
        if (!this.aegis.isBlocked(hostAddress))
        {
            event.setCancelled(true);
            event.setCancelReason("Disconnected");
        }
    }
}
