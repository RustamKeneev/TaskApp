package com.example.taskapp.ui.main.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.example.taskapp.App;
import com.example.taskapp.MainActivity;
import com.example.taskapp.R;
import com.example.taskapp.TaskActivity;
import com.example.taskapp.ui.main.Adapter.TaskAdapter;
import com.example.taskapp.ui.main.Model.Task;
import com.example.taskapp.ui.main.PageViewModel;

import java.util.ArrayList;
import java.util.List;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    RecyclerView recyclerView;

    private List<Task> list;
    private TaskAdapter taskAdapter;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        initList();
        return root;
    }

    private void initList(){
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(list);
        recyclerView.setAdapter(taskAdapter);
        App.getInstance().getAppDatabase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                list.clear();
                list.addAll(tasks);
                taskAdapter.notifyDataSetChanged();
            }
        });
        taskAdapter.setClickListener(new TaskAdapter.ClickListener() {
            @Override
            public void onClick(int pos) {
                Task task = list.get(pos);
                Intent intent =new Intent(getContext(),TaskActivity.class);

                intent.putExtra("task",task);
                startActivity(intent);

                Toast.makeText(getContext(),task.getTitle(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int pos) {
                Task task = list.get(pos);
                showAllert(task);
            }
        });
    }
    private void showAllert( final Task task){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Сиз чын эле жок кыласызбы "+task.getTitle()+" ?");
        builder.setNegativeButton("жокко чыгаруу", null);
        builder.setPositiveButton("ооба", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getInstance().getAppDatabase().taskDao().delete(task);
            }
        });
        builder.create().show();
    }
}