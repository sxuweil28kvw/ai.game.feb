package leigh.ai.game.feb.dto.mercenary;

import java.util.List;

public class BatchEndTrainingParam {
	private List<String> usernames;
	private String password;
	private List<String> passTo;
	private String minAtkReview;
	private String minDefReview;
	private String reviewUser;
	private String soldierReviewDataLog;
	private boolean trainNew;
	public List<String> getUsernames() {
		return usernames;
	}
	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getPassTo() {
		return passTo;
	}
	public void setPassTo(List<String> passTo) {
		this.passTo = passTo;
	}
	public String getMinAtkReview() {
		return minAtkReview;
	}
	public void setMinAtkReview(String minAtkReview) {
		this.minAtkReview = minAtkReview;
	}
	public String getMinDefReview() {
		return minDefReview;
	}
	public void setMinDefReview(String minDefReview) {
		this.minDefReview = minDefReview;
	}
	public String getReviewUser() {
		return reviewUser;
	}
	public void setReviewUser(String reviewUser) {
		this.reviewUser = reviewUser;
	}
	public String getSoldierReviewDataLog() {
		return soldierReviewDataLog;
	}
	public void setSoldierReviewDataLog(String soldierReviewDataLog) {
		this.soldierReviewDataLog = soldierReviewDataLog;
	}
	public boolean isTrainNew() {
		return trainNew;
	}
	public void setTrainNew(boolean trainNew) {
		this.trainNew = trainNew;
	}
}
