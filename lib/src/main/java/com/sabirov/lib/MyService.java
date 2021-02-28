package com.sabirov.lib;


import java.util.ArrayList;
import java.util.List;

import gprimo.grpc.messages.Empty;
import gprimo.grpc.messages.Message;
import gprimo.grpc.messages.RunTaskGrpc;
import io.grpc.stub.StreamObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyService extends RunTaskGrpc.RunTaskImplBase {
    @Override
    public void getMessages(Empty request, StreamObserver<Message> responseObserver) {
        List<Message> messages=new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
                String text1 = "Title" + i;
                String text2 = "Description" + i;
                String text3 = "Message" + i;
                messages.add(Message.newBuilder().setText1(text1).setText2(text2).setText3(text3).build());
        }
        Observable<Message> taskObservable=Observable.fromIterable(messages).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        taskObservable.subscribe(new Observer<Message>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Message message) {
                responseObserver.onNext(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                responseObserver.onCompleted();
            }
        });
    }

}

