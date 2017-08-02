package leigh.ai.game.feb.service;

import java.util.HashMap;
import java.util.Map;

import leigh.ai.game.feb.parsers.NpcParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WabaoService {
	private static final Logger logger = LoggerFactory.getLogger(WabaoService.class);
	public static String desert() {
		MoveService.moveTo(2188);
		String faTell = NpcParser.parse(HttpUtil.get("npc.php?npcid=169"));
		if(!faTell.contains("进入寻宝地图")) {
			logger.info(faTell);
		}
		int[] row = new int[5];
		int[] col = new int[5];
		int chestNum = 0;
		String desert = HttpUtil.get("s_desert.php");
		Document doc = Jsoup.parse(desert);
		Element desertTbody = doc.body().child(0).child(0).child(1).child(0).child(0).child(0);
		for(int y = 0; y < desertTbody.children().size(); y++) {
			Element tr = desertTbody.child(y);
			for(int x = 0; x < tr.children().size(); x++) {
				Element td = tr.child(x);
				if(td.html().startsWith("<img ")) {
					String tdid = td.id();
					row[chestNum] = Integer.parseInt(tdid.split("-")[0]);
					col[chestNum] = Integer.parseInt(tdid.split("-")[1]);
					chestNum++;
				}
			}
		}
		for(int i = 0; i < chestNum; i++) {
			HttpUtil.get("s_desert.php?goto=mov&H=" + row[i] + "&W=" + col[i]);
			HttpUtil.get("s_desert.php");
			HttpUtil.get("s_desert.php?goto=goend");
			String a4 = HttpUtil.get("s_desert.php");
			doc = Jsoup.parse(a4);
			String found = doc.body().child(0).child(0).child(2).child(2).child(0).html();
			if(!found.equals("")) {
				
			}
		}
		HttpUtil.get("s_desert.php?goto=mov&H=15&W=0");
		String win = HttpUtil.get("s_desert.php?goto=win");
		doc = Jsoup.parse(win);
		Element tbody = doc.body().child(0).child(0).child(1).child(0).child(0).child(0).child(0).child(0).child(0).child(0).child(1).child(1).child(0).child(2);
		Map<String, Object> param = new HashMap<String, Object>(chestNum);
		StringBuilder result = new StringBuilder("沙漠挖到：");
		for(int i = 0; i < tbody.children().size() - 1; i++) {
			Element tr = tbody.child(i);
			String name = tr.child(1).child(0).attr("name");
			param.put(name, "E");
			String itemName = tr.child(0).html();
			result.append(itemName).append(",");
		}
		param.put("goto", "wins");
		HttpUtil.post("s_desert.php", param);
		return result.toString();
	}
	public static String shrine() {
		MoveService.moveTo(2075);
		String statusStr = NpcParser.parse(HttpUtil.get("npc.php?npcid=235"));
		if(!statusStr.contains("进入寻宝地图")) {
			logger.info(statusStr);
			return "";
		}
		HttpUtil.get("s_dragon.php");
		for(int i = 0; i < 5; i++) {
			HttpUtil.get("s_dragon.php?goto=mov&W=0");
			String dragonMap = HttpUtil.get("s_dragon.php");
			boolean chest = false;
			try {
				Document doc = Jsoup.parse(dragonMap);
				chest = !doc.body().child(0).child(0).child(2).child(2).html().equals("");
			} catch(Exception e) {
				ParserExceptionHandler.warn(e, dragonMap, "解析神殿挖宝失败！");
			}
			if(chest) {
				HttpUtil.get("s_dragon.php?goto=goend");
				HttpUtil.get("s_dragon.php");
			}
		}
		HttpUtil.get("s_dragon.php?goto=mov&W=0");
		HttpUtil.get("s_dragon.php");
		String result = HttpUtil.get("s_dragon.php?goto=win");
		int chests = 0;
		Map<String, Object> param = new HashMap<String, Object>(5);
		StringBuilder itemListStr = new StringBuilder("神殿挖到：");
		try {
			Document doc = Jsoup.parse(result);
			Element tbody = doc.body().child(0).child(0).child(1).child(0).child(0).child(0).child(0).child(0).child(0).child(0).child(1).child(1).child(0).child(2);
			chests = tbody.children().size() - 1;
			for(int i = 0; i < chests; i++) {
				Element tr = tbody.child(i);
				String item = tr.child(0).html();
				if(item.equals("全无收获")) {
					logger.info("神殿挖宝全空！");
					return "";
				}
				if(item.equals("伤药") || item.equals("圣水")) {
					param.put(tr.child(1).child(0).attr("name"), "del");
				} else {
					itemListStr.append(item).append(",");
					param.put(tr.child(1).child(0).attr("name"), "E");
				}
			}
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, result, "解析神殿结果失败！");
		}
		param.put("goto", "wins");
		String response = HttpUtil.post("s_dragon.php", param);
		if(!response.contains("本次寻宝华丽的结束了")) {
			logger.error("神殿拾取失败！");
			return "";
		}
		if(itemListStr.length() > 0) {
			return itemListStr.toString();
		} else {
			return "";
		}
	}
}
