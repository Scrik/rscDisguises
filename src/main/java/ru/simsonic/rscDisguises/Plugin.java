package ru.simsonic.rscDisguises;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.TargetedDisguise.TargetType;
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
	private final HashSet<Player> disguises = new HashSet<>();
	private Disguise disguise = null;
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
		// reloadConfig();
		consoleLog.log(Level.INFO, chatPrefix + "Plugin has been successfully enabled.");
	}
	@Override
	public void onDisable()
	{
		if(disguise != null)
			disguise.removeDisguise();
		for(Player player : getServer().getOnlinePlayers())
			DisguiseAPI.undisguiseToAll(player);
		// saveConfig();
		getServer().getServicesManager().unregisterAll(this);
		consoleLog.info(chatPrefix + "Plugin has been disabled.");
	}
	private boolean setupDisguise(String type)
	{
		// Убиваю старую маскировку
		if(disguise != null)
		{
			disguise.removeDisguise();
		}
		// Нужна ли новая?
		if(type == null || "".equals(type))
			return false;
		// Если нужна, то какая?
		switch(type)
		{
			case "ttt":
				disguise = new MobDisguise(DisguiseType.CREEPER);
				break;
			case "none":
				return true;
			default:
				// Нет такой маскировки
				return false;
		}
		// Применяю её ко всем игрокам на сервере
		for(Player player : getServer().getOnlinePlayers())
			DisguiseAPI.disguiseToAll(player, disguise);
		return true;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(disguise != null)
			DisguiseAPI.disguiseToAll(event.getPlayer(), disguise);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		DisguiseAPI.undisguiseToAll(event.getPlayer());
	}
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		DisguiseAPI.undisguiseToAll(event.getPlayer());
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		switch(command.getName().toLowerCase())
		{
			case "rscd":
				if(args != null && args.length > 0)
				{
					if(sender.hasPermission("rscd.admin"))
					{
						sender.sendMessage(chatPrefix + "§cНедостаточно прав для выполнения этого действия.");
						return true;
					}
					boolean result = setupDisguise(args[0]);
					sender.sendMessage(chatPrefix + (result
						? "§aУспешно"
						: "§cНеизвестный тип маскировки"));
				} else
					sender.sendMessage(chatPrefix + "Plugin version " + getDescription().getVersion() + " © SimSonic.");
				return true;
		}
		return false;
	}
}
