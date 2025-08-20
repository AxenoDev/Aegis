package toutouchien.aegis;

import org.bukkit.plugin.java.JavaPlugin;

public class AegisBukkit extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new AegisListener(this), this);
    }

    @Override
    public void onDisable()
    {
        getServer().getAsyncScheduler().cancelTasks(this);
    }
}
