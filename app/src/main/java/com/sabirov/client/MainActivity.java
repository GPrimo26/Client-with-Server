package com.sabirov.client;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Service.MyServer;
import gprimo.grpc.messages.Empty;
import gprimo.grpc.messages.RunTaskGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MainActivity extends AppCompatActivity {

    public interface OnTaskResponse{
        void onTaskResponse();
    }

    private MainRVAdapter adapter;
    private RecyclerView mainRV;
    private List<com.sabirov.client.Message> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainRV=findViewById(R.id.main_rv);

        messageList=new ArrayList<>();
        adapter=new MainRVAdapter(MainActivity.this, messageList);
        mainRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        mainRV.setAdapter(adapter);


        DataBase dataBase=DataBase.getInstance(MainActivity.this);
        int port=5000+(new Random().nextInt(500));
        new Thread(() -> {
            MyServer myServer=new MyServer();
            try {
                myServer.setPort(port);
                myServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }).start();
        OnTaskResponse onTaskResponse= () -> {
            messageList=dataBase.mainDao().getMessages();
            adapter.setMessages(messageList);
        };
        new GRPCTask(onTaskResponse, dataBase).execute(port);


    }
}
class GRPCTask extends AsyncTask<Integer, Void, Void>{

    GRPCTask(MainActivity.OnTaskResponse onTaskResponse, DataBase dataBase) {
        this.onTaskResponse=onTaskResponse;
        this.dataBase=dataBase;
    }

    private MainActivity.OnTaskResponse onTaskResponse;
    private DataBase dataBase;

    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("0.0.0.0", integers[0]).usePlaintext().build();
            RunTaskGrpc.RunTaskBlockingStub runTaskBlockingStub = RunTaskGrpc.newBlockingStub(managedChannel);
            Empty emptyRequest=Empty.newBuilder().setText("").build();
            Iterator<gprimo.grpc.messages.Message> response = runTaskBlockingStub.getMessages(emptyRequest);
            for (; response.hasNext(); ) {
                com.sabirov.client.Message message = new com.sabirov.client.Message();
                message.setText1(response.next().getText1());
                message.setText2(response.next().getText2());
                message.setText3(response.next().getText3());
                dataBase.mainDao().addMessage(message);
            }
            managedChannel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        onTaskResponse.onTaskResponse();
    }
}
