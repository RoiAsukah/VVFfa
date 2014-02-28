package salutcestmoi.VV.RoiAsukah;

import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.managers.ArenaManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.Faction;

public class TaskLauching extends BukkitRunnable{

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

    public TaskLauching(JavaPlugin plugin) {
        this.plugin = plugin;
    }
 
    public void run() {
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Le free for all est lancé !");
    	Arena a = ArenaManager.getArenaByName("Emeute");
    	for(Faction MapKey : VVFFA.Lobbyffa.keySet())
    	{
    		for(Player p : VVFFA.Lobbyffa.get(MapKey))
    		{
    			p.performCommand("pa Ffa " + MapKey.getTag() );
    		}
    	}
    	VVFFA.peuEntrerLobbyffa = false;
    	VVFFA.peuQuitterLobbyffa = false;
    	VVFFA.LauchingTaskRunning = false;
    }
}