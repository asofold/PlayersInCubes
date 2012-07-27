package me.asofold.bpl.pic.core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

public class PicPlayer {
	
	/**
	 * Cubes the player is registered in.
	 */
	public final Set<CubeData> cubes = new HashSet<CubeData>();
	
	public final String playerName;
	
	public long tsLoc = 0;
	public String world;
	public int x;
	public int y;
	public int z;
	
	final Player bPlayer;
	
	public PicPlayer(final Player player){
		this.bPlayer = player;
		playerName = player.getName();
	}

	/**
	 * Remove all cube associations.
	 * @return All players that could see the player or were seen by the player.
	 */
	public final Set<String> checkOut() {
		final Set<String> out = new HashSet<String>();
		for (final CubeData cube : cubes){
			cube.remove(this);
			if (!cube.canView.isEmpty()) out.addAll(cube.canView);
		}
		cubes.clear();
		return out;
	}
	
	public  final boolean inRange(final int x, final int y, final int z, final int distLazy) {
		return Math.abs(x - this.x) < distLazy && Math.abs(y - this.y) < distLazy && Math.abs(z - this.z) < distLazy ;
	}

	/**
	 * Remove cubes that are more far than distCube.
	 * @param distCube
	 */
	public final Set<String> checkCubes(final int distCube) {
		final List<CubeData> rem = new LinkedList<CubeData>();
		final Set<String> out = new HashSet<String>();
		for (final CubeData cube : cubes){
			if (!cube.cube.inRange(x, y, z, distCube)){
				cube.remove(this);
				rem.add(cube);
				out.addAll(cube.canView);
			}
		}
		cubes.removeAll(rem);
		PicCore.stats.addStats(PicCore.idPPRemCubes, rem.size());
		return out;
	}
	
}
