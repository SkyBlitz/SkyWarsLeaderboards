package com.scarabcoder.leaderboards;

import java.util.List;

public class TenMinuteLoop implements Runnable {

	@Override
	public void run() {
		List<LeaderboardSign> signs = DataManager.getSigns();
		for(LeaderboardSign sign : signs){
			if(!sign.removeFromConfigIfNotValid()){
				sign.updateSign();
			}
		}
	}

}
