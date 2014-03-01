package it.Maida.Flows.DLVhandling;


public class DlvInvoker {

	private DLVCommand dlvCommand;
	
	public DlvInvoker(DLVCommand dlvCommand) {
		this.dlvCommand = dlvCommand;
	}
	
	public void solve() {
		dlvCommand.execute();
	}
	
}
