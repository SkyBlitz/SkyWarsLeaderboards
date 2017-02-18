package com.scarabcoder.leaderboards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;

public class DataManager {
	
	public static void saveSignBoard(LeaderboardSign sign){
		
		
		
		String id = (Main.signs.getInt("last_int") + 1) + "";
		
		
		String loc = id + ".location";
		
		Main.signs.set(loc + ".x", sign.getLoc().getBlockX());
		Main.signs.set(loc + ".y", sign.getLoc().getBlockY());
		Main.signs.set(loc + ".z", sign.getLoc().getBlockZ());
		Main.signs.set(loc + ".world", sign.getLoc().getWorld().getName());
		
		Main.signs.set(id + ".type", sign.getType().toString());
		Main.signs.set(id + ".place", sign.getPlace());
		Main.signs.set("last_int", Main.signs.getInt("last_int") + 1);
		try {
			Main.signs.save(Main.getSignsFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void removeBoardByLocation(Location loc){
		
		for(String id : Main.signs.getKeys(false)){
			if(!id.equalsIgnoreCase("last_int")){
				String l = id + ".location.";

				Location loc2 = new Location(Bukkit.getWorld(Main.signs.getString(l + "world")), Main.signs.getInt(l + "x"), Main.signs.getInt(l + "y"), Main.signs.getInt(l + "z"));

				if(loc.equals(loc2)){
					Main.signs.set(id, null);
				}
			}
		}
		
		try {
			Main.signs.save(Main.getSignsFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<LeaderboardSign> getSigns(){
		List<LeaderboardSign> signs = new ArrayList<LeaderboardSign>();
		
		for(String id : Main.signs.getKeys(false)){
			if(!id.equalsIgnoreCase("last_int")){
				
				String l = id + ".location.";
				
				Location loc = new Location(Bukkit.getWorld(Main.signs.getString(l + "world")), Main.signs.getInt(l + "x"), Main.signs.getInt(l + "y"), Main.signs.getInt(l + "z"));
				
				LeaderboardSign sign = new LeaderboardSign(loc, BoardType.valueOf(Main.signs.getString(id + ".type")), Main.signs.getInt(id + ".place"));
				signs.add(sign);
			}
		}
		
		return signs;
	}
	
	public static String fillPlaceholders(String input, int score, BoardType type, String player, int place){
		
		input = input.replace("{player}", player).replace("{score}", score + "").replace("{type}", type.toString().toLowerCase()).replace("{place}", place + "");
		input = ChatColor.translateAlternateColorCodes('&', input);
		return input;
	}
	
	
	
	
	
}
