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
		// Если нужна, то какая?
		switch(type.toUpperCase())
		{
			case "BLAZE":
				disguise = new MobDisguise(DisguiseType.BLAZE);
				break;
			case "CAVE_SPIDER":
				disguise = new MobDisguise(DisguiseType.CAVE_SPIDER);
				break;
			case "CHICKEN":
				disguise = new MobDisguise(DisguiseType.CHICKEN);
				break;
			case "COW":
				disguise = new MobDisguise(DisguiseType.COW);
				break;
			case "CREEPER":
				disguise = new MobDisguise(DisguiseType.CREEPER);
				break;
			case "DONKEY":
				disguise = new MobDisguise(DisguiseType.DONKEY);
				break;
			case "ENDER_DRAGON":
				disguise = new MobDisguise(DisguiseType.ENDER_DRAGON);
				break;
			case "ENDERMAN":
				disguise = new MobDisguise(DisguiseType.ENDERMAN);
				break;
			case "GHAST":
				disguise = new MobDisguise(DisguiseType.GHAST);
				break;
			case "GIANT":
				disguise = new MobDisguise(DisguiseType.GIANT);
				break;
			case "HORSE":
				disguise = new MobDisguise(DisguiseType.HORSE);
				break;
			case "IRON_GOLEM":
				disguise = new MobDisguise(DisguiseType.IRON_GOLEM);
				break;
			case "MAGMA_CUBE":
				disguise = new MobDisguise(DisguiseType.MAGMA_CUBE);
				break;
			case "MULE":
				disguise = new MobDisguise(DisguiseType.MULE);
				break;
			case "MUSHROOM_COW":
				disguise = new MobDisguise(DisguiseType.MUSHROOM_COW);
				break;
			case "OCELOT":
				disguise = new MobDisguise(DisguiseType.OCELOT);
				break;
			case "PIG":
				disguise = new MobDisguise(DisguiseType.PIG);
				break;
			case "PIG_ZOMBIE":
				disguise = new MobDisguise(DisguiseType.PIG_ZOMBIE);
				break;
			case "SHEEP":
				disguise = new MobDisguise(DisguiseType.SHEEP);
				break;
			case "SILVERFISH":
				disguise = new MobDisguise(DisguiseType.SILVERFISH);
				break;
			case "SKELETON":
				disguise = new MobDisguise(DisguiseType.SKELETON);
				break;
			case "SKELETON_HORSE":
				disguise = new MobDisguise(DisguiseType.SKELETON_HORSE);
				break;
			case "SLIME":
				disguise = new MobDisguise(DisguiseType.SLIME);
				break;
			case "SNOWMAN":
				disguise = new MobDisguise(DisguiseType.SNOWMAN);
				break;
			case "SPIDER":
				disguise = new MobDisguise(DisguiseType.SPIDER);
				break;
			case "SQUID":
				disguise = new MobDisguise(DisguiseType.SQUID);
				break;
			case "UNDEAD_HORSE":
				disguise = new MobDisguise(DisguiseType.UNDEAD_HORSE);
				break;
			case "VILLAGER":
				disguise = new MobDisguise(DisguiseType.VILLAGER);
				break;
			case "WITCH":
				disguise = new MobDisguise(DisguiseType.WITCH);
				break;
			case "WITHER":
				disguise = new MobDisguise(DisguiseType.WITHER);
				break;
			case "WITHER_SKELETON":
				disguise = new MobDisguise(DisguiseType.WITHER_SKELETON);
				break;
			case "WOLF":
				disguise = new MobDisguise(DisguiseType.WOLF);
				break;
			case "ZOMBIE":
				disguise = new MobDisguise(DisguiseType.ZOMBIE);
				break;
			case "ZOMBIE_VILLAGER":
				disguise = new MobDisguise(DisguiseType.ZOMBIE_VILLAGER);
				break;
			case "NONE":
				// Отмена всех маскировок
				return true;
			default:
				// Нет такой маскировки
				return false;
		}
		doDisguises = true;
		// Применяю её ко всем игрокам на сервере
		for(Player player : getServer().getOnlinePlayers())
			DisguiseAPI.disguiseToAll(player, disguise);
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
					sender.sendMessage(chatPrefix + "Плагин rscDisguises версии " + getDescription().getVersion() + " © SimSonic.");
				return true;
		}
		return false;
	}
}
