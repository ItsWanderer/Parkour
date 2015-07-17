
package Classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{
	
	public ArrayList<String> inJump = new ArrayList<>();
	public HashMap<String, Location> oldLoc = new HashMap<>();
	public HashMap<String, ItemStack[]> oldItems = new HashMap<>();
    public String prefix = "§7[§aJumper§7] ";
    public String noperm = "§7[§aJumper§7] §cYou dont have permissions to use this command!";
    public String help = "§7[§aJumper§7] §3Help: /jumper help";
	
	@Override
	public void onEnable(){
		System.out.println("[Jumper] Plugin version " +  this.getDescription().getVersion() + " by " + this.getDescription().getAuthors() + " loaded!");
		new Listeners(this);
		getCommand("jumper").setExecutor(new Commands(this));
		
	}
    @Override
    public void onDisable(){
    	System.out.println("[Jumper] Plugin disabled!");
    }
    
    
    @SuppressWarnings("deprecation")
	public void leaveArena(Player p){
    	 if(inJump.contains(p.getName())) {
    		 
    		 inJump.remove(p.getName());
    		 
    		 p.getInventory().clear();
    		 ItemStack[] old = oldItems.get(p.getName());
    		 p.getInventory().setContents(old);
    		 p.updateInventory();
    		 
    		 Location loc = oldLoc.get(p.getName()); 
    		 p.teleport(loc);
    		 
    		 p.sendMessage(prefix + "§aYou left the Jumper minigame.");
    	 
     } else {
    	 p.sendMessage(prefix + "§cYou are not in the Jumper game!");
     }
   }
}
