package leigh.ai.game.feb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.dto.team.ReceivingTeamInviteException;
import leigh.ai.game.feb.dto.team.Team;
import leigh.ai.game.feb.util.HttpUtil;

public class TeamService {
	private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
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
			logger.debug("收到来自队伍{}的邀请", invitingTeamName);
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
		team.getMemberIds().add(PersonStatusService.userId);
		
		int mainTrNum = mainTableBody.children().size();
		if(mainTrNum == 2) {
			return team;
		}
		Element tr1 = mainTableBody.child(1);
		for(Element td: tr1.children()) {
			Integer memberId = Integer.parseInt(td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(0).child(0).attr("alt"));
			String memberName = td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(1).text();
			if(memberName.endsWith("☆")) {
				team.setLeaderIndex((byte)team.getMembers().size());
				memberName = memberName.substring(0, memberName.length() - 1);
			}
			team.getMembers().add(memberName);
			team.getMemberIds().add(memberId);
		}
		if(mainTrNum == 3) {
			return team;
		}
		Element tr2 = mainTableBody.child(2);
		for(Element td: tr2.children()) {
			Integer memberId = Integer.parseInt(td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(0).child(0).attr("alt"));
			String memberName = td.child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(1).text();
			if(memberName.endsWith("☆")) {
				team.setLeaderIndex((byte)team.getMembers().size());
				memberName = memberName.substring(0, memberName.length() - 1);
			}
			team.getMembers().add(memberName);
			team.getMemberIds().add(memberId);
		}
		logger.debug(team.toString());
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
		logger.debug("建立队伍：{}", teamName);
		return teamName;
	}
	
	/******************
	 * 退出队伍。调用方必须确保不在副本中。
	 * @param myTeam if null, will query my team. 
	 */
	public static void quitTeam(Team myTeam) {
		if(myTeam == null) {
			myTeam = queryMyTeam();
		}
		if(myTeam.getLeaderIndex() == 0) {
			if(myTeam.getMembers().size() > 1) {
				changeLeader(myTeam.getMemberIds().get(1));
				logger.debug("将队长转让给了{}", myTeam.getMembers().get(1));
			}
		}
		HttpUtil.get("team_co.php?goto=quit");
		HttpUtil.get("myteam.php");
	}
	
	public static void changeLeader(int newLeader) {
		HttpUtil.get("team_co.php?goto=set&vid=" + newLeader);
		HttpUtil.get("myteam.php");
	}
	
	public static boolean invite(String teamName, String userName) {
		String inviteResponse = null;
		try {
			inviteResponse = HttpUtil.get("myteam_co.php?goto=invitedo&maintext=" + URLEncoder.encode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(inviteResponse.contains("已经加入")) {
			logger.debug(inviteResponse);
			return false;
		} else if(inviteResponse.startsWith("一支名为")) {
			logger.debug(inviteResponse);
			return false;
		} else if(inviteResponse.startsWith("邀请成功")) {
			HttpUtil.get("myteam.php");
			logger.debug("邀请了{}", userName);
			return true;
		} else {
			logger.debug(inviteResponse);
			return false;
		}
	}
	
	public static boolean acceptInvite(String teamName) {
		try {
			queryMyTeam();
			return false;
		} catch(ReceivingTeamInviteException e) {
			if(!e.getTeamName().equals(teamName)) {
				return false;
			}
			String response = HttpUtil.get("team_co.php?goto=invite&doo=yes");
			if(response.contains("已加入队伍" + teamName)) {
				logger.debug("已加入队伍{}", teamName);
				return true;
			} else {
				return false;
			}
		}
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

	public static void refuseInviting() {
		HttpUtil.get("team_co.php?goto=invite&doo=no");
		logger.debug("拒绝了一次组队邀请");
	}
}
