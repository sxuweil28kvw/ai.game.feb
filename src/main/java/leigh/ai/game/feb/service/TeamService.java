package leigh.ai.game.feb.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import leigh.ai.game.feb.dto.team.ReceivingTeamInviteException;
import leigh.ai.game.feb.dto.team.Team;
import leigh.ai.game.feb.util.HttpUtil;

public class TeamService {
	public static Team queryMyTeam() throws ReceivingTeamInviteException {
		String teamPhp = HttpUtil.get("team.php");
		Document doc = Jsoup.parse(teamPhp);
		Element mainTableBody = doc.body().child(0).child(0);
		if(mainTableBody.child(0).child(0).child(0).tagName().equals("img")) {
			//无队伍
			Element coreTd = mainTableBody.child(1).child(1);
			if(coreTd.child(0).tagName().equals("span")) {
				return null;
			}
			//正被邀请
			String inviteInfo = coreTd.child(0).child(0).child(0).child(0).text();
			String invitingTeamName = inviteInfo.substring(2).split("邀请你加入", 2)[0];
			throw new ReceivingTeamInviteException(invitingTeamName);
		}
		//有队伍
		Team team = new Team();
		String teamName = mainTableBody.child(0).child(0).child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(0).child(0).text();
		if(teamName.endsWith("☆")) {
			team.setLeaderIndex((byte)0);
			teamName = teamName.substring(0, teamName.length() - 1);
		}
		team.setName(teamName);
		team.getMembers().add(LoginService.username);
		
		int mainTrNum = mainTableBody.children().size();
		if(mainTrNum == 2) {
			return team;
		}
		Element tr1 = mainTableBody.child(1);
		for(Element td: tr1.children()) {
			String memberName = td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(1).text();
			if(memberName.endsWith("☆")) {
				team.setLeaderIndex((byte)team.getMembers().size());
				memberName = memberName.substring(0, memberName.length() - 1);
			}
			team.getMembers().add(memberName);
		}
		if(mainTrNum == 3) {
			return team;
		}
		Element tr2 = mainTableBody.child(2);
		for(Element td: tr2.children()) {
			String memberName = td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(1).text();
			if(memberName.endsWith("☆")) {
				team.setLeaderIndex((byte)team.getMembers().size());
				memberName = memberName.substring(0, memberName.length() - 1);
			}
			team.getMembers().add(memberName);
		}
		return team;
	}
	
	/*******************
	 * 
	 * @return 随机生成的队伍名
	 */
	public static String createTeam() {
		String teamName = randomTeamName();
		// 随机生成teamName全是英文，所以不用转义；如果有中文就必须用GBK转码了
		String response = HttpUtil.get("myteam_co.php?goto=newteam&maintext=" + teamName);
		while(response.endsWith("已被使用")) {
			teamName = randomTeamName();
			response = HttpUtil.get("myteam_co.php?goto=newteam&maintext=" + teamName);
		}
		return teamName;
	}
	
	/******************
	 * 退出队伍。不会校验所以请调用方确保不在副本中。
	 */
	public static void quitTeam() {
		HttpUtil.get("team_co.php?goto=quit");
		HttpUtil.get("myteam.php");
	}
	
	public static boolean invite(String teamName, String userName) {
		return true;
	}
	
	public static String randomTeamName() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 15; i++) {
			int iii = r.nextInt(26);
			sb.append((char)('a' + iii));
		}
		return sb.toString();
	}
}
