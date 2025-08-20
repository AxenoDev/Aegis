package toutouchien.aegis;

import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import toutouchien.aegis.common.AegisCommon;

import java.time.Duration;
import java.util.Set;

public class AegisListener {
    private final AegisCommon aegis;
    private final Logger logger;

    public  AegisListener(AegisSponge sponge)
    {
        this.logger = sponge.getLogger();
        this.aegis = new AegisCommon((java.util.logging.Logger) logger);

        Task.builder()
                .execute(() -> {
                    Set<String> newBlockedIPs = aegis.fetchBotIPs();
                    aegis.updateBlockedIPs(newBlockedIPs);
                })
                .delay(Duration.ofSeconds(1))
                .interval(Duration.ofHours(3))
                .build();
    }

    @Listener(order = Order.FIRST)
    public void onPlayerLogin(ServerSideConnectionEvent.Login event) {
        String hostAddress = event.connection().address().getAddress().getHostAddress();
        if (aegis.isBlocked(hostAddress)) {
            event.setCancelled(true);
            event.setMessage(Component.text("Disconnected"));
        }
    }

    @Listener(order = Order.FIRST)
    public void onPlayerAuth(ServerSideConnectionEvent.Auth event) {
        String hostAddress = event.connection().address().getAddress().getHostAddress();
        if (aegis.isBlocked(hostAddress)) {
            event.setCancelled(true);
            event.setMessage(Component.text("Disconnected"));
        }
    }

}
