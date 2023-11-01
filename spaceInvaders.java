//space invaders
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.Rectangle;

public class spaceInvaders extends JFrame implements Runnable, KeyListener{ 
  //Variable and GUIObject Declaration area 
  int thCount =0;
  MyDrawPanel draw1=new MyDrawPanel();
  
  Ship ship = new Ship();
  Rectangle shipHitbox;
  
  BufferedImage img=null;
  int switchImg = 1;
  Aliens [] aliens = new Aliens[55];
  int [] alienHit = new int[55];
  int alienCount=0;
  
  Random rand = new Random();
  int randNum, shootThCount;
  boolean alienShooting = false;
  Rectangle alienMis;
  int alMis_yPos,alMis_xPos, alMis_speed=1;
  
  int missile_yPos = 630;
  int missile_xPos = ship.xPos+22;
  Rectangle missileBound;
  boolean missilePressed = false;
  
  int score = 0, lives = 3;
  Font font= new Font("Agency FB", Font.PLAIN, 35);
  Font title= new Font("Agency FB", Font.BOLD, 35);
  
  
  public static void main(String[] args) 
  {
    new spaceInvaders();
  }  
  
  public spaceInvaders (){
//    Create object and your code goes here
    Thread th = new Thread (this); 
    th.start (); 
    
    
    for(int a=0; a<55; a++){
      aliens[a] = new Aliens();
    }
    positioning();
    
    try {
      img = ImageIO.read(new File("invaders.png"));
    } catch (IOException e) {
      System.out.println("Problem loading image");
    }
    
    this.add(draw1);
    this.setSize(800,710);
    this.setVisible(true);
    addKeyListener(this);
  }
  
  public void positioning(){
    for(int a=0; a<55; a++){
        for(int r=0; r<5; r++){
          for(int c=0; c<11; c++){
            aliens[r*11+c].place(c,r);
          }
        }
    }
  }
  
  
  public void run(){
    while(true){
      shipHitbox = new Rectangle(ship.xPos, 630, 50, 50);
      missileBound = new Rectangle(missile_xPos, missile_yPos, 10, 30);
      alienMis = new Rectangle(aliens[randNum].xPos + 12, alMis_yPos, 5, 20);
      
      ship.move(); 
      
      //moving aliens
      if(thCount == 400)
      {
        int downFlag=0;
        for (int a=0; a<55; a++){
          aliens[a].move();
          if(switchImg == 2)
            {
              switchImg = 1;
            }
            else if(switchImg ==1)
            {
              switchImg = 2;
            }
          if((aliens[a].xPos >= 750 || aliens[a].xPos <= 0) && alienHit[a]==0){
            downFlag=1;
          }
        }
        if(downFlag==1)
        {
          for (int a=0; a<55; a++){
            aliens[a].xSpeed *= -1;
            aliens[a].yPos += 30;
          }
        }
        
        thCount = 0;
      }
      
       //when alien's missile colides with the ship
      if (alienMis.intersects(shipHitbox) && alienHit[randNum] == 0 && alienShooting == true){ 
        alienShooting = false;
        lives -=1;
      }
    
      //alien's missile
      if (alienShooting == false)
      {
        randNum = rand.nextInt(54);
        if (alienHit[randNum] == 1){
          randNum = rand.nextInt(54);
        }
        alMis_yPos = aliens[randNum].yPos;
        alMis_xPos=aliens[randNum].xPos + 12;
        alienShooting = true;
      }
      else{
        alMis_yPos +=1;
      }
      if (alMis_yPos >= 700){
        alienShooting = false;
      }
      
      //ship missile movement
      if(missilePressed == true){
        missile_yPos -= 1;
      }
      if(missile_yPos < 0){
        missilePressed = false;
        missile_yPos = 630;
      }
      if(missilePressed == false){
        missile_xPos = ship.xPos+22;
      }
      
      //when missile collide with aliens
      for (int a=0; a<55; a++)
      {
        if (aliens[a].boundary.intersects(missileBound) && alienHit[a]!=1){
          alienHit[a] = 1;
          missilePressed = false;
          missile_yPos = 630;
          alienCount += 1;
          
          if (lives!=0){  
            if(a>=0 && a<=10){
              score += 40;
            }
            else if(a>=11 && a<=32){
              score += 20;
            }
            else if(a>=33 && a<=54){
              score +=10;
            }
          }
        }
      }
      
//      reseting
      if (alienCount == 55){
        for (int a=0; a<55; a++){
          alienHit[a] = 0;
        }
        alienCount = 0;
        positioning();
      }
      
      
      repaint(); 
      thCount += 1;
      try {
        Thread.sleep (3); 
      } 
      catch (InterruptedException ex) {
      } 
    }
  }


  public void keyTyped(KeyEvent e){}  
  public void keyReleased (KeyEvent e){
    ship.speed = 0;
  } 
  public void keyPressed (KeyEvent e){
    int code =e.getKeyCode();
    
    if (code == KeyEvent.VK_LEFT) { 
      ship.speed = -1;
    } 
    else if (code == KeyEvent.VK_RIGHT) {  
      ship.speed = 1;
    }
    if (code == KeyEvent.VK_SPACE) { 
      missilePressed = true;
    }
  }
  
  
  
  class MyDrawPanel extends JPanel
  {
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(Color.BLACK);
      g2.fillRect(0, 0, 800, 700);
      
      g2.setFont(font);
      g2.setColor(Color.WHITE);
      g2.drawString("SCORE: "+score,5,40);
      g2.drawString("LIVES: "+lives,650,40);
      
      g2.setFont(title);
      g2.drawString("SPACE INVADERS",300,40);
      
      //ship
      g2.setColor(Color.GREEN);
      g2.fillRect(ship.xPos+15, 640, 20, 10);
      g2.fillRect(ship.xPos, 650, 50, 20);
      
      //missile
      g2.fillRect(missile_xPos, missile_yPos, 5, 20);
      
      //aliens
      g2.setColor(Color.WHITE);
      for (int a=0; a<55; a++){
        if (alienHit[a] == 0){
          if (switchImg == 1)
          {
            if (a>=0 && a<=10){
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 40, 30, 105, 95,this);
            }
            else if(a>=11 && a<=32)
            {
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 25, 130, 115, 200,this);
            }
            else if(a>=33 && a<=54)
            {
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 10, 240, 110, 305,this);
            }
          }
          if (switchImg == 2)
          {
            if (a>=0 && a<=10){
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 140, 30, 210, 100,this);
            }
            else if(a>=11 && a<=32)
            {
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 130, 135, 225, 200,this);
            }
            else if(a>=33 && a<=54)
            {
              g2.drawImage(img ,aliens[a].xPos, aliens[a].yPos, aliens[a].xPos+30, aliens[a].yPos+30, 130, 245, 225, 305,this);
            }
          }
            
        }
          
        //alien missle
        if (alienHit[randNum] == 0){
          g2.fillRect(alMis_xPos, alMis_yPos , 5, 20);
        }
        
        if(aliens[a].yPos >= 550 || lives < 1){
          g2.drawString("GAME OVER",350,400);
        }
      }
    }
  }
}