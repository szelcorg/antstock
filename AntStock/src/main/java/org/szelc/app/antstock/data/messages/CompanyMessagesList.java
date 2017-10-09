package org.szelc.app.antstock.data.messages;

import java.util.LinkedList;
import java.util.List;

/**
 * @author by marcin.szelc on 2017-09-28.
 */
public class CompanyMessagesList {

    private List<Message> messageList = new LinkedList<>();

    public void addMessage(String company, String date, String link){
        messageList.add(new Message(company, date, link));
    }

    public void addAll(List<Message> messages){
       messageList.addAll(messages);
    }


    public List<Message> getMessageList() {
        return messageList;
    }

    public class Message {
        String company;
        String date;
        String link;

        public Message(String company, String date, String link) {
            this.company = company;
            this.date = date;
            this.link = link;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLink() {
            return link;
        }

    }
}
