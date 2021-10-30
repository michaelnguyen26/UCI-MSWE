import java.util.concurrent.Semaphore;

public class JDisplay2 implements HighLevelDisplay {

    private JDisplay d;
    private String [] text;
    private  int usedRows;
	Semaphore lockSemaphore = new Semaphore(1); //create a semaphore with 1 permit

    public JDisplay2(){
	d = new JDisplay();
	text = new String [100];
	clear();
    }

    private void updateRow(int row, String str) {
	text[row] = str;
	if (row < d.getRows()) {
	    for(int i=0; i < str.length(); i++)
		d.write(row,i,str.charAt(i));
	    for(int i=str.length(); i < d.getCols(); i++)
		d.write(row,i,' ');
	}
    }

    private void flashRow(int row, int millisecs) {
	String txt = text[row];
	try {
	    for (int i= 0; i * 200 < millisecs; i++) {
		updateRow(row,"");
		Thread.sleep(70);
		updateRow(row,txt);
		Thread.sleep(130);
	    }
	} catch (Exception e) {
	    System.err.println(e.getMessage());
	}
	
    }

    public void clear() {
	for(int i=0; i < d.getRows(); i++)
	    updateRow(i,"");
	usedRows = 0;
    }

    public void addRow(String str) {
		try{
			lockSemaphore.acquire(); //addRow acquires the lock so another thread cant access it
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		updateRow(usedRows,str);
		flashRow(usedRows,1000);
		usedRows++;
		lockSemaphore.release(); //realease the lock
    }

    public void deleteRow(int row) {
		try{
			lockSemaphore.acquire(); //deleteRow acquires the lock so another thread cant access it
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		if (row < usedRows) {
			for(int i = row+1; i < usedRows; i++)
			updateRow(i-1,text[i]);
			usedRows--;
			updateRow(usedRows,"");
			if(usedRows >= d.getRows())
			flashRow(d.getRows()-1,1000);
		}
		lockSemaphore.release(); //release the lock
    }
}