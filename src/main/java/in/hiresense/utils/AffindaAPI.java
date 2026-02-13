package in.hiresense.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class AffindaAPI {
    private static final String API_KEY = "aff_1a6887ab11de57ff14d0f6359e006588e278d14c";
	

    public static String analyzeResume(File resumeFile) throws IOException {
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID();
        String LINE_FEED = "\r\n";

        URL url = new URL("https://api.affinda.com/v2/resumes");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);

        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)) {

            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + resumeFile.getName() + "\"")
                  .append(LINE_FEED);
            writer.append("Content-Type: application/pdf").append(LINE_FEED);
            writer.append(LINE_FEED).flush();

            Files.copy(resumeFile.toPath(), output);
            output.flush();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.flush();
        }

        InputStream responseStream = connection.getResponseCode() == 200
                ? connection.getInputStream()
                : connection.getErrorStream();

        return new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    // ✅ Extract skills from Affinda JSON result
    public static List<String> extractSkills(String resultJson) {
        List<String> skills = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(resultJson);
            JSONObject data = result.getJSONObject("data");
            JSONArray skillArray = data.optJSONArray("skills");
            if (skillArray != null) {
                for (int i = 0; i < skillArray.length(); i++) {
                    JSONObject skillObj = skillArray.getJSONObject(i);
                    String name = skillObj.optString("name");
                    if (name != null && !name.isEmpty()) {
                        skills.add(name.trim().toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return skills;
    }

 // ✅ Extract summary from Affinda JSON result
    public static String extractSummary(String resultJson) {
    	String summary=null;
        try {
            JSONObject result = new JSONObject(resultJson);
            JSONObject data = result.getJSONObject("data");
            JSONArray summaryArr = data.optJSONArray("sections");
            if (summaryArr != null) {
                for (int i = 0; i < summaryArr.length(); i++) {
                    JSONObject summaryObj = summaryArr.getJSONObject(i);
                    String sectionType = summaryObj.optString("sectionType");
                    if(sectionType.equalsIgnoreCase("Summary")) {
                    	summary = summaryObj.optString("text");
                    	break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary;
    }

    // ✅ Extract personal details from Affinda JSON result
    public static String extractPersonalDetails(String resultJson) {
        String personalDetails=null;
        try {
            JSONObject result = new JSONObject(resultJson);
            JSONObject data = result.getJSONObject("data");
            JSONArray sectionArr = data.optJSONArray("sections");
            if (sectionArr != null) {
                for (int i = 0; i < sectionArr.length(); i++) {
                    JSONObject sectionObj = sectionArr.getJSONObject(i);
                    String sectionType = sectionObj.optString("sectionType");
                    if(sectionType.equalsIgnoreCase("PersonalDetails")) {
                    	personalDetails = sectionObj.optString("text");
                    	break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personalDetails;
    }

 // ✅ Extract education from Affinda JSON result
    public static String extractEducation(String resultJson) {
        String education=null;
        try {
            JSONObject result = new JSONObject(resultJson);
            JSONObject data = result.getJSONObject("data");
            JSONArray educationArr = data.optJSONArray("sections");
            if (educationArr != null) {
                for (int i = 0; i < educationArr.length(); i++) {
                    JSONObject educationObj = educationArr.getJSONObject(i);
                    String sectionType = educationObj.optString("sectionType");
                    if(sectionType.equalsIgnoreCase("Education")) {
                    	education = educationObj.optString("text");
                    	break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return education;
    }

 // ✅ Extract work experience from Affinda JSON result
    public static String extracteWorkExperience(String resultJson) {
        String experience=null;
        String totalExperience=null;
        try {
            JSONObject result = new JSONObject(resultJson);
            JSONObject data = result.getJSONObject("data");
            JSONArray experienceArr = data.optJSONArray("sections");
            totalExperience = data.optString("totalYearsExperience");            if (experienceArr != null) {
                for (int i = 0; i < experienceArr.length(); i++) {
                    JSONObject experienceObj = experienceArr.getJSONObject(i);
                    String sectionType = experienceObj.optString("sectionType");
                    if(sectionType.equalsIgnoreCase("WorkExperience")) {
                    	experience = experienceObj.optString("text");
                    	break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Total Experience : "+totalExperience+"\n"+experience;
    }

    // ✅ Calculate match score between job skills and resume skills
//    public static int calculateMatchScore(String jobSkillsCsv, List<String> resumeSkills) {
//        if (resumeSkills == null || resumeSkills.isEmpty()) {
//			return 0;
//		}
//
//        String[] jobSkills = jobSkillsCsv.split(",");
//        Set<String> required = new HashSet<>();
//        for (String js : jobSkills) {
//            required.add(js.trim().toLowerCase());
//        }
//
//        int matched = 0;
//        for (String r : resumeSkills) {
//            if (required.contains(r)) {
//				matched++;
//			}
//        }
//
//        return (int) ((matched * 100.0) / required.size());
//    }
    
    
    public static int calculateMatchScore(String jobSkillsCsv, List<String> resumeSkills) {
        if (resumeSkills == null || resumeSkills.isEmpty()) {
            return 0;
        }

        String[] jobSkills = jobSkillsCsv.split(",");
        Set<String> required = new HashSet<>();

        for (String js : jobSkills) {
            required.add(js.trim().toLowerCase());
        }

        int matched = 0;

        for (String jobSkill : required) {
            for (String resumeSkill : resumeSkills) {
                if (resumeSkill.contains(jobSkill) || jobSkill.contains(resumeSkill)) {
                    matched++;
                    break; // avoid double counting
                }
            }
        }

        return (int) ((matched * 100.0) / required.size());
    }

}
