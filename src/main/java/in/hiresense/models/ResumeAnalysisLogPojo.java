package in.hiresense.models;

public class ResumeAnalysisLogPojo {
    private int id;
    private int userId;
    private String resultJson;
    private String createdAt;

    public ResumeAnalysisLogPojo() {}

    public ResumeAnalysisLogPojo(int id, int userId, String resultJson, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.resultJson = resultJson;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getResultJson() { return resultJson; }
    public void setResultJson(String resultJson) { this.resultJson = resultJson; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "ResumeAnalysisLogPojo{" + "id=" + id + ", userId=" + userId + '}';
    }
}