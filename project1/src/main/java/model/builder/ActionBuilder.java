package model.builder;

import model.Action;

import java.util.Date;

public class ActionBuilder {
    private Action action;

    public ActionBuilder() {
        action = new Action();
    }

    public ActionBuilder setId(Long id){
        action.setId(id);
        return this;
    }

    public ActionBuilder setDescription(String description){
        action.setDescription(description);
        return this;
    }

    public ActionBuilder setUserId(Long userId){
        action.setUserId(userId);
        return this;
    }

    public ActionBuilder setDate(Date date){
        action.setDate(date);
        return this;
    }
    public Action build(){
        return action;
    }
}
