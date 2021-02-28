package com.sabirov.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import gprimo.grpc.messages.Empty;
import gprimo.grpc.messages.RunTaskGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mainRV = findViewById(R.id.main_rv);

        List<Message> messageList = new ArrayList<>();
        MainRVAdapter adapter = new MainRVAdapter(MainActivity.this, messageList);
        mainRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        mainRV.setAdapter(adapter);


        DataBase dataBase=DataBase.getInstance(MainActivity.this);
        new GRPCTask(dataBase, mainRV, adapter, messageList).execute(5067);


    }
}
class GRPCTask extends AsyncTask<Integer, Message, Void>{

    GRPCTask(DataBase dataBase, RecyclerView mainRV, MainRVAdapter adapter, List<Message> messageList) {
        this.dataBase=dataBase;
        this.adapter=adapter;
        this.messageList=messageList;
        this.mainRV=mainRV;
    }

    private MainRVAdapter adapter;
    private DataBase dataBase;
    private List<Message> messageList;
    private RecyclerView mainRV;

    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("10.0.2.2", integers[0]).usePlaintext().build();
            RunTaskGrpc.RunTaskBlockingStub runTaskBlockingStub = RunTaskGrpc.newBlockingStub(managedChannel);
            Empty emptyRequest=Empty.newBuilder().setText("").build();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                runTaskBlockingStub.getMessages(emptyRequest).forEachRemaining(message -> {
                    Message newMessage = new Message();
                    newMessage.setText1(message.getText1());
                    newMessage.setText2(message.getText2());
                    newMessage.setText3(message.getText3());
                    Message[] messages=new Message[]{newMessage};
                    publishProgress(messages);
                });
            }
            managedChannel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        dataBase.mainDao().addMessage(values[0]);
        messageList.add(values[0]);
        adapter.notifyItemInserted(messageList.size());
        mainRV.smoothScrollToPosition(messageList.size());
    }
}
