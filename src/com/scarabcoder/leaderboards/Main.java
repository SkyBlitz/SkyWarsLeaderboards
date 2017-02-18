package com.scarabcoder.leaderboards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.scarabcoder.leaderboards.listeners.SignListener;

public class Main extends JavaPlugin {
	
	
	private static Connection connect = null;
	
	private static Plugin plugin;
	
	public static FileConfiguration signs;
	
	public void onEnable(){
		
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		Main.plugin = this;
		//Attempt connection to MySQL server
         try {
			connect = DriverManager
			                 .getConnection("jdbc:mysql://" + this.getConfig().getString("mysql-address") + "/" + this.getConfig().getString("mysql-database") + "?"
			                                 + "user=" + this.getConfig().getString("mysql-user") + "&password=" + this.getConfig().getString("mysql-pass"));
			System.out.println("Successfully made connection to MySQL database.");
		} catch (Exception e) {
			System.out.println("There was a fatal error connecting to the SkyWars database!");
			e.printStackTrace();
		}
         
         
         
        //Check for proskywars table
         PreparedStatement st;
		try {
			st = connect.prepareStatement("SHOW TABLES LIKE ?");
	        st.setString(1, "proskywars");
	        ResultSet set = st.executeQuery();
	        if(set.next()){
	        	System.out.println("Found ProSkyWars table.");
	        }else{
	        	System.out.println("Fatal error; Could not find ProSkyWars table.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		this.getDataFolder().mkdir();
		
		//Initiate signs.yml
		File signsFile = new File(getDataFolder(), "signs.yml");
		
		if(!signsFile.exists()){
			try {
				signsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		signs = new YamlConfiguration();
		
		try {
			signs.load(signsFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		if(!signs.contains("last_int")){
			signs.set("last_int", 0);
		}
		try {
			signs.save(getSignsFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Start loop
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TenMinuteLoop(), 0, this.getConfig().getInt("refresh-delay") * 20);
		
		//Listeners
		this.getServer().getPluginManager().registerEvents(new SignListener(), this);
		
        
         
	}
	
	public static FileConfiguration getSignYML(){
		return signs;
	}
	
	public static File getSignsFile(){
		return new File(getPlugin().getDataFolder(), "signs.yml");
	}
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	public static Connection getConnection(){
		return connect;
	}
	
}
