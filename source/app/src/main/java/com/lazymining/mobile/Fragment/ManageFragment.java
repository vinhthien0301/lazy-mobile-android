package com.lazymining.mobile.Fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lazymining.mobile.Activity.MainActivity;
import com.lazymining.mobile.Adapter.ManageFragmentMinerAdapter;
import com.lazymining.mobile.LocalData.SharePreference;
import com.lazymining.mobile.Object.CardTempObject;
import com.lazymining.mobile.Object.MinerObject;
import com.lazymining.mobile.R;
import com.lazymining.mobile.Service.ApiService;
import com.lazymining.mobile.Utility.Enum;
import com.lazymining.mobile.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;


/**
 * Fragment class for each nav menu item
 */
public class ManageFragment extends Fragment {


    private View mContent;
    private TextView mTextView;
    private ListView lvMiner;
    private TextView tvTime;
    private ArrayList<MinerObject> minerObjectArrayList;
    private ManageFragmentMinerAdapter adapter;
    private CountDownTimer countDownTimer;
    public static Fragment newInstance() {
        Fragment frag = new ManageFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // initialize views
        mContent = view.findViewById(R.id.fragment_content);
        mTextView = (TextView) view.findViewById(R.id.text);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        lvMiner = (ListView) view.findViewById(R.id.lv_miner);
        minerObjectArrayList = new ArrayList<>();
        adapter = new ManageFragmentMinerAdapter(minerObjectArrayList, getActivity(),this);
        lvMiner.setAdapter(adapter);
        loadRig();
        startCountDown();
    }

    public void restartMiner(final String pos){
        MinerObject obj = minerObjectArrayList.get(Integer.parseInt(pos));
        String email = obj.getEmail();
        String name = obj.getName();
        ApiService.restart(email, name, new ApiService.Callback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

            }
        });
    }

    public void deleteMiner(final String pos){
        MinerObject obj = minerObjectArrayList.get(Integer.parseInt(pos));
        String email = obj.getEmail();
        String machineId = obj.getMachineId();
        ApiService.delete(email, machineId, new ApiService.Callback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    String responseCode = response.getString("response_code");
                    if (responseCode.equals(Enum.DELETE_MINERS_SUCCESS_CODE)) {
                        minerObjectArrayList.remove(Integer.parseInt(pos));
                        lvMiner.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        Utility.showToast(getActivity(), "xóa thất bại ");
                    }

                } catch (JSONException e) {
                    Utility.showToast(getActivity(), "Không thể parse Json");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

            }
        });

    }

    private void loadRig() {
        if(getActivity() != null) {
            String text = SharePreference.getAuth(getActivity());
            try {
                JSONObject obj = new JSONObject(text);
                String email = obj.getString("email");
                String token = obj.getString("token");
                ApiService.load(email, token, new ApiService.Callback() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String responseCode = response.getString("response_code");
                            if (responseCode.equals(Enum.SUCC_MINERS)) {
                                minerObjectArrayList.clear();
                                JSONObject minerArray = response.getJSONObject("data").getJSONObject("miners");

                                try {
                                    Iterator<String> temp = minerArray.keys();
                                    while (temp.hasNext()) {
                                        String key = temp.next();
                                        Object value = minerArray.get(key);
                                        updateRig((JSONObject) value);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                if(response.getString("description").equals(Enum.TOKEN_NOT_FOUND)){
                                    MainActivity activity = (MainActivity) getActivity() ;
                                    if(activity != null){
                                        activity.signout();
                                    }
                                }
                                Utility.showToast(getActivity(), "không thể lấy thông tin");
                            }

                        } catch (JSONException e) {
                            Utility.showToast(getActivity(), "Không thể parse Json");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                    }
                });
            } catch (JSONException e) {

            }
        }


    }


    private void updateRig(JSONObject minerObj) {
        try {
            String email = minerObj.getString("email");
            String local_ip = minerObj.getString("local_ip");
            String main_coin = minerObj.getString("main_coin");
            String main_coin_hr = minerObj.getString("main_coin_hr");
            String main_speed_unit = minerObj.getString("main_speed_unit");
            Integer mining_info_ready = minerObj.getInt("mining_info_ready");
            String name = minerObj.getString("name");
            String pools = minerObj.getString("pools");
            String show_mode = minerObj.getString("show_mode");
            String sub_coin = minerObj.getString("sub_coin");
            String sub_coin_hr = minerObj.getString("sub_coin_hr");
            String sub_speed_unit = minerObj.getString("sub_speed_unit");
            String temps = minerObj.getString("temps");
            String total_main_speed = minerObj.getString("total_main_speed");
            String total_sub_speed = minerObj.getString("total_sub_speed");
            String uptime = minerObj.getString("uptime");
            String ver = minerObj.getString("ver");
            String working_status = minerObj.getString("working_status");
            String warning_message = minerObj.getString("warning_message");
            String machine_id = minerObj.getString("machine_id");
            String public_ip = minerObj.getString("public_ip");
            MinerObject obj = new MinerObject();

            obj.setIp(public_ip + " (local: " + local_ip + ")");
            obj.setWorkStatus(working_status);
            obj.setWarningMessage(warning_message);
            obj.setMachineId(machine_id);
            obj.setEmail(email);
            if (working_status.equals(Enum.WORKING_STATUS_WORKING)) {
                String share = main_coin.split(";")[1];
                String reject = main_coin.split(";")[2];
                obj.setName(name);
                obj.setMineCoinHr(main_coin_hr);
                obj.setMine_hole(pools);
                obj.setWorkTime(uptime);
                obj.setShare(share);
                obj.setReject(reject);
                String x = main_coin.split(";")[0];
                if(x.length() >= 5){
                    String last = x.substring(x.length()-3, x.length());
                    String first = x.substring(0, x.length()-3);
                    obj.setSpeedAmout(first+","+last+ " Mh/s");
                    String[] array = temps.split(";");
                    ArrayList<CardTempObject> cardArray = new ArrayList<>();
                    for (int index = 0; index < array.length; index += 2) {
                        String temp = array[index];
                        String percent = "";
                        if (index < (array.length - 1)) {
                            percent = array[index + 1];
                        }
                        cardArray.add(new CardTempObject(temp, percent));
                    }
                    obj.setVgaArray(cardArray);
                }

            }else {
                obj.setName(name);
            }
            minerObjectArrayList.add(obj);
        } catch (JSONException e) {

        }
    }


    private void startCountDown() {
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTime.setText("" + millisUntilFinished / 1000 + " giây");
            }

            public void onFinish() {
                //loadRig();
                this.start();
            }
        };
        countDownTimer.start();
    }

    public void stopCountDown(){
        countDownTimer.cancel();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
