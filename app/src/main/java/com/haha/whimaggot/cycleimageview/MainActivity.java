package com.haha.whimaggot.cycleimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CycleImageView view;
    private List<CycleImageView.ImageData> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (CycleImageView) findViewById(R.id.cycle_image_view);
        data.add(new CycleImageView.ImageData("http://img3.imgtn.bdimg.com/it/u=2555074029,3311334544&fm=21&gp=0.jpg","第一个图片"));
        data.add((new CycleImageView.ImageData("http://img5.imgtn.bdimg.com/it/u=2513482463,2363775712&fm=21&gp=0.jpg","第二个图片")));
        data.add(new CycleImageView.ImageData("http://img5.imgtn.bdimg.com/it/u=2681253399,1123891232&fm=21&gp=0.jpg","第三个图片"));
        view.setUp(data, new CycleImageView.LoadImage() {
            @Override
            public ImageView setImage(CycleImageView.ImageData imgData) {
                SmartImageView view = new SmartImageView(MainActivity.this);
                view.setImageUrl(imgData.imageUrl.toString());
                return view;
            }
        });
        view.setOnPageClickListener(new CycleImageView.OnPageClickListener() {
            @Override
            public void onClick(View imageView, CycleImageView.ImageData imageInfo) {
                Toast.makeText(getBaseContext(),"点击了啊", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
