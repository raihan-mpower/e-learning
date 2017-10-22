
package mpower.org.elearning_module.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question implements Serializable {

    @SerializedName("title_text")
    @Expose
    private String titleText;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("description_text")
    @Expose
    private String descriptionText;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("right_answer")
    @Expose
    private String rightAnswer;
    @SerializedName("wrong_answer")
    @Expose
    private String wrongAnswer;
    @SerializedName("true_false")
    @Expose
    private String trueFalse;

    /**
     * No args constructor for use in serialization
     * 
     */

    private QuestionType questionType;

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public enum QuestionType{
        TRUE_FALSE,SELECT_ONE, NOT_DEFINED, MULTIPLE_SELECT,TRIVIA
    }

    public Question() {
    }

    /**
     * 
     * @param id
     * @param titleText
     * @param audio
     * @param descriptionText
     * @param trueFalse
     * @param rightAnswer
     * @param answer
     * @param image
     * @param type
     * @param wrongAnswer
     */
    public Question(String titleText, String id, String type, String image, String audio, String descriptionText, String answer, String rightAnswer, String wrongAnswer, String trueFalse) {
        super();
        this.titleText = titleText;
        this.id = id;
        this.type = type;
        this.image = image;
        this.audio = audio;
        this.descriptionText = descriptionText;
        this.answer = answer;
        this.rightAnswer = rightAnswer;
        this.wrongAnswer = wrongAnswer;
        this.trueFalse = trueFalse;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(String trueFalse) {
        this.trueFalse = trueFalse;
    }

   public QuestionType getQuestionType(){
       switch (getType()){
           case "true-false":
               return QuestionType.TRUE_FALSE;
           case "multiple-choice":
               return QuestionType.MULTIPLE_SELECT;
           case "trivia":
               return QuestionType.TRIVIA;
           default:
               return QuestionType.NOT_DEFINED;
       }
   }

    @Override
    public String toString() {
        return "Question{" +
                "titleText='" + titleText + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                ", audio='" + audio + '\'' +
                ", descriptionText='" + descriptionText + '\'' +
                ", answer='" + answer + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", wrongAnswer='" + wrongAnswer + '\'' +
                ", trueFalse='" + trueFalse + '\'' +
                ", questionType=" + questionType +
                '}';
    }
}
