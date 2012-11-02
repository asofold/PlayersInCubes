package me.asofold.bpl.pic.cubelib.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.asofold.bpl.pic.cubelib.cubes.Cube;
import me.asofold.bpl.pic.cubelib.cubes.CubePos;
import me.asofold.bpl.pic.stats.Stats;

/**
 * 
 * @author mc_dev
 *
 */
public final class CubeServer {
	
	private final Map<CubePos, CubeData> cubes = new HashMap<CubePos, CubeData>(500);
	
	public final Set<String> players = new HashSet<String>();
	
	public final ICubeCore core;

	public final int cubeSize;

	public final String world;
	
	private final Stats stats;
	private final Integer idCubes;
	private final Integer idPPCubes;
	private final Integer idPPSeen;
	private final Integer idPPRemove;

	public CubeServer(final String world, final ICubeCore core, int cubeSize){
		this.world  = world;
		stats = core.getStats();
		idCubes = stats.getId("ncubes_" + world, true);
		idPPCubes = stats.getNewId("pp_ncubes");
		idPPSeen = stats.getNewId("pp_insight");
		idPPRemove = stats.getNewId("pp_offsight");
		this.core = core;
		this.cubeSize = cubeSize;
	}
	
	/**
	 * Usually called when players have checked out (problem: will be empty then, anyway).
	 */
	public final void clear() {
		// Might de-associate cubes from players here?
		cubes.clear();
		players.clear();
	}
	
	/**
	 * Called when a CubeData has no players anymore.
	 * @param cubeData
	 */
	public final void cubeEmpty(final CubeData cubeData) {
		cubes.remove(cubeData.cube);
		// TODO: park it. ? Map by hash directly into an Array of lists + check for new cubes by hash ?
	}


	public final void outOfRange(final CubePlayer pp, final Set<String> names) {
		core.outOfRange(pp, names);
	}
	
	public final void inRange(final CubePlayer pp, final Set<String> names) {
		core.inRange(pp, names);
	}
	
	/**
	 * Render blind or seen and add.
	 * TODO: maybe also check rightaway ?
	 * @param pp
	 */
	public final void add(final CubePlayer pp, final boolean blind){
		if (!players.isEmpty()){
			if (blind) core.outOfRange(pp, players);
			else core.inRange(pp, players);	
		}
		players.add(pp.playerName);
	}
	
	/**
	 * Remove player from players set, checkout.
	 * @param pp
	 */
	public final void remove(final CubePlayer pp){
		players.remove(pp.playerName);
		if (!pp.cubes.isEmpty()) pp.checkOut();
	}

	/**
	 * Check which cubes have to be added.
	 * @param pp
	 * @param distCube Maximal distance to cube centers.
	 */
	public final void update(final CubePlayer pp, final int distCube) {
		// Remove cubes that are too far.
		final Set<String> rem = pp.checkCubes(distCube);
		// Dumb: check all cubes within distance
		// opt: calculate min/max here ?
		final Set<String> seen = new HashSet<String>();
		for (int x = pp.x - distCube; x < pp.x + distCube; x += cubeSize){
			for (int y = pp.y - distCube; y < pp.y + distCube; y += cubeSize){
				for (int z = pp.z - distCube; z < pp.z + distCube; z += cubeSize){
					// TODO: optimize here and just get the hash later 
					final CubePos pos = new CubePos(x / cubeSize, y / cubeSize, z / cubeSize);
					if (pp.cubes.contains(pos)){
						seen.addAll(cubes.get(pos).inRange);
						continue;
					}
					CubeData data = cubes.get(pos);
					if (data == null){
						// create new one
						data = new CubeData(new Cube(x, y, z, cubeSize), this);
						cubes.put(pos, data);
						data.add(pp);
						pp.cubes.add(data);
					} else{
						seen.addAll(data.inRange);
						data.add(pp);
						pp.cubes.add(data);
					}
				}
			}
		}
		// process visibility
		final int szRem;
		if (!rem.isEmpty()){
			final ArrayList<String> doRem = new ArrayList<String>(rem.size());
			for (final String name : rem){
				if (!seen.contains(name)) doRem.add(name);
			}
			szRem = doRem.size();
			if (szRem > 0) core.outOfRange(pp, doRem);
		}
		else szRem = 0;
		seen.remove(pp.playerName);
		if (!seen.isEmpty()) core.inRange(pp, seen); // Contains all already seen people, though !
		// Add stats:
		stats.addStats(idPPCubes, pp.cubes.size());
		stats.addStats(idPPSeen, seen.size());
		stats.addStats(idPPRemove, szRem);
		stats.addStats(idCubes, cubes.size());
	}
	
}
