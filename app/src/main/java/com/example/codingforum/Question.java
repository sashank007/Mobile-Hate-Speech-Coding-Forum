package com.example.codingforum;

public class Question {

    public String question;
    public String qid;
    public String questionBody;

    public Question()
    {

    }
    public Question(String qid, String question,String body)
    {
        this.question=question;
        this.questionBody = body;
        this.qid=qid;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
