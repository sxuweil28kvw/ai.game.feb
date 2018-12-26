package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.PersonStatusService;
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
}
