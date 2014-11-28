package ru.simsonic.rscDisguises;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin implements Listener
{
	public  static final Logger consoleLog = Logger.getLogger("Minecraft");
	private static final String chatPrefix = "§8[rscDisguises] §7";
	@Override
	public void onLoad()
	{
		saveDefaultConfig();
		consoleLog.log(Level.INFO, chatPrefix + "Plugin has been loaded.");
	}
	@Override
	public void onEnable()
	{
		// Register event's dispatcher
		getServer().getPluginManager().registerEvents(this, this);
		// Read settings 
		reloadConfig();
		// Done
		consoleLog.log(Level.INFO, chatPrefix + "Plugin has been successfully enabled.");
	}
	@Override
	public void onDisable()
	{
		// Save settings
		saveConfig();
		getServer().getServicesManager().unregisterAll(this);
		consoleLog.info(chatPrefix + "Plugin has been disabled.");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		switch(label.toLowerCase())
		{
			case "rscd":
				break;
		}
		return true;
	}
}