package paul.sohier.snake2;

import com.admob.android.ads.r;

import paul.sohier.snake2.view.SnakeView;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;


public class PlayActivity extends Activity {

    SnakeView view = null;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        if (view == null)  
            view = new SnakeView(this);  
        
        Resources r = this.getResources();
        
     
        setContentView(view);  
    }  
}
