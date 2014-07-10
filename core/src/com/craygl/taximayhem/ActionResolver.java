package com.craygl.taximayhem;

public interface ActionResolver {
  public boolean getSignedInGPGS();
  public void loginGPGS();
  public void submitScoreGPGS(int score, boolean overall);
  public void unlockAchievementGPGS(String achievementId);
  public void getLeaderboardGPGS(boolean overall);
  public void getAchievementsGPGS();
}