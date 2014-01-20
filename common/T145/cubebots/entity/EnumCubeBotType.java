package T145.cubebots.entity;

public enum EnumCubeBotType
{
	NORMAL(0),
	COLLECTOR(1),
	LUMBER(2),
	FIGHTER(3),
	BREEDER(4),
	FIXER(5),
	FARMER(6),
	ARCHER(7),
	MILKER(8), 
	BUTCHER(9), 
	SMITHY(10),
	CHICKBRING(11),
	OREFINDER(12),
	MINER(13),
	TELEPORTER(14),
	SIREN(15), 
	TAMER(16),
	GRASSER(17),
	NETHER(32),
	HOSTILE(33),
	;
	
	byte index;
	
	private EnumCubeBotType(int i)
	{
		index = (byte) i;
	}
}
