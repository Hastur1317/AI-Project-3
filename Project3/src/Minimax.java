import java.util.ArrayList;

public class Minimax
{
	private State head;
	
	public Minimax(int meX, int meY, int opX, int opY)
	{
		head = new State(null, meX, meY, opX, opY);
	}
	
	public void turn(State move)
	{
		head = new State(move.getBoard(), move.meX(), move.meY(), move.opX(), move.opY());
	}
	
	public State turn(int depth, long limit)
	{
		long start = System.currentTimeMillis();
		
		head.generateChildrenMe(depth,limit, start);
		State move = head.minimax();
		
		if(move == null)
			return move;
		head = new State(move.getBoard(), move.meX(), move.meY(), move.opX(), move.opY());
		
		return head;
	}
	
	public String getString()
	{
		return head.board();
	}
}

class State
{
	private int meX;
	private int meY;
	private int opX;
	private int opY;
	private boolean[][] board;
	private ArrayList<State> children;
	private int value;
	
	public State(boolean[][] board, int meX, int meY, int opX, int opY)
	{
		this.board = new boolean[8][8];
		
		if(board != null)
		{
			for(int x = 0; x<8; x++)
			{
				for(int y = 0; y<8; y++)
				{
					this.board[x][y] = board[x][y];
				}
			}
		}
		
		this.board[meX][meY] = true;
		this.board[opX][opY] = true;
		this.meX = meX;
		this.meY = meY;
		this.opX = opX;
		this.opY = opY;
	}
	
	public void makeMove(int meX, int meY, int opX, int opY)
	{
		this.meX = meX;
		this.meX = meY;
		this.opX = opX;
		this.opY = opY;
		this.board[meX][meY] = true;
		this.board[opX][opY] = true;
	}
	
	public boolean[][] getBoard()
	{
		return board;
	}
	
	public String board()
	{
		String s = "    1    2    3    4    5    6    7    8\n";
		
		for(int y=0; y<8; y++)
		{
			s= s + getLetter(y) + "    ";
			for(int x =0; x<8; x++)
			{
				if(x == meX && y == meY)
				{
					s = s + "X    ";
				}
				else if(x == opX && y == opY)
				{
					s = s + "O    ";
				}
				else if(!board[x][y])
				{
					s = s + "-    ";
				}
				else
				{
					s = s + "#    ";
				}
			}
			s = s + "\n";
		}
		
		return s;
	}
	
	private String getLetter(int x)
	{
		switch(x)
		{
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		}
		return null;
	}
	
