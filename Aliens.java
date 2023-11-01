import java.awt.Rectangle;

public class Aliens{
  int xPos = 0, yPos = 0, xSpeed = 10;
  Rectangle boundary;
  
  public void place(int x, int y)
  {
    xPos = x*60;
    yPos = (y*45)+60;
    boundary=new Rectangle(xPos,yPos,30,30);
  }
  
  public void move()
  {
    xPos += xSpeed;
    boundary.setLocation(xPos,yPos);
  }
}