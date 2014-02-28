package salutcestmoi.VV.RoiAsukah;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
public class VVFFAListener implements Listener
{
	@SuppressWarnings("unused")
	private VVFFA plugin;

	public VVFFAListener(VVFFA plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PAEndEvent(net.slipcor.pvparena.events.PAEndEvent e)
	{
		if(e.getArena().getName().equalsIgnoreCase("ffa"))
		{
			VVFFA.peuEntrerLobbyffa = true;
			VVFFA.peuQuitterLobbyffa = true;
			VVFFA.Lobbyffa.clear();
		}	
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerLeaveEvent(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		FPlayer fp = FPlayers.i.get(p);
		if(VVFFA.Lobbyffa.containsKey(fp.getFaction()))
		{
			if(VVFFA.Lobbyffa.get(fp.getFaction()).contains(p))
			{
				p.performCommand("ffaleave");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandPACDB(PlayerCommandPreprocessEvent e)
	{
		if (e.getMessage().equalsIgnoreCase("/pa Emeute") || e.getMessage().equalsIgnoreCase("/pvparena Emeute") && !e.getPlayer().hasPermission("vvcdb.admin"))
			{
				Player p = e.getPlayer();
				FPlayer fp = FPlayers.i.get(p);
				if (VVFFA.Lobbyffa.containsKey(fp.getFaction()))
					{
						if(!VVFFA.Lobbyffa.get(fp.getFaction()).contains(p))
						{
							e.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas la permission");
							e.setCancelled(true);
						}
					}
				else
				{
					e.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas la permission");
					e.setCancelled(true);
				}
			}
	}
}
