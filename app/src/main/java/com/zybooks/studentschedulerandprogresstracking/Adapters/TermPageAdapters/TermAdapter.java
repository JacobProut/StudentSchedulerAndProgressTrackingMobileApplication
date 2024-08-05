package com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.TermUI.TermsAddPage;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        private final TextView termItemView2;

        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.terms_list_textView);
            termItemView2 = itemView.findViewById(R.id.terms_list_textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term current = mTerms.get(position);
                    Intent intent = new Intent(context, TermsAddPage.class);
                    intent.putExtra("id", current.getTermId());
                    intent.putExtra("title", current.getTermTitle());
                    intent.putExtra("startDate", current.getTermStartDate());
                    intent.putExtra("endDate", current.getTermEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    public TermAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.terms_list_item,parent,false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        if (mTerms != null) {
            Term current = mTerms.get(position);
            String title = current.getTermTitle();
            int termId = current.getTermId();
            holder.termItemView.setText(title);
            holder.termItemView2.setText(Integer.toString(termId) + ".");
        }
        else {
            holder.termItemView.setText("No Term Title");
            holder.termItemView2.setText("No course id");
        }
    }

    @Override
    public int getItemCount() {
        if (mTerms != null) {
            return mTerms.size();
        }
        else {
            return 0;
        }
    }

    public void setTerms(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

}
