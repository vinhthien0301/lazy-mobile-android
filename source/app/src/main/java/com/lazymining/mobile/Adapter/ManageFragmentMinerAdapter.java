package com.lazymining.mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.lazymining.mobile.Fragment.ManageFragment;
import com.lazymining.mobile.Object.CardTempObject;
import com.lazymining.mobile.Object.MinerObject;
import com.lazymining.mobile.Utility.Enum;
import com.lazymining.mobile.R;
import com.lazymining.mobile.Utility.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doanngocduc on 1/23/18.
 */

public class ManageFragmentMinerAdapter extends ArrayAdapter<MinerObject> implements View.OnClickListener{

    private ArrayList<MinerObject> dataSet;
    private Context mContext;
    private ManageFragment fragment;
    private ViewBinderHelper binderHelper;

    // View lookup cache
    private class ViewHolder {
        /* Warning group */
        TextView txtNameWarning;
        TextView txtIpWarning;
        TextView txtMessageWarning;

        /* Error group */
        TextView txtNameError;
        TextView txtIpError;
        TextView txtMessageError;


        /* Full information */
        TextView txtName;
        TextView txtIp;
        TextView txtShare;
        TextView txtSpeed;
        TextView txtReject;
        TextView txtWorkTime;
        TextView txtMineHole;
        TextView txtRestart;
        TextView txtDelete;
        LinearLayout lnSpeed;
        LinearLayout lnShare;
        LinearLayout lnMineHole;
        LinearLayout lnTimeWork;
        LinearLayout lnMain;
        LinearLayout lnWarning;
        LinearLayout lnError;
        SwipeRevealLayout swipeLayout;
        TextView tvGpus;
    }

    public ManageFragmentMinerAdapter(ArrayList<MinerObject> data, Context context,ManageFragment fragment) {
        super(context, R.layout.fragment_manage_list_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.fragment = fragment;
        this.binderHelper = new ViewBinderHelper();
        this.binderHelper.setOpenOnlyOne(true);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.txt_restart:
                fragment.restartMiner(v.getTag().toString());
                break;
            case R.id.txt_delete:
                fragment.deleteMiner(v.getTag().toString());
                break;
        }
    }

