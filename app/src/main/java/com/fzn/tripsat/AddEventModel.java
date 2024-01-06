package com.fzn.tripsat;

public class AddEventModel {

    String groupName, groupIcon;

    public AddEventModel(String groupName, String groupIcon) {
        this.groupName = groupName;
        this.groupIcon = groupIcon;
    }

    public AddEventModel() {
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }
}
