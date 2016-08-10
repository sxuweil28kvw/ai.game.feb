package leigh.ai.game.feb.service.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapNode {
	private Integer code;
	private String name;
	private Map<Traffic, Set<MapNode>> neighbours = new HashMap<Traffic, Set<MapNode>>();
	public MapNode() {
		for(Traffic t: Traffic.values()) {
			neighbours.put(t, new HashSet<MapNode>());
		}
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Traffic, Set<MapNode>> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(Map<Traffic, Set<MapNode>> neighbours) {
		this.neighbours = neighbours;
	}
	public Set<MapNode> getNeighbours(Traffic traffic) {
		return neighbours.get(traffic);
	}
	@Override
	public String toString() {
		return code + ":" + name;
	}
}