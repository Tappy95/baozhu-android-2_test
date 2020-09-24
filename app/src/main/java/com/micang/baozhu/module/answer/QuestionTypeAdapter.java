package com.micang.baozhu.module.answer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.micang.baozhu.R;

import java.util.List;



public class QuestionTypeAdapter extends BaseAdapter {
    private String attribute;
    private List<String> list;

    public QuestionTypeAdapter(List<String> list, String attribute) {
        this.attribute = attribute;
        this.list = list;
    }

    @Override

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_questiontype, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();


        if (attribute != null) {
            if (attribute.equals(list.get(position))) {
                viewHolder.attributeItem.setSelected(true);
            } else {
                viewHolder.attributeItem.setSelected(false);
            }
        } else {

            if (position == 0) {
                viewHolder.attributeItem.setSelected(true);
            } else {
                viewHolder.attributeItem.setSelected(false);
            }
        }
        viewHolder.fillView((String) getItem(position));
        return convertView;
    }

    public class ViewHolder {

        private final TextView attributeName;
        private final LinearLayout attributeItem;

        ViewHolder(View view) {
            attributeName = (TextView) view.findViewById(R.id.tv_attribute_name);
            attributeItem = (LinearLayout) view.findViewById(R.id.ll_item_attribute);
        }

        void fillView(String host) {
            attributeName.setText(host);
        }
    }
}
