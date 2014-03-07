package it.Maida.Flows.DLVhandling;

import it.Maida.Flows.Core.FlowStruct;

import java.awt.Point;
import java.io.File;
import java.util.HashSet;

import javax.swing.JOptionPane;

import DLV.DLVInvocationException;
import DLV.DlvHandler;
import DLV.Model;
import DLV.Predicate;
import DLV.Predicate.Literal;
import DLV.Program;

public class DLVSolver {

	private Program program;
	FlowStruct flowStruct;
	private HashSet<String> colorsAdded;

	public DLVSolver(FlowStruct flowStruct) {
		this.flowStruct = flowStruct;
		this.program = new Program();
		this.colorsAdded = new HashSet();

	}

	public void solve() {

		int sizeOfMatrix = flowStruct.getSize();

		program.addProgramFile("DLV" + File.separator + "FlowSolver"
				+ File.separator + "Flow.dl");

		Predicate matrixPredicate = new Predicate("matrix", 1);
		Predicate nodePredicate = new Predicate("nodo", 1);

		Predicate startPredicate = new Predicate("start", 2);
		Predicate endPredicate = new Predicate("end", 2);

		Predicate bridgePredicate = new Predicate("bridge", 1);

		nodePredicate.addLiteral(nodePredicate.new Literal(
				new String[] { String.valueOf(sizeOfMatrix * sizeOfMatrix) }));
		matrixPredicate.addLiteral(matrixPredicate.new Literal(
				new String[] { String.valueOf(sizeOfMatrix) }));

		for (int i = 0; i < flowStruct.getSize(); i++) {
			for (int j = 0; j < flowStruct.getSize(); j++) {

				if (flowStruct.getColorAt(i, j) != FlowStruct.NONE) {

					int numberNode = (sizeOfMatrix * i) + (j + 1);
					String color = String.valueOf(flowStruct.getColorAt(i, j));

					if (flowStruct.getColorAt(i, j) == FlowStruct.BRIDGE) {

						bridgePredicate.addLiteral(bridgePredicate.new Literal(
								new String[] { String.valueOf(numberNode) }));

					} else {

						// colorPredicate.addLiteral(colorPredicate.new Literal(
						// new String[] { color }));

						if (colorsAdded.contains(color)) {

							endPredicate.addLiteral(endPredicate.new Literal(
									new String[] { String.valueOf(numberNode),
											color }));
						} else {
							startPredicate
									.addLiteral(startPredicate.new Literal(
											new String[] {
													String.valueOf(numberNode),
													color }));
							colorsAdded.add(color);
						}
					}

				}
			}
		}

		program.addPredicate(bridgePredicate);
		program.addPredicate(nodePredicate);
		program.addPredicate(matrixPredicate);
		program.addPredicate(startPredicate);
		program.addPredicate(endPredicate);

		DlvHandler dlvHandler = new DlvHandler("DLV" + File.separator
				+ "dl.exe");

		dlvHandler.setProgram(program);

		dlvHandler.setNumberOfModels(1);
		dlvHandler.setIncludeFacts(false);
		dlvHandler.setFilter(new String[] { "inPath" });

		try {

			dlvHandler.run();
			try {
				while (dlvHandler.hasMoreModels()) {
					Model model = dlvHandler.nextModel();

					while (model.hasMorePredicates()) {
						Predicate predicate = model.nextPredicate();

						while (predicate.hasMoreLiterals()) {
							Literal literal = predicate.nextLiteral();

							int indexI = Integer.valueOf(literal.getTermAt(0));
							int indexJ = Integer.valueOf(literal.getTermAt(1));
							int color = Integer.valueOf(literal.getTermAt(2));

							flowStruct.addArc(new Point(indexI, indexJ), color);

							// flowStruct.setColorAt(indexI, indexJ, color);
						}
					}
				}
			} catch (DLVInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No Model Found!");
		} catch (DLVInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
