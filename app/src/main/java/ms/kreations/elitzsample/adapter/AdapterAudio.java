package ms.kreations.elitzsample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ms.kreations.elitzsample.utility.ItemMoveCallback;
import ms.kreations.elitzsample.model.ModelAudioList;
import ms.kreations.elitzsample.R;

public class AdapterAudio extends RecyclerView.Adapter<AdapterAudio.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    private final List<ModelAudioList> modelAudioLists;
    private final Context context;
    private AppCompatActivity appCompatActivity;
    private int postion;


    public AdapterAudio(List<ModelAudioList> ModelAudioLists, Context applicationContext) {
        this.modelAudioLists = ModelAudioLists;
        this.context = applicationContext;
        this.appCompatActivity = appCompatActivity;
    }

    @NonNull
    @Override
    public AdapterAudio.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_audio_tile, parent, false);
        return new AdapterAudio.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAudio.MyViewHolder holder, int position) {
        ModelAudioList modelAudioList = modelAudioLists.get(position);
        holder.txtAudioName.setText(modelAudioList.getSongTitle());
    }
    @Override
    public int getItemCount() {
        return modelAudioLists.size();
    }
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(modelAudioLists, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(modelAudioLists, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtAudioName;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtAudioName = itemView.findViewById(R.id.txtAudioName);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

        }
    }


}
