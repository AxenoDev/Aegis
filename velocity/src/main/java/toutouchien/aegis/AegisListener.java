package toutouchien.aegis;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import net.kyori.adventure.text.Component;
import toutouchien.aegis.common.AegisCommon;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AegisListener
{

    private final AegisCommon aegis;
    private final ScheduledExecutorService scheduler;

    public AegisListener(AegisVelocity velocity)
    {
        this.aegis = new AegisCommon(velocity.getLogger());
        this.scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            Set<String> newBlockedIPs = aegis.fetchBotIPs();
            aegis.updateBlockedIPs(newBlockedIPs);
        }, 1L, 3L * 60L * 60L, TimeUnit.SECONDS);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(ProxyPingEvent event) {
        String hostAddress = event.getConnection().getRemoteAddress().getAddress().getHostAddress();
        if (aegis.isBlocked(hostAddress)) {
            event.setPing(event.getPing().asBuilder()
                    .description(Component.text("Disconnected"))
                    .build());
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(LoginEvent event) {
        String hostAddress = event.getPlayer().getRemoteAddress().getAddress().getHostAddress();
        if (aegis.isBlocked(hostAddress)) {
            event.setResult(LoginEvent.ComponentResult.denied(Component.text("Disconnected")));
        }
    }
}
