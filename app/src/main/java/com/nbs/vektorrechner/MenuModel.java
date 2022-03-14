package com.nbs.vektorrechner;

public class MenuModel {

    public String menuName;
    public int icon, textID;
    public boolean dropdownSet = false;
    public boolean hasChildren, isGroup, isExpanded;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, int icon) {
        this.menuName = menuName;
        this.icon = icon;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
