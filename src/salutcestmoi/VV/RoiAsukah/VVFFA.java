package salutcestmoi.VV.RoiAsukah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.plugin.PluginManager;

import salutcestmoi.VV.RoiAsukah.VVFFAListener;

public class VVFFA extends JavaPlugin{

	private Logger logger = Logger.getLogger("Minecraft");
	public static HashMap<Faction, ArrayList<Player>> Lobbyffa = new HashMap<Faction, ArrayList<Player>>();
	public static ArrayList<Player> ssListeLobbyffa;
	public static boolean peuEntrerLobbyffa;
	public static boolean peuQuitterLobbyffa;
	public static boolean LauchingTaskRunning;
	private int taskid = 0;
	public int nbrPlayerDansLobbyffa = 0;
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();		
		pm.registerEvents(new VVFFAListener(this), this);
		peuEntrerLobbyffa = true;
		peuQuitterLobbyffa = true;
		LauchingTaskRunning = false;
		logger.info("VVFFA est chargé");
	}

	@Override
	public void onDisable()
	{
		logger.info("VVFFA est déchargé");
	}

	public int getNbreFactionsLobby()
	{
		int i = 0;
		for(Faction f : Factions.i.get())
		{
			if(Lobbyffa.containsKey(f))
			{
				i++;
			}
		}
		return i;
	}
	
	public String getNomFactionsLobby()
	{
		String s = new String("");
		for(Faction f : Factions.i.get())
		{
			if(Lobbyffa.containsKey(f))
			{
				s = s + f.getTag() + ", ";
			}
		}
		if(s.length() >= 2)
		{
			s.substring(0, s.length() - 2);
		}
		return s;
	}

	public String chaineJoueursDansLobbyffa()
	{
		String s = new String("");
		for(Faction f : Lobbyffa.keySet())
		{
			s = s + f.getTag() + ": ";
			for(Player p : Lobbyffa.get(f))
			{
				s = s + p.getName() + ", ";
			}
		}
		if(s.length() >= 2)
		{
			s.substring(0, s.length() - 2);
		}
		return s;
	}
	public boolean onCommand(CommandSender sender, Command command, String label,String[] args)
	{
		Player p = (Player) sender;
		FPlayer fp = FPlayers.i.get(p);

		if(label.equalsIgnoreCase("ffajoin") && p.hasPermission("vvffa.basic"))
		{
			if(!fp.getFaction().getTag().equalsIgnoreCase("wilderness"))
			{
				if(peuEntrerLobbyffa)
				{
					if(Lobbyffa.containsKey(fp.getFaction()))
					{
						if(!Lobbyffa.get(fp.getFaction()).contains(p))
						{
							nbrPlayerDansLobbyffa++;
							p.sendMessage(ChatColor.AQUA + "Vous êtes entrés dans le Lobby ffa.");
							p.sendMessage(ChatColor.AQUA + "Les joueurs actuellement dans le lobby ffa sont : " + chaineJoueursDansLobbyffa());
							Lobbyffa.get(fp.getFaction()).add(p);
							if(getNbreFactionsLobby() >= 2 && nbrPlayerDansLobbyffa >= 3)
							{
								if(!LauchingTaskRunning)
								{
									LauchingTaskRunning = true;
									Bukkit.getServer().broadcastMessage("Un Free for all est sur le point de ce lancer. Les factions " + getNomFactionsLobby() + " et les joueurs " + chaineJoueursDansLobbyffa() + " y participent, les derniers ont une minute pour le rejoindre !");
									BukkitTask task = new TaskLauching(this).runTaskLater(this, 1200);
									taskid = task.getTaskId();
								}
							}
						}
						else
						{
							p.sendMessage(ChatColor.DARK_RED + "Vous êtes déjà dans le lobby ffa");
						}
					}
					else
					{
						nbrPlayerDansLobbyffa++;
						p.sendMessage(ChatColor.AQUA + "Vous êtes entrés dans le Lobby ffa.");
						p.sendMessage(ChatColor.AQUA + "Les joueurs actuellement dans le lobby ffa sont : " + chaineJoueursDansLobbyffa());
						ssListeLobbyffa = new ArrayList<Player>();
						ssListeLobbyffa.add(p);
						Lobbyffa.put(fp.getFaction(), ssListeLobbyffa);
						if(getNbreFactionsLobby() >= 2 && nbrPlayerDansLobbyffa >= 3)
						{
							if(!LauchingTaskRunning)
							{
								LauchingTaskRunning = true;
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Un Free for all est sur le point de ce lancer. Les factions " + getNomFactionsLobby() + " et les joueurs " + chaineJoueursDansLobbyffa() + " y participent, les derniers ont une minute pour le rejoindre !");
								BukkitTask task = new TaskLauching(this).runTaskLater(this, 1200);
								taskid = task.getTaskId();
							}
						}
					}
				}
				else
				{
					p.sendMessage(ChatColor.DARK_RED + "Une partie est déjà en cours, vous ne pouvez pas rejoindre le lobby");
				}
			}
			else
			{
				p.sendMessage(ChatColor.DARK_RED + "Vous n'avez pas de faction");
			}
			return true;
		}
		else if(label.equalsIgnoreCase("ffaleave") && p.hasPermission("vvffa.basic"))
		{
			if(peuQuitterLobbyffa)
			{
				if(Lobbyffa.containsKey(fp.getFaction()))
				{
					if(Lobbyffa.get(fp.getFaction()).contains(p))
					{
						nbrPlayerDansLobbyffa--;
						p.sendMessage(ChatColor.AQUA + "Vous avez quitté le Lobbyffa");
						Lobbyffa.get(fp.getFaction()).remove(p);
						if(Lobbyffa.get(fp.getFaction()).isEmpty())
						{
							Lobbyffa.remove(fp.getFaction());
						}
							if(getNbreFactionsLobby() < 2 || nbrPlayerDansLobbyffa <3)
							{
								if(LauchingTaskRunning)
								{
									LauchingTaskRunning = false;
									Bukkit.getServer().getScheduler().cancelTask(taskid);
									Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Le lancement du ffa est annulé car " + p.getName() + " a quitté le lobby");
								}
							}
					}
					else
					{
						p.sendMessage(ChatColor.DARK_RED + "Vous n'êtes pas dans le lobby ffa");
					}
				}
				else
				{
					p.sendMessage(ChatColor.DARK_RED + "Vous n'êtes pas dans le lobby ffa");
				}
			}
			else
			{
				p.sendMessage(ChatColor.DARK_RED + "Vous ne pouvez pas quitter le Lobby, la partie est lancée");
			}
			return true;
		}
		else if(label.equalsIgnoreCase("whoisinffa") || label.equalsIgnoreCase("wiiffa") && p.hasPermission("vvffa.basic"))
		{
			p.sendMessage(ChatColor.AQUA + "Les joueurs actuellement dans le lobby ffa sont : " + chaineJoueursDansLobbyffa());
			return true;
		}
		
		return false;
	}

}
