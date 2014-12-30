package ru.simsonic.rscDisguises;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin implements Listener
{
	public  static final Logger consoleLog = Logger.getLogger("Minecraft");
	private static final String chatPrefix = "§8[rscDisguises] §7";
	private Disguise disguise = null;
	private boolean doDisguises;
	@Override
	public void onLoad()
	{
		// saveDefaultConfig();
		consoleLog.log(Level.INFO, chatPrefix + "Plugin has been loaded.");
	}
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		doDisguises = false;
		// reloadConfig();
		consoleLog.log(Level.INFO, chatPrefix + "Plugin has been successfully enabled.");
	}
	@Override
	public void onDisable()
	{
		// saveConfig();
		getServer().getServicesManager().unregisterAll(this);
		consoleLog.info(chatPrefix + "Plugin has been disabled.");
	}
	private void cancelDisguises()
	{
		for(Player player : getServer().getOnlinePlayers())
			DisguiseAPI.undisguiseToAll(player);
		if(disguise != null)
			disguise.removeDisguise();
		doDisguises = false;
	}
	private boolean setupDisguise(String type)
	{
		// Убиваю старую маскировку
		cancelDisguises();
		// Нужна ли новая?
		if(type == null || "".equals(type))
			return false;
		// Отмена всех маскировок?
		if("NONE".equalsIgnoreCase(type))
			return true;
		// Если всё-таки нужна, то какая?
		try
		{
			// Поиск соответствующей маскировки
			disguise = new MobDisguise(DisguiseType.valueOf(type.toUpperCase()));
			// Применяю её ко всем игрокам на сервере
			doDisguises = true;
			for(Player player : getServer().getOnlinePlayers())
				DisguiseAPI.disguiseToAll(player, disguise);
		} catch(IllegalArgumentException ex) {
			// Поиск не дал результата — маскировка не существует
			return false;
		}
		return true;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(doDisguises && disguise != null)
			DisguiseAPI.disguiseToAll(event.getPlayer(), disguise);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		if(DisguiseAPI.isDisguised(player))
			DisguiseAPI.undisguiseToAll(player);
	}
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		final Player player = event.getPlayer();
		if(DisguiseAPI.isDisguised(player))
			DisguiseAPI.undisguiseToAll(player);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		switch(command.getName().toLowerCase())
		{
			case "rscd":
				if(args != null && args.length > 0)
				{
					if(!sender.hasPermission("rscd.admin"))
					{
						sender.sendMessage(chatPrefix + "§cНедостаточно прав для выполнения этого действия.");
						return true;
					}
					sender.sendMessage(chatPrefix + (setupDisguise(args[0])
						? "§aУспешно"
						: "§cНеизвестный тип маскировки"));
				} else
					sender.sendMessage(chatPrefix
						+ "Плагин rscDisguises версии " + getDescription().getVersion()
						+ " © " + getDescription().getAuthors().get(0) + ".");
				return true;
		}
		return false;
	}
}
