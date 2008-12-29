/*
 * Encog Workbench v1.x
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.encog.workbench.process.generate;

import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.workbench.EncogWorkBench;
import org.encog.workbench.dialogs.GenerateCode;
import org.encog.workbench.frames.TextFrame;
import org.encog.workbench.process.validate.ValidateTraining;

public class CodeGeneration {

	public void processCodeGeneration() {
		final GenerateCode dialog = new GenerateCode(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			if (!validate(dialog)) {
				return;
			}

			final TextFrame text = new TextFrame("Generated code", true);

			Generate gen = null;

			switch (dialog.getLanguage()) {
			case Java:
				gen = new GenerateJava();
				break;
			case CS:
				gen = new GenerateCSharp();
				break;
			case VB:
				gen = new GenerateVB();
				break;
			}

			if (gen != null) {
				final String source = gen.generate(dialog.getNetwork(), dialog
						.getTrainingSet(), dialog.isCopyTraining(), dialog
						.getTrainingMethod());
				text.setText(source);
				text.setVisible(true);
			}
		}
	}

	private boolean validate(final GenerateCode dialog) {
		final ValidateTraining validate = new ValidateTraining(dialog
				.getNetwork(), (BasicNeuralDataSet) dialog.getTrainingSet());
		switch (dialog.getTrainingMethod()) {
		case Anneal:
		case Genetic:
		case Backpropagation:
			if (!validate.validateFeedForward()) {
				return false;
			}
			break;
		case TrainHopfield:
			if (!validate.validateHopfield()) {
				return false;
			}
			break;
		case TrainSOM:
			if (!validate.validateSOM()) {
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}
}