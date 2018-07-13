package com.lazymining.mobile.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lazymining.mobile.Object.CardTempObject;
import com.lazymining.mobile.R;

import java.util.ArrayList;

/**
 * Created by doanngocduc on 1/23/18.
 */

public class ManageFragmentCardAdapter extends ArrayAdapter<CardTempObject> implements View.OnClickListener{

    private ArrayList<CardTempObject> dataSet;
    Context mContext;
    private String[] main_coin_hr;
    // View lookup cache
    private static class ViewHolder {
        TextView txtCardTemp,txtCoin;

    }

    public ManageFragmentCardAdapter(ArrayList<CardTempObject> data, String main_coin_hr, Context context) {
        super(context, R.layout.fragment_manage_list_item, data);

        setDataSet(data, main_coin_hr);
        this.mContext=context;
    }

    public void setDataSet(ArrayList<CardTempObject> dataSet, String main_coin_hr) {
        this.dataSet = dataSet;
        String[] splitStr = main_coin_hr.trim().split(";");
        this.main_coin_hr = splitStr;
    }

    @Override
    public void onClick(View v) {
    }

    private String getMainCoinHR(String a){
        Float b = Float.parseFloat(a)/1000;
        String number = String.valueOf(b);
        String valueStr = number.replace('.', ',');
        return valueStr+" MH/s";
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Nullable
    @Override
    public CardTempObject getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CardTempObject dataModel = getItem(position);
        if (dataModel == null) {
            return convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_manage_list_item_card, parent, false);
            viewHolder.txtCardTemp = (TextView) convertView.findViewById(R.id.txt_card_temp);
            viewHolder.txtCoin= (TextView) convertView.findViewById(R.id.txt_coin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtCardTemp.setText("GPU"+position+" "+"("+dataModel.getTemp()+"C - "+dataModel.getPercent()+"%)");
        viewHolder.txtCoin.setText(getMainCoinHR(main_coin_hr[position]));
        // Return the completed view to render on screen
        return convertView;
    }


}