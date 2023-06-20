import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    int B_Height=600;
    int B_Width=600;
    int MAX_DOTS=1600;
    int DOT_SIZE=10;
    int DOTS;
    int score=0;
    int[] x =new int[MAX_DOTS];
    int[] y =new int[MAX_DOTS];
    int apple_x;
    int apple_y;

    //Images
    Image body,head,apple;
    Timer timer;
    int DELAY = 50;
    Boolean leftDirection=true;
    Boolean rightDirection=false;
    Boolean downDirection=false;
    Boolean upDirection=false;
    boolean inGame=true;
    Board(){
        TAdapter tAdapter=new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_Width,B_Height));
        setBackground(Color.BLACK);
        InitGame();
        loadImages();
    }
    //Initialize game
    public void InitGame(){
        DOTS=3;
        x[0]=250;
        y[0]=250;
        for(int i=1;i<DOTS;i++){
            x[i]=x[0]+DOT_SIZE*i;
            y[i]=y[0];
        }
       loacteApple();
        timer=new Timer(DELAY,this);
        timer.start();

    }
    //Load images from resources folder to objects
    public void loadImages(){
        ImageIcon bodyIcon=new ImageIcon("src/resources/dot.png");
        body=bodyIcon.getImage();
        ImageIcon headIcon=new ImageIcon("src/resources/head.png");
        head=headIcon.getImage();
        ImageIcon appleIcon=new ImageIcon("src/resources/apple.png");
        apple=appleIcon.getImage();
    }

    //Draw images of snakes and apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrwaing(g);

    }
    //Draw image
    public void doDrwaing(Graphics g){
        if(inGame){
            g.drawImage(apple,apple_x,apple_y,this);
            for(int i=0;i<DOTS;i++){
                if(i==0){
                    g.drawImage(head, x[0], y[0],this);
                }
                else
                    g.drawImage(body,x[i],y[i],this);
            }
        }
        else{
            gameOver(g);
            timer.stop();
        }
    }
    //Randomize apple's position
    public void loacteApple(){
        apple_x=((int)(Math.random()*39))*DOT_SIZE;
        apple_y=((int)(Math.random()*39))*DOT_SIZE;
    }
    //Check collitions with Border
    public void checkCollision(){
        //collision with body
        for(int i=1;i<DOTS;i++){
            if(i>3 && x[0]==x[i] && y[0]==y[i]){
                inGame=false;
            }
            //collision with border
            if(x[0]<0){
                inGame=false;
            }
            if(x[0]>=B_Width){
                inGame=false;
            }
            if(y[0]<0){
                inGame=false;
            }
            if(y[0]>=B_Height){
                inGame=false;
            }
        }
    }
    public void restart(){

        inGame=true;
        x[0]=250;
        y[0]=250;
        DOTS=3;
        score=0;
        leftDirection=true;
        rightDirection=false;
        upDirection=false;
        downDirection=false;
        timer.start();
        repaint();
    }
    //Game over msg
    public void gameOver(Graphics g){
        String msg="Game Over";
        score=(DOTS-3)*100;
        String scoremsg="Score "+ score;
        String regame="Press SPACE to restart";
        Font small = new Font("Roboto",Font.BOLD,14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.drawString(scoremsg, (B_Width-fontMetrics.stringWidth(scoremsg))/2,B_Height/4);
        g.drawString(regame, (B_Width-fontMetrics.stringWidth(regame))/2,3*B_Height/4);
        g.drawString(msg,(B_Width-fontMetrics.stringWidth(msg))/2,2*B_Height/4);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    //Make snake move
    public void move(){
        for(int i=DOTS-1;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0]-=DOT_SIZE;
        }
        if(rightDirection){
            x[0]+=DOT_SIZE;
        }
        if(upDirection){
            y[0]-=DOT_SIZE;
        }
        if(downDirection){
            y[0]+=DOT_SIZE;
        }
    }
    //Make Snake eat food
    public void checkApple(){
        if(apple_x==x[0] && apple_y==y[0]){
            DOTS++;
            loacteApple();
        }
    }
    //implement controls
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if(!inGame){
                if(key==KeyEvent.VK_SPACE) {
                    restart();
                }
            }
            if(key==KeyEvent.VK_LEFT && !rightDirection){
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_UP && !downDirection){
                leftDirection=false;
                upDirection=true;
                rightDirection=false;
            }
            if(key==KeyEvent.VK_DOWN && !upDirection){
                leftDirection=false;
                rightDirection=false;
                downDirection=true;
            }
        }
    }
}
