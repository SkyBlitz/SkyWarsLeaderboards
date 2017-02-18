package com.scarabcoder.leaderboards;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class LeaderboardSign {
	
	private Location loc;
	
	private BoardType type;
	
	private int place;
	
	public LeaderboardSign(Location loc, BoardType type, int place){
		
		this.loc = loc;
		this.type = type;
		this.place = place;
		
	}
	
	public boolean removeFromConfigIfNotValid(){
		
		
		if(!(loc.getBlock().getType().equals(Material.SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST) || loc.getBlock().getType().equals(Material.WALL_SIGN))){
			DataManager.removeBoardByLocation(this.loc);
			
			return true;
		}
		
		return false;
	}
	
	public void updateSign(){
		
		
		String type = this.getType().toString().toLowerCase();
		
		
		try {
			PreparedStatement st = Main.getConnection().prepareStatement("SELECT " + type + ", name FROM proskywars");
			
			HashMap<Integer, String> data = new HashMap<Integer, String>();
			
			ResultSet set = st.executeQuery();
			
		
			
			while(set.next()){
				data.put(Integer.valueOf(set.getString(type)), set.getString("name"));
			}
			
			Map<Integer, String> sorted = new TreeMap<Integer, String>(Collections.reverseOrder());
			sorted.putAll(data);
			
			
			Sign sign = (Sign) this.getLoc().getWorld().getBlockAt(this.getLoc()).getState();
			
			if(!sorted.isEmpty()){
				int x1 = 0;
				for(int key : sorted.keySet()){
					
					if(x1 == place - 1){
						for(int x = 1; x < 5; x++){
							sign.setLine(x - 1, DataManager.fillPlaceholders(Main.getPlugin().getConfig().getString("line" + x), Integer.valueOf(key), this.getType(), sorted.get(key), place));
							sign.update(true);
						}
					}
					x1++;
				}
			}else{
				sign.setLine(1, ChatColor.RED + "Empty");
				sign.update(true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Sign getSign(){
		return (Sign) this.getLoc().getBlock().getState();
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public BoardType getType() {
		return type;
	}

	public void setType(BoardType type) {
		this.type = type;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}
	
	
	
	
}
