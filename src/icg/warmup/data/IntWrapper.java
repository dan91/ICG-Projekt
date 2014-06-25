package icg.warmup.data;

public class IntWrapper{
	public int value;
	public IntWrapper(int val){ 
		value = val; 
	}
	public boolean gt(IntWrapper that){ return value > that.value; }
	public boolean lt(IntWrapper that){ return value < that.value; }
	public int sub(IntWrapper that)	  {	return value - that.value; }
	public void swap(IntWrapper that){
		int tmp = this.value;
		this.value = that.value;
		that.value = tmp;
	}		
}