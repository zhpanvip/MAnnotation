package com.zhpan.mannotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhpan.annotation.annotation.BindView;
import com.zhpan.annotation.annotation.Factory;
import com.zhpan.annotation.annotation.InjectLayout;
import com.zhpan.annotation.annotation.OnClick;
import com.zhpan.api.MButterKnife;
import com.zhpan.mannotation.factory.Shape;
import com.zhpan.mannotation.factory.ShapeFactory;

@InjectLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_test)
    Button mButton;
    private ShapeFactory mShapeFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MButterKnife.bind(this);
        BindProcessor.bind(this);
        mButton.setText("通过注解设置的Text");
        mShapeFactory = new ShapeFactory();
    }

    @OnClick({R.id.btn_factory, R.id.tv_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_test:
                Toast.makeText(this, "通过注解点击了Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_factory:
                Shape circle = mShapeFactory.create("Circle");
                circle.draw();
                break;
        }
    }
}
