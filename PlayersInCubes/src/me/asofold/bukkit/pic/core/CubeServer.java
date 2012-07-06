package me.asofold.bukkit.pic.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author mc_dev
 *
 */
public final class CubeServer {
	
	private final Map<CubePos, CubeData> cubes = new HashMap<CubePos, CubeData>(500);
	
	public PicCore core;

	public final int cubeSize;

	public CubeServer(final PicCore core, int cubeSize){
		this.core = core;
		this.cubeSize = cubeSize;
	}
	
	/**
	 * Called when a CubeData has no players anymore.
	 * @param cubeData
	 */
	public final void cubeEmpty(final CubeData cubeData) {
		cubes.remove(cubeData.cube);
		// TODO: park it. ? Map by hash directly into an Array of lists + check for new cubes by hash ?
	}


	public final void renderBlind(final PicPlayer pp, final Set<String> names) {
		core.renderBlind(pp, names);
	}
	
	public final void renderSeen(final PicPlayer pp, final Set<String> names) {
		core.renderSeen(pp, names);
	}

	/**
	 * Check which cubes have to be added.
	 * @param pp
	 * @param distCube Maximal distance to cube centers.
	 */
	public final void update(final PicPlayer pp, final int distCube) {
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
						seen.addAll(cubes.get(pos).canView);
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
						seen.addAll(data.canView);
						data.add(pp);
						pp.cubes.add(data);
					}
				}
			}
		}
		// process visibility
		if (!rem.isEmpty()){
			final ArrayList<String> doRem = new ArrayList<String>(rem.size());
			for (final String name : rem){
				if (!seen.contains(name)) doRem.add(name);
			}
			if (!doRem.isEmpty()) core.renderBlind(pp, doRem);
		}
		core.renderSeen(pp, seen); // Contains all already seen people, though !
	}
	
}
