package com.zybooks.studentschedulerandprogresstracking.UI.TermUI;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.studentschedulerandprogresstracking.Adapters.TermPageAdapters.TermAdapter;
import com.zybooks.studentschedulerandprogresstracking.Database.Repository;
import com.zybooks.studentschedulerandprogresstracking.Entities.Term;
import com.zybooks.studentschedulerandprogresstracking.R;
import com.zybooks.studentschedulerandprogresstracking.UI.MainActivity;

import java.util.List;

public class TermsOverviewPage extends AppCompatActivity {
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_overview_page);

        FloatingActionButton addButton = findViewById(R.id.TermsFloatingActionAddButton);
        addButton.setOnClickListener(v -> {
            Intent add = new Intent(TermsOverviewPage.this, TermsAddPage.class);
            startActivity(add);
        });

        TextView txt = (TextView) findViewById(R.id.term_overview_list_text);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        RecyclerView recyclerView = findViewById(R.id.terms_overview_recyclerView);
        repository = new Repository(getApplication());
        final TermAdapter termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(repository.getmAllTerms());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnToCoursePage = new Intent(TermsOverviewPage.this, MainActivity.class);
            startActivity(returnToCoursePage);
            Toast.makeText(this, "Returning to Main Menu", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Term> allTerms = repository.getmAllTerms();
        RecyclerView recyclerView = findViewById(R.id.terms_overview_recyclerView);
        final TermAdapter termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);
    }
}
