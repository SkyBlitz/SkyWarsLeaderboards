package com.scarabcoder.leaderboards.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.scarabcoder.leaderboards.BoardType;
import com.scarabcoder.leaderboards.DataManager;
import com.scarabcoder.leaderboards.LeaderboardSign;
import com.scarabcoder.leaderboards.Main;

public class SignListener implements Listener{
	
	@EventHandler
	public void blockBreakEvent(BlockBreakEvent e){
		Location loc = e.getBlock().getLocation();
		if(loc.getBlock().getType().equals(Material.SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST) || loc.getBlock().getType().equals(Material.WALL_SIGN)){
			DataManager.removeBoardByLocation(e.getBlock().getLocation());
			e.getPlayer().sendMessage(ChatColor.GREEN + "Removed leaderboard sign.");
		}
	}
	
	@EventHandler
	public void signPlaceEvent(SignChangeEvent e){
		System.out.println(e.getLine(0));
		if(e.getLine(0).equalsIgnoreCase("[leaderboard]")){
			try {
				BoardType type = BoardType.valueOf(e.getLine(1).toUpperCase());
				
				int place = Integer.valueOf(e.getLine(2));
				LeaderboardSign sign = new LeaderboardSign(e.getBlock().getLocation(), type, place);
				
				
				
				DataManager.saveSignBoard(sign);
				
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable(){

					@Override
					public void run() {
						
						sign.updateSign();						
					}
					
				}, 5);
				
				
				
			} catch (IllegalArgumentException ex){
				e.getPlayer().sendMessage(ChatColor.RED + "Board type or place not found!");
				e.getBlock().setType(Material.AIR);
				ex.printStackTrace();
			}
		}
	}
	
}
