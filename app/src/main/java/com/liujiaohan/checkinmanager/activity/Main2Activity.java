
package com.liujiaohan.checkinmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.liujiaohan.checkinmanager.Message;
import com.liujiaohan.checkinmanager.R;
import com.liujiaohan.checkinmanager.db.MessageDao;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Main2Activity extends AppCompatActivity {
    private ListView lvMessage;
    private Button btnSendAll;
    private MessageDao mMessageDao;
    private ArrayAdapter arrayAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mMessageDao=new MessageDao(this);
        initUI();
    }

    private void initUI() {
        btnSendAll= (Button) findViewById(R.id.sendAll);
        lvMessage= (ListView) findViewById(R.id.list_messages);

        final List<Message> messageList=mMessageDao.queryAllMessages();

        final List<String>  messageInfo=new ArrayList<>();

        for (int i=0; i<messageList.size();i++){
            messageInfo.add(messageList.get(i).toString());
        }

        arrayAdapter=new ArrayAdapter<String>(Main2Activity.this,
                android.R.layout.simple_list_item_1,messageInfo);
        lvMessage.setAdapter(arrayAdapter);


        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<messageList.size();i++){
                    final Message m=messageList.get(i);
                    messageList.get(i).save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                mMessageDao.deleteMessage(m);
                            }
                            else {
                                Log.i("TAG", "done: "+e.toString());
                            }
                        }
                    });
                }

                final List<Message> messageList=mMessageDao.queryAllMessages();

                messageInfo.clear();

                for (int i=0; i<messageList.size();i++){
                    messageInfo.add(messageList.get(i).toString());
                }

                arrayAdapter.notifyDataSetChanged();
            }
        });


    }
}