	public State minimax()
	{
		//make all states have value
		for(int x =0; x<children.size(); x++)
		{
			children.get(x).min(Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		State best = null;
		for(int x=0; x<children.size(); x++)
		{
			if(best == null)
			{
				best = children.get(x);
			}
			else
			{
				if(best.getValue() < children.get(x).getValue())
				{
					best = children.get(x);
				}
				else if(best.getValue() == children.get(x).getValue())
				{
					if(Math.random() > 0.5)
					{
						best = children.get(x);
					}
				}
			}
		}
		
		return best;
	}
	
	private int max(int alpha, int beta)
	{
		value = Integer.MIN_VALUE;
		if(children == null)
		{
			return stateValue(beta, false);
		}
		if(children.size() == 0)
		{
			return stateValue(beta, false);
		}
		else
		{
			for(int x=0; x<children.size(); x++)
			{
				int store = children.get(x).min(alpha, beta);
				if(store > alpha)
					alpha = store;
				if(children.get(x).getValue() > value)
				{
					value = children.get(x).getValue();
				}
				else if(children.get(x).getValue() == value)
				{
					if(Math.random() > 0.5)
					{
						value = children.get(x).getValue();
					}
				}
			}
			return value;
		}
	}
	
	private int min(int alpha, int beta)
	{
		value = Integer.MAX_VALUE;
		if(children == null)
		{
			return stateValue(alpha, true);
		}
		if(children.size() == 0)
		{
			return stateValue(alpha, true);
		}
		else
		{
			for(int x=0; x<children.size(); x++)
			{
				int store = children.get(x).max(alpha, beta);
				if(store < beta)
					beta = store;
				if(children.get(x).getValue() < value)
				{
					value = children.get(x).getValue();
				}
				else if(children.get(x).getValue() == value)
				{
					if(Math.random() > 0.5)
					{
						value = children.get(x).getValue();
					}
				}
			}
			return value;
		}
	}

	public void setValue(int x)
	{
		value = x;
	}
	
	public int getValue()
	{
		return value;
	}

	public ArrayList<State> getChildren()
	{
		return children;
	}

	public int meX()
	{
		return meX;
	}
	
	public int meY()
	{
		return meY;
	}
	
	public int opX()
	{
		return opX;
	}
	
	public int opY()
	{
		return opY;
	}
	
	public void generateChildrenMe(int depth, long limit, long start)
	{
		if(depth <= 0)
		{
			return;
		}
		
		if(System.currentTimeMillis() - start >= limit)
			return;
		
		children = new ArrayList<>();
		
				             //North  NE     East   SE     South  SW     West   NW     ALL
		boolean[] deadEnd = {false, false, false, false, false, false, false, false, false};
		int count =0;
		while(!deadEnd[8])
		{
			count++;
			////////////////////////////// AI
			//North
			if(!deadEnd[0])
			{
				if((meY - count) >=0)
				{
					if(!board[meX][meY-count])
					{
						children.add(new State(board, meX, meY-count, opX, opY));
					}
					else
					{
						deadEnd[0] = true;
					}
				}
				else
				{
					deadEnd[0] = true;
				}
			}
	
			//NorthEast
			if(!deadEnd[1])
			{
				if((meX + count) <8 && (meY - count)>=0)
				{
					if(!board[meX + count][meY - count])
					{
						children.add(new State(board, meX + count, meY - count, opX, opY));
					}
					else
					{
						deadEnd[1] = true;
					}
				}
				else
				{
					deadEnd[1] = true;
				}
			}
	
			//East
			if(!deadEnd[2])
			{
				if((meX + count) <8)
				{
					if(!board[meX + count][meY])
					{
						children.add(new State(board, meX + count, meY, opX, opY));
					}
					else
					{
						deadEnd[2] = true;
					}
				}
				else
				{
					deadEnd[2] = true;
				}
			}
	
			//SouthEast
			if(!deadEnd[3])
			{
				if(meX + count <8 && meY + count<8)
				{
					if(!board[meX + count][meY + count])
					{
						children.add(new State(board, meX + count, meY + count, opX, opY));
					}
					else
					{
						deadEnd[3] = true;
					}
				}
				else
				{
					deadEnd[3] = true;
				}
			}
	
			//South
			if(!deadEnd[4])
			{
				if(meY + count<8)
				{
					if(!board[meX][meY + count])
					{
						children.add(new State(board, meX, meY + count, opX, opY));
					}
					else
					{
						deadEnd[4] = true;
					}
				}
				else
				{
					deadEnd[4] = true;
				}
			}
	
			//SouthWest
			if(!deadEnd[5])
			{
				if(meX - count >=0 && meY + count<8)
				{
					if(!board[meX - count][meY + count])
					{
						children.add(new State(board, meX - count, meY + count, opX, opY));
					}
					else
					{
						deadEnd[5] = true;
					}
				}
				else
				{
					deadEnd[5] = true;
				}
			}
	
			//West
			if(!deadEnd[6])
			{
				if(meX - count>=0)
				{
					if(!board[meX - count][meY])
					{
						children.add(new State(board, meX - count, meY, opX, opY));
					}
					else
					{
						deadEnd[6] = true;
					}
				}
				else
				{
					deadEnd[6] = true;
				}
			}
	
			//Northwest
			if(!deadEnd[7])
			{
				if(meX - count >=0 && meY - count>=0)
				{
					if(!board[meX - count][meY - count])
					{
						children.add(new State(board, meX - count, meY - count, opX, opY));
					}
					else
					{
						deadEnd[7] = true;
					}
				}
				else
				{
					deadEnd[7] = true;
				}
			}
	
			deadEnd[8] = deadEnd[0] && deadEnd[1] && deadEnd[2] && deadEnd[3] && deadEnd[4] && deadEnd[5] && deadEnd[6] && deadEnd[7];
		}
		
		for(int x =0; x<children.size(); x++)
		{
			children.get(x).generateChildrenOp(depth - 1, limit, start);
		}

	}
	
	public void generateChildrenOp(int depth, long limit, long start)
	{
		if(depth <= 0)
		{
			return;
		}
		
		children = new ArrayList<>();
		
		//North  NE     East   SE     South  SW     West   NW     ALL
		boolean[] deadEndOpponent = {false, false, false, false, false, false, false, false, false};
		int count =0;
		while(!deadEndOpponent[8])
		{
			count++;
			/////////////////////////////Opponent
			//North
			if(!deadEndOpponent[0])
			{
				if(opY - count >=0)
				{
					if(!board[opX][opY - count])
					{
						children.add(new State(board, meX, meY, opX, opY - count));
					}
					else
					{
						deadEndOpponent[0] = true;
					}
				}
				else
				{
					deadEndOpponent[0] = true;
				}
			}

			//NorthEast
			if(!deadEndOpponent[1])
			{
				if(opX + count <8 && opY - count>=0)
				{
					if(!board[opX + count][opY - count])
					{
						children.add(new State(board, meX, meY, opX + count, opY - count));
					}
					else
					{
						deadEndOpponent[1] = true;
					}
				}
				else
				{
					deadEndOpponent[1] = true;
				}
			}

			//East
			if(!deadEndOpponent[2])
			{
				if(opX + count <8)
				{
					if(!board[opX + count][opY])
					{
						children.add(new State(board, meX, meY, opX + count, opY));
					}
					else
					{
						deadEndOpponent[2] = true;
					}
				}
				else
				{
					deadEndOpponent[2] = true;
				}
			}

			//SouthEast
			if(!deadEndOpponent[3])
			{
				if(opX + count <8 && opY + count<8)
				{
					if(!board[opX + count][opY + count])
					{
						children.add(new State(board, meX, meY, opX + count, opY + count));
					}
					else
					{
						deadEndOpponent[3] = true;
					}
				}
				else
				{
					deadEndOpponent[3] = true;
				}
			}
			
			//South
			if(!deadEndOpponent[4])
			{
				if(opY + count<8)
				{
					if(!board[opX][opY + count])
					{
						children.add(new State(board, meX, meY, opX, opY + count));
					}
					else
					{
						deadEndOpponent[4] = true;
					}
				}
				else
				{
					deadEndOpponent[4] = true;
				}
			}
			
			//SouthWest
			if(!deadEndOpponent[5])
			{
				if(opX - count >=0 && opY + count<8)
				{
					if(!board[opX - count][opY + count])
					{
						children.add(new State(board, meX, meY, opX - count, opY + count));
					}
					else
					{
						deadEndOpponent[5] = true;
					}
				}
				else
				{
					deadEndOpponent[5] = true;
				}
			}
			
			//	West
			if(!deadEndOpponent[6])
			{
				if(opX - count>=0)
				{
					if(!board[opX - count][opY])
					{
						children.add(new State(board, meX, meY, opX - count, opY));
					}
					else
					{
						deadEndOpponent[6] = true;
					}
				}
				else
				{
					deadEndOpponent[6] = true;
				}
			}
			
			//Northwest
			if(!deadEndOpponent[7])
			{
				if(opX - count >=0 && opY - count>=0)
				{
					if(!board[opX - count][opY - count])
					{	
						children.add(new State(board, meX, meY, opX - count, opY - count));
					}
					else
					{
						deadEndOpponent[7] = true;
					}	
				}
				else
				{
					deadEndOpponent[7] = true;
				}
			}
			
			deadEndOpponent[8] = deadEndOpponent[0] && deadEndOpponent[1] && deadEndOpponent[2] && deadEndOpponent[3] && deadEndOpponent[4] && deadEndOpponent[5] && deadEndOpponent[6] && deadEndOpponent[7];
		}
		
		for(int x =0; x<children.size(); x++)
		{
			children.get(x).generateChildrenMe(depth - 1, limit, start);
		}

	}
	
	public int stateValue(int test, boolean max)
	{
						   //North  NE     East   SE     South  SW     West   NW     ALL
		boolean[] deadEnd = {false, false, false, false, false, false, false, false, false};
		boolean[] deadEndOpponent = {false, false, false, false, false, false, false, false, false};
		int count =0;
		int sum = 0;
		int diff = 0;
		while(!deadEnd[8] || !deadEndOpponent[8])
		{
			count++;
			////////////////////////////// AI
			//North
			if(!deadEnd[0])
			{
				if(meY - count >=0)
				{
					if(!board[meX][meY-count])
					{
						sum++;
					}
					else
					{
						deadEnd[0] = true;
					}
				}
				else
				{
					deadEnd[0] = true;
				}
			}
			
			//NorthEast
			if(!deadEnd[1])
			{
				if(meX + count <8 && meY - count>=0)
				{
					if(!board[meX + count][meY - count])
					{
						sum++;
					}
					else
					{
						deadEnd[1] = true;
					}
				}
				else
				{
					deadEnd[1] = true;
				}
			}
			
			//East
			if(!deadEnd[2])
			{
				if(meX + count <8)
				{
					if(!board[meX + count][meY])
					{
						sum++;
					}
					else
					{
						deadEnd[2] = true;
					}
				}
				else
				{
					deadEnd[2] = true;
				}
			}
			
			//SouthEast
			if(!deadEnd[3])
			{
				if(meX + count <8 && meY + count<8)
				{
					if(!board[meX + count][meY + count])
					{
						sum++;
					}
					else
					{
						deadEnd[3] = true;
					}
				}
				else
				{
					deadEnd[3] = true;
				}
			}
			
			//South
			if(!deadEnd[4])
			{
				if(meY + count<8)
				{
					if(!board[meX][meY + count])
					{
						sum++;
					}
					else
					{
						deadEnd[4] = true;
					}
				}
				else
				{
					deadEnd[4] = true;
				}
			}
			
			//SouthWest
			if(!deadEnd[5])
			{
				if(meX - count >=0 && meY + count<8)
				{
					if(!board[meX - count][meY + count])
					{
						sum++;
					}
					else
					{
						deadEnd[5] = true;
					}
				}
				else
				{
					deadEnd[5] = true;
				}
			}
			
			//West
			if(!deadEnd[6])
			{
				if(meX - count>=0)
				{
					if(!board[meX - count][meY])
					{
						sum++;
					}
					else
					{
						deadEnd[6] = true;
					}
				}
				else
				{
					deadEnd[6] = true;
				}
			}
			
			//Northwest
			if(!deadEnd[7])
			{
				if(meX - count >=0 && meY - count>=0)
				{
					if(!board[meX - count][meY - count])
					{
						sum++;
					}
					else
					{
						deadEnd[7] = true;
					}
				}
				else
				{
					deadEnd[7] = true;
				}
			}
			
			deadEnd[8] = deadEnd[0] && deadEnd[1] && deadEnd[2] && deadEnd[3] && deadEnd[4] && deadEnd[5] && deadEnd[6] && deadEnd[7];
			
			/////////////////////////////Opponent
			//North
			if(!deadEndOpponent[0])
			{
				if(opY - count >=0)
				{
					if(!board[opX][opY - count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[0] = true;
					}
				}
				else
				{
					deadEndOpponent[0] = true;
				}
			}
			
			//NorthEast
			if(!deadEndOpponent[1])
			{
				if(opX + count <8 && opY - count>=0)
				{
					if(!board[opX + count][opY - count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[1] = true;
					}
				}
				else
				{
					deadEndOpponent[1] = true;
				}
			}
			
			//East
			if(!deadEndOpponent[2])
			{
				if(opX + count <8)
				{
					if(!board[opX + count][opY])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[2] = true;
					}
				}
				else
				{
					deadEndOpponent[2] = true;
				}
			}
			
			//SouthEast
			if(!deadEndOpponent[3])
			{
				if(opX + count <8 && opY + count<8)
				{
					if(!board[opX + count][opY + count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[3] = true;
					}
				}
				else
				{
					deadEndOpponent[3] = true;
				}
			}
			
			//South
			if(!deadEndOpponent[4])
			{
				if(opY + count<8)
				{
					if(!board[opX][opY + count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[4] = true;
					}
				}
				else
				{
					deadEndOpponent[4] = true;
				}
			}
			
			//SouthWest
			if(!deadEndOpponent[5])
			{
				if(opX - count >=0 && opY + count<8)
				{
					if(!board[opX - count][opY + count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[5] = true;
					}
				}
				else
				{
					deadEndOpponent[5] = true;
				}
			}
			
			//West
			if(!deadEndOpponent[6])
			{
				if(opX - count>=0)
				{
					if(!board[opX - count][opY])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[6] = true;
					}
				}
				else
				{
					deadEndOpponent[6] = true;
				}
			}
			
			//Northwest
			if(!deadEndOpponent[7])
			{
				if(opX - count >=0 && opY - count>=0)
				{
					if(!board[opX - count][opY - count])
					{
						diff--;
					}
					else
					{
						deadEndOpponent[7] = true;
					}
				}
				else
				{
					deadEndOpponent[7] = true;
				}
			}
			
			deadEndOpponent[8] = deadEndOpponent[0] && deadEndOpponent[1] && deadEndOpponent[2] && deadEndOpponent[3] && deadEndOpponent[4] && deadEndOpponent[5] && deadEndOpponent[6] && deadEndOpponent[7];
			
			if(max)
			{
				if(sum + diff < test)
				{
					value = Integer.MIN_VALUE;
					return value;
				}
			}
			else
			{
				if(sum + diff > test)
				{
					value = Integer.MAX_VALUE;
					return value;
				}
			}
		}
		
		if(diff == 0)
		{
			value = Integer.MAX_VALUE;
		}
		else if(sum == 0)
		{
			value = Integer.MIN_VALUE;
		}
		else
		{
			value = sum + diff;
		}
		
		return value;
	}
	
}