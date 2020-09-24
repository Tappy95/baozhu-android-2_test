package com.micang.baozhu.http.bean.anwer;

import java.io.Serializable;
import java.util.List;

/**
  * @version 1.0
 * @Package com.dizoo.http.bean.anwer
 * @time 2019/3/5 11:40
 * @describe describe
 */
public class QuestionTopResult implements Serializable {
    /**
     * data : {"answer":[{"questionId":61,"answer":"","pageSize":0,"ansId":99,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":100,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":101,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":102,"pageNum":0,"isCorrect":0}],"question":{"creator":"1043572b61b945ed81c21d9c1120d34a","question":"lol哦咯lol哦咯哦哦安咯哦咯","questionState":1,"answers":"","creatorName":"","pageSize":0,"countTime":10,"pageNum":0,"qId":61,"questionTypeName":"","score":0,"rejectReason":"","createTime":1551754092522,"questionType":8,"coin":0}}
     * message : 操作成功
     * statusCode : 2000
     * token : 4099ac907466a9a6be506560a0e84b20
     */

    /**
     * answer : [{"questionId":61,"answer":"","pageSize":0,"ansId":99,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":100,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":101,"pageNum":0,"isCorrect":0},{"questionId":61,"answer":"","pageSize":0,"ansId":102,"pageNum":0,"isCorrect":0}]
     * question : {"creator":"1043572b61b945ed81c21d9c1120d34a","question":"lol哦咯lol哦咯哦哦安咯哦咯","questionState":1,"answers":"","creatorName":"","pageSize":0,"countTime":10,"pageNum":0,"qId":61,"questionTypeName":"","score":0,"rejectReason":"","createTime":1551754092522,"questionType":8,"coin":0}
     */

    public QuestionBean question;
    public List<AnswerBean> answer;
    public int res;

    public static class QuestionBean {
        /**
         * creator : 1043572b61b945ed81c21d9c1120d34a
         * question : lol哦咯lol哦咯哦哦安咯哦咯
         * questionState : 1
         * answers :
         * creatorName :
         * pageSize : 0
         * countTime : 10
         * pageNum : 0
         * qId : 61
         * questionTypeName :
         * score : 0
         * rejectReason :
         * createTime : 1551754092522
         * questionType : 8
         * coin : 0
         */

        public String creator;
        public String question;
        public int questionState;
        public String answers;
        public String creatorName;
        public int pageSize;
        public int countTime;
        public int pageNum;
        public int qId;
        public String questionTypeName;
        public int score;
        public String rejectReason;
        public long createTime;
        public int questionType;
        public int coin;
    }

    public static class AnswerBean {
        /**
         * questionId : 61
         * answer :
         * pageSize : 0
         * ansId : 99
         * pageNum : 0
         * isCorrect : 0
         */

        public int questionId;
        public String answer;
        public int pageSize;
        public int ansId;
        public int pageNum;
        public int isCorrect;
    }

}
