ackage Classes;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {
	
	private main plugin;
	
	public Listeners(main main) {
		this.plugin = main;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
            
			
			plugin.leaveArena(e.getPlayer());
            }
	
	
	@EventHandler
	public void onFeed(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(plugin.inJump.contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if(plugin.inJump.contains(e.getPlayer().getName())){
		e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent e) {
		if(plugin.inJump.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCommandDeny(PlayerCommandPreprocessEvent e){
		if(plugin.inJump.contains(e.getPlayer().getName())){
			if(!e.getMessage().contains("/jumper")){
				e.setCancelled(true);
				e.getPlayer().sendMessage(plugin.prefix + "§cThis command isnt allowed in the Jumper minigame! §c[" + e.getMessage() + "§c]");
			     
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		if(plugin.inJump.contains(e.getPlayer().getName())) {
			
			plugin.leaveArena(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onSignChage(SignChangeEvent e){
		if(e.getPlayer().hasPermission("jumper.sign")){
			if(e.getLine(0).equalsIgnoreCase("[jumper]")){
				e.setLine(0, "§b§aJumper");
				e.setLine(1, "§b§cFinish");
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		if(plugin.inJump.contains(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(plugin.inJump.contains(p.getName())){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign) e.getClickedBlock().getState();
					
					if(s.getLine(0).equalsIgnoreCase("§b§aJumper")){
						if(s.getLine(1).equalsIgnoreCase("§b§cFinish")){
							
							plugin.inJump.remove(p.getName());
							
							p.getInventory().clear();
				    		 ItemStack[] old = plugin.oldItems.get(p.getName());
				    		 p.getInventory().setContents(old);
				    		 p.updateInventory();
				    		 
				    		 Location loc = plugin.oldLoc.get(p.getName()); 
				    		 p.teleport(loc);
				    		 
				    		 p.sendMessage(plugin.prefix + "§aYou got 2 Diamonds for finishing the Parkour!");
				    		 p.getInventory().addItem(new ItemStack(Material.DIAMOND));
						
						}
					}
				}
			}
			
		}
		
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(plugin.inJump.contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Block b = p.getLocation().subtract(0.0D, 1.0D, 0).getBlock();
		
		if(b.getType() == Material.GOLD_BLOCK){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 3));
			p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 3, 2);
		}
		if(b.getType() == Material.WATER){
			teleportToSpawn(p);
		   
		}
	}
	
	

	@SuppressWarnings("deprecation")
	private void teleportToSpawn(Player p) {
		FileConfiguration cfg = plugin.getConfig();
		
		 String world = cfg.getString("World");
		 double x = cfg.getDouble("Spawn.PosX");
		 double y = cfg.getDouble("Spawn.PosY");
		 double z = cfg.getDouble("Spawn.PosZ");
		 double Yaw = cfg.getDouble("Spawn.PosYaw");
		 double Pitch = cfg.getDouble("Spawn.PosPitch");
		 Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		 loc.setPitch((float)Pitch);
		 loc.setYaw((float)Yaw);
		 
		 p.teleport(loc);
		 p.sendMessage(plugin.prefix + "§cYou died, going to start!");
		 p.playEffect(p.getLocation(), Effect.GHAST_SHOOT, 3);
		 
		
	}
}


