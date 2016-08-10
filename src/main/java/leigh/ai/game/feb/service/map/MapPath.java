package leigh.ai.game.feb.service.map;

import leigh.ai.game.feb.service.MapService;

/*****************
 * 
 * A path from starting location to the target, each step with the moving method.
 * @author leightao
 *
 */
public class MapPath {
	private Integer code;
	private Traffic traffic;
	private MapPath next;
	private Integer pathLength;
	public Integer getPathLength() {
		return pathLength;
	}
	public void setPathLength(Integer pathLength) {
		this.pathLength = pathLength;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Traffic getTraffic() {
		return traffic;
	}
	public void setTraffic(Traffic traffic) {
		this.traffic = traffic;
	}
	public MapPath getNext() {
		return next;
	}
	public void setNext(MapPath next) {
		this.next = next;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(code)
			.append(':')
			.append(MapService.map.get(code).getName())
			.append('(')
			.append(pathLength)
			.append(") ")
			;
		if(traffic != null) {
			sb.append(traffic)
				.append(" to ")
				;
		}
		MapPath next = this.next;
		while(next != null) {
			sb.append(next.getCode())
				.append(':')
				.append(MapService.map.get(next.getCode()).getName())
				.append('(')
				.append(next.getPathLength())
				.append(") ")
				;
			if(next.getTraffic() != null) {
				sb.append(next.getTraffic())
					.append(" to ");
			}
			next = next.getNext();
		}
		return sb.toString();
	}
}
