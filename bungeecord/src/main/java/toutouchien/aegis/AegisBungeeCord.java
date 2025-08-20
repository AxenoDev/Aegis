package toutouchien.aegis;

import net.md_5.bungee.api.plugin.Plugin;

public class AegisBungeeCord extends Plugin
{

    @Override
    public void onEnable()
    {
        getProxy().getPluginManager().registerListener(this, new AegisListener(this));
    }

    @Override
    public void onDisable()
    {
        getProxy().getScheduler().cancel(this);
    }
}
