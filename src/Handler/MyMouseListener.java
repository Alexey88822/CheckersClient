package Handler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.border.Border;

import Model.Square;
import View.SquarePanel;

public class MyMouseListener extends MouseAdapter{
	
	private SquarePanel squarePanel;
	private Controller controller;
	
	public void setController(Controller c){
		this.controller = c;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		
		try{			
			if(controller.isHisTurn()){
				ToggleSelectPiece(e);
			}else{
				JOptionPane.showMessageDialog(null, "Не ваша очередь",
					"Ошибка", JOptionPane.ERROR_MESSAGE, null);
			}
		}catch(Exception ex){
			System.out.println("Ошибка");
		}	
		
		
	}
	
	private void ToggleSelectPiece(MouseEvent e){
		try{
			squarePanel = (SquarePanel) e.getSource();
			Square s = squarePanel.getSquare();
			
			//if square is already selected - deselect
			if(s.isSelected()){
				System.out.println("Отмена выбора квадрата - "+s.getSquareID());
				controller.squareDeselected();				
			}
			//else select
			else{
				System.out.println("Выбран квадрат - "+s.getSquareID());
				controller.squareSelected(s);
			}
		}catch(Exception ex){
			System.out.println("Ошибка");
		}
	}
}
