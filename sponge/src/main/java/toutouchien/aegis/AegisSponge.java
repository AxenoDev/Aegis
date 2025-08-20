package toutouchien.aegis;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppedGameEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("aegis")
public class AegisSponge
{
    private final Logger logger;

    @Inject
    public AegisSponge(Logger logger)
    {
        this.logger = logger;
    }

    @Listener
    public void onServerStart(StartedEngineEvent<Server> event)
    {
        EventManager eventManager = Sponge.eventManager();
        eventManager.registerListener(this, this);
    }

    @Listener
    public void onServerStop(StoppedGameEvent event)
    {
        // Clean up any resources or tasks if necessary
    }

    public Logger getLogger()
    {
        return logger;
    }
}
