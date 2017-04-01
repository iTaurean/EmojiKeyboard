package com.android.lvxin.emoticon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.lvxin.R;
import com.android.lvxin.emoticon.bean.EmoticonBean;
import com.android.lvxin.emoticon.view.i.IView;

import java.util.List;

/**
* @ClassName: EmoticonsAdapter 
* @Description: TODO
* @author lvxin
* @date May 26, 2015 10:17:09 AM
 */
@SuppressLint("InflateParams")
public class EmoticonsAdapter extends BaseAdapter {
    
    private LayoutInflater inflater;
    private Context mContext;
    
    private List<EmoticonBean> data;
    
    public EmoticonsAdapter(Context context, List<EmoticonBean> list) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }
    
    @Override
    public int getCount() {
        return data.size();
    }
    
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_emoticon, null);
            viewHolder.ivFace = (ImageView)convertView.findViewById(R.id.item_iv_face);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
        final EmoticonBean emoticonBean = data.get(position);
        if (emoticonBean != null) {
            if (mOnItemListener != null) {
                int resID = mContext.getResources().getIdentifier(emoticonBean.getIconUri(),
                    "drawable", mContext.getPackageName());
                
                viewHolder.ivFace.setImageResource(resID);
            }
            
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onItemClick(emoticonBean);
                    }
                }
            });
        }
        return convertView;
    }
    
    static class ViewHolder {
        public ImageView ivFace;
    }
    
    IView mOnItemListener;
    
    public void setOnItemListener(IView listener) {
        this.mOnItemListener = listener;
    }
}