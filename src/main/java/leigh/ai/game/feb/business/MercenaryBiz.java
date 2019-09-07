package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import leigh.ai.game.feb.dto.mercenary.BatchEndTrainingParam;
import leigh.ai.game.feb.service.FacilityService;
import leigh.ai.game.feb.service.JobService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.MultiAccountService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryDetail;
import leigh.ai.game.feb.service.mercenary.MercenaryStatus;
import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.util.UnicodeReader;

public class MercenaryBiz {
	private static final Logger logger = LoggerFactory.getLogger(MercenaryBiz.class);
	public static void pickVeryGood(String u, String p, int mahuaLeft) {
		LoginService.login(u, p);
		MercenaryService.mercenaryBatch(mahuaLeft);
	}
	public static void pickGood(String filePath) {
		BufferedReader br = null;
		Map<String, Integer> mahuaFormer = new HashMap<String, Integer>();
		Map<String, Integer> mahuaLeft = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new UnicodeReader(new FileInputStream(filePath), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("")) {
					continue;
				}
				String[] spl = line.split("=");
				LoginService.login(spl[0], spl[1]);
				mahuaFormer.put(spl[0], PersonStatusService.mahua);
				MercenaryService.mercenary3333();
				mahuaLeft.put(spl[0], PersonStatusService.mahua);
				line = br.readLine();
			}
		} catch(FileNotFoundException e) {
			logger.error("指定的账号文件不存在！可能路径写法错误？");
			System.exit(1);
		} catch (IOException e) {
			logger.error("账号文件读取出错！");
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		for(String u: mahuaLeft.keySet()) {
			logger.error("{}\t花费{}麻花\t剩余{}麻花", u,
					mahuaFormer.get(u) - mahuaLeft.get(u), mahuaLeft.get(u));
		}
	}
	
	public static void batchTraining(String yml) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		BatchEndTrainingParam param = null;
		try {
			param = mapper.readValue(new File(yml), BatchEndTrainingParam.class);
		} catch (IOException e) {
			logger.error("解析yml文件失败！", e);
			return;
		}
		if(param == null) {
			logger.error("解析yml文件失败！");
			return;
		}
		
		boolean logM = true;
		File mLogFile = new File(param.getSoldierReviewDataLog());
		if(!mLogFile.exists()) {
			try {
				mLogFile.createNewFile();
			} catch (IOException e) {
				logger.error("创建佣兵日志文件{}失败", param.getSoldierReviewDataLog(), e);
				logM = false;
			}
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(mLogFile, true), true);
		} catch (IOException e) {
			logger.error("", e);
			logM = false;
		}
		
		int currentGiveto = 0;
		
		Account[] accounts = new Account[param.getUsernames().size()];
		int reviewer = -1;
		// 见公主者是否在练兵名单中。
		boolean reviewerTraining = true;
		for(int i = 0; i < param.getUsernames().size(); i++) {
			String username = param.getUsernames().get(i);
			accounts[i] = new Account(username, param.getPassword());
			if(reviewer == -1 && username.equals(param.getReviewUser())) {
				reviewer = i;
			}
		}
		if(reviewer == -1) {
			Account[] tmp = Arrays.copyOf(accounts, accounts.length + 1);
			tmp[tmp.length - 1] = new Account(param.getReviewUser(), param.getPassword());
			reviewer = tmp.length - 1;
			reviewerTraining = false;
			accounts = tmp;
		}