    public static int getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();

        }

        return totalHeight;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MinerObject dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_manage_list_item, parent, false);


            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txtIp = (TextView) convertView.findViewById(R.id.txt_ip);
            viewHolder.txtReject = (TextView) convertView.findViewById(R.id.txt_reject);
            viewHolder.txtShare = (TextView) convertView.findViewById(R.id.txt_share);
            viewHolder.txtSpeed = (TextView) convertView.findViewById(R.id.txt_speed);
            viewHolder.txtMineHole = (TextView) convertView.findViewById(R.id.txt_mine_hole);
            viewHolder.txtWorkTime = (TextView) convertView.findViewById(R.id.txt_work_time);
            viewHolder.txtRestart = (TextView) convertView.findViewById(R.id.txt_restart);
            viewHolder.txtDelete = (TextView) convertView.findViewById(R.id.txt_delete);
            viewHolder.lnSpeed = (LinearLayout) convertView.findViewById(R.id.ln_speed);
            viewHolder.lnShare = (LinearLayout) convertView.findViewById(R.id.ln_share);
            viewHolder.lnTimeWork = (LinearLayout) convertView.findViewById(R.id.ln_time_work);
            viewHolder.lnMineHole = (LinearLayout) convertView.findViewById(R.id.ln_mine_hole);
            viewHolder.lnMain = (LinearLayout) convertView.findViewById(R.id.ln_main);
            viewHolder.tvGpus = (TextView) convertView.findViewById(R.id.tvGPUs);

            /* Warning */
            viewHolder.lnWarning = (LinearLayout) convertView.findViewById(R.id.ln_warning);
            viewHolder.txtNameWarning = (TextView) convertView.findViewById(R.id.txt_name_warning);
            viewHolder.txtIpWarning = (TextView) convertView.findViewById(R.id.txt_ip_warning);
            viewHolder.txtMessageWarning = (TextView) convertView.findViewById(R.id.txt_message_warning);
            //-------------

            /* Error */
            viewHolder.lnError = (LinearLayout) convertView.findViewById(R.id.ln_error);
            viewHolder.txtNameError = (TextView) convertView.findViewById(R.id.txt_name_error);
            viewHolder.txtIpError = (TextView) convertView.findViewById(R.id.txt_ip_error);
            viewHolder.txtMessageError = (TextView) convertView.findViewById(R.id.txt_message_error);
            //-------------

            viewHolder.swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            viewHolder.txtDelete.setOnClickListener(this);
            viewHolder.txtRestart.setOnClickListener(this);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        binderHelper.bind(viewHolder.swipeLayout, String.valueOf(position));
        viewHolder.txtDelete.setTag(position);
        viewHolder.txtRestart.setTag(position);

        viewHolder.lnMain.clearAnimation();

        viewHolder.lnMain.setVisibility(View.GONE);
        viewHolder.lnWarning.setVisibility(View.GONE);
        viewHolder.lnError.setVisibility(View.GONE);
        switch (dataModel.getWorkStatus()){
            case Enum.WORKING_STATUS_WORKING:
                viewHolder.lnMain.setVisibility(View.VISIBLE);

                viewHolder.txtName.setText(dataModel.getName() + " - (" + getCoinSpeedNb(dataModel.getMineCoinHr()) + " VGA)");
                viewHolder.txtIp.setText(dataModel.getIp());

                viewHolder.txtReject.setText(dataModel.getReject());
                viewHolder.txtShare.setText(dataModel.getShare());
                viewHolder.txtSpeed.setText(dataModel.getSpeedAmout());
                viewHolder.txtMineHole.setText(dataModel.getMine_hole());
                viewHolder.txtWorkTime.setText(dataModel.getWorkTime());
                viewHolder.tvGpus.setText(getGPUInfo(dataModel.getVgaArray(), dataModel.getMineCoinHr()));
                break;
            case Enum.WORKING_STATUS_WARNING:
                viewHolder.lnWarning.setVisibility(View.VISIBLE);

                viewHolder.txtNameWarning.setText(dataModel.getName() + " - (" + getCoinSpeedNb(dataModel.getMineCoinHr()) + " VGA)");
                viewHolder.txtIpWarning.setText(dataModel.getIp());
                viewHolder.txtMessageWarning.setText(dataModel.getWarningMessage());
                break;
            case Enum.WORKING_STATUS_STOPPED:
                viewHolder.lnError.setVisibility(View.VISIBLE);

                viewHolder.txtNameError.setText(dataModel.getName() + " - (" + getCoinSpeedNb(dataModel.getMineCoinHr()) + " VGA)");
                viewHolder.txtIpError.setText(dataModel.getIp());
                viewHolder.txtMessageError.setText(dataModel.getWarningMessage());
                break;


        }
        return convertView;
    }

    private String getMainCoinHR(String a){
        Float b = Float.parseFloat(a)/1000;
        String number = String.valueOf(b);
        String valueStr = number.replace('.', ',');
        return valueStr+" MH/s";
    }

    private String getGPUInfo(List<CardTempObject> cardTempList, String main_coin_hr) {
        String info = "";
        String[] splitStr = main_coin_hr.trim().split(";");
        for (int index = 0; index < cardTempList.size(); index++) {
            CardTempObject cardTemp = cardTempList.get(index);
            info += "GPU"+index+" "+"("+cardTemp.getTemp()+"C - "+cardTemp.getPercent()+"%)  " + getMainCoinHR(splitStr[index]) + "\n";
        }
        return info;
    }

    private int getCoinSpeedNb(String coinSpeedString) {
        return coinSpeedString.split(";").length;
    }

}