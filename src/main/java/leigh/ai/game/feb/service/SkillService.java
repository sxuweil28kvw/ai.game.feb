package leigh.ai.game.feb.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import leigh.ai.game.feb.dto.skill.MySkill;
import leigh.ai.game.feb.dto.skill.Skill;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.util.HttpUtil;

public class SkillService {
	
	public static MySkill viewSkills() {
		MySkill ret = new MySkill();
		if(!PersonStatusService.goodCard) {
			return ret;
		}
		String src = HttpUtil.get("skill.php");
		try {
			Document doc = Jsoup.parse(src);
			Element equippingTbody = doc.body()
					.child(0).child(0).child(0).child(0).child(0).child(0)
					.child(1).child(1).child(0).child(0);
			for(int i = 1; i <= 3; i++) {
				Element span = equippingTbody.getElementById("skill_updata" + i);
				if(span.text().equals("无")) {
					ret.getEquipped().add(null);
				} else {
					ret.getEquipped().add(Skill.valueOf(span.text()));
				}
			}
			
			Element learntTbody = doc.body()
					.child(0).child(0).child(1).child(0).child(0).child(0).child(1).child(1)
					.child(0).child(0).child(0);
			Elements trs = learntTbody.children();
			int startingIndex = 0;
			for(int i = 0; i < trs.size(); i++) {
				if(trs.get(i).text().contains("清空")) {
					startingIndex = i + 1;
					break;
				}
			}
			if(startingIndex >= trs.size()) {
				return ret;
			}
			for(int i = startingIndex; i < trs.size(); i++) {
				try {
					Skill s = Skill.valueOf(trs.get(i).child(1).child(0).text());
					ret.getLearnt().add(s);
				} catch(IllegalArgumentException e) {
					continue;
				}
			}
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, src, "特技界面解析失败！");
		}
		return ret;
	}
	
	/****************
	 * 
	 * @param skill
	 * @param position 从1开始！！
	 */
	public static void equip(Skill skill, int position) {
		HttpUtil.get("skill_updata.php?goto=look&skillid=" + skill.getId());
		HttpUtil.get("skill_updata.php?goto=updata&skillid=" + skill.getId() + "&num=" + position);
	}
}
