package com.sandeepani.interfaces;

import com.sandeepani.model.StudentDTO;

/**
 * Created by Sandeep on 22-03-2015.
 */
public interface IOnCheckedChangeListener {
    public void checkedStateChanged(StudentDTO studentDTO, boolean isChecked);
}
