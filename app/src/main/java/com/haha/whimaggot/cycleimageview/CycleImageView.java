package com.haha.whimaggot.cycleimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * Created by whiMaggot on 2015/12/16.
 */
public class CycleImageView extends FrameLayout{
    private Context mContext;
    private List<ImageData> imgData = new ArrayList<>();
    private CycleImageViewPager mViewPager;
    private OnPageClickListener mOnPageClickListener;
    private LoadImage mLoadImage;
    private int imageDataCount = 0;
    private float IndicationMargin = 0.07f;
    private LinearLayout ll_indication_group;
    private Bitmap btm_indication_unFocus;
    private Bitmap btm_indication_focus;
    private TextView tv_indication_textView;
    private CycleImageViewAdapter mAdapter = new CycleImageViewAdapter();
    private boolean isAutoCycle=true;
    private long mCycleDelayed=5000;



    /**
     *
     * 下面是定义图片轮播控件的属性
     *
     * */

    //设置图片轮播切换时间~
    public void setCycleDelayed(long delayed){
        mCycleDelayed=delayed;
    }


    //设置是否图片轮播~
    public void setAutoCycle(Boolean state){
        isAutoCycle=state;
    }

    //设置指示器样式~
    public void setIndicationStyle(IndicationStyle indicationStyle,int unFocus,int focus,float indication_self_percent){
        if(indicationStyle== IndicationStyle.COLOR){
            btm_indication_unFocus=drawIndication(50,unFocus);
            btm_indication_focus=drawIndication(50,focus);
        }else if(indicationStyle== IndicationStyle.IMAGE){
            btm_indication_unFocus= BitmapFactory.decodeResource(mContext.getResources(), unFocus);
            btm_indication_focus=BitmapFactory.decodeResource(mContext.getResources(), focus);
        }
        IndicationMargin=indication_self_percent;
        initIndication();
    }




    /**
     *  初始化~
     */

    public CycleImageView(Context context) {
        super(context);
        init(context);
    }


    public CycleImageView(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
    }


    private void init(Context context) {
        mContext=context;
        btm_indication_unFocus=drawIndication(50, Color.GRAY);
        btm_indication_focus=drawIndication(50,Color.WHITE);
        initView();
    }


    private void initView() {
        View.inflate(mContext, R.layout.layout_cycle_image_view, this);
        FrameLayout fl_image_cycle = (FrameLayout) findViewById(R.id.fl_cycle_image_view);
        mViewPager=new CycleImageViewPager(mContext);
        mViewPager.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        fl_image_cycle.addView(mViewPager);
        mViewPager.setOnPageChangeListener(new ImageCyclePageChangeListener());
        ll_indication_group = (LinearLayout) findViewById(R.id.ll_indication_group);
        tv_indication_textView=(TextView)findViewById(R.id.tv_text);
    }




    /**
     * 初始化指标器
     */
    private void initIndication(){
        ll_indication_group.removeAllViews();
        for(int i=0;i<imageDataCount;i++){
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ll_indication_group.getLayoutParams().height, LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = (int)(ll_indication_group.getLayoutParams().height*IndicationMargin);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            if(i==0) {
                imageView.setImageBitmap(btm_indication_focus);
            }else{
                imageView.setImageBitmap(btm_indication_unFocus);
            }
            ll_indication_group.addView(imageView);
        }
    }

    private Bitmap drawIndication(int radius, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);// 设置颜色
        Bitmap bitmap=Bitmap.createBitmap(radius,radius, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);

        //默认两种两种指示器，自己看心情~
        //方形指示器
        canvas.drawRect(12, 39, 54, 55, paint);

