package com.example.kr.huarongdao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.Enumeration;
//棋盘组件
public class ChessboardView extends View
{
    private static final String TAG = "hrd";
    private MainRoad playBoard;
    private int gridSize, gap;
    private int[] levelInfo;
    private winCallBack winBack;

    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChessboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ChessboardView(Context context)
    {
        super(context);
    }
    //棋盘初始滑块位置
    public void setLevelInfo(int[] levelInfo) {
        this.levelInfo = levelInfo;
    }

    public void init () {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        gridSize = (int) (dm.density * 80 + 0.5f);
        gap = (int) (dm.density * 3 + 0.5f);
        playBoard = new MainRoad();
        Fragment.reset();
        Fragment.setPlayBoard(playBoard);
        setChess();

        this.setOnTouchListener(new OnTouchListener()
        {
            private int xPos = 0;
            private int yPos = 0;
            private int selectedValue = 0;
            // private int mScreenWidth, mScreenHeight;//屏幕宽高
            private int start_x, start_y;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // mScreenWidth = v.getResources().getDisplayMetrics().widthPixels;
                        // mScreenHeight = v.getResources().getDisplayMetrics().heightPixels;
                        //得到当前方块
                        start_x = (int)event.getX()/gridSize;
                        start_y = (int)event.getY()/gridSize;
                        selectedValue = playBoard.getBoardValue(start_x, start_y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xPos = (int)event.getX()/gridSize;
                        yPos = (int)event.getY()/gridSize;
                        if(selectedValue == 0 || xPos > 3 || yPos > 4)
                            break;// 选取空白或者超出边界时不反应
                        int direction = decideDirection(xPos, yPos, (Fragment)Fragment.fragmentHashTable.get(selectedValue));
                        if(direction != Fragment.DIRECTION_DONTMOVE)
                        {
                            // 判断是否可移动
                            Fragment newFragment = Fragment.fragmentHashTable.get(selectedValue).move(direction);
                            if(newFragment != null){
                                // 移动并重绘
                                Fragment.fragmentHashTable.put(selectedValue, newFragment);
                                start_x = xPos;
                                start_y = yPos;
                                playBoard.addCount();
                                winBack.showCount(playBoard.getCount());
                                v.invalidate();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 判断胜利条件
                        if(playBoard.getBoardValue(1, 4)==1 && playBoard.getBoardValue(2, 4)==1) {
                            winBack.isWin(playBoard.getCount());
                        }
                        break;
                }
                return false;
            }
            // 比较鼠标位置与当前位置得到移动方向
            private int decideDirection(int xPos, int yPos, Fragment fragment)
            {
                if((xPos == fragment.getxPos() - 1) && (yPos >= fragment.getyPos() && yPos <= fragment.getyPos() + fragment.getHeight() - 1))
                    return Fragment.DIRECTION_LEFT;
                if((xPos == fragment.getxPos() + fragment.getLength()) && (yPos >= fragment.getyPos() && yPos <= fragment.getyPos() + fragment.getHeight() - 1))
                    return Fragment.DIRECTION_RIGHT;
                if((xPos >= fragment.getxPos() && xPos <= fragment.getxPos() + fragment.getLength() - 1) && (yPos == fragment.getyPos() - 1))
                    return Fragment.DIRECTION_UP;
                if((xPos >= fragment.getxPos() && xPos <= fragment.getxPos() + fragment.getLength() - 1) && (yPos == fragment.getyPos() + fragment.getHeight()))
                    return Fragment.DIRECTION_DOWN;
                return Fragment.DIRECTION_DONTMOVE;
            }
        });
    }
    public void findGameWin(winCallBack winBack){
        this.winBack = winBack;
    }
    public interface winCallBack {
        void isWin(int count);
        void showCount(int count);
    }

    // 设置所有方块
    public void setChess()
    {
        Fragment.addFragment(new Fragment("Cao Cao", 1, 2, 2, levelInfo[0], levelInfo[1], R.drawable.core));
        Fragment.addFragment(new Fragment("Zhang Fei", 2, 1, 2, levelInfo[2], levelInfo[3], R.drawable.rectangle2));
        Fragment.addFragment(new Fragment("Huang Zhong", 3, 1, 2, levelInfo[4], levelInfo[5], R.drawable.rectangle2));
        Fragment.addFragment(new Fragment("Ma Chao", 4, 1, 2, levelInfo[6], levelInfo[7], R.drawable.rectangle2));
        Fragment.addFragment(new Fragment("Zhao Yun", 5 , 1, 2, levelInfo[8], levelInfo[9], R.drawable.rectangle2));
        Fragment.addFragment(new Fragment("Guan Yu", 6, 2, 1, levelInfo[10], levelInfo[11], R.drawable.rectangle1));
        Fragment.addFragment(new Fragment("Soldier1", 7, 1, 1, levelInfo[12], levelInfo[13], R.drawable.square1));
        Fragment.addFragment(new Fragment("Soldier2", 8, 1, 1, levelInfo[14], levelInfo[15], R.drawable.square1));
        Fragment.addFragment(new Fragment("Soldier3", 9, 1, 1, levelInfo[16], levelInfo[17], R.drawable.square2));
        Fragment.addFragment(new Fragment("Soldier4", 10, 1, 1, levelInfo[18], levelInfo[19], R.drawable.square2));
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Enumeration<Fragment> enumeration = Fragment.fragmentHashTable.elements();
        while(enumeration.hasMoreElements())
        {
            Fragment fragment = enumeration.nextElement();
            int color = fragment.getValue();
            drawFragment(canvas, fragment, color);
        }
    }
    // 根据方块大小画出图像
    private void drawFragment(Canvas canvas, Fragment fragment, int color)
    {
        Paint paint = new Paint();

        Rect rect = new Rect();
        rect.left = fragment.getxPos() * gridSize + gap;
        rect.top = fragment.getyPos() * gridSize + gap;
        rect.right = (fragment.getxPos() + fragment.getLength()) * gridSize - gap;
        rect.bottom = (fragment.getyPos() + fragment.getHeight()) * gridSize - gap;


        InputStream is = this.getContext().getResources().openRawResource(fragment.getPicture());
        @SuppressWarnings("deprecation")
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        Bitmap mPic = bmpDraw.getBitmap();
        canvas.drawBitmap(mPic, null, rect, paint);
    }

}
