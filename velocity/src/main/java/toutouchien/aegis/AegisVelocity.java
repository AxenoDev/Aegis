package toutouchien.aegis;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.logging.Logger;

@Plugin(
        id = "aegis",
        name = "Aegis",
        version = "1.0.0",
        authors = {"Toutouchien"}
)
public class AegisVelocity
{
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public AegisVelocity(ProxyServer server, Logger logger)
    {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
        server.getEventManager().register(this, new AegisListener(this));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event)
    {
        // Clean up any resources or tasks if necessary
    }

    public ProxyServer getServer()
    {
        return server;
    }

    public Logger getLogger()
    {
        return logger;
    }
}
