package com.gastos.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
	Pattern mPattern;
	
	public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
		mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
	}

	@Override
	public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5) {
		Matcher matcher = mPattern.matcher(arg3);
		if(!matcher.matches())
			return "";
		return null;
	}
}
