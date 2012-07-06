package me.asofold.bukkit.pic.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author mc_dev
 *
 */
public final class CubeData {
	
	
	public final Set<String> canView = new HashSet<String>();
//	public final Set<String> inside = new HashSet<String>();
	
	public final Cube cube;

	public final CubeServer server;

	public CubeData(final Cube cube, final CubeServer server){
		this.cube = cube;
		this.server = server;
	}

	public void remove(PicPlayer pp) {
		canView.remove(pp.playerName);
//		server.renderBlind(pp, canView);
		if (canView.isEmpty()) server.cubeEmpty(this);
	}
	
	public void add(PicPlayer pp) {
//		if (!canView.isEmpty()) server.renderSeen(pp, canView);
		canView.add(pp.playerName);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof CubeData){
			return cube.equals(((CubeData) obj).cube);
		}
		else if (obj instanceof CubePos){
			return cube.equals(obj);
		}
		else return false;
	}

	@Override
	public final int hashCode() {
		return cube.hash;
	}
	
}
