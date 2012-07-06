package me.asofold.bukkit.pic.core;

public class CubePos {
	
	private static final int p1 = 73856093;
    private static final int p2 = 19349663;
	private static final int p3 = 83492791;
	
	// Cube coordinates: 
	public final int x;
	public final int y;
	public final int z;
	
	public final int hash;
	
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param size
	 */
	public CubePos	(final int x, final int y, final int z){
		// Cube related coordinates:
		this.x = x;
		this.y = y;
		this.z = z;
		// Hash
		hash = getHash(this.x, this.y, this.z);
	}
	
	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof CubePos){
			final CubePos other = (CubePos) obj;
			return other.x == x && other.y == y && other.z == z;
		}
		else return false;
	}

	@Override
	public final int hashCode() {
		return hash;
	}
	
	public static final int getHash(final int x, final int y, final int z) {
		return p1 * x ^ p2 * y ^ p3 * z;
	}
}
