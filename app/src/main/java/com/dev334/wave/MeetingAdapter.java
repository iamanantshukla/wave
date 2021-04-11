package com.dev334.wave;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter< MeetingAdapter.mViewHolder>{

    private List<MeetingModel> meetingModels;
    private SelectedPager selectedpager;

    public MeetingAdapter(List< MeetingModel> meetingModels, SelectedPager selectedpager){
        this.meetingModels=meetingModels;
        this.selectedpager=selectedpager;
    }

    @NonNull
    @Override
    public  mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_card, parent, false);
        //view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new  mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  mViewHolder holder, int position) {
        holder.setTopic(meetingModels.get(position).getTopic());
        holder.setDescription(meetingModels.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return meetingModels.size();
    }


    public interface SelectedPager{
        void selectedpager( MeetingModel meetingModel);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView Desc;
        TextView Topic;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedpager.selectedpager(meetingModels.get(getAdapterPosition()));
                }
            });
        }
        public void setTopic(String topic){
            Topic=view.findViewById(R.id.MeetingTopic);
            Topic.setText(topic);
        }
        public void setDescription(String desc){
            Desc=view.findViewById(R.id.MeetingDesc);
            Desc.setText(desc);
        }

    }
}
