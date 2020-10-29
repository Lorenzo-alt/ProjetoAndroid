package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TelaPrincipal.class);
                i.putExtra("texto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tortor ex, mollis in sollicitudin quis, sodales vitae orci. Morbi vitae molestie libero. Proin auctor nisi sed hendrerit egestas. Praesent hendrerit eu justo nec tristique. Vestibulum quis egestas libero. Nam iaculis pellentesque velit. Sed blandit convallis dui, a euismod arcu bibendum vel. Proin erat sem, ultricies quis lorem et, interdum ullamcorper lectus. Maecenas eleifend venenatis nibh, sit amet vestibulum diam varius quis. Nulla lacinia ante sed auctor tempus. Nullam posuere, orci eget ultricies euismod, enim mi placerat turpis, non lobortis tellus lorem quis nisl. Nunc sed malesuada risus, et blandit mauris.\n" +
                        "\n" +
                        "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla dapibus lectus lacus, sit amet facilisis augue porta vel. Nunc eleifend sit amet libero nec feugiat. Sed scelerisque ante id maximus scelerisque. Pellentesque porta sem ut felis porta finibus. Proin quis risus porttitor, viverra nibh sed, convallis mi. Proin molestie dictum odio, quis porta diam. In mi orci, consequat id gravida sit amet, volutpat ac neque. Praesent a lorem at neque accumsan consectetur. Ut in tortor lacus. In lobortis augue ac posuere varius. Duis vehicula ut lorem ac sagittis. Fusce eu quam venenatis odio luctus iaculis. Sed interdum maximus leo, a maximus leo suscipit quis. Donec nunc nunc, feugiat eget suscipit suscipit, sollicitudin eu lorem. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;\n" +
                        "\n" +
                        "Maecenas diam ante, tempus vel elit in, pharetra scelerisque leo. Nullam sit amet quam at sapien semper vulputate. Pellentesque mattis nulla id quam elementum rutrum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse nulla est, egestas eget erat et, commodo molestie libero. Etiam sollicitudin libero vel velit blandit, id egestas nisi finibus. Aliquam tristique nibh at dolor mollis, at maximus ante fermentum. Donec et nunc in lectus tincidunt ullamcorper. Pellentesque et efficitur ante, sed accumsan odio. Praesent pulvinar dapibus sem ut scelerisque. Integer suscipit ipsum eu elit efficitur aliquet. Nam feugiat ultricies purus, vel egestas tellus eleifend eu.");
                startActivity(i);
            }
        });
    }
}