//		MultiAccountService.login(accounts);
		for(int i = 0; i < accounts.length; i++) {
			if(i == reviewer && !reviewerTraining) {
				continue;
			}
			
			if(i == reviewer) {
				LoginService.login(accounts[i].getU(), accounts[i].getP());
			} else {
				MultiAccountService.login(new Account[] {accounts[reviewer], accounts[i]});
				MultiAccountService.activate(1);
			}
			//获取佣兵列表状态
			MercenaryService.update();
			for(int j = 0; j < MercenaryService.myMercenaries.size(); j++) {
				Mercenary m = MercenaryService.myMercenaries.get(j);
				if(!m.getStatus().equals(MercenaryStatus.train)) {
					continue;
				}
				MercenaryDetail originDetail = MercenaryService.queryDetail(m.getId());
				//停止训练
				MercenaryService.endTraining(m);
				//查询佣兵详细信息
				MercenaryDetail detail = MercenaryService.queryDetail(m.getId());
				//不到20级的继续训练
				if(detail.getLevel() < 20) {
					MercenaryService.startTraining(m.getId());
					continue;
				}
				if(i != reviewer) {
					MercenaryService.giveTo(m.getId(), accounts[reviewer].getU());
					MultiAccountService.activate(0);
				}
				String[] review = MercenaryService.review(m.getId());
				if(logM) {
					logMercenary(pw, detail, review);
				}
				boolean atkOk = false, defOk = false;
				for(String r: MercenaryService.mercenaryReviewClass) {
					if(r.equals(param.getMinAtkReview())) {
						atkOk = true;
						break;
					}
					if(r.equals(review[0])) {
						break;
					}
				}
				for(String r: MercenaryService.mercenaryReviewClass) {
					if(r.equals(param.getMinDefReview())) {
						defOk = true;
						break;
					}
					if(r.equals(review[1])) {
						break;
					}
				}
				
				if((atkOk && defOk) || review[0].startsWith("S") || review[1].startsWith("S")
						|| detail.getSpd() >= 18) {
					logger.info("练出合格佣兵：{}, {}/{}", detail.toString(), review[0], review[1]);
					String newName = detail.getJob().name() + review[0]
							+ (review[0].charAt(review[0].length() - 1) == review[1].charAt(0) ? "/" : "") + review[1]
							+ originDetail.getHp() + originDetail.getPwr() + originDetail.getSpd()
							+ originDetail.getDef() + originDetail.getPrt();
					MercenaryService.rename(m.getId(), newName);
					while(!MercenaryService.giveTo(m.getId(), param.getPassTo().get(currentGiveto))) {
						currentGiveto++;
						if(currentGiveto >= param.getPassTo().size()) {
							logger.error("所有合格兵收纳者均已满员！程序退出");
							if(pw != null) {
								pw.close();
							}
							return;
						}
					}
					logger.debug("已转移给{}", param.getPassTo().get(currentGiveto));
					j--;
				} else {
					MercenaryService.fireMercenary(m.getId());
					j--;
				}
				
				if(i != reviewer) {
					MultiAccountService.activate(1);
					MercenaryService.update();
				}
			}
			
			MercenaryService.update();
			if(!param.isTrainNew()) {
				continue;
			}
			
			if(PersonStatusService.mahua > 45 &&
					MercenaryService.limit - MercenaryService.myMercenaries.size() > 10) {
				FacilityService.mercenaryTen();
				MercenaryService.update();
			}
			int training = 0;
			int trainingLimit = 10;
			if(JobService.is0zhuan(PersonStatusService.myjob)) {
				trainingLimit = 5;
			} else if(PersonStatusService.goodCard) {
				trainingLimit = 15;
			}
			if(PersonStatusService.justiceCard) {
				trainingLimit = 25;
			}
			for(Mercenary m: MercenaryService.myMercenaries) {
				if(m.getStatus().equals(MercenaryStatus.train)) {
					training++;
					continue;
				}
				if(!m.getStatus().equals(MercenaryStatus.rest)) {
					continue;
				}
				if(MercenaryService.queryDetail(m.getId()).getLevel() < 20) {
					if(!MercenaryService.startTraining(m.getId())) {
						break;
					}
					training++;
					if(training >= trainingLimit) {
						break;
					}
				}
			}
			LoginService.logout();
		}
		
		if(pw != null) {
			pw.close();
		}
	}
	private static void logMercenary(PrintWriter pw, MercenaryDetail md, String[] review) {
		StringBuilder sb = new StringBuilder()
				.append(md.getJob().getName()).append(',')
				.append(md.getMaxHp()).append(',')
				.append(md.getPwr()).append(',')
				.append(md.getAgi()).append(',')
				.append(md.getSpd()).append(',')
				.append(md.getLck()).append(',')
				.append(md.getDef()).append(',')
				.append(md.getPrt()).append(',')
				.append(review[0]).append(',')
				.append(review[1]);
		pw.println(sb.toString());
	}
	public static void batchAdventure(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] account = line.split("=");
				LoginService.login(account[0], account[1]);
				MercenaryService.adventureBatch();
				LoginService.logout();
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
