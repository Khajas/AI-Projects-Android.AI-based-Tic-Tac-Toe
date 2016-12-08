package a1778291.cs.niu.edu.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private ArrayList<ImageView> imagesArray;
    private HashMap<ImageView , Integer> boardMap;
    private int vacant=0, plyrId=1, sysId=2;
    private int turnId=plyrId;
    private int wins, losses, draws;
    private static final String TAG = "MyMainActivity";
    private TextView gameStatus, winScore, lossScore, drawScore;
    private ArrayList<ArrayList<ImageView>> allLines;
    private ArrayList<ImagesAndScores> rootsChildrenScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initComponents();
    }
    private void initComponents(){
        boardMap = new HashMap<>();
        b1=(ImageView) findViewById(R.id.box1);
        b2=(ImageView) findViewById(R.id.box2);
        b3=(ImageView) findViewById(R.id.box3);
        b4=(ImageView) findViewById(R.id.box4);
        b5=(ImageView) findViewById(R.id.box5);
        b6=(ImageView) findViewById(R.id.box6);
        b7=(ImageView) findViewById(R.id.box7);
        b8=(ImageView) findViewById(R.id.box8);
        b9=(ImageView) findViewById(R.id.box9);

        gameStatus=(TextView) findViewById(R.id.textView);
        winScore=(TextView) findViewById(R.id.tvWins);
        lossScore=(TextView) findViewById(R.id.tvLoss);
        drawScore=(TextView) findViewById(R.id.tvDraws);

        // Init ArrayList
        imagesArray=new ArrayList<>();
        rootsChildrenScores = new ArrayList<>();
        this.clearBoard();
        this.setListener();
        this.wins=0;
        this.losses=0;
        this.draws=0;
        // Init map
        initMap();
        initLines();
        // Button
        Button rstBtn=(Button) findViewById(R.id.btnRst);
        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBoard();
            }
        });
        Button qutBtn=(Button) findViewById(R.id.btnQuit);
        qutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
             }
        });
    }

    private void initMap(){
        boardMap.put(b1,0); boardMap.put(b2,0); boardMap.put(b3,0);
        boardMap.put(b4,0); boardMap.put(b5,0); boardMap.put(b6,0);
        boardMap.put(b7,0); boardMap.put(b8,0); boardMap.put(b9,0);
    }

    private void setListener() {

        for(ImageView im: imagesArray) {
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startGame(view);
                }
            });
        }
    }

    // Check if the box selected is marked

    //////////////////////////////////////////////////////////////////////////
    private void initLines(){
        allLines= new ArrayList<>();
        ArrayList<ImageView> oneLine=new ArrayList<>();
        //line1 159,  line2 123, line3 147, line4 357, line5 789, line6 369, line7 258, line8 456
        oneLine.add(b1); oneLine.add(b5); oneLine.add(b9);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b3); oneLine.add(b5); oneLine.add(b7);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b4); oneLine.add(b5); oneLine.add(b6);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b2); oneLine.add(b5); oneLine.add(b8);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b1); oneLine.add(b2); oneLine.add(b3);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b3); oneLine.add(b6); oneLine.add(b9);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b7); oneLine.add(b8); oneLine.add(b9);
        allLines.add(oneLine);

        oneLine=new ArrayList<>();
        oneLine.add(b1); oneLine.add(b4); oneLine.add(b7);
        allLines.add(oneLine);

    }
    //////////////////////////////////////////////////////////////////////////

    //Is the game over ?

    public boolean isGameOver() {
        //Game is over is someone has won, or board is full (draw)
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    private boolean hasXWon() {
        for (ArrayList<ImageView> line : allLines) {
            if (wonGame(line, plyrId)){
     //           Log.d(TAG,"System might win!");
                return true;
            }
        }
   //     Log.d(TAG,"X has not won yet");
        return false;
    }

    private boolean hasOWon() {
        for (ArrayList<ImageView> line : allLines) {
            if (wonGame(line, sysId)){
     //           Log.d(TAG,"System might loose!");
                return true;
            }
        }
     //   Log.d(TAG,"O has not won yet");
        return false;
    }

    /////////////////////////////////////////////////////////////////////////

    private ArrayList<ImageView> getAvailableStates(){
        ArrayList<ImageView> availableStates=new ArrayList<>();
        for(ImageView im: imagesArray)
            if(boardMap.get(im)==vacant) {
//                Log.d(TAG,"Vacancy here!!!");
                availableStates.add(im);
            }
   //     Log.d(TAG,"Vacancies: "+availableStates.size());
        return availableStates;
    }

    ////////////////////////////////////////////////////////////////////////
    private void placeAMove(ImageView im){
        rootsChildrenScores.clear();
        markBox(im,sysId);
    //    Log.d(TAG,"Placing Move");
        updateImage(im, sysId);
    //    Log.d(TAG,"Image updated?: "+itos(im));
    }
    ///////////////////////////////////////////////////////////////////////
    public ImageView returnBestMove() {
        int MAX = -100000;
        int best = -1;
  //      Log.d(TAG,"Return best: size of roots: "+rootsChildrenScores.size());
        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
  //          Log.d(TAG,"Loop score: "+ rootsChildrenScores.get(i).score);
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
    //    Log.d(TAG, "Inside return best Move! with best "+best);
        return rootsChildrenScores.get(best).im;
    }
///////////////////////////////////////////////////////////////////////
    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
////////////////////////////////////////////////////////////////////////////
    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
///////////////////////////////////////////////////////////////////////////

    public void callMinimax(int depth, int turn){
 //       Log.d(TAG,"Inside callMiniMax ");
        minimax(depth, turn);
    }
//////////////////////////////////////////////////////////////////////////
    public int minimax(int depth, int turn) {
  //      Log.d(TAG,"Inside miniMax");
        if (hasOWon()) return +1;
        if (hasXWon()) return -1;

        ArrayList<ImageView> imagesAvailable = getAvailableStates();
//        Log.d(TAG,"Size of imagesAvailable: "+imagesAvailable.size());
        if (imagesAvailable.isEmpty()) return 0;

        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < imagesAvailable.size(); ++i) {

            ImageView im = imagesAvailable.get(i);
            if (turn == sysId) { //O's turn select the highest from below minimax() call
                if(boardMap.get(im)==vacant) boardMap.put(im, sysId);
                int currentScore = minimax(depth + 1, plyrId);
  //              Log.d(TAG,"CurrentScore: "+currentScore);
                scores.add(currentScore);
                if (depth == 0) {
//                    Log.d(TAG,"Adding to Roots Children Scores");
                    rootsChildrenScores.add(new ImagesAndScores(currentScore, im, itos(im)));
                }
            } else if (turn == plyrId) {//X's turn select the lowest from below minimax() call
                if(boardMap.get(im)==vacant) boardMap.put(im, plyrId);
                scores.add(minimax(depth + 1, sysId));
            }
            boardMap.put(im, vacant); //Reset this point
        }
//        Log.d(TAG,"Min Score: "+returnMin(scores)+"MaxScores: "+returnMax(scores));
        return turn == sysId ? returnMax(scores) : returnMin(scores);
    }

    ////////////////////////////////////////////////////////////////////////
    private void whoWon(){
        if(isGameOver()){
            if(hasOWon()){
                losses++;
                lossScore.setText(String.valueOf(losses));
                gameStatus.setText("You Lost!");
            }
            else if(hasXWon()){
                wins++;
                winScore.setText(String.valueOf(wins));
                gameStatus.setText("You Wins!");
            }
            else if(isFull()){
                draws++;
                drawScore.setText(String.valueOf(draws));
                gameStatus.setText("Draw!");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////
    private void startGame(View v){
        if(isGameOver()){
            gameStatus.setText("Click Restart to play again!");
            return;
        }
        //       Log.d(TAG, "Clicked on ImageView!");
        Random rand = new Random();
        // Should have a method call displayBoard
        if(isFull()){
  //          Log.d(TAG,"Board is full");
            return;
        }

        if(turnId==plyrId){
    //        Log.d(TAG,"It's players Turn");
            if(tryMarking(v))
                startGame(v);
            return;
        }

        if(turnId==sysId & isEmpty()){
    //        Log.d(TAG,"System will start");
            ImageView im = imagesArray.get(rand.nextInt(10));
            placeAMove(im);
            this.turnId=plyrId;
            gameStatus.setText("Your Turn");
            return;
        }
        else if(turnId==sysId & !isEmpty() ){
   //         Log.d(TAG,"System's Turn");
            callMinimax(0, sysId);
            placeAMove(returnBestMove());
            this.turnId=plyrId;
            gameStatus.setText("Your Turn");
            return;
        }
    }

    ////////////////
    private boolean isEmpty(){

        for(ImageView im: imagesArray)
            if(boardMap.get(im)!=vacant)
                return false;
        return true;
    }
    ////////////////////
    private boolean isNotMarked(ImageView tv){
        return boardMap.get(tv)==vacant;
    }

    //Mark the box with the specified turnId

    private void markBox(ImageView tv, int turnId){
        boardMap.put(tv,turnId);
//        Log.d(TAG, "Marked and updated!");
        whoWon();
    }

    //Try marking box

    private boolean tryMarking(View v){
        // get the tv who invoked the method.
        ImageView  calledTv= (ImageView)findViewById(v.getId());
        if(isNotMarked(calledTv) & turnId==plyrId){
            markBox(calledTv, turnId);
            updateImage(calledTv, plyrId);
            this.turnId=sysId;
            gameStatus.setText("Wait!");
            return true;
        }
        return false;
    }


    // Update the mark

    private void updateImage(ImageView tv, int turnId){
        if(turnId==plyrId) tv.setImageResource(R.drawable.cctb);
        else tv.setImageResource(R.drawable.crosstb);
    }

    //////////////////////////////////////////////////////////////////////////


    private boolean isFull() {
        for (ImageView im : imagesArray) {
            int id = boardMap.get(im);
            if (id == vacant) return false;
        }
        return true;
    }

    private boolean wonGame(ArrayList<ImageView> curLine, int turnId){
        ImageView v1= curLine.get(0);
        ImageView v2= curLine.get(1);
        ImageView v3= curLine.get(2);
        int id1=boardMap.get(v1);
        int id2=boardMap.get(v2);
        int id3=boardMap.get(v3);
        if(id1==turnId & id2==turnId & id3==turnId) return true;
        return false;
    }

    // If the over clear the array once restart button is clicked
    private void clearBoard(){
        // ArrayList of ImageViews
        imagesArray=new ArrayList<>();
        imagesArray.add(b1); imagesArray.add(b2); imagesArray.add(b3);
        imagesArray.add(b4); imagesArray.add(b5); imagesArray.add(b6);
        imagesArray.add(b7); imagesArray.add(b8); imagesArray.add(b9);
        rootsChildrenScores.clear();
        for(ImageView img: imagesArray){
            img.setImageResource(R.drawable.unmarked1);
        }
        initMap();
        initLines();
        gameStatus.setText("Started!");
    }


    private String itos(ImageView im){
        if(im.equals(b1)) return "b1";
        else if(im.equals(b2)) return "b2";
        else if(im.equals(b3)) return "b3";
        else if(im.equals(b4)) return "b4";
        else if(im.equals(b5)) return "b5";
        else if(im.equals(b6)) return "b6";
        else if(im.equals(b7)) return "b7";
        else if(im.equals(b8)) return "b8";
        else if(im.equals(b9)) return "b9";
        return "None";
    }
}
///////////////////////////////// END OF SOURCE FILE  ///////////////////////////////////////////////