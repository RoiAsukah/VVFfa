package salutcestmoi.VV.RoiAsukah;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import salutcestmoi.VV.RoiAsukah.VVFFA;
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
}
