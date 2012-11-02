package me.asofold.bpl.pic.cubelib.server;

import java.util.LinkedHashSet;
import java.util.Set;

import me.asofold.bpl.pic.cubelib.cubes.Cube;
import me.asofold.bpl.pic.cubelib.cubes.CubePos;

/**
 * 
 * @author mc_dev
 *
 */
public final class CubeData {
	
	
	public final Set<String> inRange = new LinkedHashSet<String>();
//	public final Set<String> inside = new HashSet<String>();
	
	public final Cube cube;

	public final CubeServer server;

	public CubeData(final Cube cube, final CubeServer server){
		this.cube = cube;
		this.server = server;
	}

	public void remove(CubePlayer pp) {
		inRange.remove(pp.playerName);
//		server.renderBlind(pp, inRange);
		if (inRange.isEmpty()) server.cubeEmpty(this);
	}
	
	public void add(CubePlayer pp) {
//		if (!inRange.isEmpty()) server.renderSeen(pp, inRange);
		inRange.add(pp.playerName);
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
