package leigh.ai.game.feb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.map.MapNode;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapService {
	private static Logger logger = LoggerFactory.getLogger(MapService.class);
	public static Map<Integer, MapNode> map = new HashMap<Integer, MapNode>();
	public static Map<String, Integer> nameLookup = new HashMap<String, Integer>();
	static {
		initMap();
	}
	public static void initMap() {
		map.clear();
		nameLookup.clear();
		String[] maps = new String[] {
				"data/map/majiweier.map",
				"data/map/longzhimen.map",
				"data/map/lijiya.map",
				"data/map/aiteluliya.map",
				"data/map/xifangsandao.map",
				"data/map/beilun.map",
				};
		for(String map: maps) {
			initMap(map);
		}
		for(Integer code: map.keySet()) {
			nameLookup.put(map.get(code).getName(), code);
		}
		// 船运-旧大陆：
		Set<MapNode> tagang = map.get(1161).getNeighbours(Traffic.ship);
		tagang.add(map.get(1160));
		tagang.add(map.get(1198));
		tagang.add(map.get(1194));
		Set<MapNode> beisilong = map.get(1160).getNeighbours(Traffic.ship);
		beisilong.add(map.get(1161));
		beisilong.add(map.get(1198));
		beisilong.add(map.get(1194));
		Set<MapNode> jilisi = map.get(1198).getNeighbours(Traffic.ship);
		jilisi.add(map.get(1161));
		jilisi.add(map.get(1160));
		jilisi.add(map.get(1194));
		Set<MapNode> mlkn = map.get(1194).getNeighbours(Traffic.ship);
		mlkn.add(map.get(1161));
		mlkn.add(map.get(1160));
		mlkn.add(map.get(1198));
		
		// 船运-萨拉丁海岸和港镇巴顿
		map.get(2004).getNeighbours(Traffic.ship).add(map.get(2003));
		map.get(2003).getNeighbours(Traffic.ship).add(map.get(2004));
		// 船运-迷雾岛和珍珠港
		map.get(2094).getNeighbours(Traffic.ship).add(map.get(2049));
		map.get(2049).getNeighbours(Traffic.ship).add(map.get(2094));
		// 跳裂缝：
		map.get(1188).getNeighbours(Traffic.crack).add(map.get(2001));
		map.get(2001).getNeighbours(Traffic.crack).add(map.get(1188));
		// 过境：
		map.get(2033).getNeighbours(Traffic.border).add(map.get(2034));//黑克境
		map.get(2034).getNeighbours(Traffic.border).add(map.get(2033));//黑克境
		map.get(2060).getNeighbours(Traffic.border).add(map.get(2016));//乌兰境
		map.get(2016).getNeighbours(Traffic.border).add(map.get(2060));//乌兰境
		// 飞行-旧大陆
		int[] airports = new int[] {
				1152, 1137, 1128, 1123, 1122, 1119, 1149, 1113, 1204,
				1156, 1142, 1102, 1107, 1183, 1179, 
		};
		for(int i = 0; i < airports.length; i++) {
			for(int j = 0; j < airports.length; j++) {
				if(j == i) {
					continue;
				}
				map.get(airports[i]).getNeighbours(Traffic.fly).add(map.get(airports[j]));
			}
		}
		// 副本-旧大陆
		map.get(-1).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-2).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-3).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-4).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-5).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-6).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-7).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-8).getNeighbours(Traffic.raid_exit).add(map.get(1114));
		map.get(-9).getNeighbours(Traffic.raid_exit).add(map.get(1184));
		map.get(-10).getNeighbours(Traffic.raid_exit).add(map.get(1130));
		map.get(-11).getNeighbours(Traffic.raid_exit).add(map.get(1130));
	}
	
	private static void initMap(String filePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(MapService.class.getClassLoader().getResourceAsStream(filePath), "utf8"));
			String line = br.readLine();
			while(line != null && !line.equals("")) {
				String[] array = line.split(",");
				Integer code = new Integer(array[0]);
				MapNode node = null;
				if(MapService.map.containsKey(code)) {
					node = MapService.map.get(code);
				} else {
					node = new MapNode();
					node.setCode(code);
				}
				node.setName(array[1]);
				for(int i = 2; i < array.length; i++) {
					Integer ncode = new Integer(array[i]);
					MapNode nnode = null;
					if(MapService.map.containsKey(ncode)) {
						nnode = MapService.map.get(ncode);
					} else {
						nnode = new MapNode();
						nnode.setCode(ncode);
						map.put(ncode, nnode);
					}
					node.getNeighbours(Traffic.walk).add(nnode);
				}
				map.put(code, node);
				line = br.readLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
	public static void printMap() {
		for(Integer code: map.keySet()) {
			MapNode node = map.get(code);
			System.out.print(code + "," + node.getName() + ",相邻：[");
			for(int i = 0; i < node.getWalkNeighbours().size() - 1; i++) {
				System.out.print(node.getWalkNeighbours().get(i).getCode());
				System.out.print(node.getWalkNeighbours().get(i).getName());
				System.out.print(",");
			}
			try {
				System.out.print(node.getWalkNeighbours().get(node.getWalkNeighbours().size() - 1).getCode());
				System.out.print(node.getWalkNeighbours().get(node.getWalkNeighbours().size() - 1).getName());
				System.out.println("]");
			} catch(IndexOutOfBoundsException e) {
				System.out.println();
				System.out.println("IndexOutOfBoundsException: code=" + code + ",neighbours.size=" + node.getWalkNeighbours().size());
			}
		}
	}
	*/
	public static MapPath findPath(Integer startingCode, Integer targetCode) {
		if(startingCode.equals(targetCode)) {
			return null;
		}
		Map<Integer, MapPath> startPath = new HashMap<Integer, MapPath>();
		MapPath startingPath = new MapPath();
		startingPath.setCode(startingCode);
		startingPath.setPathLength(0);
		startPath.put(startingCode, startingPath);
		
		Map<Integer, MapPath> endPath = new HashMap<Integer, MapPath>();
		MapPath targetPath = new MapPath();
		targetPath.setCode(targetCode);
		targetPath.setPathLength(0);
		endPath.put(targetCode, targetPath);
		
		for(int pathlen = 1; ; pathlen++) {
			logger.debug("pathlen++: " + pathlen);
			MapPath path = expandPath(startPath, pathlen, endPath);
			if(path != null) {
				if(logger.isDebugEnabled()) {
					logger.debug("path from " + startingCode + " to " + targetCode + " = " + path.toString());
				}
				return path;
			}
			path = expandPath(endPath, pathlen, startPath);
			if(path != null) {
				path = reverseMapPath(path);
				if(logger.isDebugEnabled()) {
					logger.debug("path from " + startingCode + " to " + targetCode + " = " + path.toString());
				}
				return path;
			}
		}
	}
	private static MapPath expandPath(Map<Integer, MapPath> path, int targetLength, Map<Integer, MapPath> otherPath) {
		logger.debug("expandPath, targetLength=" + targetLength);
		for(Traffic traffic: Traffic.values()) {
			MapPath p = expandByTraffic(path, targetLength, otherPath, traffic);
			if(p != null) {
				return p;
			}
		}
		return null;
	}
	private static MapPath expandByTraffic(Map<Integer, MapPath> path,
			int targetLength, Map<Integer, MapPath> otherPath, Traffic traffic) {
		if(traffic.equals(Traffic.fly) && !JobService.canFly()) {
			return null;
		}
		
		Map<Integer, MapPath> newPaths = new HashMap<Integer, MapPath>();
		for(Integer key: path.keySet()) {
			if(path.get(key).getPathLength() != targetLength - 1) {
				continue;
			}
			Set<MapNode> walkNodes = map.get(key).getNeighbours(traffic);
			for(MapNode node: walkNodes) {
				if(path.containsKey(node.getCode()) || newPaths.containsKey(node.getCode())) {
					continue;
				}
				if(otherPath.containsKey(node.getCode())) {
					MapPath tailPath = otherPath.get(node.getCode());
					MapPath reversePath = reverseMapPath(path.get(key));
					int totalLength = reversePath.getPathLength() + tailPath.getPathLength() + 1;
					MapPath tmp = reversePath;
					int reversePathLength = reversePath.getPathLength();
					for(int i = 0; i < reversePathLength; i++) {
						tmp.setPathLength(totalLength - i);
						tmp = tmp.getNext();
					}
					tmp.setPathLength(tailPath.getPathLength() + 1);
					tmp.setTraffic(traffic);
					tmp.setNext(tailPath);
					return reversePath;
				}
				MapPath newpath = new MapPath();
				newpath.setCode(node.getCode());
				newpath.setPathLength(targetLength);
				newpath.setTraffic(traffic);
				newpath.setNext(path.get(key));
				newPaths.put(node.getCode(), newpath);
				if(logger.isDebugEnabled()) {
					logger.debug("new path = " + newpath);
				}
			}
		}
		path.putAll(newPaths);
		return null;
	}
	public static MapPath reverseMapPath(MapPath mapPath) {
		int totalLength = mapPath.getPathLength();
		if(mapPath.getPathLength() == 0) {
			return mapPath;
		}
		MapPath next = mapPath.getNext();
		if(mapPath.getPathLength() == 1) {
			next.setNext(mapPath);
			next.setTraffic(mapPath.getTraffic());
			next.setPathLength(1);
			mapPath.setNext(null);
			mapPath.setPathLength(0);
			return next;
		}
		Traffic tmpTraffic = next.getTraffic();
		MapPath nextnext = next.getNext();
		next.setNext(mapPath);
		next.setTraffic(mapPath.getTraffic());
		next.setPathLength(1);
		mapPath.setNext(null);
		mapPath.setPathLength(0);
		mapPath.setTraffic(null);
		
		for(int i = 0; i < totalLength - 2; i++) {
			mapPath = next;
			next = nextnext;
			nextnext = nextnext.getNext();
			
			Traffic tmp2 = next.getTraffic();
			next.setNext(mapPath);
			next.setTraffic(tmpTraffic);
			next.setPathLength(i + 2);
			tmpTraffic = tmp2;
		}
		nextnext.setNext(next);
		nextnext.setPathLength(totalLength);
		nextnext.setTraffic(tmpTraffic);
		if(logger.isDebugEnabled()) {
			logger.debug(nextnext.toString());
		}
		return nextnext;
	}
	
	public static MapPath findFacilityExceptTraffics(int location, FacilityType[] facilityTypes, Traffic... traffics) {
		for(FacilityType type: facilityTypes) {
			if(FacilityService.hasFacility(location, type)) {
				return null;
			}
		}
		MapPath start = new MapPath();
		start.setCode(location);
		start.setPathLength(0);
		Map<Integer, MapPath> paths = new HashMap<Integer, MapPath>();
		paths.put(location, start);
		int i = 1;
		MapPath result = findFacilityByStep(facilityTypes, i, paths, traffics);
		while(result == null) {
			i++;
			if(i > 30) {
				logger.error("寻找设施失败！startLocation=" + location + ",设施=" + facilityTypes[0]);
				return null;
			}
			result = findFacilityByStep(facilityTypes, i, paths, traffics);
		}
		return result;
	}
	public static MapPath findFacility(int location, FacilityType[] facilityTypes) {
		for(FacilityType type: facilityTypes) {
			if(FacilityService.hasFacility(location, type)) {
				return null;
			}
		}
		MapPath start = new MapPath();
		start.setCode(location);
		start.setPathLength(0);
		Map<Integer, MapPath> paths = new HashMap<Integer, MapPath>();
		paths.put(location, start);
		int i = 1;
		MapPath result = findFacilityByStep(facilityTypes, i, paths);
		while(result == null) {
			i++;
			if(i > 30) {
				logger.error("寻找设施失败！startLocation=" + location + ",设施=" + facilityTypes[0]);
				return null;
			}
			result = findFacilityByStep(facilityTypes, i, paths);
		}
		return result;
	}
	private static MapPath findFacilityByStep(FacilityType[] facilityTypes,
			int step, Map<Integer, MapPath> paths, Traffic... exceptTraffics) {
		Map<Integer, MapPath> newPaths = new HashMap<Integer, MapPath>();
		for(Integer code: paths.keySet()) {
			MapPath path = paths.get(code);
			if(path.getPathLength() != step - 1) {
				continue;
			}
			for1:
			for(Traffic t: Traffic.values()) {
				if(exceptTraffics != null) {
					for(Traffic except: exceptTraffics) {
						if(t.equals(except)) {
							continue for1;
						}
					}
				}
				if(t.equals(Traffic.fly) && !JobService.canFly()) {
					continue;
				}
				MapPath result = findFacilityByTraffic(facilityTypes, step, paths, newPaths, code, t);
				if(result != null) {
					return result;
				}
			}
		}
		paths.putAll(newPaths);
		return null;
	}
	private static MapPath findFacilityByTraffic(FacilityType[] facilityTypes,
			int step, Map<Integer, MapPath> paths, Map<Integer, MapPath> newPaths, Integer location, Traffic traffic) {
		for(MapNode node: map.get(location).getNeighbours(traffic)) {
			if(paths.containsKey(node.getCode()) || newPaths.containsKey(node.getCode())) {
				continue;
			}
			MapPath newpath = new MapPath();
			newpath.setCode(node.getCode());
			newpath.setPathLength(step);
			newpath.setTraffic(traffic);
			newpath.setNext(paths.get(location));
			newPaths.put(node.getCode(), newpath);
			for(FacilityType type: facilityTypes) {
				if(FacilityService.hasFacility(node.getCode(), type)) {
					return reverseMapPath(newpath);
				}
			}
		}
		return null;
	}
}

