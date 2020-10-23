package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shape {
	
	private int color;
	
	private int x, y; 
	
	private long time, lastTime;
	
	private int normal = 600, fast = 60;
	
	private int delay;
	
	private BufferedImage block;
	
	private int[][] coords;
	
	private int[][] reference;
	
	private int deltaX;
	
	private Board board;
	
	private boolean collision = false, moveX = false;
	
	public Shape(int[][] coords, BufferedImage block, Board board, int color){
		this.coords = coords;
		this.block = block;
		this.board = board;
		this.color = color;
		deltaX = 0;
		x = 4;
		y = 0;
		delay = normal;
		time = 0;
		lastTime = System.currentTimeMillis();
		reference = new int[coords.length][coords[0].length];
		
		System.arraycopy(coords, 0, reference, 0, coords.length);
		
	}
	
	public void update(){
		moveX = true;
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		//System.out.println(lasttime);
		if(collision)//edge has detected
		{
			for(int row = 0; row < coords.length; row ++)
			{
				for(int col = 0; col < coords[0].length; col ++)
				{
					if(coords[row][col] != 0)
						board.getBoard()[y + row][x + col] = color;//set values to board array and auto call paintComponent method.
					// coords[row][col]=0;

				}
			}
			//checkline();
			checkLine();
			board.addScore();
			board.setCurrentShape();
		}
		
		if(!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0))
		{
			
			for(int row = 0; row < coords.length; row++)
			{
				for(int col = 0; col < coords[row].length; col ++)
				{
					if(coords[row][col] != 0)
					{
						if(board.getBoard()[y + row][x + deltaX + col] != 0)
						{
							moveX = false;// when found collisions in x direction.
						}
						
					}
				}
			}
			
			if(moveX)//not finding any collision in x direction
				x += deltaX;
			
		}
		
		if(!(y + 1 + coords.length > 20))
		{//edge detecting
			//the shape is at one place up from the down edge
			
			for(int row = 0; row < coords.length; row++)
			{
				for(int col = 0; col < coords[row].length; col ++)
				{
					if(coords[row][col] != 0)
					{
						//check weather the edge of board has fill already 	
						if(board.getBoard()[y + 1 + row][x +  col] != 0)
						{
							collision = true;//call collision method to fix that in the place 
						}
					}
				}
			}
			if(time > delay)
				{
					y++;
					time = 0;
				}
		}//if meet any edge

		else{
			collision = true;//no collision found can move x direction.		
			}
		
		deltaX = 0;
	}
	//drawing shapes	
	public void render(Graphics g){
		
		for(int row = 0; row < coords.length; row ++)
		{
			for(int col = 0; col < coords[0].length; col ++)
			{
				if(coords[row][col] != 0)
				{
					g.drawImage(block, col*30 + x*30, row*30 + y*30, null);	
						
				}
			}		
		}
		
		for(int row = 0; row < reference.length; row ++)
		{
			for(int col = 0; col < reference[0].length; col ++)
			{
				if(reference[row][col] != 0)
				{
					g.drawImage(block, col*30 + 320, row*30 + 160, null);	
				}	
				
			}
				
		}

	}
	//from checkline if the a line fully filled with bloacks sholud remove that line
	private void checkLine(){
		int size = board.getBoard().length - 1;
		
		for(int i = board.getBoard().length - 1; i > 0; i--)
		{
			int count = 0;
			for(int j = 0; j < board.getBoard()[0].length; j++)
			{
				if(board.getBoard()[i][j] != 0)
					count++;
				
				board.getBoard()[size][j] = board.getBoard()[i][j];
			}
			if(count < board.getBoard()[0].length)
				size --;
		}
	}
	
	
	public void rotateShape()
	{
		
		int[][] rotatedShape = null;
		//{{1,1,1,},{1,0,0}} exchange i,j places {{1,1},{1,0},{1,0}}
		rotatedShape = transposeMatrix(coords);
		//{{1,1},{1,0},{1,0}} exchange as {{1,0},{1,0},{1,1}}
		//exchage 1st and 3rd raw except middle raw
		rotatedShape = reverseRows(rotatedShape);
			
		if((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20))
		{
			return;
		}
		//stopping the rotation when at down no space for the rotated shape example space availbale for 3 if a rotation done need 4 space so sholud stop rotate
		for(int row = 0; row < rotatedShape.length; row++)
		{
			for(int col = 0; col < rotatedShape[row].length; col ++)
			{
				if(rotatedShape[row][col] != 0)
				{
					if(board.getBoard()[y + row][x + col] != 0)
					{
						return;
					}
				}
			}
		}
		coords = rotatedShape;
	}

	
    private int[][] transposeMatrix(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                temp[j][i] = matrix[i][j];
        return temp;
    }


private int[][] reverseCols(int[][] matrix){
		
	//	int middle = matrix.length/2;
		
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j=0;j<matrix[0].length;j++) {
			int tempp = matrix[i][0];
					matrix[i][0]=matrix[i][1];
							
				matrix[i][1]=tempp;
			//int[] temp = matrix[0];
			
		//	matrix[i] = matrix[matrix.length - i - 1];
			//matrix[matrix.length - i - 1] = temp;
		}}
		
		return matrix;
		
	} 
    
//raw reverersing
//exchange the raws first ones with last ones.	
	private int[][] reverseRows(int[][] matrix){
		
		int middle = matrix.length/2;
		
		
		for(int i = 0; i < middle; i++)
		{
			int[] temp = matrix[i];
			
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = temp;
		}
		
		return matrix;
		
	}
	
	
	public int getColor(){
		return color;
	}
	
	public void setDeltaX(int deltaX){
		this.deltaX = deltaX;
	}
	
	public void speedUp(){
		delay = fast;
	}
	
	public void speedDown(){
		delay = normal;
	}
	
	public BufferedImage getBlock(){
		return block;
	}
	
	public int[][] getCoords(){
		return coords;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	
	
}
