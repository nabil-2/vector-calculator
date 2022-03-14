package com.nbs.vektorrechner;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbs.vektorrechner.MenuModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader, HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        ImageView imageView = convertView.findViewById(R.id.child_icon);
        imageView.setImageResource(getChild(groupPosition, childPosition).icon);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    private int counter = 0;
    private int counter2_0 = 1;
    private int counter2_1 = 1;

    private int getCounter(String groupTitle) {
        switch(groupTitle) {
            case "Vektoren":
                return counter2_0;
            case "Geraden":
                return counter2_1;
            default:
                break;
        }
        return 0;
    }

    private void setCounter(String groupTitle, int val) {
        switch(groupTitle) {
            case "Vektoren":
                counter2_0 = val;
            case "Geraden":
                counter2_1 = val;
            default:
                break;
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        /*ImageView dropdown = convertView.findViewById(R.id.dropdown);
        if(getGroup(groupPosition).hasChildren) {
            if(counter == 2) {
                if(getCounter(headerTitle) == 1) {
                    if(isExpanded) {
                        dropdown.setImageResource(R.drawable.ic_dropdown_up);
                        System.out.println("animate");
                        Animation aniRotate = AnimationUtils.loadAnimation(this.context.getApplicationContext(), R.anim.rotation_clockwise);
                        //dropdown.startAnimation(aniRotate);
                    } else {
                        if(!getGroup(groupPosition).dropdownSet) {
                            listDataHeader.get(groupPosition).dropdownSet = true;
                        } else {
                            System.out.println("animate");
                            Animation aniRotate = AnimationUtils.loadAnimation(this.context.getApplicationContext(), R.anim.rotation_counterclockwise);
                            //dropdown.startAnimation(aniRotate);
                        }
                        dropdown.setImageResource(R.drawable.ic_dropdown);
                    }
                    setCounter(headerTitle, 0);
                } else {
                    setCounter(headerTitle, getCounter(headerTitle)+1);
                }
            } else {
                counter++;
            }
        } else {
            dropdown.setImageDrawable(null);
        }*/

        ImageView dropdown = convertView.findViewById(R.id.dropdown);
        if(getGroup(groupPosition).hasChildren) {
            if(isExpanded) {
                dropdown.setImageResource(R.drawable.ic_dropdown_up);
            } else {
                dropdown.setImageResource(R.drawable.ic_dropdown);
            }
        } else {
            dropdown.setImageDrawable(null);
        }

        ImageView imageView = convertView.findViewById(R.id.header_icon);
        imageView.setImageResource(getGroup(groupPosition).icon);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
