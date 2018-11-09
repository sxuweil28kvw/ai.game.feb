package leigh.ai.game.feb.mainclass;

import java.util.Arrays;

import leigh.ai.game.feb.business.AccountBiz;
import leigh.ai.game.feb.business.MercenaryBiz;
import leigh.ai.game.feb.business.MoveBiz;
import leigh.ai.game.feb.business.RaidBiz;
import leigh.ai.game.feb.business.ShuaBiz;
import leigh.ai.game.feb.business.TagangBiz;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.WabaoService;

public class Assembled {
	public static void main(String[] args) {
		if(args.length == 0) {
			printUsage();
			System.exit(0);
		}
		String action = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		if(action.equals("tagang")) {
			TagangBiz.tagang(args);
		} else if(action.equals("tagangmowu")) {
			TagangBiz.onlyMowu(args);
		} else if(action.equals("move")) {
			Integer code = MapService.nameLookup.get(args[2]);
			if(code == null) {
				System.out.println("指定移动目标不存在，请检查错别字");
			}
			LoginService.login(args[0], args[1]);
			MoveService.moveTo(code);
		} else if(action.equals("allmovetodesert")) {
			MoveBiz.allMoveToDesert(args);
		} else if(action.equals("allmoveto")) {
			MoveBiz.allMoveTo(args);
		} else if(action.equals("guozi")) {
			if(args.length < 3) {
				printUsage();
				System.exit(0);
			}
			ShuaBiz.guozi(args[0], args[1], args[2]);
		} else if(action.equals("guya")) {
			RaidBiz.guya(args);
		} else if(action.equals("jiapian")) {
			if(args.length < 3) {
				printUsage();
				System.exit(0);
			}
			ShuaBiz.jiapian(args[0], args[1], args[2]);
		} else if(action.equals("longpi")) {
			if(args.length < 4) {
				printUsage();
				System.exit(0);
			}
			RaidBiz.longpi(args);
		} else if(action.equals("moyan")) {
			if(args.length < 4) {
				printUsage();
				System.exit(0);
			}
			RaidBiz.moyan(args);
		} else if(action.equals("ta5")) {
			RaidBiz.ta5(args[0], args[1]);
		} else if(action.equals("ta6once")) {
			RaidBiz.ta6Once(args[0]);
		} else if(action.equals("xiaohao")) {
			AccountBiz.newAccount(args[0], args[1], args[2], args[3], args[4]);
		} else if(action.equals("xiaohaobatch")) {
			AccountBiz.batchNewAccount(args[0]);
		} else if(action.equals("1zhuan")) {
			AccountBiz.yizhuan(args[0], args[1]);
		} else if(action.equals("zhaobing")) {
			MercenaryBiz.pick(args[0], args[1], Integer.parseInt(args[2]));
		} else if(action.equals("shendian")) {
			LoginService.login(args[0], args[1]);
			System.out.println(WabaoService.shrine());
		} else if(action.equals("desert")) {
			LoginService.login(args[0], args[1]);
			System.out.println(WabaoService.desert());
		} else if(action.equals("tagangdesert")) {
			TagangBiz.tagangDesert(args);
		} else if(action.equals("tagangshendian")) {
			TagangBiz.tagangShrine(args);
		} else {
			System.out.println("不认识的参数！");
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("参数：");
		System.out.println("塔港： tagang 帐号文件");
		System.out.println("塔港只做魔物任务： tagangmowu 帐号文件");
		System.out.println("移动： move 角色  密码   目标地点名称");
		System.out.println("全体移动至沙漠： allmovetodesert 帐号文件");
		System.out.println("全体移动至某地： allmoveto 地点名称  帐号文件");
		System.out.println("在某地刷魔石的碎片： guozi 角色  密码  地点名称");
		System.out.println("刷骨牙： guya 角色  密码");
		System.out.println("塔5做题（角色必须已经在塔5）： ta5 角色  密码");
		System.out.println("刷龙皮（请自行保证刺客和卡进度号在同一队）： longpi 刺客名  密码  卡进度角色  密码");
		System.out.println("训练小号： xiaohao 角色  密码  职业(如：天马骑士[女])  支援属性  问题答案(五位数字：42413)");
		System.out.println("挑苗子：zhaobing 角色  密码  留下的麻花数");
		System.out.println("神殿挖宝：shendian 角色  密码");
		System.out.println("沙漠挖宝：desert 角色  密码");
	}
}
