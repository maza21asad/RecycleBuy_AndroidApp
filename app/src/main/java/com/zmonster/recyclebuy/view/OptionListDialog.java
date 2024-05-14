package com.zmonster.recyclebuy.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.NameValue;

import java.util.ArrayList;
import java.util.List;


public class OptionListDialog extends DialogFragment {
    private final static String TAG = "OptionListDialog";

    private RecyclerView listView;
    private ActionAdapter listAdapter;

    private OnOptionActionListener listener;

    public List<NameValue> options = new ArrayList<NameValue>();

    private FragmentActivity activity;

    private void init(FragmentActivity activity, List<NameValue> options, OnOptionActionListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.options = options;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = inflater.inflate(R.layout.dialog_option_list_action, container, false);
        listView =  contentView.findViewById(R.id.option_list_view);
        LinearLayoutManager mLayoutMgr = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listAdapter = new ActionAdapter();
        listView.setLayoutManager(mLayoutMgr);
        listView.setAdapter(listAdapter);

        return contentView;
    }

    private void showView() {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        show(ft, "optionlistdialog");
    }

    public static void show(final FragmentActivity activity, List<NameValue> options, OnOptionActionListener listener) {
        OptionListDialog dialog = new OptionListDialog();
        dialog.init(activity, options, listener);
        dialog.showView();
    }

    private class ActionAdapter extends RecyclerView.Adapter<ActionViewHolder> {

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                ActionViewHolder holder = (ActionViewHolder) view.getTag();
                if (listener != null) {
                    int i = (int) (holder.option.value);
                    if (i >= 0) {
                        listener.onOptionSelected(i);
                    }
                }
                OptionListDialog.this.dismiss();
            }
        };

        @Override
        public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_action_option_list_item_view, parent, false);
            ActionViewHolder holder = new ActionViewHolder(itemView);
            itemView.setOnClickListener(onClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(ActionViewHolder holder, int position) {
            holder.bindData(position != 0, options.get(position));
        }

        @Override
        public int getItemCount() {
            return options.size();
        }
    }

    private class ActionViewHolder extends RecyclerView.ViewHolder {

        TextView actionNameView;
        View seperator;

        NameValue option;

        public ActionViewHolder(View itemView) {
            super(itemView);
            actionNameView = (TextView) itemView.findViewById(R.id.text_button);
            seperator = itemView.findViewById(R.id.seperator);
            itemView.setTag(this);
        }

        public void bindData(boolean needSeperator, NameValue option) {
            int i = (int) option.value;
            if (i < 0) {
                actionNameView.setGravity(Gravity.CENTER);
                actionNameView.setTextColor(activity.getResources().getColor(R.color.foreLight));
                actionNameView.setTextSize(14f);
            } else {
                actionNameView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }
            actionNameView.setText(option.name);
            seperator.setVisibility(needSeperator ? View.VISIBLE : View.GONE);
            this.option = option;
        }
    }

    public interface OnOptionActionListener {
        void onOptionSelected(int id);
    }

}
