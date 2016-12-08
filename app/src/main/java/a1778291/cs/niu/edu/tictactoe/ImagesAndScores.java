package a1778291.cs.niu.edu.tictactoe;

import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Anwar on 10/1/2016.
 */

class ImagesAndScores {
    int score;
    ImageView im;
    String name;
    String TAG="ImagesANDScores";
    ImagesAndScores(int score, ImageView im, String name) {
        this.score = score;
//        Log.d(TAG,"Score: "+score+"Box : "+name);
        this.im = im;
        this.name=name;
    }
}
