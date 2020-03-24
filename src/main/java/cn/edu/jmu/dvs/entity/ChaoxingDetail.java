package cn.edu.jmu.dvs.entity;

public class ChaoxingDetail {
    private double videoScore;
    private double quizScore;
    private double discussScore;
    private double workScore;
    private double examScore;
    private double score;
    private String level;

    public double getVideoScore() {
        return videoScore;
    }

    public void setVideoScore(double videoScore) {
        this.videoScore = videoScore;
    }

    public double getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    public double getDiscussScore() {
        return discussScore;
    }

    public void setDiscussScore(double discussScore) {
        this.discussScore = discussScore;
    }

    public double getWorkScore() {
        return workScore;
    }

    public void setWorkScore(double workScore) {
        this.workScore = workScore;
    }

    public double getExamScore() {
        return examScore;
    }

    public void setExamScore(double examScore) {
        this.examScore = examScore;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
