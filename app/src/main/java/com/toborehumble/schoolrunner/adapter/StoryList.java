package com.toborehumble.schoolrunner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toborehumble.schoolrunner.R;
import com.toborehumble.schoolrunner.pojo.Story;

import java.util.ArrayList;


public class StoryList extends RecyclerView.Adapter<StoryList.ViewHolder> {
    private Context context;
    private ArrayList<Story> stories;

    public StoryList(){}


    public StoryList(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_story, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView verb = holder.verb;
        TextView object = holder.object;
        TextView subject = holder.subject;

        subject.setText(stories.get(position).getSubjectUsername());
        verb.setText(stories.get(position).getVerb());

        if(stories.get(position).getObjectUsername() != null){
            object.setText(stories.get(position).getObjectUsername());
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView subject;
        private TextView verb;
        private TextView object;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            verb = itemView.findViewById(R.id.verb);
            object = itemView.findViewById(R.id.object);
            subject = itemView.findViewById(R.id.subject);
        }
    }
}
