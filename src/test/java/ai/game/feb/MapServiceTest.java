package ai.game.feb;

import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;

import org.junit.Assert;
import org.junit.Test;

public class MapServiceTest {
	@Test
	public void testReversePath() {
//		MapService.initMap(false);
//		MapService.printMap();
		MapPath p1 = new MapPath();
		p1.setCode(1158);
		p1.setPathLength(3);
		p1.setTraffic(Traffic.walk);
		MapPath p2 = new MapPath();
		p2.setCode(1159);
		p2.setPathLength(2);
		p2.setTraffic(Traffic.walk);
		MapPath p3 = new MapPath();
		p3.setCode(1211);
		p3.setPathLength(1);
		p3.setTraffic(Traffic.walk);
		MapPath p4 = new MapPath();
		p4.setCode(1212);
		p4.setPathLength(0);
		
		p1.setNext(p2);
		p2.setNext(p3);
		p3.setNext(p4);
		
		System.out.println(p1);
		System.out.println(MapService.reverseMapPath(p1));
	}
	@Test
	public void testFindFacility() {
//		Assert.assertEquals(MapService.findFacility(1145, new FacilityType[] {FacilityType.itemshop}).toString(),
//				"1145:古拉德区域12(3) walk to 1141:古拉德区域13(2) walk to 1150:古拉德区域14(1) walk to 1152:古拉德城(0) ");
		System.out.println(MapService.findFacility(1173, new FacilityType[] {FacilityType.itemshop}));
	}
}
