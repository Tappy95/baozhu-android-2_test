package com.micang.baozhu.http.bean.home;

/**
 * @author
 * @version 1.0
 * @E-mail
 * @time 2019/9/3 14:18
 * @describe describe
 */
public class ConductTaskBean {

    public Task task;
    public int isGame;         //是否有访问限制1没有2有限制且未匹配任何任务3有限制且匹配了任务

    public static class Task {
        public String reward;
        public int id;
        public String logo;
        public String name;

    }
}