        //圆形指示器
        //IndicationMargin =0.5f ;
        //canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
        return bitmap;
    }

    public static class ImageData {
        /**
         * 轮播图片的数据结构，看需要自己增加~
         */
        public ImageData(Object imageUrl, String text) {
            this.imageUrl = imageUrl;
            this.text = text;

        }
        public Object imageUrl;
        public String text="";

    }
    public enum IndicationStyle{
        COLOR,IMAGE
    }

    private class CycleImageViewPager extends ViewPager{
        public CycleImageViewPager(Context context) {
            super(context);
        }
        public CycleImageViewPager(Context context,AttributeSet attr){
            super(context,attr);

        }
        /**
         * 事件拦截
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return super.onInterceptTouchEvent(ev);
        }

        /**
         * 事件分发
         */
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.dispatchTouchEvent(ev);
        }
        /**
         * 事件处理
         */
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return super.onTouchEvent(ev);
        }

    }


    /**
     * 这个是选择图片加载的接口方法，只要返回是一个ImageVIew就可以了哟，可以加载本地或网络图片，加载网络图片的方法网上有很多
     * 例如 SmartImageView
     *  SmartImageView view = new SmartImageView(MainActivity.this);
        view.setImageUrl(imgData.imageUrl.toString());
        return view;
     * */
    public  interface LoadImage {
        ImageView setImage(ImageData imgData);
    }

    /**
     * 图片点击事件的接口方法，这个很简单就不说啦
     *
     * */
    public interface OnPageClickListener {
        void onClick(View imageView, ImageData imageInfo);
    }

    private class CycleImageViewAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if(imageDataCount!=0){
                final ImageData imageInfo = imgData.get(position % imageDataCount);

                ImageView imageView= mLoadImage.setImage(imageInfo);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                // 设置图片点击监听
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mOnPageClickListener!=null) {
                            mOnPageClickListener.onClick(v,imageInfo);
                        }
                    }
                });

                container.addView(imageView);
                return imageView;
            }else{
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }

    //更新图片轮播的图片
    /**
     * 注意，如果图片轮播数目改变了（即imgData的size()改变了），记得调用这个方法~
     * */
    public void updateViewPager(){
        imageDataCount = imgData.size();
        mAdapter.notifyDataSetChanged();
    }

    public void setOnPageClickListener(OnPageClickListener listener){
        mOnPageClickListener=listener;
    }

    public final class ImageCyclePageChangeListener implements OnPageChangeListener {

        //上次指示器指示的位置,开始为默认位置0
        private int preIndex=0;

        @Override
        public void onPageSelected(int index) {
            if( imageDataCount!= 0){
                index = index % imageDataCount;
                //更新文本信息
                String text= imgData.get(index).text;
                tv_indication_textView.setText(TextUtils.isEmpty(text) ? "" : text);
                //恢复默认没有获得焦点指示器样式
                ((ImageView)(ll_indication_group.getChildAt(preIndex))).setImageBitmap(btm_indication_unFocus);
                // 设置当前显示图片的指示器样式
                ((ImageView)(ll_indication_group.getChildAt(index))).setImageBitmap(btm_indication_focus);
                preIndex=index;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }
    }


    public void setUp(List<ImageData> imgData,LoadImage callBack){
        this.imgData = imgData;
        imageDataCount=imgData.size();
        initIndication();
        if(callBack==null){
            new IllegalArgumentException("LoadImageCallBack 回调函数不能为空！");

        }
        mLoadImage =callBack;
        mViewPager.setAdapter(mAdapter);
        //最大值中间 的第一个
        if(imageDataCount!=0){
            mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % imageDataCount));
        }

    }







    /**
     * 下面主要是图片轮播的开始以及一些事件处理
     * */

    //开始图片轮播
    private void startImageCycle() {
        handler.sendEmptyMessageDelayed(0, mCycleDelayed);
    }

    //暂停轮播
    private void stopImageCycle() {
        handler.removeCallbacksAndMessages(null);
    }

    //轮播方法
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                handler.sendEmptyMessageDelayed(0,mCycleDelayed);
            }
            return false;
        }
    });




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(isAutoCycle) {
                // 开始图片滚动
                startImageCycle();
            }
        }else{
            if(isAutoCycle) {
                // 停止图片滚动
                stopImageCycle();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止图片滚动
        stopImageCycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(isAutoCycle) {
            startImageCycle();
        }
    }
}


