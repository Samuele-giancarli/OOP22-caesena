package it.unibo.caesena.view.components;

import java.awt.Font;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NumericUpDownImpl extends JSpinner implements NumericUpDown<JSpinner> {

    private final SpinnerNumberModel model;
    
    public NumericUpDownImpl(final int start, final int min, final int max, final int step) {
        super();
        final DefaultEditor editor;

        this.model = new SpinnerNumberModel(start, min, max, step);
        this.setModel(model);

        editor = new DefaultEditor(this);
        editor.getTextField().setEditable(false);
        editor.getTextField().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        this.setEditor(editor);
    }

    @Override
    public int getValueAsInt() {
        return this.model.getNumber().intValue();
    }

    @Override
    public JSpinner getComponent() {
        return this;
    }

}
