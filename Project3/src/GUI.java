import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class GUI implements ActionListener{

	private JFrame frmIsolation;
	private JTextField txtMove;
	private boolean turnCounter = true;
	private Minimax ai1;
	private Minimax ai2;
	private JTextArea txtBoard;
	private JTextArea txtMoves;
	private JButton btnMove;
	private JButton btnStart;
	private long time;
	private int turn = 1;
	private JMenuItem btnRestart;
	private JRadioButtonMenuItem firstG;
	private JRadioButtonMenuItem firstB;
	private JRadioButtonMenuItem firstP;
	private JRadioButtonMenuItem secG;
	private JRadioButtonMenuItem secB;
	private JRadioButtonMenuItem secP;
	private JSpinner timeLimit;
	private State currentBoard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmIsolation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		currentBoard = new State(null, 0, 0, 7, 7);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIsolation = new JFrame();
		frmIsolation.setTitle("ISOLATION");
		frmIsolation.setBounds(100, 100, 800, 500);
		frmIsolation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIsolation.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 341, 296);
		frmIsolation.getContentPane().add(scrollPane);
		
		txtBoard = new JTextArea();
		txtBoard.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtBoard.setEditable(false);
		scrollPane.setViewportView(txtBoard);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(433, 36, 341, 296);
		frmIsolation.getContentPane().add(scrollPane_1);
		
		txtMoves = new JTextArea();
		txtMoves.setEditable(false);
		txtMoves.setText("First vs Second\r\n");
		scrollPane_1.setViewportView(txtMoves);
		
		txtMove = new JTextField();
		txtMove.setEnabled(false);
		txtMove.setBounds(290, 378, 86, 20);
		frmIsolation.getContentPane().add(txtMove);
		txtMove.setColumns(10);
		
		btnMove = new JButton("Move");
		btnMove.setEnabled(false);
		btnMove.addActionListener(this);
		btnMove.setBounds(386, 377, 89, 23);
		frmIsolation.getContentPane().add(btnMove);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(628, 377, 89, 23);
		frmIsolation.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		
		timeLimit = new JSpinner();
		timeLimit.setModel(new SpinnerNumberModel(new Integer(60), new Integer(0), null, new Integer(1)));
		timeLimit.setBounds(554, 378, 64, 20);
		frmIsolation.getContentPane().add(timeLimit);
		
		JMenuBar menuBar = new JMenuBar();
		frmIsolation.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		btnRestart = new JMenuItem("Restart");
		file.add(btnRestart);
		btnRestart.addActionListener(this);
		
		JMenu menuFirst = new JMenu("First Player");
		file.add(menuFirst);
		
		ButtonGroup first = new ButtonGroup();
		
		firstG = new JRadioButtonMenuItem("Good Ai");
		firstG.setSelected(true);
		menuFirst.add(firstG);
		
		firstB = new JRadioButtonMenuItem("Bad Ai");
		menuFirst.add(firstB);
		
		firstP = new JRadioButtonMenuItem("Player");
		menuFirst.add(firstP);
		
		JMenu menuSec = new JMenu("Second Player");
		file.add(menuSec);
		first.add(firstG);
		first.add(firstB);
		first.add(firstP);
		
		
		ButtonGroup second = new ButtonGroup();
		
		secG = new JRadioButtonMenuItem("Good Ai");
		menuSec.add(secG);
		
		secB = new JRadioButtonMenuItem("Bad Ai");
		menuSec.add(secB);
		
		secP = new JRadioButtonMenuItem("Player");
		secP.setSelected(true);
		menuSec.add(secP);
		second.add(secG);
		second.add(secB);
		second.add(secP);
	}

	@Override
	public void actionPerformed(ActionEvent arg)
	{
		switch(arg.getActionCommand())
		{
		case "Start":
			start();
			break;
		case "Restart":
			reset();
			break;
		case "Move":
			move();
			break;
		}
	}
	
	private void move()
	{
		if(turnCounter)
		{
			State move = firstPerson();
			if(move == null)
			{
				endGame(false);
				return;
			}
			currentBoard = move;
			txtMoves.append("\t" + turn + ". " + cord(move.meX(), move.meY()));
			turn++;
			turnCounter = false;
		}
		else
		{
			State move = secondPerson();
			if(move == null)
			{
				endGame(true);
				return;
			}
			currentBoard = new State(move.getBoard(), move.opX(), move.opY(), move.meX(), move.meY());
			txtMoves.append("\t" + cord(move.meX(), move.meY()) + "\n");
			turnCounter = true;
		}
		
		txtBoard.setText(currentBoard.board());
	}

	private State secondPerson()
	{
		State move = null;
		if(secG.isSelected())
		{
			ai2.turn(new State(currentBoard.getBoard(), currentBoard.opX(), currentBoard.opY(), currentBoard.meX(), currentBoard.meY()));
			move = ai2.turn(6, time);
		}else if(secB.isSelected())
		{
			ai2.turn(new State(currentBoard.getBoard(), currentBoard.opX(), currentBoard.opY(), currentBoard.meX(), currentBoard.meY()));
			move = ai2.turn(2, time);
		}
		else
		{
			int[] x = playerMove();
			move = new State(currentBoard.getBoard(), x[0], x[1], currentBoard.meX(), currentBoard.meY());
		}
		
		return move;
	}

	private State firstPerson()
	{
		State move = null;
		if(firstG.isSelected())
		{
			ai1.turn(currentBoard);
			move = ai1.turn(5, time);
		}else if(firstB.isSelected())
		{
			ai1.turn(currentBoard);
			move = ai1.turn(2, time);
		}
		else
		{
			int[] x = playerMove();
			move = new State(currentBoard.getBoard(), x[0], x[1], currentBoard.opX(), currentBoard.opY());
		}
		
		return move;
	}

	private int[] playerMove()
	{
		int[] x = new int[2];
		String[] s = txtMove.getText().split(" ");
		switch(s[0])
		{
		case "a":
			x[1] = 0;
			break;
		case "b":
			x[1] = 1;
			break;
		case "c":
			x[1] = 2;
			break;
		case "d":
			x[1] = 3;
			break;
		case "e":
			x[1] = 4;
			break;
		case "f":
			x[1] = 5;
			break;
		case "g":
			x[1] = 6;
			break;
		case "h":
			x[1] = 7;
			break;
		}
		x[0] = Integer.parseInt(s[1]) - 1;
		
		return x;
	}

	private void reset()
	{
		btnMove.setEnabled(false);
		btnStart.setEnabled(true);
		txtMove.setEnabled(false);
		firstG.setSelected(true);
		firstB.setSelected(true);
		firstP.setSelected(true);
		secG.setSelected(true);
		secB.setSelected(true);
		secP.setSelected(true);
		txtBoard.setText("");
		turn = 1;
		turnCounter = true;
		txtMoves.setText("First vs. Second\n");
		timeLimit.setEnabled(true);
	}

	private void start()
	{
		btnMove.setEnabled(true);
		btnStart.setEnabled(false);
		txtMove.setEnabled(true);
		firstG.setSelected(false);
		firstB.setSelected(false);
		firstP.setSelected(false);
		secG.setSelected(false);
		secB.setSelected(false);
		secP.setSelected(false);
		int store = (int) timeLimit.getValue();
		time = (long) (store * 1000);
		timeLimit.setEnabled(false);
		if(firstG.isSelected() || firstB.isSelected())
		{
			ai1 = new Minimax(0, 0, 7, 7);
		}
		if(secG.isSelected() || secB.isSelected())
		{
			ai2 = new Minimax(7, 7, 0, 0);
		}
	}

	private String cord(int x, int y)
	{
		String c = "";
		
		switch(y)
		{
		case 0:
			c = "A" + (x+1);
			break;
		case 1:
			c = "B" + (x+1);
			break;
		case 2:
			c = "C" + (x+1);
			break;
		case 3:
			c = "D" + (x+1);
			break;
		case 4:
			c = "E" + (x+1);
			break;
		case 5:
			c = "F" + (x+1);
			break;
		case 6:
			c = "G" + (x+1);
			break;
		case 7:
			c = "H" + (x+1);
			break;
		}
		
		return c;
	}
	
	private void endGame(boolean b)
	{
		if(b)
		{
			txtBoard.append("\nFirst player Wins");
		}
		else
		{
			txtBoard.append("\nSecond player Wins");
		}
		
		
	}
}
