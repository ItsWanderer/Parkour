package Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor{
	
	private main plugin;

	public Commands(main main){
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player) {
			
		
		Player p = (Player) sender;
		
		
		if(args.length != 1) {
			p.sendMessage(plugin.help);
		} else {
			if(args[0].equalsIgnoreCase("join")) {
				joinArena(p);
			} else 
			if(args[0].equalsIgnoreCase("leave")) {
			     plugin.leaveArena(p);
			} else 
		if(args[0].equalsIgnoreCase("help")) {
			sendHelp(p);
		} else 
			if(args[0].equalsIgnoreCase("setspawn")) {
			   setspawn(p);
	
		} else {
		       p.sendMessage(plugin.help);
		}
	}
		} else {
			sender.sendMessage("You must be a player!");
		}
		return true;
	}
	


     //METHODS
     public void sendHelp(Player p) {
    	 p.sendMessage("§6[]===============(§b§lJumper Help §6)===============[]");
			p.sendMessage("§lVersion: §e" + plugin.getDescription().getVersion());
			p.sendMessage("§lDeveloper: §cxCoder_");
			p.sendMessage("§6<<===========================================>>");
			p.sendMessage("§f/jumper join - §eJoin the Jumper minigame");
			p.sendMessage("§f/jumper leave - §eTo leave the Jumper minigame");
			p.sendMessage("§f/Jumper help - §eShows the Jumper Help");
			p.sendMessage("§f/jumper setspawn - §eSets the minigame spawn.");
			p.sendMessage(""); 
     }
     
     public void setspawn(Player p) {
    	 if(p.hasPermission("jumper.setspawn")) {
    		 String world = p.getWorld().getName();
    		 double x = p.getLocation().getX();
    		 double y = p.getLocation().getY();
    		 double z = p.getLocation().getZ();
    		 double yaw = p.getLocation().getYaw();
    		 double Pitch = p.getLocation().getPitch();
    		 
    		 FileConfiguration cfg = plugin.getConfig();
    		 cfg.set("Spawn.World", world);
    		 cfg.set("Spawn.PosX", x);
    		 cfg.set("Spawn.PosY", y);
    		 cfg.set("Spawn.PosZ", z);
    		 cfg.set("Spawn.PosYaw", yaw);
    		 cfg.set("Spawn.PosPitch", Pitch);
    		 plugin.saveConfig();
    		 p.sendMessage(plugin.prefix + "§aThe Jumper spawn was succesfully set!");
    	 
    	 } else {
    		 p.sendMessage(plugin.noperm);
    	 }
     }
     
     @SuppressWarnings("deprecation")
	public void joinArena(Player p) {
    	 if(plugin.inJump.contains(p.getName())) {
    		 
    		 
    		 try {
    		 plugin.inJump.add(p.getName());
    		 
    		 plugin.oldLoc.put(p.getName(), p.getLocation());
    		 plugin.oldItems.put(p.getName(), p.getInventory().getContents());
    		 p.getInventory().clear();
    		 p.updateInventory();
    		 
    		 
    		 FileConfiguration cfg = plugin.getConfig();
    		 String world = cfg.getString("World");
    		 double x = cfg.getDouble("Spawn.PosX");
    		 double y = cfg.getDouble("Spawn.PosY");
    		 double z = cfg.getDouble("Spawn.PosZ");
    		 double Yaw = cfg.getDouble("Spawn.PosYaw");
    		 double Pitch = cfg.getDouble("Spawn.PosPitch");
    		 Location loc = new Location(Bukkit.getWorld(world), x, y, z);
    		 loc.setPitch((float) Pitch);
    		 loc.setYaw((float) Yaw);
    		 
    		 p.teleport(loc);
    		 
    		 p.sendMessage(plugin.prefix + "§aYou joined the Jumper Parkour. Good luck!");
    		 
    	
    	 } catch (Exception e) {
    		 p.sendMessage(plugin.prefix + "§cThere is a mistake. Please contact the server Administrators!");
    	 }
    	 
     } else {
         p.sendMessage(plugin.prefix + "§cYou are already in the Jumper minigame! Use /jumper leave to leave the Jumper minigame!");
              
     }
}
          

             


    	 
     
     
     @SuppressWarnings("deprecation")
	public void leaveArena(Player p){
    	 if(plugin.inJump.contains(p.getName())) {
    		 
    		 plugin.inJump.remove(p.getName());
    		 
    		 p.getInventory().clear();
    		 ItemStack[] old = plugin.oldItems.get(p.getName());
    		 p.getInventory().setContents(old);
    		 p.updateInventory();
    		 
    		 Location loc = plugin.oldLoc.get(p.getName()); 
    		 p.teleport(loc);
    		 
    		 p.sendMessage(plugin.prefix + "§aYou left the Jumper minigame.");
    	 
     } else {
    	 p.sendMessage(plugin.prefix + "§cYou are not in the Jumper game!");
     }
   }
}
