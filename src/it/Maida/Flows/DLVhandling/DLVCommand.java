package it.Maida.Flows.DLVhandling;


public class DLVCommand implements Command{

	DLVSolver dlvSolver;
	
	public DLVCommand(DLVSolver dlvSolver) {
		this.dlvSolver = dlvSolver;
	}
	
	@Override
	public void execute() {
		dlvSolver.solve();
	}

}
