package tr.edu.yildiz.mucahitcataler;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String question;
    private ArrayList<String> answers;
    private int correctAnswerIndex;
    private Drawable pic;

    public Question(String question, ArrayList<String> answers, int correctAnswerIndex) {
        this.ID = getHashedID(question, getSalt());
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getID() {return ID;}

    public Drawable getPic() {
        return pic;
    }

    public void setPic(Drawable pic) {
        this.pic = pic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    private static String getHashedID (String question, byte[] salt) {
        String generatedID = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(question.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedID = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedID;
    }

    private static byte[] getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
